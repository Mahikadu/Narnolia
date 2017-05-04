package com.narnolia.app;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.test.mock.MockPackageManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.narnolia.app.adapter.NotificationAdapter;
import com.narnolia.app.dbconfig.DbHelper;
import com.narnolia.app.libs.Utils;
import com.narnolia.app.model.AttendenceReportModel;
import com.narnolia.app.model.GetMessagesModel;
import com.narnolia.app.model.LeadInfoModel;
import com.narnolia.app.model.LoginDetailsModel;
import com.narnolia.app.network.GPSTracker;
import com.narnolia.app.network.LoginWebService;
import com.narnolia.app.network.SOAPWebService;

import org.ksoap2.serialization.SoapObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Admin on 24-10-2016.
 */

public class LoginActivity extends AbstractActivity {

    private Context mContext;
    private Utils utils;
    private ProgressDialog progressDialog;
    private SharedPref sharedPref;
    private String responseId;
    public String email, loginId, mobile, result, status, userId, strAttendance, is_rm;
    private String mId, mType, mValue;
    private String f_Mailid, forgot_flag;
    String versionName = "";
    private LoginDetailsModel loginDetailsModel;
    String user_check, attendance_check;

    EditText username, password;
    TextView t_username, t_password, forget_password, forgot_pass_message;
    Button Login;
    private String strPass, strUser;


    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    GPSTracker gps;
    String lat, lang, location;
    PackageManager manager;
    String currentDate = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_acitivity);
        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

            }

            Calendar c = Calendar.getInstance();
            // SimpleDateFormat df = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            currentDate = df.format(c.getTime());

        } catch (Exception e) {
            e.printStackTrace();
        }


        initView();


    }


    private void initView() {
        try {

            mContext = LoginActivity.this;
            progressDialog = new ProgressDialog(mContext);
            sharedPref = new SharedPref(mContext);
            utils = new Utils(mContext);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            manager = mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            String packageName = info.packageName;
            int versionCode = info.versionCode;
            versionName = info.versionName;


            username = (EditText) findViewById(R.id.etUserName);
            password = (EditText) findViewById(R.id.etPassword);

            //..........text view validations.
            t_username = (TextView) findViewById(R.id.t_username);
            t_password = (TextView) findViewById(R.id.t_password);
            forget_password = (TextView) findViewById(R.id.forget_password);
            forgot_pass_message = (TextView) findViewById(R.id.forgot_pass_message);
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
                  /*  if (TextUtils.isEmpty(username.getText().toString())){
                        t_username.setVisibility(View.GONE);
                    }*/
                    if (!TextUtils.isEmpty(username.getText().toString())) {
                        t_username.setVisibility(View.GONE);
                    }
                }
            });

            username.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    username.setFocusableInTouchMode(true);
                    return false;
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
                    if (password.length() > 0) {
                        t_password.setVisibility(View.GONE);
                    }
                    if (editable.length() > 0) {
                        password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_24dp, 0);
                    }
                    if (editable.length() == 0) {
                        password.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    }
                    password.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            password.setFocusableInTouchMode(true);

                            final int DRAWABLE_LEFT = 0;
                            final int DRAWABLE_TOP = 1;
                            final int DRAWABLE_RIGHT = 2;
                            final int DRAWABLE_BOTTOM = 3;
                            if (password.getText().toString().length() > 0 && !TextUtils.isEmpty(password.getText().toString().trim())) {
                                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                    if (event.getRawX() >= (password.getRight() - password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                                        password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                                        password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_24dp, 0);
                                    }
                                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                                    if (event.getRawX() >= (password.getRight() - password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                                        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                        password.setSelection(password.length());
                                    }
                                }
                            }

                            return false;
                        }


                    });
                }
            });

            password.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    password.setFocusableInTouchMode(true);
                    return false;
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
                        if (TextUtils.isEmpty(username.getText().toString()) && TextUtils.isEmpty(strPass)) {
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
                                gps = new GPSTracker(mContext);
                                if (gps.canGetLocation()) {

                                    double latitude = gps.getLatitude();
                                    lat = Double.toString(latitude);
                                    double longitude = gps.getLongitude();
                                    lang = Double.toString(longitude);
                                    try {

                                        Geocoder geo = new Geocoder(mContext, Locale.getDefault());
                                        List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);
                                        if (addresses.isEmpty()) {
                                        if (lat.equals("0.0")&&lang.equals("0.0"))
                                            Toast.makeText(mContext, "Please Wait, Waiting for location", Toast.LENGTH_SHORT).show();

                                        } else {
                                            if (addresses.size() > 0) {
                                                if (!lat.equals("0.0")&&!lang.equals("0.0"))
                                                    //  Toast.makeText(mContext, "your location is"+addresses.get(0), Toast.LENGTH_SHORT).show();
                                                //addres.setText(addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() +", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
                                                location = addresses.get(0).getLocality();
                                                // Toast.makeText(getApplicationContext(), "Address:- " + addresses.get(0).getLocality(), Toast.LENGTH_LONG).show();
                                                new AttendenceReportDateWise().execute();
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace(); // getFromLocation() may sometimes fail
                                    }
                                   // new AttendenceReportDateWise().execute();


                                } else {
                                    // can't get location
                                    // GPS or Network is not enabled
                                    // Ask user to enable GPS/network in settings
                                    gps.showSettingsAlert();
                                }

                            } /*else if ((sharedPref != null && sharedPref.checkForLoginData(mContext, strUser))) {
                                pushActivity(LoginActivity.this, HomeActivity.class, null, true);
                            } */ else if (checkForLoginData(mContext, strUser, strPass)) {

                            } else {
                                displayMessage(getString(R.string.warning_internet));
                            }
                        }
                    } else {
                        displayMessage(getString(R.string.warning_internet));
                    }
                }
            });
            forget_password.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(username.getText().toString())) {
                        //   displayMessage("Enter UserName");
                        t_username.setVisibility(View.VISIBLE);
                    } else {
                        t_username.setVisibility(View.GONE);
                        strUser = username.getText().toString().trim();
                        new FrogotPassword().execute();
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
                strAttendance = cursor.getString(cursor.getColumnIndex(mContext.getString(R.string.column_attendance)));

                String resultData = cursor.getString(cursor.getColumnIndex(mContext.getString(R.string.column_result)));
                sharedPref.clearPref();
                sharedPref.setSharedPrefLogin(email, loginId, mobile, resultData, status, userId, is_rm);
                if (strAttendance.equals("Present")) {
                    result = true;
                } else {
                    result = false;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public LoginDetailsModel loginDetails(Cursor cursor) {
        loginDetailsModel = new LoginDetailsModel();
        try {
            loginDetailsModel.setLogin_LoginID(cursor.getString(cursor.getColumnIndex("loginId")));
            loginDetailsModel.setLogin_UserID(cursor.getString(cursor.getColumnIndex("userId")));
            loginDetailsModel.setLogin_Email(cursor.getString(cursor.getColumnIndex("email")));
            loginDetailsModel.setLogin_Mobile_no(cursor.getString(cursor.getColumnIndex("mobile")));
            loginDetailsModel.setLogin_Result(cursor.getString(cursor.getColumnIndex("result")));
            loginDetailsModel.setLogin_Status(cursor.getString(cursor.getColumnIndex("status")));
            loginDetailsModel.setLogin_Username(cursor.getString(cursor.getColumnIndex("username")));
            loginDetailsModel.setLogin_IsRM(cursor.getString(cursor.getColumnIndex("is_rm")));
            loginDetailsModel.setLogin_Attendence(cursor.getString(cursor.getColumnIndex("Attendance")));


        } catch (Exception e) {
            e.printStackTrace();
        }
        return loginDetailsModel;

    }

    private void insertDataInDb() {
        try {


            String columnNames[] = {mContext.getString(R.string.column_userId), mContext.getString(R.string.column_loginId), mContext.getString(R.string.column_email), mContext.getString(R.string.column_mobile),
                    mContext.getString(R.string.column_result), mContext.getString(R.string.column_status)
                    , mContext.getString(R.string.column_username),
                    mContext.getString(R.string.column_password),
                    mContext.getString(R.string.column_attendance),
                    mContext.getString(R.string.column_role_id)};
            String valuesArray[] = {userId, loginId, email, mobile, result, status, strUser, strPass, strAttendance, is_rm};
            // WHERE   clause
            String selection = mContext.getString(R.string.column_loginId) + " = ?";

            // WHERE clause arguments
            String[] selectionArgs = {loginId};

            boolean result = Narnolia.dbCon.update(DbHelper.TABLE_USER_DETAIL, selection, valuesArray, columnNames, selectionArgs);

            if (result) {

                try {
                    String where = " where loginId = '" + loginId + "'";

                    Cursor cursor = Narnolia.dbCon.fetchFromSelect(DbHelper.TABLE_USER_DETAIL, where);
                    if (cursor != null && cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        do {
                            loginDetailsModel = loginDetails(cursor);
//                                        leadInfoModelList.add(leadInfoModel);

                        } while (cursor.moveToNext());
                        cursor.close();

                    } else {
                        Toast.makeText(mContext, "No data found..!", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
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
            LoginWebService webService = new LoginWebService(mContext);
            String attendance = "";
         /*   String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());*/

            SoapObject object1 = webService.LoginLead(strUser, strPass, "APP", versionName, lat, lang, attendance, currentDate, location, "");

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
                    is_rm = res.getPropertyAsString("is_rm");

                    sharedPref.clearPref();
                    sharedPref.setSharedPrefLogin(email, loginId, mobile, result, status, userId, is_rm);

                    insertDataInDb();

                   /* pushActivity(LoginActivity.this, HomeActivity.class, null, true);*/

                    Bundle bundle1 = new Bundle();
                    bundle1.putString("from_login", "FromLogin"); ///bundle1.putString("key", "value");
                    pushActivity(LoginActivity.this, MyCalendarActivity.class, bundle1, true);
                    sharedPref.setSharedPrefLoginWithPass(strUser, strPass, status, "App", versionName, lat, lang, "", currentDate, location);

                }


            } catch (Exception e) {
                System.out.println("AyushmanBhav : " + e);
                e.printStackTrace();
            }
        }
    }

    public class FrogotPassword extends AsyncTask<Void, Void, SoapObject> {

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
                object = webService.ForgotPassword(strUser);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return object;
        }

        @Override
        protected void onPostExecute(SoapObject soapObject) {
            super.onPostExecute(soapObject);
            try {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                responseId = String.valueOf(soapObject);
                if (responseId.equals("anyType{}")) {
                    Toast.makeText(mContext, "Sorry you Enterd Wrong Username", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                    SoapObject root = (SoapObject) soapObject.getProperty(i);
                    if (root.getProperty("email_id") != null) {

                        if (!root.getProperty("email_id").toString().equalsIgnoreCase("anyType{}")) {
                            f_Mailid = root.getProperty("email_id").toString();
                        } else {
                            f_Mailid = "";
                        }
                    } else {
                        f_Mailid = "";
                    }
                    if (root.getProperty("lead_status") != null) {

                        if (!root.getProperty("lead_status").toString().equalsIgnoreCase("anyType{}")) {
                            forgot_flag = root.getProperty("lead_status").toString();
                        } else {
                            forgot_flag = "";
                        }
                    } else {
                        forgot_flag = "";
                    }
                    if (forgot_flag.equals("True")) {
                        forgot_pass_message.setVisibility(View.VISIBLE);
                    } else {
                        forgot_pass_message.setVisibility(View.GONE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public class AttendenceReportDateWise extends AsyncTask<Void, Void, SoapObject> {

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


                object = webService.Attendence_Report_Datewise(strUser, currentDate);
              /*  statusReportModel.getStatus_1()*/
            } catch (Exception e) {
                e.printStackTrace();
            }
            return object;
        }

        @Override
        protected void onPostExecute(SoapObject soapObject) {
            super.onPostExecute(soapObject);
            try {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                responseId = String.valueOf(soapObject);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                    SoapObject root = (SoapObject) soapObject.getProperty(i);


                    if (root.getProperty("emp_code") != null && !root.getProperty("emp_code").toString().equalsIgnoreCase("?")) {

                        if (!root.getProperty("emp_code").toString().equalsIgnoreCase("anyType{}")) {
                            user_check = root.getProperty("emp_code").toString();
                        } else {
                            user_check = "";
                        }
                    } else {
                        user_check = "";
                    }

                    if (root.getProperty("attendance1") != null && !root.getProperty("attendance1").toString().equalsIgnoreCase("?")) {

                        if (!root.getProperty("attendance1").toString().equalsIgnoreCase("anyType{}")) {
                            attendance_check = root.getProperty("attendance1").toString();


                        } else {
                            attendance_check = "";
                        }
                    } else {
                        attendance_check = "";
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (strUser.equals(user_check) && attendance_check.equals("Present")) {
                    pushActivity(LoginActivity.this, HomeActivity.class, null, true);
                    sharedPref.setSharedPrefLogin(email, strUser, mobile, result, status, userId, is_rm);
                }else if (strUser.equals(user_check) && attendance_check.equals("Absent")) {
                    Toast.makeText(mContext, "This user is Absent today", Toast.LENGTH_SHORT).show();
                } else {
                    new UserLogin().execute();
                }
            }


        }
    }


}
