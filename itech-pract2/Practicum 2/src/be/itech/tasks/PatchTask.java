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
import be.itech.model.Message;
import be.itech.model.Person;
import be.itech.model.Type;

/**
 *
 * @author Titouan Vervack & Caroline De Brouwer
 * @param <T> The type
 */
public class PatchTask<T> extends AsyncTask<Void, Void, Void> {

    private final ProgressDialog dialog;
    private String msg;
    private final Activity activity;
    private final T element;
    private final Type type;

    public PatchTask(Activity activity, T element, Type type) {
        this.activity = activity;
        this.element = element;
        this.type = type;
        dialog = new ProgressDialog(activity);
        msg = null;
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Patching content...");
        dialog.show();
        dialog.setCancelable(false);
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            switch (type) {
                case PERSON:
                    DAO.patchPerson((Person) element);
                    break;
                case EVENT:
                    DAO.patchEvent((Event) element);
                    break;
                case MESSAGE:
                    DAO.patchMessage((Message) element);
            }
        } catch (DataAccessException ex) {
            msg = ex.getMessage();
            Log.e(DAO.ERROR_TAG, ex.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        if (msg != null) {
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "The content was patched!", Toast.LENGTH_SHORT).show();
            MainModel.getInstance(element).editElement(element);
        }
        dialog.dismiss();
    }
}
