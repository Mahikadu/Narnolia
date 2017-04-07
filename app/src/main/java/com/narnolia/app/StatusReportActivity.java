package com.narnolia.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.narnolia.app.adapter.NotificationAdapter;
import com.narnolia.app.adapter.StatusReportAdapter;
import com.narnolia.app.adapter.SubStatusReportAdapter;
import com.narnolia.app.dbconfig.DbHelper;
import com.narnolia.app.model.LoginDetailsModel;
import com.narnolia.app.model.StatusReportModel;
import com.narnolia.app.model.SubStatusReportModel;
import com.narnolia.app.network.SOAPWebService;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import static com.narnolia.app.adapter.StatusReportAdapter.subStatusReportModelList;

/**
 * Created by Sudesi on 23/03/2017.
 */

public class StatusReportActivity extends AbstractActivity {
    private Context mContext;
    private ProgressDialog progressDialog;
    String empcode;
    private SharedPref sharedPref;
    private List<StatusReportModel> getStatusReportModelList;
    StatusReportModel statusReportModel;
    private List<LoginDetailsModel> loginDetailsModels;
    public LoginDetailsModel loginDetailsModel;
    private ListView lvStatusReport;
    private ListView lvSubStatusReport;
    LinearLayout linear_sub_status1;
//    public List<SubStatusReportModel> subStatusReportModelList;
    private String responseId;
    public StatusReportAdapter statusReportAdapter;
    public SubStatusReportAdapter subStatusReportAdapter;
    private TextView report_today1, report_t1_1, report_t2_1, report_t3_1, report_t4_1, report_t5_1, report_t6_1, report_t7_1, report_t_month_1, report_t_quarter_1;
    private TextView report_today, report_t1, report_t2, report_t3, report_t4, report_t5, report_t6, report_t7, report_t_month, report_t_quarter;
    String t1_1, t2_1, t3_1, t4_1, t5_1, t6_1, t7_1, t_month_1, t_quater_1, today_report_1, status_1;
    String t1, t2, t3, t4, t5, t6, t7, t_month, t_quater, today_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_report);
        mContext = StatusReportActivity.this;

        //...........Text View References...

        //... row...
        report_today = (TextView) findViewById(R.id.report_today);
        report_t1 = (TextView) findViewById(R.id.report_t1);
        report_t2 = (TextView) findViewById(R.id.report_t2);
        report_t3 = (TextView) findViewById(R.id.report_t3);
        report_t4 = (TextView) findViewById(R.id.report_t4);
        report_t5 = (TextView) findViewById(R.id.report_t5);
        report_t6 = (TextView) findViewById(R.id.report_t6);
        report_t7 = (TextView) findViewById(R.id.report_t7);
        report_t_month = (TextView) findViewById(R.id.report_t_month);
        report_t_quarter = (TextView) findViewById(R.id.report_t_quarter);
        linear_sub_status1=(LinearLayout)findViewById(R.id.linear_sub_status);

        lvStatusReport=(ListView)findViewById(R.id.list_Status_Report);
        lvSubStatusReport=(ListView)findViewById(R.id.list_sub_status_Report);
        sharedPref = new SharedPref(mContext);
        empcode = sharedPref.getLoginId();
        progressDialog = new ProgressDialog(mContext);
        getStatusReportModelList = new ArrayList<>();
        loginDetailsModel = new LoginDetailsModel();
//        subStatusReportModelList = new ArrayList<>();
        setHeader();
        new GetStatusReport().execute();

        lvStatusReport.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
             //   Toast.makeText(mContext, view.getId()+"- position" + position, Toast.LENGTH_SHORT).show();
                /*if (subStatusReportModelList != null && subStatusReportModelList.size() > 0) {
                    subStatusReportAdapter = new SubStatusReportAdapter(mContext, subStatusReportModelList);
                    lvSubStatusReport.setAdapter(subStatusReportAdapter);
                }*/
            }
        });
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        goBack();
    }

    private void goBack() {
        pushActivity(mContext, HomeActivity.class, null, true);
        finish();
    }
    public void setSubStatusData(){
        if (subStatusReportModelList != null && subStatusReportModelList.size() > 0) {
            subStatusReportAdapter = new SubStatusReportAdapter(mContext, subStatusReportModelList);
            linear_sub_status1.setVisibility(View.VISIBLE);
            lvSubStatusReport.setAdapter(subStatusReportAdapter);
            setListViewHeightBasedOnItems(lvSubStatusReport);
            subStatusReportAdapter.notifyDataSetChanged();

        }
    }


    private void setHeader() {
        try {
            TextView tvHeader = (TextView) findViewById(R.id.textTitle);
            ImageView ivHome = (ImageView) findViewById(R.id.iv_home);
            ImageView ivLogout = (ImageView) findViewById(R.id.iv_logout);
            tvHeader.setText(mContext.getResources().getString(R.string.status_report));

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
    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                float px = 500 * (listView.getResources().getDisplayMetrics().density);
                item.measure(View.MeasureSpec.makeMeasureSpec((int) px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            // Get padding
            int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + totalPadding;
            listView.setLayoutParams(params);
            listView.requestLayout();
            return true;

        } else {
            return false;
        }

    }



    public class GetStatusReport extends AsyncTask<Void, Void, SoapObject> {

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

                object = webService.StatusReport(empcode,sharedPref.getIsRM());

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
                getStatusReportModelList.clear();
                for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                    SoapObject root = (SoapObject) soapObject.getProperty(i);


                    if (root.getProperty("T_1") != null) {

                        if (!root.getProperty("T_1").toString().equalsIgnoreCase("anyType{}")) {
                            t1_1 = root.getProperty("T_1").toString();


                        } else {
                            t1_1 = "";
                        }
                    } else {
                        t1_1 = "";
                    }
                    if (root.getProperty("T_2") != null) {

                        if (!root.getProperty("T_2").toString().equalsIgnoreCase("anyType{}")) {
                            t2_1 = root.getProperty("T_2").toString();

                        } else {
                            t2_1 = "";
                        }
                    } else {
                        t2_1 = "";
                    }

                    if (root.getProperty("T_3") != null) {

                        if (!root.getProperty("T_3").toString().equalsIgnoreCase("anyType{}")) {
                            t3_1 = root.getProperty("T_3").toString();

                        } else {
                            t3_1 = "";
                        }
                    } else {
                        t3_1 = "";
                    }
                    if (root.getProperty("T_4") != null) {

                        if (!root.getProperty("T_4").toString().equalsIgnoreCase("anyType{}")) {
                            t4_1 = root.getProperty("T_4").toString();

                        } else {
                            t4_1 = "";
                        }
                    } else {
                        t4_1 = "";
                    }
                    if (root.getProperty("T_5") != null) {

                        if (!root.getProperty("T_5").toString().equalsIgnoreCase("anyType{}")) {
                            t5_1 = root.getProperty("T_5").toString();

                        } else {
                            t5_1 = "";
                        }
                    } else {
                        t5_1 = "";
                    }
                    if (root.getProperty("T_6") != null) {

                        if (!root.getProperty("T_6").toString().equalsIgnoreCase("anyType{}")) {
                            t6_1 = root.getProperty("T_6").toString();

                        } else {
                            t6_1 = "";
                        }
                    } else {
                        t6_1 = "";
                    }
                    if (root.getProperty("T_7") != null) {

                        if (!root.getProperty("T_7").toString().equalsIgnoreCase("anyType{}")) {
                            t7_1 = root.getProperty("T_7").toString();

                        } else {
                            t7_1 = "";
                        }
                    } else {
                        t7_1 = "";
                    }
                    if (root.getProperty("T_Month") != null) {

                        if (!root.getProperty("T_Month").toString().equalsIgnoreCase("anyType{}")) {
                            t_month_1 = root.getProperty("T_Month").toString();

                        } else {
                            t_month_1 = "";
                        }
                    } else {
                        t_month_1 = "";
                    }
                    if (root.getProperty("T_quater") != null) {

                        if (!root.getProperty("T_quater").toString().equalsIgnoreCase("anyType{}")) {
                            t_quater_1 = root.getProperty("T_quater").toString();

                        } else {
                            t_quater_1 = "";
                        }
                    } else {
                        t_quater_1 = "";
                    }
                    if (root.getProperty("status") != null) {

                        if (!root.getProperty("status").toString().equalsIgnoreCase("anyType{}")) {
                            status_1 = root.getProperty("status").toString();

                        } else {
                            status_1 = "";
                        }
                    } else {
                        status_1 = "";
                    }
                    if (root.getProperty("todayreport") != null) {

                        if (!root.getProperty("todayreport").toString().equalsIgnoreCase("anyType{}")) {
                            today_report_1 = root.getProperty("todayreport").toString();

                        } else {
                            today_report_1 = "";
                        }
                    } else {
                        today_report_1 = "";
                    }

                    statusReportModel = new StatusReportModel(t1_1, t2_1, t3_1, t4_1, t5_1, t6_1, t7_1, t_month_1, t_quater_1, today_report_1, status_1);
                    getStatusReportModelList.add(statusReportModel);

                }
                if (getStatusReportModelList != null && getStatusReportModelList.size() > 0) {
                    statusReportAdapter = new StatusReportAdapter(mContext,getStatusReportModelList);
                    lvStatusReport.setAdapter(statusReportAdapter);
                    setListViewHeightBasedOnItems(lvStatusReport);
                    statusReportAdapter.notifyDataSetChanged();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
