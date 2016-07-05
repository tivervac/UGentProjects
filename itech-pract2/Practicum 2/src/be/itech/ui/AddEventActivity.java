package be.itech.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
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
import static be.itech.model.Type.EVENT;
import be.itech.tasks.AddTask;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author Titouan Vervack & Caroline De Brouwer
 */
public class AddEventActivity extends Activity {

    private String eventsUrl;
    private String peopleUrl;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        eventsUrl = getIntent().getExtras().getString("events_url");
        peopleUrl = getIntent().getExtras().getString("people_url");
        setContentView(R.layout.add_event);

        fillView();
    }

    private void fillView() {
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        final SimpleDateFormat formatterh = new SimpleDateFormat("HH:mm");

        final Button start = (Button) findViewById(R.id.add_start_date);
        final Button end = (Button) findViewById(R.id.add_end_date);
        final Button starth = (Button) findViewById(R.id.add_start_hour);
        final Button endh = (Button) findViewById(R.id.add_end_hour);
        final Calendar s = Calendar.getInstance(TimeZone.getDefault());
        final Calendar e = Calendar.getInstance(TimeZone.getDefault());

        start.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                int year = s.get(Calendar.YEAR);
                int month = s.get(Calendar.MONTH);
                int day = s.get(Calendar.DAY_OF_MONTH);

                new DatePickerDialog(AddEventActivity.this, new OnDateSetListener() {
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

                new TimePickerDialog(AddEventActivity.this, new OnTimeSetListener() {
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

                new DatePickerDialog(AddEventActivity.this, new OnDateSetListener() {
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

                new TimePickerDialog(AddEventActivity.this, new OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hour, int min) {
                        e.set(Calendar.HOUR_OF_DAY, hour);
                        e.set(Calendar.MINUTE, min);
                        endh.setText(formatterh.format(e.getTime()));
                    }
                }, hour, min, true).show();
            }
        });

        final EditText titleField = (EditText) findViewById(R.id.add_title_field);
        final EditText descField = (EditText) findViewById(R.id.add_desc_field);
        Button send = (Button) findViewById(R.id.add_create_button);

        final Activity activity = this;

        send.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String title = titleField.getText().toString();
                String desc = descField.getText().toString();
                Date start = s.getTime();
                Date end = e.getTime();
                if (title.isEmpty()) {
                    titleField.setText("");
                    Toast.makeText(activity, "Please enter a title", Toast.LENGTH_SHORT).show();
                    return;
                }
                Event event = new Event(title, desc, start, end, eventsUrl);
                Intent i = new Intent(AddEventActivity.this, DetailedEventActivity.class);
                i.putExtra("add_call", true);
                i.putExtra("events_url", eventsUrl);
                i.putExtra("people_url", peopleUrl);
                i.putExtra("event_call", false);
                startActivity(i);
                new AddTask<Event>(activity, event, EVENT).execute();
            }
        });

        s.setTime(new Date());
        e.setTime(new Date());

        start.setText(formatter.format(s.getTime()));
        end.setText(formatter.format(e.getTime()));
        starth.setText(formatterh.format(s.getTime()));
        endh.setText(formatterh.format(e.getTime()));
    }

    @Override
    public void onBackPressed() {
        // Redownload all events
        navigateUp();
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

    public void navigateUp() {
        Intent i = new Intent(AddEventActivity.this, EventsActivity.class);
        i.putExtra("events_url", eventsUrl);
        startActivity(i);
    }
}
