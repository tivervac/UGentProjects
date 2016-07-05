package be.itech.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import be.itech.dao.DAO;
import be.itech.dao.DataAccessException;
import be.itech.model.Confirmation;
import be.itech.model.MainModel;
import be.itech.ui.DetailedEventActivity;
import org.json.JSONObject;

public class ChangeParticipantStatusTask extends AsyncTask<Void, Void, Boolean> {

    private final ProgressDialog dialog;
    private DetailedEventActivity activity;
    private Confirmation c;
    private String msg = null;
    private final String confirmationUrl;

    public ChangeParticipantStatusTask(String confirmationUrl, Confirmation c, DetailedEventActivity activity) {
        this.activity = activity;
        this.c = c;
        this.confirmationUrl = confirmationUrl;
        dialog = new ProgressDialog(activity);

    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Changing the participant status...");
        dialog.show();
        dialog.setCancelable(false);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        boolean success = false;
        try {
            JSONObject confirmation = DAO.editParticipantStatus(confirmationUrl, c.getPerson(), !c.isGoing());
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
            Toast.makeText(activity, msg != null ? msg : "There was a problem editing the participant. Please try again later.", Toast.LENGTH_SHORT).show();
        } else {
            Confirmation cc = (Confirmation) MainModel.getInstance(c).getElement(c);
            cc.setGoing(!c.isGoing());
            MainModel.getInstance(c).editElement(cc);
        }
        dialog.dismiss();
    }
}
