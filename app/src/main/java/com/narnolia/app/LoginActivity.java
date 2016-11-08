package com.narnolia.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.narnolia.app.dbconfig.DbHelper;
import com.narnolia.app.libs.Utils;
import com.narnolia.app.network.LoginWebService;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by Admin on 24-10-2016.
 */

public class LoginActivity extends AbstractActivity {

    private Context mContext;
    private Utils utils;
    private ProgressDialog progressDialog;
    private SharedPref sharedPref;
    private String responseId;
    public String email, loginId, mobile, result, status, userId;
    private String mId, mType, mValue;


    EditText username, password;
    TextView t_username,t_password;
    Button Login;
    private String strPass, strUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_acitivity);

        initView();
    }

    private void initView() {
        try {

            mContext = LoginActivity.this;
            progressDialog = new ProgressDialog(mContext);
            sharedPref = new SharedPref(mContext);
            utils = new Utils(mContext);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            username = (EditText) findViewById(R.id.etUserName);
            password = (EditText) findViewById(R.id.etPassword);

            //..........text view validations.
            t_username=(TextView)findViewById(R.id.t_username);
            t_password=(TextView)findViewById(R.id.t_password);
            //.........Text watcher for hiding tex views......
            username.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (username.length()>0){
                        t_username.setVisibility(View.GONE);
                    }
                }
            });
            password.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (password.length()>0){
                        t_password.setVisibility(View.GONE);
                    }
                }
            });




            Login = (Button) findViewById(R.id.buttonLogin);
            Login.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //  pushActivity(LoginActivity.this, HomeActivity.class, null, true);
                    if (isConnectingToInternet()) {
                        View focusView = null;
                        strUser = username.getText().toString().trim();
                        strPass = password.getText().toString().trim();
                        if (TextUtils.isEmpty(strUser)&&TextUtils.isEmpty(strPass)){
                            t_username.setVisibility(View.VISIBLE);
                            t_password.setVisibility(View.VISIBLE);
                        }
                        if (TextUtils.isEmpty(strUser)) {
                            //   displayMessage("Enter UserName");
                            t_username.setVisibility(View.VISIBLE);
                        } else if (TextUtils.isEmpty(strPass)) {
                            //displayMessage("Enter Password");
                            t_password.setVisibility(View.VISIBLE);
                        } else {
                            if (isConnectingToInternet()) {
                                new UserLogin().execute();
                            } else if ((sharedPref != null && sharedPref.checkForLoginData(mContext, strUser))) {
                                pushActivity(LoginActivity.this, HomeActivity.class, null, true);
                            } else if (checkForLoginData(mContext, strUser, strPass)) {
                                pushActivity(LoginActivity.this, HomeActivity.class, null, true);
                            } else {
                                displayMessage(getString(R.string.warning_internet));
                            }
                        }
                    } else {
                        displayMessage(getString(R.string.warning_internet));
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkForLoginData(Context mContext, String userName, String password) {
        Boolean result = false;
        try {

            String whereClause = " where " + mContext.getString(R.string.column_username) + " = '" + userName + "' AND " + mContext.getString(R.string.column_password) + " = '" + password + "'";
            Cursor cursor = Narnolia.dbCon.fetchFromSelect(DbHelper.TABLE_USER_DETAIL, whereClause);


            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();

                email = cursor.getString(cursor.getColumnIndex(mContext.getString(R.string.column_email)));
                loginId = cursor.getString(cursor.getColumnIndex(mContext.getString(R.string.column_loginId)));
                mobile = cursor.getString(cursor.getColumnIndex(mContext.getString(R.string.column_mobile)));
                status = cursor.getString(cursor.getColumnIndex(mContext.getString(R.string.column_status)));
                userId = cursor.getString(cursor.getColumnIndex(mContext.getString(R.string.column_userId)));

                String resultData = cursor.getString(cursor.getColumnIndex(mContext.getString(R.string.column_result)));
                sharedPref.clearPref();
                sharedPref.setSharedPrefLogin(email, loginId, mobile, resultData, status, userId);
                result = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void insertDataInDb() {
        try {


            String columnNames[] = {mContext.getString(R.string.column_mobile), mContext.getString(R.string.column_email),
                    mContext.getString(R.string.column_loginId), mContext.getString(R.string.column_mobile),
                    mContext.getString(R.string.column_result), mContext.getString(R.string.column_status),
                    mContext.getString(R.string.column_userId), mContext.getString(R.string.column_username),
                    mContext.getString(R.string.column_password)};
            String valuesArray[] = {email, loginId, mobile, result, status, userId, strUser, strPass};
            // WHERE   clause
            String selection = mContext.getString(R.string.column_userId) + " = ?";

            // WHERE clause arguments
            String[] selectionArgs = {userId};

            boolean result = Narnolia.dbCon.update(DbHelper.TABLE_USER_DETAIL, selection, valuesArray, columnNames, selectionArgs);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class UserLogin extends AsyncTask<Void, Void, SoapObject> {

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
            PackageManager manager = getPackageManager();
            String versionName = "";
            try {
                PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
                String packageName = info.packageName;
                int versionCode = info.versionCode;
                versionName = info.versionName;

            } catch (Exception e) {
                // TODO Auto-generated catch block
            }
            LoginWebService webService = new LoginWebService(mContext);
            SoapObject object1 = webService.LoginLead(strUser, strPass, "APP", versionName);

            return object1;
        }

        @Override
        protected void onPostExecute(SoapObject soapObject) {
            super.onPostExecute(soapObject);
            try {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                String response = String.valueOf(soapObject);

                System.out.println("Life : : " + response);

                SoapObject res = (SoapObject) soapObject.getProperty(0);
                result = res.getPropertyAsString("Result");

                if (result.equalsIgnoreCase("false")) {

                    displayMessage(getString(R.string.error_login));
                } else {

                    email = res.getPropertyAsString("Email");
                    loginId = res.getPropertyAsString("LoginId");
                    mobile = res.getPropertyAsString("Mobile");
                    status = res.getPropertyAsString("Status");
                    userId = res.getPropertyAsString("UserId");

                    sharedPref.clearPref();
                    sharedPref.setSharedPrefLogin(email, loginId, mobile, result, status, userId);

                    insertDataInDb();

                    pushActivity(LoginActivity.this, HomeActivity.class, null, true);
                }


            } catch (Exception e) {
                System.out.println("AyushmanBhav : " + e);
                e.printStackTrace();
            }
        }
    }


}
