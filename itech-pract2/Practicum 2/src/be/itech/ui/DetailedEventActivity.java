package be.itech.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import be.itech.R;
import be.itech.dao.DAO;
import be.itech.dao.DataAccessException;
import be.itech.model.Confirmation;
import be.itech.model.Event;
import be.itech.model.MainModel;
import be.itech.model.Message;
import be.itech.tasks.ChangeParticipantStatusTask;
import be.itech.tasks.RemoveTask;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Titouan Vervack & Caroline De Brouwer
 */
public class DetailedEventActivity extends Activity implements Observer {

    /**
     * Called when the activity is first created.
     */
    private GetDEventTask eventTask;
    private String eventUrl;
    private String eventsUrl;
    private boolean personCall;
    private String peopleUrl;
    SimpleDateFormat sdf_day = new SimpleDateFormat("dd MMMM, yyyy");
    SimpleDateFormat sdf_hour = new SimpleDateFormat("HH:mm");
    private boolean addCall;
    private Event event;
    private String personUrl;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        Bundle extras = getIntent().getExtras();
        eventUrl = extras.getString("event_url");
        eventsUrl = extras.getString("events_url");
        // Tells us if the activity was entered from Events or a DetailedPerson
        personCall = extras.getBoolean("person_call");
        personUrl = extras.getString("person_url");
        peopleUrl = extras.getString("people_url");

        addCall = extras.getBoolean("add_call");
        if (addCall) {
            MainModel.getInstance(Event.class).addObserver(this);
        } else {
            eventTask = new GetDEventTask();
            eventTask.execute();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (eventTask != null) {
            eventTask.cancel(true);
        }
        if (addCall) {
            MainModel.getInstance(Event.class).deleteObserver(this);
        }
        MainModel.getInstance(Confirmation.class).deleteObserver(this);
        MainModel.getInstance(Message.class).deleteObserver(DetailedEventActivity.this);
    }

    public void update(Observable observable, Object data) {
        Event e = (Event) data;
        if (e == null) {
            if (event == null) {
                Toast.makeText(DetailedEventActivity.this, "There was a problem loading the event. Please try again later.", Toast.LENGTH_SHORT).show();
            } else {
                fillList(event, true);
            }
        } else {
            fillList(e, false);
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
        // Go back to detailed person
        if (personCall) {
            i = new Intent(DetailedEventActivity.this, DetailedPersonActivity.class);
            i.putExtra("person_url", personUrl);
            i.putExtra("people_url", peopleUrl);
            i.putExtra("events_url", eventsUrl);
            startActivity(i);
        } else { //Navigate up
            i = NavUtils.getParentActivityIntent(this);
            i.putExtra("events_url", eventsUrl);
            i.putExtra("people_url", peopleUrl);
            NavUtils.navigateUpTo(this, i);
        }
    }

    @Override
    public void onBackPressed() {
        navigateUp();
    }

    private class GetDEventTask extends AsyncTask<Void, Void, Event> {

        private final ProgressDialog dialog = new ProgressDialog(DetailedEventActivity.this);
        private String msg = null;

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
                event = DAO.getEvent(eventUrl);
            } catch (DataAccessException ex) {
                msg = ex.getMessage();
                Log.e(DAO.ERROR_TAG, ex.getMessage());
            }
            return event;
        }

        @Override
        protected void onPostExecute(Event e) {
            if (e == null) {
                Toast.makeText(DetailedEventActivity.this, msg != null ? msg : "There was a problem loading the event. Please try again later.", Toast.LENGTH_SHORT).show();
            } else {
                fillList(e, false);
            }
            dialog.dismiss();
        }
    }

    private void fillList(final Event event, boolean refresh) {
        this.event = event;
        setContentView(R.layout.detailed_event);

        TextView title = (TextView) findViewById(R.id.devent_title);
        title.setText(event.getTitle());

        TextView desc = (TextView) findViewById(R.id.devent_desc);
        desc.setText(event.getDescription());

        TextView start = (TextView) findViewById(R.id.devent_start);
        start.setText("Start: " + sdf_day.format(event.getStart()) + " at " + sdf_hour.format(event.getStart()));

        TextView end = (TextView) findViewById(R.id.devent_end);
        end.setText("End: " + sdf_day.format(event.getEnd()) + " at " + sdf_hour.format(event.getEnd()));

        TableLayout tl = (TableLayout) findViewById(R.id.devent_people_table);
        TableLayout tlmsg = (TableLayout) findViewById(R.id.devent_comments_table);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Button add = (Button) findViewById(R.id.add_participant);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(DetailedEventActivity.this, ParticipantsActivity.class);
                i.putExtra("people_url", peopleUrl);
                i.putExtra("event_url", eventUrl);
                i.putExtra("events_url", eventsUrl);
                i.putExtra("confirmations_url", event.getConfirmationsUrl());
                startActivity(i);
            }
        });

        final Activity activity = this;

        List<Confirmation> confs;
        if (refresh) {
            confs = MainModel.getInstance(Confirmation.class).getData();
        } else {
            confs = event.getConfirmations();
        }

        List<Confirmation> confirmations = new ArrayList<Confirmation>();
        for (final Confirmation c : confs) {
            RelativeLayout rl = (RelativeLayout) inflater.inflate(R.layout.participant_row, tl, false);
            TextView name = (TextView) rl.findViewById(R.id.participant_name);
            ImageButton delete = (ImageButton) rl.findViewById(R.id.remove_participant_button);

            if (!c.isGoing()) {
                name.setTextColor(Color.RED);
            } else {
                name.setTextColor(Color.rgb(46, 139, 87));
            }
            name.setText(c.getPerson().getName());

            rl.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    Intent i = new Intent(DetailedEventActivity.this, DetailedPersonActivity.class);
                    i.putExtra("person_url", c.getPerson().getUrl());
                    i.putExtra("people_url", peopleUrl);
                    i.putExtra("event_url", eventUrl);
                    i.putExtra("events_url", eventsUrl);
                    i.putExtra("event_call", true);
                    startActivity(i);
                }
            });

            confirmations.add(c);
            tl.addView(rl);

            delete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    new ChangeParticipantStatusTask(event.getConfirmationsUrl(), c, DetailedEventActivity.this).execute();
                }
            });
        }

        // Makes sure we don't redraw again after setting the data
        MainModel.getInstance(Confirmation.class).deleteObserver(this);
        MainModel.getInstance(Confirmation.class).setData(confirmations);
        // Listen again
        MainModel.getInstance(Confirmation.class).addObserver(this);

        Button addComment = (Button) findViewById(R.id.add_comment);
        addComment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(DetailedEventActivity.this, AddMessageActivity.class);
                i.putExtra("people_url", peopleUrl);
                i.putExtra("events_url", eventsUrl);
                i.putExtra("event_url", eventUrl);
                i.putExtra("comments_url", event.getMessagesUrl());
                startActivity(i);
            }
        });

        List<Message> msgs;
        if (refresh) {
            msgs = MainModel.getInstance(Message.class).getData();
        } else {
            msgs = event.getMessages();
        }

        List<Message> messages = new ArrayList<Message>();
        for (final Message m : msgs) {
            RelativeLayout rl = (RelativeLayout) inflater.inflate(R.layout.message_row, tl, false);
            TextView author = (TextView) rl.findViewById(R.id.commenter);
            TextView date = (TextView) rl.findViewById(R.id.comment_date);
            TextView text = (TextView) rl.findViewById(R.id.comment_msg);
            ImageButton delete = (ImageButton) rl.findViewById(R.id.remove_message_button);
            ImageButton edit = (ImageButton) rl.findViewById(R.id.edit_message_button);

            author.setText(m.getPerson().getName());
            date.setText(sdf_day.format(m.getCreated()));
            text.setText(m.getText());

            edit.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    Intent i = new Intent(DetailedEventActivity.this, PatchMessageActivity.class);
                    i.putExtra("people_url", peopleUrl);
                    i.putExtra("events_url", eventsUrl);
                    i.putExtra("event_url", eventUrl);
                    i.putExtra("comment_url", m.getUrl());
                    i.putExtra("comments_url", event.getMessagesUrl());
                    startActivity(i);
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    new RemoveTask<Message>(activity, m, m.getUrl()).execute();
                }
            });
            messages.add(m);
            tlmsg.addView(rl);
        }
        // Makes sure we don't redraw again after setting the data
        MainModel.getInstance(Message.class).deleteObserver(this);
        MainModel.getInstance(Message.class).setData(messages);
        // Listen again
        MainModel.getInstance(Message.class).addObserver(this);
    }
}
