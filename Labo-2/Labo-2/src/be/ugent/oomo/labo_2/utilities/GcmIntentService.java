package be.ugent.oomo.labo_2.utilities;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import be.ugent.oomo.labo_2.ChatMessageListActivity;
import be.ugent.oomo.labo_2.database.DatabaseContract;

/**
 *
 * @author Titouan Vervack
 */
public class GcmIntentService extends IntentService {

    public GcmIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        String message = (String) bundle.get(DatabaseContract.Message.COLUMN_NAME_CONTACT);
        String sender = (String) bundle.get(DatabaseContract.Message.COLUMN_NAME_MESSAGE);
        ChatServerUtilities.saveMessage(this, message, sender);
        Intent a = new Intent(this, ChatMessageListActivity.class);
        //a.putExtra(DatabaseContract.Message.COLUMN_NAME_CONTACT);
        NotificationCompat.Builder nbuilder = new NotificationCompat.Builder(this);
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(0, nbuilder.build());
        WakefulBroadcastReceiver.startWakefulService(this, a);
    }
}
