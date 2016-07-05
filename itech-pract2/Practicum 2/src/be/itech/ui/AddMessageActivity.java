package be.itech.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import be.itech.R;
import be.itech.dao.DAO;
import be.itech.dao.DataAccessException;
import be.itech.model.Event;
import be.itech.model.MainModel;
import be.itech.model.Message;
import be.itech.model.Person;
import static be.itech.model.Type.PERSON;
import be.itech.tasks.GetTask;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Titouan Vervack & Caroline De Brouwer
 */
public class AddMessageActivity extends Activity implements Observer {

    private List<Person> people;
    private String peopleUrl;
    private String eventsUrl;
    private String eventUrl;
    private GetTask<Person> peopleTask;
    private String messagesUrl;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Bundle extras = getIntent().getExtras();
        peopleUrl = extras.getString("people_url");
        eventsUrl = extras.getString("events_url");
        eventUrl = extras.getString("event_url");
        messagesUrl = extras.getString("comments_url");
        people = MainModel.getInstance(Person.class).getData();
        if (people.isEmpty()) {
            MainModel.getInstance(Person.class).addObserver(this);
            peopleTask = new GetTask(this, peopleUrl, PERSON);
            peopleTask.execute();
        } else {
            createUI();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (peopleTask != null) {
            peopleTask.cancel(true);
        }
        MainModel.getInstance(Person.class).deleteObserver(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigateUp(false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void navigateUp(boolean addCall) {
        Intent i = NavUtils.getParentActivityIntent(this);
        i.putExtra("people_url", peopleUrl);
        i.putExtra("events_url", eventsUrl);
        i.putExtra("event_url", eventUrl);
        i.putExtra("add_call", addCall);
        NavUtils.navigateUpTo(this, i);
    }

    @Override
    public void onBackPressed() {
        navigateUp(false);
    }

    public void update(Observable observable, Object data) {
        people = MainModel.getInstance(Person.class).getData();
        createUI();
    }

    private void createUI() {
        setContentView(R.layout.add_message);

        final Spinner s = (Spinner) findViewById(R.id.comments_spinner);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, people);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        final EditText et = (EditText) findViewById(R.id.comment_text);

        Button add = (Button) findViewById(R.id.add_comment);
        add.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                String text = et.getText().toString();
                AddMessageActivity activity = AddMessageActivity.this;
                if (text.length() < 3) {
                    Toast.makeText(activity, "A comment has at least 3 characters", Toast.LENGTH_LONG).show();
                } else {
                    Person p = (Person) s.getSelectedItem();
                    Message m = new Message(text, new Date(), p, messagesUrl);
                    AddMessageActivity.this.navigateUp(true);
                    new AddMessageTask(m).execute();
                }
            }
        });
    }

    private class AddMessageTask extends AsyncTask<Void, Void, Event> {

        private final ProgressDialog dialog = new ProgressDialog(AddMessageActivity.this);
        private String msg = null;
        private final Message m;

        public AddMessageTask(Message m) {
            this.m = m;
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Downloading the event data...");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Event doInBackground(Void... params) {
            Event event = null;
            try {
                event = DAO.addMessage(messagesUrl, m);
            } catch (DataAccessException ex) {
                msg = ex.getMessage();
                Log.e(DAO.ERROR_TAG, ex.getMessage());
            }
            return event;
        }

        @Override
        protected void onPostExecute(Event e) {
            if (e == null) {
                Toast.makeText(AddMessageActivity.this, msg != null ? msg : "There was a problem adding the message. Please try again later.", Toast.LENGTH_SHORT).show();
            } else {
                MainModel.getInstance(e).addElement(e);
            }
            dialog.dismiss();
        }
    }
}
