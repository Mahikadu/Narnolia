package com.narnolia.app;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.narnolia.app.dbconfig.DbHelper;
import com.narnolia.app.libs.Utils;
import com.narnolia.app.model.ClientDetailsModel;
import com.narnolia.app.model.LeadInfoModel;
import com.narnolia.app.network.MyScheduledReceiver;
import com.narnolia.app.network.SOAPWebService;

import org.ksoap2.serialization.SoapObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AbstractActivity implements View.OnClickListener {

    LinearLayout linear_dashboard, linear_create_lead, linear_update_lead, linear_attendence,
            linear_setting, linear_mis_reports, linear_notification;
    private Context mContext;
    private Utils utils;
    String[] strLeadArray = null;
    private String empcode,leadid,RMCode;
    private SharedPref sharedPref;
    private TextView admin;
    private ProgressDialog progressDialog;
    private String responseId;

    LeadInfoModel leadInfoModel;
    ClientDetailsModel clientDetailsModel;
    List<LeadInfoModel> leadInfoModelList;
    List<ClientDetailsModel>clientInfoModelList;

    private String strFlag,
           leadId,str_cust_id, str_fname,str_mname,str_lname,str_mobile_no,str_email,str_date_of_birth,str_address,str_flat,str_street,str_laocion,str_city,
            str_pincode,str_next_meeting_date,str_metting_agenda,str_lead_update_log,
            str_reason,str_spinner_lead_name, str_spinner_source_of_lead, str_spinner_sub_source, str_spinner_age_group,
            str_spinner_occupation,str_spinner_annual_income, str_spinner_other_broker,str_spinner_lead_status,str_spinner_research_type,
            strCreatedfrom, strAppVersion, strAppdt, strAllocated_userid,
    strCompitator_Name, strProduct, strRemark,str_rg_meeting_status,
    strPanNo, strB_Margin, strB_aum, strB_sip, strB_number, strB_value, strB_premium,strEmpCode,
    strCreatedby, strCreateddt, strUpdateddt, strUpdatedby
    ,strBusiness_opp,strLastMeetingDate,strLastMeetingUpdate,str_spinner_duration,strCustomer_id_name;
    private String Address,AnnualIncome,BirthDate,Branchid,City,ClientCat,ClientID,ClientName,EmailAddress,MobileNumber,PanNumber,PinCode,RMName,StateId,
            Telephonenumber,Ucc,bankaccount,bankname,cStatus,dopeningDate,dpac,dpid,ifsc,micr,result;


    String spinMasterList[]={"--select--","Prakash","Bhavin","Chirag","Yakub"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = new SharedPref(HomeActivity.this);
        empcode = sharedPref.getLoginId();
        progressDialog = new ProgressDialog(HomeActivity.this);

       new GetLead().execute();
        initView();
        try {
            //Create alarm manager

            AlarmManager mAlarmManger = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            //Create pending intent & register it to your alarm notifier class
            Intent intent = new Intent(this, MyScheduledReceiver.class);
            intent.putExtra("uur", "1e"); // if you wanst
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

            //set timer you want alarm to work (here I have set it to 24.00)
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 24);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            //set that timer as a RTC Wakeup to alarm manager object
            mAlarmManger.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        try {

            getAllLeadData();

            mContext = HomeActivity.this;

            setHeader();

            utils = new Utils(mContext);

            admin = (TextView) findViewById(R.id.admin);
            admin.setText(empcode);

            //Dashboard icon reference
            linear_dashboard = (LinearLayout) findViewById(R.id.linear_dashboard);
            linear_create_lead = (LinearLayout) findViewById(R.id.linear_create_lead);
            linear_update_lead = (LinearLayout) findViewById(R.id.linear_update_lead);
//            linear_master = (LinearLayout) findViewById(R.id.linear_master);
//            linear_setting = (LinearLayout) findViewById(R.id.linear_setting);
            linear_mis_reports = (LinearLayout) findViewById(R.id.linear_mis_reports);
            linear_notification = (LinearLayout) findViewById(R.id.linear_notification);
            linear_attendence=(LinearLayout)findViewById(R.id.linear_attendence);

            linear_dashboard.setOnClickListener(this);
            linear_create_lead.setOnClickListener(this);
            linear_update_lead.setOnClickListener(this);
            linear_attendence.setOnClickListener(this);
//            linear_master.setOnClickListener(this);
//            linear_setting.setOnClickListener(this);
            linear_mis_reports.setOnClickListener(this);
            linear_notification.setOnClickListener(this);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setHeader() {
        try {
            TextView tvHeader = (TextView) findViewById(R.id.textTitle);
            ImageView ivHome = (ImageView) findViewById(R.id.iv_home);
            ImageView ivLogout = (ImageView) findViewById(R.id.iv_logout);

            tvHeader.setText(mContext.getResources().getString(R.string.header_text_home));

            ivHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    finish();
                }
            });

            ivLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Narnolia");
                    builder.setMessage("Are you sure you want to LogOut?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    utils.logout(mContext);
                                    pushActivity(mContext, LoginActivity.class, null, true);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                }
            });
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.linear_dashboard:
                pushActivity(mContext, DashboardActivity.class, null, true);
                break;
            case R.id.linear_create_lead:
                pushActivity(mContext, LeadActivity.class, null, true);
                break;
            case R.id.linear_update_lead:
                pushActivity(mContext, UpdateLeadActivity.class, null, true);
                break;
          /*  case R.id.linear_master:

                showMasterDialog();

                break;
            case R.id.linear_setting:

                showSettingDialog();

                break;*/
            case R.id.linear_mis_reports:
                pushActivity(mContext, StatusReportActivity.class, null, true);


            break;
            case R.id.linear_notification:
                pushActivity(mContext, NotificationActivity.class, null, true);
                break;
            case R.id.linear_attendence:
                Bundle bundle = new Bundle();
                bundle.putString("from_home","FromHome");
                pushActivity(mContext, MyCalendarActivity.class, bundle, true);

        }

    }


    private void showMasterDialog(){

       final AlertDialog.Builder DialogMaster = new AlertDialog.Builder(this);

        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogViewMaster = li.inflate(R.layout.master_custom_dialog, null);
        DialogMaster.setView(dialogViewMaster);

        final AlertDialog showMaster = DialogMaster.show();

        Button btnDismissMaster = (Button) showMaster.findViewById(R.id.iv_close);
        TextView tvHeaderMaster = (TextView) showMaster.findViewById(R.id.textTitle);
        TextView tvnameMaster = (TextView) showMaster.findViewById(R.id.txtmaster);

        tvHeaderMaster.setText(mContext.getResources().getString(R.string.master));
        tvnameMaster.setText(mContext.getResources().getString(R.string.master));

        Spinner spinnerMaster = (Spinner) showMaster
                .findViewById(R.id.spin_master);




        ArrayAdapter<String> adapterMaster = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinMasterList) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                View v = null;
                // If this is the initial dummy entry, make it hidden
                if (position == 0) {
                    TextView tv = new TextView(getContext());
                    tv.setHeight(0);
                    tv.setVisibility(View.GONE);
                    v = tv;
                }
                else {
                    // Pass convertView as null to prevent reuse of special case views
                    v = super.getDropDownView(position, null, parent);
                }
                // Hide scroll bar because it appears sometimes unnecessarily, this does not prevent scrolling
                parent.setVerticalScrollBarEnabled(false);
                return v;
            }
        };

        adapterMaster.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaster.setAdapter(adapterMaster);
        spinnerMaster.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int arg2, long arg3) {
                String selItem = parent.getSelectedItem().toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        btnDismissMaster.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                showMaster.dismiss();
            }
        });


    }

    private void showSettingDialog(){


        final AlertDialog.Builder DialogMaster = new AlertDialog.Builder(this);

        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogViewMaster = li.inflate(R.layout.master_custom_dialog, null);
        DialogMaster.setView(dialogViewMaster);

        final AlertDialog showMaster = DialogMaster.show();

        Button btnDismissMaster = (Button) showMaster.findViewById(R.id.iv_close);
        TextView tvHeaderMaster = (TextView) showMaster.findViewById(R.id.textTitle);
        TextView tvnameMaster = (TextView) showMaster.findViewById(R.id.txtmaster);

        tvHeaderMaster.setText(mContext.getResources().getString(R.string.setting));
        tvnameMaster.setText(mContext.getResources().getString(R.string.setting));

        Spinner spinnerMaster = (Spinner) showMaster
                .findViewById(R.id.spin_master);



        ArrayAdapter<String> adapterMaster = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinMasterList) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                View v = null;
                // If this is the initial dummy entry, make it hidden
                if (position == 0) {
                    TextView tv = new TextView(getContext());
                    tv.setHeight(0);
                    tv.setVisibility(View.GONE);
                    v = tv;
                }
                else {
                    // Pass convertView as null to prevent reuse of special case views
                    v = super.getDropDownView(position, null, parent);
                }
                // Hide scroll bar because it appears sometimes unnecessarily, this does not prevent scrolling
                parent.setVerticalScrollBarEnabled(false);
                return v;
            }
        };

        adapterMaster.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaster.setAdapter(adapterMaster);

        spinnerMaster.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int arg2, long arg3) {
                String selItem = parent.getSelectedItem().toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        btnDismissMaster.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                showMaster.dismiss();
            }
        });
    }

    private void showReportDialog(){


        final AlertDialog.Builder DialogMaster = new AlertDialog.Builder(this);

        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogViewMaster = li.inflate(R.layout.master_custom_dialog, null);
        DialogMaster.setView(dialogViewMaster);

        final AlertDialog showMaster = DialogMaster.show();

        Button btnDismissMaster = (Button) showMaster.findViewById(R.id.iv_close);
        TextView tvHeaderMaster = (TextView) showMaster.findViewById(R.id.textTitle);
        TextView tvnameMaster = (TextView) showMaster.findViewById(R.id.txtmaster);

        tvHeaderMaster.setText(mContext.getResources().getString(R.string.mis_report));
        tvnameMaster.setText(mContext.getResources().getString(R.string.mis_report));

        Spinner spinnerMaster = (Spinner) showMaster
                .findViewById(R.id.spin_master);



        ArrayAdapter<String> adapterMaster = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinMasterList) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                View v = null;
                // If this is the initial dummy entry, make it hidden
                if (position == 0) {
                    TextView tv = new TextView(getContext());
                    tv.setHeight(0);
                    tv.setVisibility(View.GONE);
                    v = tv;
                }
                else {
                    // Pass convertView as null to prevent reuse of special case views
                    v = super.getDropDownView(position, null, parent);
                }
                // Hide scroll bar because it appears sometimes unnecessarily, this does not prevent scrolling
                parent.setVerticalScrollBarEnabled(false);
                return v;
            }
        };

        adapterMaster.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaster.setAdapter(adapterMaster);

        spinnerMaster.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int arg2, long arg3) {
                String selItem = parent.getSelectedItem().toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        btnDismissMaster.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                showMaster.dismiss();
            }
        });
    }

    public class GetLead extends AsyncTask<Void, Void, SoapObject> {

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
                object = webService.GetLead(empcode);

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
            } catch (Exception e) {
                e.printStackTrace();
            }


            //response :anyType{Lead=anyType{Address1=null; Address2=null; Address3=null; Age=null; BranchCode=null; Channel=null;
            //City=anyType{}; Country=null; Currency=null; DOB=null; Designation=null; Duration_date=null; Duration_date_2=null;
            // EmailId=null; FName=KHSAGFK; Flag=null; Gender=null; Id=0; Income=null; LG_DST_Code=null; LName=null; L_Country=null;
            // L_Std=null; Landline=null; Last_meeting_date=anyType{}; LeadSource=null; Lead_id=L30097; MName=null;
            // MobileNo=9879879879; NextCallDate=null; Occupation=null; Pincode=anyType{}; Remarks=null; Result=null;
            // Stages=null; SubSource=null; app_version=null; empcode=null; last_meeting_update=anyType{}; lead_status=anyType{};
            // updated_date=null; };


            try {
                for (int i = 0; i < soapObject.getPropertyCount(); i++){
                    SoapObject root = (SoapObject) soapObject.getProperty(i);


                    if (root.getProperty("Address1") != null) {

                        if (!root.getProperty("Address1").toString().equalsIgnoreCase("anyType{}")) {
                            str_address = root.getProperty("Address1").toString();

                        } else {
                            str_address = "";
                        }
                    } else {
                        str_address = "";
                    }

                    if (root.getProperty("Address2") != null) {

                        if (!root.getProperty("Address2").toString().equalsIgnoreCase("anyType{}")) {
                            str_flat = root.getProperty("Address2").toString();

                        } else {
                            str_flat = "";
                        }
                    } else {
                        str_flat = "";
                    }

                    if (root.getProperty("Address3") != null) {

                        if (!root.getProperty("Address3").toString().equalsIgnoreCase("anyType{}")) {
                            str_street = root.getProperty("Address3").toString();

                        } else {
                            str_street = "";
                        }
                    } else {
                        str_street = "";
                    }

                    if (root.getProperty("City") != null) {

                        if (!root.getProperty("City").toString().equalsIgnoreCase("anyType{}")) {
                            str_city = root.getProperty("City").toString();

                        } else {
                            str_city = "";
                        }
                    } else {
                        str_city = "";
                    }

                    if (root.getProperty("Duration_date") != null) {

                        if (!root.getProperty("Duration_date").toString().equalsIgnoreCase("anyType{}")) {
                            str_spinner_duration = root.getProperty("Duration_date").toString();

                        } else {
                            str_spinner_duration = "";
                        }
                    } else {
                        str_spinner_duration = "";
                    }

                    if (root.getProperty("EmailId") != null) {

                        if (!root.getProperty("EmailId").toString().equalsIgnoreCase("anyType{}")) {
                            str_email = root.getProperty("EmailId").toString();

                        } else {
                            str_email = "";
                        }
                    } else {
                        str_email = "";
                    }

                    if (root.getProperty("FName") != null) {

                        if (!root.getProperty("FName").toString().equalsIgnoreCase("anyType{}")) {
                            str_fname = root.getProperty("FName").toString();

                        } else {
                            str_fname = "";
                        }
                    } else {
                        str_fname = "";
                    }

                    if (root.getProperty("Income") != null) {

                        if (!root.getProperty("Income").toString().equalsIgnoreCase("anyType{}")) {
                            str_spinner_annual_income = root.getProperty("Income").toString();

                        } else {
                            str_spinner_annual_income = "";
                        }
                    } else {
                        str_spinner_annual_income = "";
                    }

                    if (root.getProperty("LName") != null) {

                        if (!root.getProperty("LName").toString().equalsIgnoreCase("anyType{}")) {
                            str_lname = root.getProperty("LName").toString();

                        } else {
                            str_lname = "";
                        }
                    } else {
                        str_lname = "";
                    }

                    if (root.getProperty("Flag") != null) {

                        if (!root.getProperty("Flag").toString().equalsIgnoreCase("anyType{}")) {
                            strFlag = root.getProperty("Flag").toString();

                        } else {
                            strFlag = "";
                        }
                    } else {
                        strFlag = "";
                    }

                    if (root.getProperty("MName") != null) {

                        if (!root.getProperty("MName").toString().equalsIgnoreCase("anyType{}")) {
                            str_mname = root.getProperty("MName").toString();

                        } else {
                            str_mname = "";
                        }
                    } else {
                        str_mname = "";
                    }

                    if (root.getProperty("Last_meeting_date") != null) {

                        if (!root.getProperty("Last_meeting_date").toString().equalsIgnoreCase("anyType{}")) {
                            String lastmeeting = root.getProperty("Last_meeting_date").toString();
                            DateFormat inputDF = new SimpleDateFormat("M/dd/yyyy");
                            DateFormat outputDF = new SimpleDateFormat("M/dd/yyyy");
                            Date date = inputDF.parse(lastmeeting);
                            strLastMeetingDate = outputDF.format(date);

                        } else {
                            strLastMeetingDate = "";
                        }
                    } else {
                        strLastMeetingDate = "";
                    }

                    if (root.getProperty("last_meeting_update") != null) {

                        if (!root.getProperty("last_meeting_update").toString().equalsIgnoreCase("anyType{}")) {
                            strLastMeetingUpdate = root.getProperty("last_meeting_update").toString();

                        } else {
                            strLastMeetingUpdate = "";
                        }
                    } else {
                        strLastMeetingUpdate = "";
                    }

                    if (root.getProperty("lead_status") != null) {

                        if (!root.getProperty("lead_status").toString().equalsIgnoreCase("anyType{}")) {
                            str_spinner_lead_status = root.getProperty("lead_status").toString();

                        } else {
                            str_spinner_lead_status = "";
                        }
                    } else {
                        str_spinner_lead_status = "";
                    }

                    if (root.getProperty("updated_date") != null) {

                        if (!root.getProperty("updated_date").toString().equalsIgnoreCase("anyType{}")) {
                            strUpdateddt = root.getProperty("updated_date").toString();

                        } else {
                            strUpdateddt = "";
                        }
                    } else {
                        strUpdateddt = "";
                    }

                    if (root.getProperty("DOB") != null) {

                        if (!root.getProperty("DOB").toString().equalsIgnoreCase("anyType{}")) {
                            String Dob = root.getProperty("DOB").toString();
                            DateFormat inputDF = new SimpleDateFormat("M/dd/yyyy");
                            DateFormat outputDF = new SimpleDateFormat("M/dd/yyyy");
                            Date date = inputDF.parse(Dob);
                            str_date_of_birth = outputDF.format(date);
                        } else {
                            str_date_of_birth = "";
                        }
                    } else {
                        str_date_of_birth = "";
                    }

                    if (root.getProperty("Lead_id") != null) {

                        if (!root.getProperty("Lead_id").toString().equalsIgnoreCase("anyType{}")) {
                            leadId = root.getProperty("Lead_id").toString();

                        } else {
                            leadId = "";
                        }
                    } else {
                        leadId = "";
                    }

                    if (root.getProperty("MobileNo") != null) {

                        if (!root.getProperty("MobileNo").toString().equalsIgnoreCase("anyType{}")) {
                            str_mobile_no = root.getProperty("MobileNo").toString();

                        } else {
                            str_mobile_no = "";
                        }
                    } else {
                        str_mobile_no = "";
                    }
                    if (root.getProperty("Occupation") != null) {

                        if (!root.getProperty("Occupation").toString().equalsIgnoreCase("anyType{}")) {
                            str_spinner_occupation = root.getProperty("Occupation").toString();

                        } else {
                            str_spinner_occupation = "";
                        }
                    } else {
                        str_spinner_occupation = "";
                    }

                    if (root.getProperty("Remarks") != null) {

                        if (!root.getProperty("Remarks").toString().equalsIgnoreCase("anyType{}")) {
                            strRemark = root.getProperty("Remarks").toString();

                        } else {
                            strRemark = "";
                        }
                    } else {
                        strRemark = "";
                    }

                    if (root.getProperty("Pincode") != null) {

                        if (!root.getProperty("Pincode").toString().equalsIgnoreCase("anyType{}")) {
                            str_pincode = root.getProperty("Pincode").toString();

                        } else {
                            str_pincode = "";
                        }
                    } else {
                        str_pincode = "";
                    }

                    if (root.getProperty("SubSource") != null) {

                        if (!root.getProperty("SubSource").toString().equalsIgnoreCase("anyType{}")) {
                            str_spinner_sub_source = root.getProperty("SubSource").toString();

                        } else {
                            str_spinner_sub_source = "";
                        }
                    } else {
                        str_spinner_sub_source = "";
                    }



                    String strLastSync = "1";
                    String strStages = "Lead Updated";
                    String selection = mContext.getString(R.string.column_lead_id) + " = ?";
                    String[] selectionArgs = {leadId};
                    String valuesArray[] = { "" + "",leadId,strStages, str_spinner_source_of_lead, str_spinner_sub_source, str_cust_id, str_fname, str_mname, str_lname,
                            str_date_of_birth, str_spinner_age_group, str_mobile_no, str_address, str_flat, str_street, str_laocion, str_city, str_pincode, str_email, str_spinner_annual_income,
                            str_spinner_occupation, strCreatedfrom, strAppVersion, strAppdt, strFlag, strAllocated_userid, str_spinner_other_broker,
                            str_rg_meeting_status, str_spinner_lead_status, strCompitator_Name, strProduct, strRemark, str_spinner_research_type,
                            str_spinner_duration, strPanNo, strB_Margin, strB_aum, strB_sip, strB_number, strB_value, strB_premium, str_reason,
                            str_next_meeting_date, str_metting_agenda, "", strCreatedby, strCreateddt, strUpdateddt, strUpdatedby,strEmpCode,
                            strLastMeetingDate,strLastMeetingUpdate,strBusiness_opp,strCustomer_id_name,""};

                    boolean result = Narnolia.dbCon.update(DbHelper.TABLE_DIRECT_LEAD, selection, valuesArray, utils.columnNamesLeadUpdate, selectionArgs);








                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            finally {

                new Get_ALL_Client().execute();
            }
        }
    }
//.....................................get All Client..........................................
public class Get_ALL_Client extends AsyncTask<Void, Void, SoapObject> {

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
        RMCode = "CH";
        try {
            SOAPWebService webService = new SOAPWebService(mContext);
            object = webService.Get_ALL_Client(RMCode);

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            for (int i = 0; i < soapObject.getPropertyCount(); i++){
                SoapObject root = (SoapObject) soapObject.getProperty(i);


                if (root.getProperty("Address") != null) {

                    if (!root.getProperty("Address").toString().equalsIgnoreCase("anyType{}")) {
                        Address = root.getProperty("Address").toString();

                    } else {
                        Address = "";
                    }
                } else {
                    Address = "";
                }

                if (root.getProperty("AnnualIncome") != null) {

                    if (!root.getProperty("AnnualIncome").toString().equalsIgnoreCase("anyType{}")) {
                        AnnualIncome = root.getProperty("AnnualIncome").toString();

                    } else {
                        AnnualIncome = "";
                    }
                } else {
                    AnnualIncome = "";
                }
                if (root.getProperty("BirthDate") != null) {

                    if (!root.getProperty("BirthDate").toString().equalsIgnoreCase("anyType{}")) {
                        BirthDate = root.getProperty("BirthDate").toString();

                    } else {
                        BirthDate = "";
                    }
                } else {
                    BirthDate = "";
                }

                if (root.getProperty("Branchid") != null) {

                    if (!root.getProperty("Branchid").toString().equalsIgnoreCase("anyType{}")) {
                        Branchid = root.getProperty("Branchid").toString();

                    } else {
                        Branchid = "";
                    }
                } else {
                    Branchid = "";
                }

                if (root.getProperty("City") != null) {

                    if (!root.getProperty("City").toString().equalsIgnoreCase("anyType{}")) {
                        City = root.getProperty("City").toString();

                    } else {
                        City = "";
                    }
                } else {
                    City = "";
                }

                if (root.getProperty("ClientCat") != null) {

                    if (!root.getProperty("ClientCat").toString().equalsIgnoreCase("anyType{}")) {
                        ClientCat = root.getProperty("ClientCat").toString();

                    } else {
                        ClientCat = "";
                    }
                } else {
                    ClientCat = "";
                }

                if (root.getProperty("ClientID") != null) {

                    if (!root.getProperty("ClientID").toString().equalsIgnoreCase("anyType{}")) {
                        ClientID = root.getProperty("ClientID").toString();

                    } else {
                        ClientID = "";
                    }
                } else {
                    ClientID = "";
                }

                if (root.getProperty("ClientName") != null) {

                    if (!root.getProperty("ClientName").toString().equalsIgnoreCase("anyType{}")) {
                        ClientName = root.getProperty("ClientName").toString();

                    } else {
                        ClientName = "";
                    }
                } else {
                    ClientName = "";
                }

                if (root.getProperty("EmailAddress") != null) {

                    if (!root.getProperty("EmailAddress").toString().equalsIgnoreCase("anyType{}")) {
                        EmailAddress = root.getProperty("EmailAddress").toString();

                    } else {
                        EmailAddress = "";
                    }
                } else {
                    EmailAddress = "";
                }

                if (root.getProperty("MobileNumber") != null) {

                    if (!root.getProperty("MobileNumber").toString().equalsIgnoreCase("anyType{}")) {
                        MobileNumber = root.getProperty("MobileNumber").toString();

                    } else {
                        MobileNumber = "";
                    }
                } else {
                    MobileNumber = "";
                }

                if (root.getProperty("PanNumber") != null) {

                    if (!root.getProperty("PanNumber").toString().equalsIgnoreCase("anyType{}")) {
                        PanNumber = root.getProperty("PanNumber").toString();

                    } else {
                        PanNumber = "";
                    }
                } else {
                    PanNumber = "";
                }

                if (root.getProperty("PinCode") != null) {

                    if (!root.getProperty("PinCode").toString().equalsIgnoreCase("anyType{}")) {
                        PinCode = root.getProperty("PinCode").toString();

                    } else {
                        PinCode = "";
                    }
                } else {
                    PinCode = "";
                }

                if (root.getProperty("RMCode") != null) {

                    if (!root.getProperty("RMCode").toString().equalsIgnoreCase("anyType{}")) {
                        RMCode = root.getProperty("RMCode").toString();

                    } else {
                        RMCode = "";
                    }
                } else {
                    RMCode = "";
                }

                if (root.getProperty("RMName") != null) {

                    if (!root.getProperty("RMName").toString().equalsIgnoreCase("anyType{}")) {
                        RMName = root.getProperty("RMName").toString();

                    } else {
                        RMName = "";
                    }
                } else {
                    RMName = "";
                }

                if (root.getProperty("StateId") != null) {

                    if (!root.getProperty("StateId").toString().equalsIgnoreCase("anyType{}")) {
                        StateId = root.getProperty("StateId").toString();

                    } else {
                        StateId = "";
                    }
                } else {
                    StateId = "";
                }

                if (root.getProperty("Telephonenumber") != null) {

                    if (!root.getProperty("Telephonenumber").toString().equalsIgnoreCase("anyType{}")) {
                        Telephonenumber = root.getProperty("Telephonenumber").toString();

                    } else {
                        Telephonenumber = "";
                    }
                } else {
                    Telephonenumber = "";
                }



                if (root.getProperty("Ucc") != null) {

                    if (!root.getProperty("Ucc").toString().equalsIgnoreCase("anyType{}")) {
                        Ucc = root.getProperty("Ucc").toString();

                    } else {
                        Ucc = "";
                    }
                } else {
                    Ucc = "";
                }

                if (root.getProperty("bankaccount") != null) {

                    if (!root.getProperty("bankaccount").toString().equalsIgnoreCase("anyType{}")) {
                        bankaccount = root.getProperty("bankaccount").toString();

                    } else {
                        bankaccount = "";
                    }
                } else {
                    bankaccount = "";
                }
                if (root.getProperty("bankname") != null) {

                    if (!root.getProperty("bankname").toString().equalsIgnoreCase("anyType{}")) {
                        bankname = root.getProperty("bankname").toString();

                    } else {
                        bankname = "";
                    }
                } else {
                    bankname = "";
                }

                if (root.getProperty("cStatus") != null) {

                    if (!root.getProperty("cStatus").toString().equalsIgnoreCase("anyType{}")) {
                        cStatus = root.getProperty("cStatus").toString();

                    } else {
                        cStatus = "";
                    }
                } else {
                    cStatus = "";
                }
                if (root.getProperty("dopeningDate") != null) {

                    if (!root.getProperty("dopeningDate").toString().equalsIgnoreCase("anyType{}")) {
                        dopeningDate = root.getProperty("dopeningDate").toString();

                    } else {
                        dopeningDate = "";
                    }
                } else {
                    dopeningDate = "";
                }
                if (root.getProperty("dpac") != null) {

                    if (!root.getProperty("dpac").toString().equalsIgnoreCase("anyType{}")) {
                        dpac = root.getProperty("dpac").toString();

                    } else {
                        dpac = "";
                    }
                } else {
                    dpac = "";
                }
                if (root.getProperty("dpid") != null) {

                    if (!root.getProperty("dpid").toString().equalsIgnoreCase("anyType{}")) {
                        dpid = root.getProperty("dpid").toString();

                    } else {
                        dpid = "";
                    }
                } else {
                    dpid = "";
                }
                if (root.getProperty("ifsc") != null) {

                    if (!root.getProperty("ifsc").toString().equalsIgnoreCase("anyType{}")) {
                        ifsc = root.getProperty("ifsc").toString();

                    } else {
                        ifsc = "";
                    }
                } else {
                    ifsc = "";
                }
                if (root.getProperty("micr") != null) {

                    if (!root.getProperty("micr").toString().equalsIgnoreCase("anyType{}")) {
                        micr = root.getProperty("micr").toString();

                    } else {
                        micr = "";
                    }
                } else {
                    micr = "";
                }
                if (root.getProperty("result") != null) {

                    if (!root.getProperty("result").toString().equalsIgnoreCase("anyType{}")) {
                        result = root.getProperty("result").toString();

                    } else {
                        result = "";
                    }
                } else {
                    result = "";
                }


               // String strLastSync = "1";
               // String strStages = "Lead Updated";
                String selection = mContext.getString(R.string.column_cust_id) + " = ?";
                String[] selectionArgs = {ClientID};
                String valuesArray[] = {Address,AnnualIncome,BirthDate,Branchid,City,ClientCat,ClientID,ClientName,
                        EmailAddress,MobileNumber,PanNumber,PinCode,RMCode,RMName,StateId,Telephonenumber,
                        Ucc,bankaccount,bankname,cStatus,dopeningDate,dpac,dpid,ifsc,micr,result};

                boolean result = Narnolia.dbCon.update(DbHelper.TABLE_CLIENT_DETAILS, selection, valuesArray, utils.columnNamesClientDetails, selectionArgs);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        finally {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

        }
    }
}
//.................................................................................................
    public void getAllLeadData() {

        leadInfoModelList = new ArrayList<>();
        try {
            // WHERE clause
            String where = " where flag NOT IN('D')";
            Cursor cursor = Narnolia.dbCon.fetchFromSelect(DbHelper.TABLE_DIRECT_LEAD, where);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    leadInfoModel = createLeadInfoModel(cursor);
                    leadInfoModelList.add(leadInfoModel);
                } while (cursor.moveToNext());
                cursor.close();
            }else {
                Toast.makeText(HomeActivity.this, "No data found..!",Toast.LENGTH_SHORT).show();
            }
          //  getAllClientData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public LeadInfoModel createLeadInfoModel(Cursor cursor) {
         leadInfoModel = new LeadInfoModel();
        try {
            leadInfoModel.setStages(cursor.getString(cursor.getColumnIndex("stages")));
            leadInfoModel.setLead_id(cursor.getString(cursor.getColumnIndex("lead_id")));
            leadInfoModel.setDirect_lead_id(cursor.getInt(cursor.getColumnIndex("direct_lead_id")));
            leadInfoModel.setSource_of_lead(cursor.getString(cursor.getColumnIndex("source_of_lead")));
            leadInfoModel.setSub_source(cursor.getString(cursor.getColumnIndex("sub_source")));
            leadInfoModel.setCustomerID(cursor.getString(cursor.getColumnIndex("customer_id")));
            leadInfoModel.setFirstname(cursor.getString(cursor.getColumnIndex("fname")));
            leadInfoModel.setMiddlename(cursor.getString(cursor.getColumnIndex("mname")));
            leadInfoModel.setLastname(cursor.getString(cursor.getColumnIndex("lname")));
            leadInfoModel.setDob(cursor.getString(cursor.getColumnIndex("dob")));
            leadInfoModel.setAge(cursor.getString(cursor.getColumnIndex("age")));
            leadInfoModel.setMobile_no(cursor.getString(cursor.getColumnIndex("mobile_no")));
            leadInfoModel.setAddress1(cursor.getString(cursor.getColumnIndex("address_1")));
            leadInfoModel.setAddress2(cursor.getString(cursor.getColumnIndex("address_2")));
            leadInfoModel.setAddress3(cursor.getString(cursor.getColumnIndex("address_3")));
            leadInfoModel.setLocation(cursor.getString(cursor.getColumnIndex("location")));
            leadInfoModel.setCity(cursor.getString(cursor.getColumnIndex("city")));
            leadInfoModel.setPincode(cursor.getString(cursor.getColumnIndex("pincode")));
            leadInfoModel.setEmail_id(cursor.getString(cursor.getColumnIndex("email_id")));
            leadInfoModel.setAnnual_income(cursor.getString(cursor.getColumnIndex("annual_income")));
            leadInfoModel.setOccupation(cursor.getString(cursor.getColumnIndex("occupation")));
            leadInfoModel.setCreatedfrom(cursor.getString(cursor.getColumnIndex("created_from")));
            leadInfoModel.setAppversion(cursor.getString(cursor.getColumnIndex("app_version")));
            leadInfoModel.setAppdt(cursor.getString(cursor.getColumnIndex("app_dt")));
            leadInfoModel.setFlag(cursor.getString(cursor.getColumnIndex("flag")));
            leadInfoModel.setAllocateduserid(cursor.getString(cursor.getColumnIndex("allocated_user_id")));
            leadInfoModel.setBrokerdetls(cursor.getString(cursor.getColumnIndex("other_broker_dealt_with")));
            leadInfoModel.setMeetingstatus(cursor.getString(cursor.getColumnIndex("meeting_status")));
            leadInfoModel.setLeadstatus(cursor.getString(cursor.getColumnIndex("lead_status")));
            leadInfoModel.setCompitatorname(cursor.getString(cursor.getColumnIndex("competitor_name")));
            leadInfoModel.setProduct(cursor.getString(cursor.getColumnIndex("product")));
            leadInfoModel.setRemark(cursor.getString(cursor.getColumnIndex("remarks")));
            leadInfoModel.setTypeofsearch(cursor.getString(cursor.getColumnIndex("typeofsearch")));
            leadInfoModel.setDuration(cursor.getString(cursor.getColumnIndex("duration")));
            leadInfoModel.setPanno(cursor.getString(cursor.getColumnIndex("pan_no")));
            leadInfoModel.setB_margin(cursor.getString(cursor.getColumnIndex("b_margin")));
            leadInfoModel.setB_aum(cursor.getString(cursor.getColumnIndex("b_aum")));
            leadInfoModel.setB_sip(cursor.getString(cursor.getColumnIndex("b_sip")));
            leadInfoModel.setB_number(cursor.getString(cursor.getColumnIndex("b_number")));
            leadInfoModel.setB_value(cursor.getString(cursor.getColumnIndex("b_value")));
            leadInfoModel.setB_premium(cursor.getString(cursor.getColumnIndex("b_premium")));
            leadInfoModel.setReason(cursor.getString(cursor.getColumnIndex("reason")));
            leadInfoModel.setMeetingdt(cursor.getString(cursor.getColumnIndex("next_meeting_date")));
            leadInfoModel.setMeetingagenda(cursor.getString(cursor.getColumnIndex("meeting_agenda")));
            leadInfoModel.setLead_updatelog(cursor.getString(cursor.getColumnIndex("lead_update_log")));
            leadInfoModel.setCreatedby(cursor.getString(cursor.getColumnIndex("created_by")));
            leadInfoModel.setCreateddt(cursor.getString(cursor.getColumnIndex("created_dt")));
            leadInfoModel.setUpdateddt(cursor.getString(cursor.getColumnIndex("updated_dt")));
            leadInfoModel.setUpdatedby(cursor.getString(cursor.getColumnIndex("updated_by")));
            leadInfoModel.setEmpcode(cursor.getString(cursor.getColumnIndex("emp_code")));
            leadInfoModel.setLast_meeting_date(cursor.getString(cursor.getColumnIndex("last_meeting_date")));
            leadInfoModel.setLast_meeting_update(cursor.getString(cursor.getColumnIndex("last_meeting_update")));
            leadInfoModel.setBusiness_opp(cursor.getString(cursor.getColumnIndex("business_opportunity")));
            leadInfoModel.setCustomer_id_name(cursor.getString(cursor.getColumnIndex("customer_id_name")));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return leadInfoModel;

    }
  /*  //.......................Client Details Model
    public void getAllClientData() {

        clientInfoModelList = new ArrayList<>();
        try {
            // WHERE clause
            String where = " where ClientID = '"+ClientID+"'";
            Cursor cursor = Narnolia.dbCon.fetchFromSelect(DbHelper.TABLE_CLIENT_DETAILS, where);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    clientDetailsModel = createClientDetailsModel(cursor);
                    clientInfoModelList.add(clientDetailsModel);

                } while (cursor.moveToNext());
                cursor.close();

            }else {
                Toast.makeText(HomeActivity.this, "No data found..!",Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public ClientDetailsModel createClientDetailsModel(Cursor cursor) {
        clientDetailsModel = new ClientDetailsModel();
        try {
            clientDetailsModel.setAddress(cursor.getString(cursor.getColumnIndex("Address")));
            clientDetailsModel.setAnnualIncome(cursor.getString(cursor.getColumnIndex("AnnualIncome")));
            clientDetailsModel.setBirthDate(cursor.getString(cursor.getColumnIndex("BirthDate")));
            clientDetailsModel.setBranchid(cursor.getString(cursor.getColumnIndex("Branchid")));
            clientDetailsModel.setCity(cursor.getString(cursor.getColumnIndex("City")));
            clientDetailsModel.setClientCat(cursor.getString(cursor.getColumnIndex("ClientCat")));
            clientDetailsModel.setClientID(cursor.getString(cursor.getColumnIndex("ClientID")));
            clientDetailsModel.setClientName(cursor.getString(cursor.getColumnIndex("ClientName")));
            clientDetailsModel.setEmailAddress(cursor.getString(cursor.getColumnIndex("EmailAddress")));
            clientDetailsModel.setMobileNumber(cursor.getString(cursor.getColumnIndex("MobileNumber")));
            clientDetailsModel.setPanNumber(cursor.getString(cursor.getColumnIndex("PanNumber")));
            clientDetailsModel.setPinCode(cursor.getString(cursor.getColumnIndex("PinCode")));
            clientDetailsModel.setRMCode(cursor.getString(cursor.getColumnIndex("RMCode")));
            clientDetailsModel.setRMName(cursor.getString(cursor.getColumnIndex("RMName")));
            clientDetailsModel.setStateId(cursor.getString(cursor.getColumnIndex("StateId")));
            clientDetailsModel.setTelephonenumber(cursor.getString(cursor.getColumnIndex("Telephonenumber")));
            clientDetailsModel.setUcc(cursor.getString(cursor.getColumnIndex("Ucc")));
            clientDetailsModel.setBankaccount(cursor.getString(cursor.getColumnIndex("bankaccount")));
            clientDetailsModel.setBankname(cursor.getString(cursor.getColumnIndex("bankname")));
            clientDetailsModel.setcStatus(cursor.getString(cursor.getColumnIndex("cStatus")));
            clientDetailsModel.setDopeningDate(cursor.getString(cursor.getColumnIndex("dopeningDate")));
            clientDetailsModel.setDpac(cursor.getString(cursor.getColumnIndex("dpac")));
            clientDetailsModel.setDpid(cursor.getString(cursor.getColumnIndex("dpid")));
            clientDetailsModel.setIfsc(cursor.getString(cursor.getColumnIndex("ifsc")));
            clientDetailsModel.setMicr(cursor.getString(cursor.getColumnIndex("micr")));
            clientDetailsModel.setResult(cursor.getString(cursor.getColumnIndex("result")));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return clientDetailsModel;

    }

    //................................................................
*/

}
