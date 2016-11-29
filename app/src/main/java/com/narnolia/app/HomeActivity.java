package com.narnolia.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.narnolia.app.model.LeadInfoModel;
import com.narnolia.app.network.SOAPWebService;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AbstractActivity implements View.OnClickListener {

    LinearLayout linear_dashboard, linear_create_lead, linear_update_lead, linear_master,
            linear_setting, linear_mis_reports, linear_notification;
    private Context mContext;
    private Utils utils;
    String[] strLeadArray = null;
    private String empcode;
    private SharedPref sharedPref;
    private TextView admin;
    private ProgressDialog progressDialog;
    private String responseId;

    LeadInfoModel leadInfoModel;
    List<LeadInfoModel> leadInfoModelList;


    String spinMasterList[]={"--select--","Prakash","Bhavin","Chirag","Yakub"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = new SharedPref(HomeActivity.this);
        empcode = sharedPref.getLoginId();
        progressDialog = new ProgressDialog(HomeActivity.this);

      //  new GetLead().execute();
        initView();
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

            linear_dashboard.setOnClickListener(this);
            linear_create_lead.setOnClickListener(this);
            linear_update_lead.setOnClickListener(this);
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
                    finish();
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

              //  showReportDialog();

            break;
            case R.id.linear_notification:

                break;
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
                String get_for = "app";
                SOAPWebService webService = new SOAPWebService(mContext);
                object = webService.GetLead(empcode,"",get_for);

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
            finally {
                if (progressDialog != null) {
                        progressDialog.dismiss();
                    }

            }
        }
    }


    public void getAllLeadData() {

        leadInfoModelList = new ArrayList<>();
        try {
            // WHERE clause
            //String where = " where last_sync = '0'";
            Cursor cursor = Narnolia.dbCon.getAllDataFromTable(DbHelper.TABLE_DIRECT_LEAD);
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

        } catch (Exception e) {
            e.printStackTrace();
        }
        return leadInfoModel;

    }

}
