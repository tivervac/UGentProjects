package be.ugent.oomo.labo_2.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import java.io.IOException;

public final class GcmRegistrar {

    private static final String TAG = GcmRegistrar.class.getName();
    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static GoogleCloudMessaging gcm;
    private static String regid;

    private GcmRegistrar() {
        throw new UnsupportedOperationException();
    }

    public static class GcmException extends Exception {

        private static final long serialVersionUID = -3963924290119629732L;

        public GcmException(String error) {
            super(error);
        }

        public GcmException(String error, Exception ex) {
            super(error, ex);
        }
    }

    /**
     * Checks whether the application was successfully registered on GCM
     * service.
     */
    public static boolean isRegistered(Context context) {
        return getRegistrationId(context).length() > 0;
    }

    public static void register(Context context) throws GcmException {
        if (gcm == null) {
            gcm = GoogleCloudMessaging.getInstance(context);
        }
        regid = getRegistrationId(context);

        if (regid.isEmpty()) {
            registerInBackground(context);
        }

    }

    public static void unregister(Context context) throws GcmException {
        try {
            gcm.unregister();
            clearRegistrationId(context);
        } catch (IOException ex) {
            throw new GcmException("Failed to unregister" + ex.getMessage());
        }
    }

    /**
     * Gets the current registration ID for application on GCM service, if there
     * is one.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */
    public static String getRegistrationId(Context context) {
        final SharedPreferences prefs = CommonUtilities.getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = CommonUtilities.getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private static void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = CommonUtilities.getGcmPreferences(context);
        int appVersion = CommonUtilities.getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * Clears the registration id in the persistence store.
     *
     * <p>
     * As a side-effect, it also expires the registeredOnServer property.
     *
     * @param context application's context.
     * @return old registration id.
     */
    private static void clearRegistrationId(Context context) {
        storeRegistrationId(context, "");
    }

    private static void registerInBackground(final Context context) {
        new AsyncTask() {
            protected Object doInBackground(Object... params) {
                String msg;
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(CommonUtilities.SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;
                    try {
                        // You should send the registration ID to your server over HTTP,
                        // so it can use GCM/HTTP or CCS to send messages to your app.
                        // The request to your server should be authenticated if your app
                        // is using accounts.
                        ChatServerUtilities.registerOnServer(context, regid, CommonUtilities.username);
                    } catch (ChatServerUtilities.ServerException ex) {
                        throw new GcmException("Unable to register on server");
                    }

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.
                    // Persist the regID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                } catch (GcmException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }
        }.execute(null, null, null);
    }
}
