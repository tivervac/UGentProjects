/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.ugent.oomo.labo_2.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 *
 * @author flash
 */
public class WakefulReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context cntxt, Intent intent) {
        startWakefulService(cntxt, intent);
        setResultCode(Activity.RESULT_OK);
    }

}
