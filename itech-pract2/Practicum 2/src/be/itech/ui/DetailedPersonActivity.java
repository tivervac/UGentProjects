package be.itech.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import be.itech.R;
import be.itech.dao.DAO;
import be.itech.dao.DataAccessException;
import be.itech.model.Event;
import be.itech.model.MainModel;
import be.itech.model.Person;
import java.text.SimpleDateFormat;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Titouan Vervack & Caroline De Brouwer
 */
public class DetailedPersonActivity extends Activity implements Observer {

    private String personUrl;
    private String peopleUrl;
    private GetDPersonTask personTask;
    SimpleDateFormat sdfDay = new SimpleDateFormat("dd MMMM, yyyy");
    SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm:ss");
    private boolean eventCall;
    private String eventsUrl;
    private String eventUrl;
    private boolean addCall;

    /**
     * Called when the activity is first created.
     *
     * @param icicle: the old state
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        Bundle extras = getIntent().getExtras();
        personUrl = extras.getString("person_url");
        peopleUrl = extras.getString("people_url");
        eventCall = extras.getBoolean("event_call");
        if (eventCall) {
            eventUrl = extras.getString("event_url");
            eventsUrl = extras.getString("events_url");
        }
        addCall = extras.getBoolean("add_call");
        if (addCall) {
            MainModel.getInstance(Person.class).addObserver(this);
        } else {
            personTask = new GetDPersonTask();
            personTask.execute();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (personTask != null) {
            personTask.cancel(true);
        }
        if (addCall) {
            MainModel.getInstance(Person.class).deleteObserver(this);
        }
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
        Intent i;
        // Go back to detailed event
        if (eventCall) {
            i = new Intent(DetailedPersonActivity.this, DetailedEventActivity.class);
            i.putExtra("event_url", eventUrl);
            i.putExtra("events_url", eventsUrl);
            i.putExtra("people_url", peopleUrl);
            i.putExtra("person_call", false);
            startActivity(i);
        } else { //Navigate up
            i = NavUtils.getParentActivityIntent(this);
            i.putExtra("people_url", peopleUrl);
            NavUtils.navigateUpTo(this, i);
        }
    }

    @Override
    public void onBackPressed() {
        navigateUp();
    }

    public void update(Observable observable, Object data) {
        Person p = (Person) data;
        if (p == null) {
            Toast.makeText(DetailedPersonActivity.this, "There was a problem loading the person. Please try again later.", Toast.LENGTH_SHORT).show();
        } else {
            fillList(p);
        }
    }

    private class GetDPersonTask extends AsyncTask<Void, Void, Person> {

        private final ProgressDialog dialog = new ProgressDialog(DetailedPersonActivity.this);
        private String msg = null;

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Downloading the person data...");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Person doInBackground(Void... params) {
            Person person = null;
            try {
                person = DAO.getPerson(personUrl);
            } catch (DataAccessException ex) {
                msg = ex.getMessage();
                Log.e(DAO.ERROR_TAG, ex.getMessage());
            }
            return person;
        }

        @Override
        protected void onPostExecute(Person person) {
            if (person == null) {
                Toast.makeText(DetailedPersonActivity.this, msg != null ? msg : "There was a problem loading the person. Please try again later.", Toast.LENGTH_SHORT).show();
            } else {
                fillList(person);
            }
            dialog.dismiss();
        }
    }

    private void fillList(Person person) {
        setContentView(R.layout.detailed_person);

        TextView personTitle = (TextView) findViewById(R.id.dperson_name);
        personTitle.setText(person.getName());

        TextView email = (TextView) findViewById(R.id.dperson_email);
        email.setText(person.getEmail());

        TextView birth = (TextView) findViewById(R.id.dperson_birth);

        birth.setText("Birth date: " + ((person.getBirth() == null)
                ? "Not specified" : sdfDay.format(person.getBirth())));

        TextView created = (TextView) findViewById(R.id.dperson_created);
        created.setText("Created on: " + ((person.getCreated() == null)
                ? "Not specified" : sdfDay.format(person.getCreated()) + " at " + sdfHour.format(person.getCreated())));

        TextView updated = (TextView) findViewById(R.id.dperson_updated);
        updated.setText("Last update: " + ((person.getUpdated() == null)
                ? "Not specified" : sdfDay.format(person.getUpdated()) + " at " + sdfHour.format(person.getUpdated())));

        TableLayout tl = (TableLayout) findViewById(R.id.dperson_events_table);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (final Event e : person.getEventsList()) {
            RelativeLayout rl = (RelativeLayout) inflater.inflate(R.layout.event_row, tl, false);
            TextView title = (TextView) rl.findViewById(R.id.event_title);
            TextView start = (TextView) rl.findViewById(R.id.event_start);
            ImageButton edit = (ImageButton) rl.findViewById(R.id.edit_event_button);
            ImageButton del = (ImageButton) rl.findViewById(R.id.remove_event_button);

            start.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
            del.setVisibility(View.GONE);

            title.setText(e.getTitle());

            rl.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    Intent i = new Intent(DetailedPersonActivity.this, DetailedEventActivity.class);
                    i.putExtra("event_url", e.getUrl());
                    i.putExtra("person_url", personUrl);
                    i.putExtra("people_url", peopleUrl);
                    i.putExtra("events_url", eventsUrl);
                    i.putExtra("person_call", true);
                    startActivity(i);
                }

            });
            tl.addView(rl);
        }
    }
}
