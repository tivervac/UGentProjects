package be.itech.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import be.itech.R;
import be.itech.model.Event;
import be.itech.model.MainModel;
import static be.itech.model.Type.EVENT;
import be.itech.tasks.PatchTask;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.TimeZone;

/**
 *
 * @author Titouan Vervack & Caroline De Brouwer
 */
public class PatchEventActivity extends Activity implements Observer {

    private String eventsUrl;
    private String eventUrl;
    private String peopleUrl;

    /**
     * Called when the activity is first created.
     *
     * @param icicle The old state
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        final SimpleDateFormat formatterh = new SimpleDateFormat("HH:mm");

        eventUrl = getIntent().getExtras().getString("event_url");
        eventsUrl = getIntent().getExtras().getString("events_url");
        peopleUrl = getIntent().getExtras().getString("people_url");
        setContentView(R.layout.edit_event);

        final EditText titleET = (EditText) findViewById(R.id.edit_title_field);
        final EditText descET = (EditText) findViewById(R.id.edit_desc_field);

        final Button start = (Button) findViewById(R.id.edit_start_date);
        final Button end = (Button) findViewById(R.id.edit_end_date);
        final Button starth = (Button) findViewById(R.id.edit_start_hour);
        final Button endh = (Button) findViewById(R.id.edit_end_hour);
        final Calendar s = Calendar.getInstance(TimeZone.getDefault());
        final Calendar e = Calendar.getInstance(TimeZone.getDefault());

        start.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                int year = s.get(Calendar.YEAR);
                int month = s.get(Calendar.MONTH);
                int day = s.get(Calendar.DAY_OF_MONTH);

                new DatePickerDialog(PatchEventActivity.this, new OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        s.set(Calendar.DATE, day);
                        s.set(Calendar.MONTH, month);
                        s.set(Calendar.YEAR, year);
                        start.setText(formatter.format(s.getTime()));
                    }
                }, year, month, day).show();
            }
        });

        starth.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                int hour = s.get(Calendar.HOUR_OF_DAY);
                int min = s.get(Calendar.MINUTE);

                new TimePickerDialog(PatchEventActivity.this, new OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hour, int min) {
                        s.set(Calendar.HOUR, hour);
                        s.set(Calendar.MINUTE, min);
                        starth.setText(formatterh.format(s.getTime()));
                    }
                }, hour, min, true).show();
            }
        });

        end.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                int year = e.get(Calendar.YEAR);
                int month = e.get(Calendar.MONTH);
                int day = e.get(Calendar.DAY_OF_MONTH);

                new DatePickerDialog(PatchEventActivity.this, new OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        e.set(Calendar.DATE, day);
                        e.set(Calendar.MONTH, month);
                        e.set(Calendar.YEAR, year);

                        end.setText(formatter.format(e.getTime()));
                    }
                }, year, month, day).show();
            }
        });

        endh.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                int hour = e.get(Calendar.HOUR_OF_DAY);
                int min = e.get(Calendar.MINUTE);

                new TimePickerDialog(PatchEventActivity.this, new OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hour, int min) {
                        e.set(Calendar.HOUR_OF_DAY, hour);
                        e.set(Calendar.MINUTE, min);
                        endh.setText(formatterh.format(e.getTime()));
                    }
                }, hour, min, true).show();
            }
        });

        Button edit = (Button) findViewById(R.id.edit_submit_button);
        edit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String title = titleET.getText().toString();
                String desc = descET.getText().toString();
                Date start = s.getTime();
                Date end = e.getTime();
                if (title.isEmpty() && desc.isEmpty()) {
                    titleET.setText("");
                    descET.setText("");
                    Toast.makeText(PatchEventActivity.this, "Please enter a title or a description", Toast.LENGTH_SHORT).show();
                    return;
                }
                Event event = new Event(title, desc, start, end, eventsUrl);
                event.setUrl(eventUrl);
                new PatchTask<Event>(PatchEventActivity.this, event, EVENT).execute();
            }
        });

        s.setTime(new Date());
        e.setTime(new Date());
        start.setText(formatter.format(s.getTime()));
        starth.setText(formatterh.format(s.getTime()));
        end.setText(formatter.format(e.getTime()));
        endh.setText(formatterh.format(e.getTime()));
    }

    @Override
    public void onResume() {
        super.onResume();

        MainModel.getInstance(Event.class).addObserver(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        MainModel.getInstance(Event.class).deleteObserver(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigateUp();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void navigateUp() {
        Intent i = NavUtils.getParentActivityIntent(this);
        i.putExtra("events_url", eventsUrl);
        i.putExtra("people_url", peopleUrl);
        NavUtils.navigateUpTo(this, i);
    }

    @Override
    public void onBackPressed() {
        navigateUp();
    }

    public void update(Observable observable, Object data) {
        navigateUp();
    }

}
