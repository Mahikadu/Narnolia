package com.narnolia.app.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.narnolia.app.dbconfig.DataBaseCon;
import com.narnolia.app.dbconfig.DbHelper;
import com.narnolia.app.Narnolia;
import com.narnolia.app.SharedPref;

/**
 * Created by Admin on 07-09-2016.
 */

public class MyScheduledReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent recievedIntent) {
        // TODO Auto-generated method stub
        //Toast.makeText(context, "Session Timeout", Toast.LENGTH_LONG).show();
        Log.v("MyScheduledReceiver", "Intent Fired");

       /* Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("truiton.ACTION_FINISH");
        context.sendBroadcast(broadcastIntent);*/
        try {
            // Intent i = new Intent(context, LoginActivity.class);
            // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            SharedPref pref = new SharedPref(context);
            pref.clearPref();

            if (Narnolia.dbCon == null) {
                Narnolia.dbCon = DataBaseCon.getInstance(context);
            }
            Narnolia.dbCon.alterTable(DbHelper.TABLE_USER_DETAIL);


            //context.startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
