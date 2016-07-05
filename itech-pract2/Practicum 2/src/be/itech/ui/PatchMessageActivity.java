package be.itech.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import be.itech.R;
import be.itech.model.MainModel;
import be.itech.model.Message;
import be.itech.model.Person;
import static be.itech.model.Type.MESSAGE;
import static be.itech.model.Type.PERSON;
import be.itech.tasks.GetTask;
import be.itech.tasks.PatchTask;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Titouan Vervack & Caroline De Brouwer
 */
public class PatchMessageActivity extends Activity implements Observer {

    private String peopleUrl;
    private String eventsUrl;
    private String eventUrl;
    private String messagesUrl;
    private List<Person> people;
    private GetTask<Person> peopleTask;
    private String messageUrl;

    /**
     * Called when the activity is first created.
     *
     * @param icicle The old state
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        Bundle extras = getIntent().getExtras();
        peopleUrl = extras.getString("people_url");
        eventsUrl = extras.getString("events_url");
        eventUrl = extras.getString("event_url");
        messageUrl = extras.getString("comment_url");
        messagesUrl = extras.getString("comments_url");
        if (MainModel.getInstance(Person.class).getData().isEmpty()) {
            MainModel.getInstance(Person.class).addObserver(this);
            people = null;
            peopleTask = new GetTask(this, peopleUrl, PERSON);
            peopleTask.execute();
        } else {
            people = MainModel.getInstance(Person.class).getData();
            createUI();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        MainModel.getInstance(Message.class).addObserver(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (peopleTask != null) {
            peopleTask.cancel(true);
        }
        MainModel.getInstance(Person.class).deleteObserver(this);
        MainModel.getInstance(Message.class).deleteObserver(this);
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
        Intent i = NavUtils.getParentActivityIntent(this);
        i.putExtra("people_url", peopleUrl);
        i.putExtra("events_url", eventsUrl);
        i.putExtra("event_url", eventUrl);
        NavUtils.navigateUpTo(this, i);
    }

    @Override
    public void onBackPressed() {
        navigateUp();
    }

    public void update(Observable observable, Object data) {
        // People hasn't been downloaded yet
        if (people == null) {
            people = MainModel.getInstance(Person.class).getData();
            createUI();
        } // People has been downloaded at some point, listen for a successfull submit of the patch 
        else {
            navigateUp();
        }
    }

    private void createUI() {
        setContentView(R.layout.edit_message);

        final Spinner s = (Spinner) findViewById(R.id.edit_message_spinner);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, people);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        final EditText et = (EditText) findViewById(R.id.edit_comment_text);

        Button add = (Button) findViewById(R.id.edit_comment);
        add.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String text = et.getText().toString();
                if (text.length() < 3 && text.length() > 0) {
                    Toast.makeText(PatchMessageActivity.this, "A comment has at least 3 characters", Toast.LENGTH_LONG).show();
                } else {
                    Person p = (Person) s.getSelectedItem();
                    Message m = new Message(text, new Date(), p, messagesUrl);
                    m.setUrl(messageUrl);
                    new PatchTask<Message>(PatchMessageActivity.this, m, MESSAGE).execute();
                }
            }
        });
    }
}
