package be.ugent.oomo.labo_3;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.opengl.Matrix;
import android.util.FloatMath;
import android.view.Surface;
import java.util.List;

/**
 *
 * @author Titouan Vervack & Eveline Hoogstoel
 */
public class MainLayout extends Activity implements Tracker, SensorEventListener {

    private static final float ALPHA = 0.15f;
    private TextView sensors;
    private TextView velocity;
    private TextView distance;
    private OpenGLRenderer renderer;
    private GLSurfaceView glView;
    private float[] rotationMatrix;
    private SensorManager sManager;
    private Sensor accelerometer;
    private Sensor magnometer;
    private float[] magnoValues = {1, 1, 1};
    private float[] output = {0, 0, 0};
    private float[] magnoOutput = {0, 0, 0};
    private Sensor gyrometer;

    /**
     * Called when the activity is first created.
     *
     * @param icicle: The given bundle
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        // Display in Fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main_layout);

        // Add the GLSurfaceView
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.relative_layout);
        RelativeLayout.LayoutParams lparams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        glView = new GLSurfaceView(this);
        glView.setLayoutParams(lparams);
        layout.addView(glView);

        // TextView references
        sensors = (TextView) findViewById(R.id.sensor_info);
        velocity = (TextView) findViewById(R.id.velocity_info);
        distance = (TextView) findViewById(R.id.distance_info);

        // Bring the TextViews to the front
        sensors.bringToFront();
        velocity.bringToFront();
        distance.bringToFront();

        // Set the renderer
        renderer = new OpenGLRenderer(this);
        glView.setRenderer(renderer);

        // Render only when matrix was updated
        glView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        // List sensors
        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensorList = sManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor s : sensorList) {
            sensors.append(s.getName() + "\n");
        }

        // Define sensor
        accelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnometer = sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        gyrometer = sManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    @Override
    public void onStart() {
        super.onStart();
        rotationMatrix = new float[4 * 4];
        Matrix.setIdentityM(rotationMatrix, 0);
    }

    @Override
    public void onPause() {
        super.onPause();
        glView.onPause();
        sManager.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        glView.onResume();
        // Register as listener
        sManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        sManager.registerListener(this, magnometer, SensorManager.SENSOR_DELAY_FASTEST);
        sManager.registerListener(this, gyrometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public float[] getRotationMatrix() {
        return rotationMatrix;
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // TODO Auto-generated method stub

    }

    private float[] lowPass(float[] input, float[] output) {
        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Check type of event
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            SensorManager.getRotationMatrix(rotationMatrix, null, output = lowPass(event.values, output), magnoOutput = lowPass(magnoValues, magnoOutput));
            drawMatrix();
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magnoValues = event.values.clone();
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            getGyroRotation(event);
        }
    }

    private void drawMatrix() {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                // Do nothing
                break;
            case Surface.ROTATION_90:
                SensorManager.remapCoordinateSystem(rotationMatrix.clone(), SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, rotationMatrix);
                break;
            case Surface.ROTATION_180:
                SensorManager.remapCoordinateSystem(rotationMatrix.clone(), SensorManager.AXIS_MINUS_X, SensorManager.AXIS_MINUS_Y, rotationMatrix);
                break;
            case Surface.ROTATION_270:
                SensorManager.remapCoordinateSystem(rotationMatrix.clone(), SensorManager.AXIS_MINUS_Y, SensorManager.AXIS_X, rotationMatrix);
                break;
        }
        glView.requestRender();
    }

    private static final float NS2S = 1.0f / 1000000000.0f;
    private final float[] deltaRotationVector = new float[4];
    private float timestamp;
    private static final float EPSILON = 0.1f;

    private void getGyroRotation(SensorEvent event) {
        // Create a constant to convert nanoseconds to seconds.

        // This timestep's delta rotation to be multiplied by the current rotation
        // after computing it from the gyro sample data.
        if (timestamp != 0) {
            final float dT = (event.timestamp - timestamp) * NS2S;
            // Axis of the rotation sample, not normalized yet.
            float axisX = event.values[0];
            float axisY = event.values[1];
            float axisZ = event.values[2];

            // Calculate the angular speed of the sample
            float omegaMagnitude = FloatMath.sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);

            // Normalize the rotation vector if it's big enough to get the axis
            // (that is, EPSILON should represent your maximum allowable margin of error)
            if (omegaMagnitude > EPSILON) {
                axisX /= omegaMagnitude;
                axisY /= omegaMagnitude;
                axisZ /= omegaMagnitude;
            }

            // Integrate around this axis with the angular speed by the timestep
            // in order to get a delta rotation from this sample over the timestep
            // We will convert this axis-angle representation of the delta rotation
            // into a quaternion before turning it into the rotation matrix.
            float thetaOverTwo = omegaMagnitude * dT / 2.0f;
            float sinThetaOverTwo = FloatMath.sin(thetaOverTwo);
            float cosThetaOverTwo = FloatMath.cos(thetaOverTwo);
            deltaRotationVector[0] = sinThetaOverTwo * axisX;
            deltaRotationVector[1] = sinThetaOverTwo * axisY;
            deltaRotationVector[2] = sinThetaOverTwo * axisZ;
            deltaRotationVector[3] = cosThetaOverTwo;
        }
        timestamp = event.timestamp;
        float[] deltaRotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
        // User code should concatenate the delta rotation we computed with the current rotation
        // in order to get the updated rotation.
        // rotationCurrent = rotationCurrent * deltaRotationMatrix;
    }
}
