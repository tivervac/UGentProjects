package be.itech.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import be.itech.model.Confirmation;
import be.itech.model.MainModel;
import be.itech.model.Person;
import static be.itech.model.Type.PERSON;
import be.itech.tasks.GetTask;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author Titouan Vervack & Caroline De Brouwer
 */
public class ParticipantsActivity extends AbstractPeopleActivity {

    private String peopleUrl;
    private String eventUrl;
    private List<Confirmation> participants;
    private String confirmationsUrl;
    private String eventsUrl;

    /**
     * Called when the activity is first created.
     *
     * @param icicle: the previous state
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        Bundle extras = getIntent().getExtras();
        eventUrl = extras.getString("event_url");
        eventsUrl = extras.getString("events_url");
        peopleUrl = extras.getString("people_url");
        confirmationsUrl = extras.getString("confirmations_url");

        peopleTask = new GetTask(this, peopleUrl, PERSON);
        peopleTask.execute();
    }

    @Override
    protected void fillList(List<Person> people) {
        participants = MainModel.getInstance(Confirmation.class).getData();
        setContentView(R.layout.participants);
        TableLayout tl = (TableLayout) findViewById(R.id.participant_table);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (final Person p : people) {
            if (!participants.contains(new Confirmation(p))) {
                RelativeLayout rl = (RelativeLayout) inflater.inflate(R.layout.participant_row, tl, false);
                TextView name = (TextView) rl.findViewById(R.id.participant_name);

                ImageButton delete = (ImageButton) rl.findViewById(R.id.remove_participant_button);
                delete.setVisibility(View.GONE);

                rl.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View view) {
                        new ConfirmationTask(p).execute();
                    }
                });

                name.setText(p.getName());

                tl.addView(rl);
            }
        }
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
        Intent i = new Intent(ParticipantsActivity.this, DetailedEventActivity.class);
        i.putExtra("event_url", eventUrl);
        i.putExtra("events_url", eventsUrl);
        i.putExtra("people_url", peopleUrl);
        startActivity(i);
    }

    private class ConfirmationTask extends AsyncTask<Void, Void, Boolean> {

        private final ProgressDialog dialog = new ProgressDialog(ParticipantsActivity.this);
        private String msg = null;
        private Person person;

        public ConfirmationTask(Person person) {
            this.person = person;
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Sending the confirmation data...");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean success = false;
            try {
                JSONObject confirmation = DAO.editParticipantStatus(confirmationsUrl, person, true);
                if (confirmation != null) {
                    success = true;
                }
            } catch (DataAccessException ex) {
                msg = ex.getMessage();
                Log.e(DAO.ERROR_TAG, ex.getMessage());
            }
            return success;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast.makeText(ParticipantsActivity.this, msg != null ? msg : "There was a problem sending the confirmation. Please try again later.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ParticipantsActivity.this, "Confirmation added!", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
            navigateUp();
        }
    }
}
