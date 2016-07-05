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

/**
 *
 * @author Titouan Vervack & Caroline De Brouwer
 * @param <T> The type
 */
public class AddTask<T> extends AsyncTask<Void, Void, T> {

    private final ProgressDialog dialog;
    private String msg;
    private final Activity activity;
    private final T element;
    private final Type type;

    public AddTask(Activity activity, T element, Type type) {
        this.activity = activity;
        this.element = element;
        this.type = type;
        dialog = new ProgressDialog(activity);
        msg = null;
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Adding content...");
        dialog.show();
        dialog.setCancelable(false);
    }

    @Override
    protected T doInBackground(Void... params) {
        T response = null;
        try {
            switch (type) {
                case PERSON:
                    response = (T) DAO.addPerson((Person) element);
                    break;
                case EVENT:
                    response = (T) DAO.addEvent((Event) element);
                    break;
            }
        } catch (DataAccessException ex) {
            msg = ex.getMessage();
            Log.e(DAO.ERROR_TAG, ex.getMessage());
        }
        return response;
    }

    @Override
    protected void onPostExecute(T response) {
        if (response == null) {
            Toast.makeText(activity, (msg != null) ? msg : "There was a problem adding the content. Please try again later.", Toast.LENGTH_SHORT).show();
        } else {
            MainModel.getInstance(response).addElement(response);
            Toast.makeText(activity, "The content was added!", Toast.LENGTH_SHORT).show();
        }
        dialog.dismiss();
    }
}
