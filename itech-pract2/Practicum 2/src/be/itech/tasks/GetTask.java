package be.itech.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import be.itech.dao.DAO;
import be.itech.dao.DataAccessException;
import be.itech.model.Event;
import be.itech.model.MainModel;
import be.itech.model.Person;
import be.itech.model.Type;
import java.util.List;

/**
 *
 * @author Titouan Vervack & Caroline De Brouwer
 * @param <T> The type
 */
public class GetTask<T> extends AsyncTask<Void, Void, List<T>> {

    private final ProgressDialog dialog;
    private String msg = null;
    private final Activity activity;
    private final String url;
    private final Type type;

    public GetTask(Activity activity, String url, Type type) {
        this.activity = activity;
        this.url = url;
        this.type = type;
        dialog = new ProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Downloading the contents...");
        dialog.show();
        dialog.setCancelable(false);
    }

    @Override
    protected List<T> doInBackground(Void... params) {
        List<T> data = null;
        try {
            switch (type) {
                case PERSON:
                    data = (List<T>) DAO.getPeople(url);
                    break;
                case EVENT:
                    data = (List<T>) DAO.getEvents(url);
                    break;
            }
        } catch (DataAccessException ex) {
            msg = ex.getMessage();
            Log.e(DAO.ERROR_TAG, ex.getMessage());
        }
        return data;
    }

    @Override
    protected void onPostExecute(List<T> data) {
        if (data == null) {
            Toast.makeText(activity, msg != null ? msg : "There was a problem loading the contents. Please try again later.", Toast.LENGTH_SHORT).show();
        } else {
            Class c = null;
            switch (type) {
                case PERSON:
                    c = Person.class;
                    break;
                case EVENT:
                    c = Event.class;
                    break;
            }
            MainModel.getInstance(c).setData(data);
            dialog.dismiss();
        }
    }
}
