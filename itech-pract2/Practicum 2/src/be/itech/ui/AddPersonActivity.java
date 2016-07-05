package be.itech.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import be.itech.R;
import be.itech.model.Person;
import static be.itech.model.Type.PERSON;
import be.itech.tasks.AddTask;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author Titouan Vervack & Caroline De Brouwer
 */
public class AddPersonActivity extends Activity {

    private String peopleUrl;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        peopleUrl = getIntent().getExtras().getString("people_url");
        setContentView(R.layout.add_person);

        fillView();
    }

    private void fillView() {
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        final Button date = (Button) findViewById(R.id.add_birth_date);
        final Calendar c = Calendar.getInstance(TimeZone.getDefault());

        date.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                new DatePickerDialog(AddPersonActivity.this, new OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        c.set(Calendar.DATE, day);
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.YEAR, year);
                        date.setText(formatter.format(c.getTime()));
                    }
                }, year, month, day).show();
            }
        });
        final EditText nameField = (EditText) findViewById(R.id.add_name_field);
        final EditText emailField = (EditText) findViewById(R.id.add_email_field);
        Button send = (Button) findViewById(R.id.add_create_button);

        final AddPersonActivity activity = this;

        send.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                String name = nameField.getText().toString();
                String email = emailField.getText().toString();
                Date birth = c.getTime();
                if (name.isEmpty() || email.isEmpty()) {
                    nameField.setText("");
                    emailField.setText("");
                    Toast.makeText(activity, "Please enter name and email.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Person person = new Person(name, email, birth, peopleUrl);
                Intent i = new Intent(AddPersonActivity.this, DetailedPersonActivity.class);
                i.putExtra("add_call", true);
                i.putExtra("people_url", peopleUrl);
                i.putExtra("event_call", false);
                startActivity(i);
                new AddTask<Person>(activity, person, PERSON).execute();
            }
        });

        c.setTime(new Date());

        date.setText(formatter.format(c.getTime()));
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
        Intent i = new Intent(AddPersonActivity.this, PeopleActivity.class);
        i.putExtra("people_url", peopleUrl);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        // Redownload all people
        navigateUp();
    }
}
