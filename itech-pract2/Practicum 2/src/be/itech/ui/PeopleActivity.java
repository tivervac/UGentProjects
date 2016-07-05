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
import be.itech.model.Person;
import static be.itech.model.Type.PERSON;
import be.itech.tasks.GetTask;
import be.itech.tasks.RemoveTask;
import java.util.List;
import java.util.Observer;

/**
 *
 * @author Titouan Vervack & Caroline De Brouwer
 */
public class PeopleActivity extends AbstractPeopleActivity implements Observer {

    private String peopleUrl;
    private static final int ADD_PERSON_ID = 1;

    /**
     * Called when the activity is first created.
     *
     * @param icicle: the previous state
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        peopleUrl = getIntent().getExtras().getString("people_url");

        peopleTask = new GetTask(this, peopleUrl, PERSON);
        peopleTask.execute();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Remove the addPersonButton
        menu.removeItem(ADD_PERSON_ID);

        // Add the addPersonButton
        MenuItem notificationsButton = menu.add(Menu.NONE, ADD_PERSON_ID, Menu.NONE, getResources().getString(R.string.add_person));
        notificationsButton.setIcon(R.drawable.ic_add).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Add person
            case ADD_PERSON_ID:
                Intent i = new Intent(PeopleActivity.this, AddPersonActivity.class);
                i.putExtra("people_url", peopleUrl);
                startActivity(i);
                break;
            // Navigate up
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

    protected void fillList(List<Person> people) {
        setContentView(R.layout.people);

        TableLayout tl = (TableLayout) findViewById(R.id.people_table);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (final Person p : people) {
            RelativeLayout rl = (RelativeLayout) inflater.inflate(R.layout.person_row, tl, false);
            TextView name = (TextView) rl.findViewById(R.id.person_name);
            ImageButton edit = (ImageButton) rl.findViewById(R.id.edit_person_button);
            ImageButton remove = (ImageButton) rl.findViewById(R.id.remove_person_button);

            name.setText(p.getName());
            final Activity activity = this;

            edit.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    Intent i = new Intent(PeopleActivity.this, PatchPersonActivity.class);
                    i.putExtra("people_url", peopleUrl);
                    i.putExtra("person_url", p.getUrl());
                    startActivity(i);
                }
            });

            remove.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    new RemoveTask<Person>(activity, p, p.getUrl()).execute();
                }
            });

            rl.setOnClickListener(new OnClickListener() {

                public void onClick(View view) {
                    Intent i = new Intent(PeopleActivity.this, DetailedPersonActivity.class);
                    i.putExtra("person_url", p.getUrl());
                    i.putExtra("people_url", peopleUrl);
                    i.putExtra("event_call", false);
                    startActivity(i);
                }
            });
            tl.addView(rl);
        }
    }
}
