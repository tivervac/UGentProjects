package be.itech.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import be.itech.R;
import be.itech.dao.DAO;
import be.itech.dao.DataAccessException;
import java.util.List;

/**
 *
 * @author Titouan Vervack & Caroline De Brouwer
 */
public class HomePageActivity extends Activity {

    private GetHomePageTask homePageTask;
    private boolean visible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        visible = false;
    }

    @Override
    public void onResume() {
        super.onResume();

        visible = true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // Make sure we start downloading only when the app is actually visible
        if (hasFocus && visible && homePageTask == null) {
            homePageTask = new GetHomePageTask();
            homePageTask.execute();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        visible = false;
        if (homePageTask != null) {
            homePageTask.cancel(true);
        }
    }

    private class GetHomePageTask extends AsyncTask<Void, Void, List<String>> {

        private final ProgressDialog dialog = new ProgressDialog(HomePageActivity.this);
        private String msg = null;

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading the homepage data...");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            List<String> info = null;
            try {
                info = DAO.getHomePage();
            } catch (DataAccessException ex) {
                msg = ex.getMessage();
                Log.e(DAO.ERROR_TAG, ex.getMessage());
            }
            return info;
        }

        @Override
        protected void onPostExecute(List<String> info) {
            if (info == null) {
                Toast.makeText(HomePageActivity.this, msg != null ? msg : "There was a problem loading the homepage. Please try again later.", Toast.LENGTH_SHORT).show();
            } else {
                fillList(info);
            }
            dialog.dismiss();
        }
    }

    private void fillList(final List<String> info) {
        setContentView(R.layout.homepage);

        TextView title = (TextView) findViewById(R.id.homepage_title);
        title.setText("Welcome to " + info.get(0) + "!");

        Button eventsButton = (Button) findViewById(R.id.events_button);
        Button peopleButton = (Button) findViewById(R.id.people_button);

        eventsButton.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(HomePageActivity.this, EventsActivity.class);
                i.putExtra("events_url", info.get(1));
                i.putExtra("people_url", info.get(2));
                startActivity(i);
            }
        });

        peopleButton.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(HomePageActivity.this, PeopleActivity.class);
                i.putExtra("people_url", info.get(2));
                startActivity(i);
            }
        });
    }
}
