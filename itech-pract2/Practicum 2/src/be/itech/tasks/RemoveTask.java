package be.itech.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import be.itech.dao.DAO;
import be.itech.dao.DataAccessException;
import be.itech.model.MainModel;

/**
 *
 * @author Titouan Vervack & Caroline De Brouwer
 * @param <T> The type
 */
public class RemoveTask<T> extends AsyncTask<Void, Void, Boolean> {

    private final ProgressDialog dialog;
    private String msg;
    private final Activity activity;
    private final T element;
    private final String url;

    public RemoveTask(Activity activity, T element, String url) {
        this.activity = activity;
        this.element = element;
        this.url = url;
        dialog = new ProgressDialog(activity);
        msg = null;
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Removing the content...");
        dialog.show();
        dialog.setCancelable(false);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        boolean success = false;
        try {
            success = DAO.deleteQuery(url);

        } catch (DataAccessException ex) {
            msg = ex.getMessage();
            Log.e(DAO.ERROR_TAG, ex.getMessage());
        }
        return success;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (!success) {
            Toast.makeText(activity, msg != null ? msg : "There was a problem removing the content. Please try again later.", Toast.LENGTH_SHORT).show();
        } else {
            MainModel.getInstance(element).removeElement(element);
            Toast.makeText(activity, "The content was removed!", Toast.LENGTH_SHORT).show();
        }
        dialog.dismiss();
    }
}
