package com.narnolia.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.narnolia.app.dbconfig.DataBaseCon;
import com.narnolia.app.dbconfig.DatabaseCopy;
import com.narnolia.app.dbconfig.DbHelper;
import com.narnolia.app.libs.Utils;
import com.narnolia.app.network.SOAPWebService;

import org.ksoap2.serialization.SoapObject;

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
    private Utils utils;
    private ProgressDialog progressDialog;
    private String responseId;

    private String mFlag,mTransfer,mlmd,mId, mType, mValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {
        try {
            mContext = SplashActivity.this;


            progressDialog = new ProgressDialog(mContext);
            utils = new Utils(mContext);

            DatabaseCopy databaseCopy = new DatabaseCopy();
            AssetManager assetManager = this.getAssets();
            databaseCopy.copy(assetManager, mContext);
            Narnolia.dbCon = DataBaseCon.getInstance(getApplicationContext());
            threadRunnable = new Handler();
            exportDB();


        } catch (Exception e) {
            e.printStackTrace();
        }
        new GetAllMaster().execute();

    }

    @Override
    public void run() {
        SharedPref sharedPref = new SharedPref(mContext);
        String result = sharedPref.getLoginResult();
        if (result.equalsIgnoreCase(getString(R.string.FALSE))) {

            pushActivity(SplashActivity.this, LoginActivity.class, null, true);
        } else {
            sharedPref.setKeyNodata(true);
            pushActivity(SplashActivity.this, HomeActivity.class, null, true);
        }
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

    public class GetAllMaster extends AsyncTask<Void, Void, SoapObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                if (progressDialog != null && !progressDialog.isShowing()) {
                    progressDialog.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected SoapObject doInBackground(Void... params) {

            SoapObject object = null;
            try {
                SOAPWebService webService = new SOAPWebService(mContext);

                object = webService.GetMasterDetails();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return object;
        }

        @Override
        protected void onPostExecute(SoapObject soapObject) {
            super.onPostExecute(soapObject);
            try {

                responseId = String.valueOf(soapObject);

                    try {
                        for (int j = 0; j < soapObject.getPropertyCount(); j++) {

                            SoapObject root = (SoapObject) soapObject.getProperty(j);

                            if (root.getProperty("Flag") != null) {

                                if (!root.getProperty("Flag").toString().equalsIgnoreCase("anyType{}")) {
                                    mFlag = root.getProperty("Flag").toString();

                                } else {
                                    mFlag = "";
                                }
                            } else {
                                mFlag = "";
                            }
                            if (root.getProperty("Id") != null) {

                                if (!root.getProperty("Id").toString().equalsIgnoreCase("anyType{}")) {
                                    mId = root.getProperty("Id").toString();

                                } else {
                                    mId = "";
                                }
                            } else {
                                mId = "";
                            }
                            if (root.getProperty("Transfer") != null) {

                                if (!root.getProperty("Transfer").toString().equalsIgnoreCase("anyType{}")) {
                                    mTransfer = root.getProperty("Transfer").toString();

                                } else {
                                    mTransfer = "";
                                }
                            } else {
                                mTransfer = "";
                            }
                            if (root.getProperty("lmd") != null) {

                                if (!root.getProperty("lmd").toString().equalsIgnoreCase("anyType{}")) {
                                    mlmd = root.getProperty("lmd").toString();

                                } else {
                                    mlmd = "";
                                }
                            } else {
                                mlmd = "";
                            }
                            if (root.getProperty("type") != null) {

                                if (!root.getProperty("type").toString().equalsIgnoreCase("anyType{}")) {
                                    mType = root.getProperty("type").toString();

                                } else {
                                    mType = "";
                                }
                            } else {
                                mType = "";
                            }
                            if (root.getProperty("value") != null) {

                                if (!root.getProperty("value").toString().equalsIgnoreCase("anyType{}")) {
                                    mValue = root.getProperty("value").toString();

                                } else {
                                    mValue = "";
                                }
                            } else {
                                mValue = "";
                            }

                            String selection = "id" + " = ?";

                            // WHERE clause arguments
                            String[] selectionArgs = {mId};

                            String valuesArray[] = {mId,mFlag,mTransfer,mlmd, mType, mValue};

                            boolean result = Narnolia.dbCon.updateBulk(DbHelper.TABLE_M_PARAMETER, selection, valuesArray, utils.columnNamesMasterDetails, selectionArgs);
                            Log.i("TAG", "Result " + result);

                            // boolean result = Narnolia.dbCon.delete(DbHelper.TABLE_M_PARAMETER, selection, selectionArgs);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {

                try {
                    if (progressDialog != null && !progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
    }



}
