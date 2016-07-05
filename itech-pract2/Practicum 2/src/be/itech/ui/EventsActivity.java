package be.itech.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import be.itech.R;
import be.itech.model.Event;
import be.itech.model.MainModel;
import static be.itech.model.Type.EVENT;
import be.itech.tasks.GetTask;
import be.itech.tasks.RemoveTask;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Titouan Vervack & Caroline De Brouwer
 */
public class EventsActivity extends Activity implements Observer {

    private String eventsUrl;
    private String peopleUrl;
    private GetTask<Event> eventsTask;
    private static final SimpleDateFormat SDF_DAY = new SimpleDateFormat("dd MMMM, yyyy");
    private static final SimpleDateFormat SDF_HOUR = new SimpleDateFormat("HH:mm");
    private static final int ADD_EVENT_ID = 1;

    /**
     * Called when the activity is first created.
     *
     * @param icicle: the previous state
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        peopleUrl = getIntent().getExtras().getString("people_url");
        eventsUrl = getIntent().getExtras().getString("events_url");

        MainModel.getInstance(Event.class).addObserver(this);

        eventsTask = new GetTask(this, eventsUrl, EVENT);
        eventsTask.execute();
    }

    @Override
    public void onResume() {
        super.onResume();

        MainModel.getInstance(Event.class).addObserver(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (eventsTask != null) {
            eventsTask.cancel(true);
        }

        MainModel.getInstance(Event.class).deleteObserver(this);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Remove the addPersonButton
        menu.removeItem(ADD_EVENT_ID);

        // Add the addPersonButton
        MenuItem notificationsButton = menu.add(Menu.NONE, ADD_EVENT_ID, Menu.NONE, getResources().getString(R.string.add_event));
        notificationsButton.setIcon(R.drawable.ic_add).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case ADD_EVENT_ID:
                Intent i = new Intent(EventsActivity.this, AddEventActivity.class);
                i.putExtra("events_url", eventsUrl);
                i.putExtra("people_url", peopleUrl);
                startActivity(i);
                break;

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }

    public void update(Observable o, Object o1) {
        fillList(MainModel.getInstance(Event.class).getData());
    }

    private void fillList(List<Event> events) {
        setContentView(R.layout.events);

        TableLayout tl = (TableLayout) findViewById(R.id.events_table);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (final Event e : events) {
            RelativeLayout rl = (RelativeLayout) inflater.inflate(R.layout.event_row, tl, false);
            TextView title = (TextView) rl.findViewById(R.id.event_title);
            TextView start = (TextView) rl.findViewById(R.id.event_start);
            ImageButton edit = (ImageButton) rl.findViewById(R.id.edit_event_button);
            ImageButton remove = (ImageButton) rl.findViewById(R.id.remove_event_button);

            title.setText(e.getTitle());
            final Activity activity = this;

            if (e.getStart() != null) {
                start.setText(SDF_DAY.format(e.getStart()) + " at " + SDF_HOUR.format(e.getStart()));
            } else {
                start.setText("Not specified");
            }

            edit.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    Intent i = new Intent(EventsActivity.this, PatchEventActivity.class);
                    i.putExtra("events_url", eventsUrl);
                    i.putExtra("people_url", peopleUrl);
                    i.putExtra("event_url", e.getUrl());
                    startActivity(i);
                }
            });

            remove.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    new RemoveTask<Event>(activity, e, e.getUrl()).execute();
                }
            });

            rl.setOnClickListener(new OnClickListener() {

                public void onClick(View view) {
                    Intent i = new Intent(EventsActivity.this, DetailedEventActivity.class);
                    i.putExtra("event_url", e.getUrl());
                    i.putExtra("events_url", eventsUrl);
                    i.putExtra("people_url", peopleUrl);
                    i.putExtra("person_call", false);
                    startActivity(i);
                }

            });
            tl.addView(rl);
        }
    }
}
