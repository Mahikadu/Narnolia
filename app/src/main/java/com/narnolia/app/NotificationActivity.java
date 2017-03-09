package com.narnolia.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.narnolia.app.adapter.NotificationAdapter;
import com.narnolia.app.dbconfig.DbHelper;
import com.narnolia.app.libs.Utils;
import com.narnolia.app.model.GetMessagesModel;
import com.narnolia.app.model.LeadInfoModel;
import com.narnolia.app.network.SOAPWebService;

import org.ksoap2.serialization.SoapObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sudesi on 09/03/2017.
 */

public class NotificationActivity extends AbstractActivity{
    private Context mContext;
    private List<GetMessagesModel> getMessagesModelList;
    private ListView lvNotification;
    public Utils utils;
    private ProgressDialog progressDialog;
   String empcode;
    private String responseId;
    private SharedPref sharedPref;
    String str_date,strId, strEmp,str_Message,str_Result,str_Role;
    GetMessagesModel getMessagesModel;
    public NotificationAdapter notificationAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_notification);
        mContext = NotificationActivity.this;
        utils = new Utils(mContext);
        sharedPref = new SharedPref(mContext);
        empcode = sharedPref.getLoginId();

        progressDialog = new ProgressDialog(mContext);
        getMessagesModelList = new ArrayList<>();
        setHeader();

        new GetMessages().execute();
    }
    //...........Header View
    private void setHeader() {
        try {
            TextView tvHeader = (TextView) findViewById(R.id.textTitle);
            ImageView ivHome = (ImageView) findViewById(R.id.iv_home);
            ImageView ivLogout = (ImageView) findViewById(R.id.iv_logout);
            lvNotification = (ListView) findViewById(R.id.listNotification);
            tvHeader.setText(mContext.getResources().getString(R.string.header_text_dashboard));

            ivHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pushActivity(mContext, HomeActivity.class, null, true);
                }
            });

            ivLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pushActivity(mContext, HomeActivity.class, null, true);
                }
            });
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }
    public class GetMessages extends AsyncTask<Void, Void, SoapObject> {

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
                String roleid="2";
                String Dates="";
                object = webService.GetMessages(empcode,roleid,Dates);

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
                getMessagesModelList.clear();
                for (int i = 0; i < soapObject.getPropertyCount(); i++){
                    SoapObject root = (SoapObject) soapObject.getProperty(i);


                    if (root.getProperty("Date") != null) {

                        if (!root.getProperty("Date").toString().equalsIgnoreCase("anyType{}")) {
                            str_date = root.getProperty("Date").toString();
                           /* DateFormat inputDF = new SimpleDateFormat("dd/MM/yyyy");
                            DateFormat outputDF = new SimpleDateFormat("dd/MM/yyyy");
                            Date date = inputDF.parse(date11);
                            str_date = outputDF.format(date);*/

                        } else {
                            str_date = "";
                        }
                    } else {
                        str_date = "";
                    }
                    if (root.getProperty("Id") != null) {

                        if (!root.getProperty("Id").toString().equalsIgnoreCase("anyType{}")) {
                            strId = root.getProperty("Id").toString();

                        } else {
                            strId = "";
                        }
                    } else {
                        strId= "";
                    }

                    if (root.getProperty("Emp") != null) {

                        if (!root.getProperty("Emp").toString().equalsIgnoreCase("anyType{}")) {
                            strEmp = root.getProperty("Emp").toString();

                        } else {
                            strEmp = "";
                        }
                    } else {
                        strEmp= "";
                    }
                    if (root.getProperty("Message") != null) {

                        if (!root.getProperty("Message").toString().equalsIgnoreCase("anyType{}")) {
                            str_Message = root.getProperty("Message").toString();

                        } else {
                            str_Message = "";
                        }
                    } else {
                        str_Message = "";
                    }
                    if (root.getProperty("Result") != null) {

                        if (!root.getProperty("Result").toString().equalsIgnoreCase("anyType{}")) {
                            str_Result = root.getProperty("Result").toString();

                        } else {
                            str_Result = "";
                        }
                    } else {
                        str_Result = "";
                    }
                    if (root.getProperty("Role") != null) {

                        if (!root.getProperty("Role").toString().equalsIgnoreCase("anyType{}")) {
                            str_Role = root.getProperty("Role").toString();

                        } else {
                            str_Role = "";
                        }
                    } else {
                        str_Role = "";
                    }


                    getMessagesModel = new GetMessagesModel(str_date, strEmp, strId, str_Message, str_Result, str_Role);
                    getMessagesModelList.add(getMessagesModel);

                }

                if (getMessagesModelList != null && getMessagesModelList.size() > 0) {
                    notificationAdapter = new NotificationAdapter(mContext, getMessagesModelList);
                    lvNotification.setAdapter(notificationAdapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }



}
