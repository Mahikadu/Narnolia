package com.narnolia.app;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import com.narnolia.app.dbconfig.DataBaseCon;
import com.narnolia.app.dbconfig.DatabaseCopy;
import com.narnolia.app.dbconfig.DbHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by Admin on 24-10-2016.
 */

public class SplashActivity extends AbstractActivity implements Runnable {

    private Handler threadRunnable;
    private int time = 2000;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {
        try {
            mContext = this;

            DatabaseCopy databaseCopy = new DatabaseCopy();
            AssetManager assetManager = this.getAssets();
            databaseCopy.copy(assetManager, mContext);
            Narnolia.dbCon = DataBaseCon.getInstance(getApplicationContext());
            threadRunnable = new Handler();
            exportDB();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // Start your app main activity
        pushActivity(SplashActivity.this,LoginActivity.class,null,true);
        // close this activity
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (threadRunnable != null) {
                threadRunnable.postDelayed(this, time);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            if (threadRunnable != null) {
                threadRunnable.removeCallbacks(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportDB() {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String currentDBPath = "/data/" + "com.narnolia.app" + "/databases/" + DbHelper.DATABASE_NAME;
        String backupDBPath = DbHelper.DATABASE_NAME;
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
//            Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
