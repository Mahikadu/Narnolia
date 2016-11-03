package com.narnolia.app;

import android.app.Application;

import com.narnolia.app.dbconfig.DataBaseCon;

/**
 * Created by Admin on 27-10-2016.
 */

public class Narnolia extends Application {

    public static DataBaseCon dbCon = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        try {
            if (dbCon != null) {
                dbCon.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
