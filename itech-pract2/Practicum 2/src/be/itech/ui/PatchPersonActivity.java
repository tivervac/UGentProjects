package be.itech.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import be.itech.R;
import be.itech.model.MainModel;
import be.itech.model.Person;
import static be.itech.model.Type.PERSON;
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
public class PatchPersonActivity extends Activity implements Observer {

    private String peopleUrl;
    private String personUrl;

    /**
     * Called when the activity is first created.
     *
     * @param icicle The old state
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        peopleUrl = getIntent().getExtras().getString("people_url");
        personUrl = getIntent().getExtras().getString("person_url");
        setContentView(R.layout.edit_people);
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        final EditText nameET = (EditText) findViewById(R.id.edit_name_field);
        final EditText emailET = (EditText) findViewById(R.id.edit_email_field);

        final Button date = (Button) findViewById(R.id.edit_birth_date);
        final Calendar c = Calendar.getInstance(TimeZone.getDefault());

        date.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                new DatePickerDialog(PatchPersonActivity.this, new OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        c.set(Calendar.DATE, day);
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.YEAR, year);
                        date.setText(formatter.format(c.getTime()));
                    }
                }, year, month, day).show();
            }
        });

        c.setTime(new Date());
        date.setText(formatter.format(c.getTime()));

        Button edit = (Button) findViewById(R.id.edit_button);
        edit.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                String name = nameET.getText().toString();
                String email = emailET.getText().toString();
                Date birth = c.getTime();
                if (name.isEmpty() && email.isEmpty()) {
                    nameET.setText("");
                    emailET.setText("");
                    Toast.makeText(PatchPersonActivity.this, "Please enter name or email.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Person person = new Person(name, email, birth, peopleUrl);
                person.setUrl(personUrl);
                new PatchTask<Person>(PatchPersonActivity.this, person, PERSON).execute();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        MainModel.getInstance(Person.class).addObserver(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        MainModel.getInstance(Person.class).deleteObserver(this);
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
