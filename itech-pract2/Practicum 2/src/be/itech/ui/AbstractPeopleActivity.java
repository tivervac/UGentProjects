package be.itech.ui;

import android.app.Activity;
import android.os.Bundle;
import be.itech.model.MainModel;
import be.itech.model.Person;
import be.itech.tasks.GetTask;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Titouan Vervack & Caroline De Brouwer
 */
abstract public class AbstractPeopleActivity extends Activity implements Observer {

    protected GetTask<Person> peopleTask;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        MainModel.getInstance(Person.class).addObserver(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        MainModel.getInstance(Person.class).addObserver(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (peopleTask != null) {
            peopleTask.cancel(true);
        }
        MainModel.getInstance(Person.class).deleteObserver(this);
    }

    abstract protected void fillList(List<Person> people);

    public void update(Observable observable, Object data) {
        fillList(MainModel.getInstance(Person.class).getData());
    }

}
