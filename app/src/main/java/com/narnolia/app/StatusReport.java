package com.narnolia.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.narnolia.app.adapter.CustomSpinnerAdapter;
import com.narnolia.app.adapter.StatusReportAdapter;
import com.narnolia.app.adapter.SubStatusReportAdapter;
import com.narnolia.app.model.LoginDetailsModel;
import com.narnolia.app.model.StatusReportModel;
import com.narnolia.app.network.SOAPWebService;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import static com.narnolia.app.adapter.StatusReportAdapter.subStatusReportModelList;

public class StatusReport extends AbstractActivity {

    private Context mContext;
    private Spinner spinNation, spinZone, spinRegion, spinCluster, spinLocation, spinEmployee,spinSearchBy;
    private ProgressDialog progressDialog;
    private String empcode;
    private SharedPref sharedPref;
    private String param, nation, zone, region, cluster, location, employee,emp_id;
    public static String nationVal, zoneVal, regionVal, clusterVal, locationVal;
    private String responseId;
    String fromHomeKey,fromHomeKey1;
    private List<String> result, empList;
    String[] strResultArray = null;
    String emp,rm;
    LinearLayout attendence_report_menu,date_wise_report,main_menu;
    private List<StatusReportModel> getStatusReportModelList;
    StatusReportModel statusReportModel;
    private List<LoginDetailsModel> loginDetailsModels;
    public LoginDetailsModel loginDetailsModel;
    private ListView lvStatusReport;
    private ListView lvSubStatusReport;
    LinearLayout linear_sub_status1, layout_status_table;
    Button btn_search;
    String spinSearchByList[] = {"--select--", "Location", "Date"};
    //    public List<SubStatusReportModel> subStatusReportModelList;
    public StatusReportAdapter statusReportAdapter;
    public SubStatusReportAdapter subStatusReportAdapter;
    private TextView report_today1, report_t1_1, report_t2_1, report_t3_1, report_t4_1, report_t5_1, report_t6_1, report_t7_1, report_t_month_1, report_t_quarter_1;
    private TextView report_today, report_t1, report_t2, report_t3, report_t4, report_t5, report_t6, report_t7, report_t_month, report_t_quarter;
    String t1_1, t2_1, t3_1, t4_1, t5_1, t6_1, t7_1, t_month_1, t_quater_1, today_report_1, status_1;
    String t1, t2, t3, t4, t5, t6, t7, t_month, t_quater, today_report;
    String[] arrayForSpinner = {""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_status_reports);
        mContext = StatusReport.this;
        setHeader();

        sharedPref = new SharedPref(mContext);
        empcode = sharedPref.getLoginId();
        result = new ArrayList<>();
        progressDialog = new ProgressDialog(mContext);
        spinNation = (Spinner) findViewById(R.id.spin_nation);
        spinZone = (Spinner) findViewById(R.id.spin_zone);
        spinRegion = (Spinner) findViewById(R.id.spin_region);
        spinCluster = (Spinner) findViewById(R.id.spin_cluster);
        spinLocation = (Spinner) findViewById(R.id.spin_location);
        spinEmployee = (Spinner) findViewById(R.id.spin_employee);
        spinSearchBy=(Spinner)findViewById(R.id.search_by);
        ArrayAdapter<String> adapterSerchBy = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinSearchByList) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = null;
                // If this is the initial dummy entry, make it hidden
                if (position == 0) {
                    TextView tv = new TextView(getContext());
                    tv.setHeight(0);
                    tv.setVisibility(View.GONE);
                    v = tv;
                } else {
                    // Pass convertView as null to prevent reuse of special case views
                    v = super.getDropDownView(position, null, parent);
                }
                // Hide scroll bar because it appears sometimes unnecessarily, this does not prevent scrolling
                parent.setVerticalScrollBarEnabled(false);
                return v;
            }
        };
        adapterSerchBy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinSearchBy.setAdapter(adapterSerchBy);

        spinEmployee.setAdapter(new CustomSpinnerAdapter(this, R.layout.spinner_row, arrayForSpinner, "Select Employee"));
        btn_search = (Button) findViewById(R.id.btn_search);
        lvStatusReport=(ListView)findViewById(R.id.list_Status_Report);
        lvSubStatusReport=(ListView)findViewById(R.id.list_sub_status_Report);
        linear_sub_status1=(LinearLayout)findViewById(R.id.linear_sub_status);
        layout_status_table = (LinearLayout)findViewById(R.id.status_table);

        getStatusReportModelList = new ArrayList<>();
        loginDetailsModel = new LoginDetailsModel();

        attendence_report_menu=(LinearLayout)findViewById(R.id.attendence_report_menu);
        date_wise_report=(LinearLayout)findViewById(R.id.date_wise_report);
        if (getIntent() != null) {
            fromHomeKey = getIntent().getStringExtra("from_status");
            fromHomeKey1 = getIntent().getStringExtra("form_Attendence");
            if (fromHomeKey != null) {
                if (fromHomeKey.equals("FromStatus")) {
                    attendence_report_menu.setVisibility(View.GONE);
                    date_wise_report.setVisibility(View.GONE);
                }
            } else if (fromHomeKey1 != null) {
                if (fromHomeKey1.equals("FromAttendence")) {
                    attendence_report_menu.setVisibility(View.VISIBLE);
                }
            }
        }

        new GetGeoHierarchyNation().execute();

        spinNation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ((spinLocation.getSelectedItemPosition() > 0) && (spinZone.getSelectedItemPosition() > 0)
                        && (spinRegion.getSelectedItemPosition() > 0) && (spinCluster.getSelectedItemPosition() > 0)
                        && (position > 0)) {
                    spinEmployee.setClickable(true);
                    nationVal = parent.getSelectedItem().toString();
                    zoneVal = spinZone.getSelectedItem().toString();
                    regionVal = spinRegion.getSelectedItem().toString();
                    clusterVal = spinCluster.getSelectedItem().toString();
                    locationVal = spinLocation.getSelectedItem().toString();
                    new GetEmpList().execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinZone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ((spinNation.getSelectedItemPosition() > 0) && (spinLocation.getSelectedItemPosition() > 0)
                        && (spinRegion.getSelectedItemPosition() > 0) && (spinCluster.getSelectedItemPosition() > 0)
                        && (position > 0)) {
                    spinEmployee.setClickable(true);
                    nationVal = spinNation.getSelectedItem().toString();
                    zoneVal = parent.getSelectedItem().toString();
                    regionVal = spinRegion.getSelectedItem().toString();
                    clusterVal = spinCluster.getSelectedItem().toString();
                    locationVal = spinLocation.getSelectedItem().toString();
                    new GetEmpList().execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ((spinNation.getSelectedItemPosition() > 0) && (spinZone.getSelectedItemPosition() > 0)
                        && (spinLocation.getSelectedItemPosition() > 0) && (spinCluster.getSelectedItemPosition() > 0)
                        && (position > 0)) {
                    spinEmployee.setClickable(true);
                    nationVal = spinNation.getSelectedItem().toString();
                    zoneVal = spinZone.getSelectedItem().toString();
                    regionVal = parent.getSelectedItem().toString();
                    clusterVal = spinCluster.getSelectedItem().toString();
                    locationVal = spinLocation.getSelectedItem().toString();
                    new GetEmpList().execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinCluster.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ((spinNation.getSelectedItemPosition() > 0) && (spinZone.getSelectedItemPosition() > 0)
                        && (spinRegion.getSelectedItemPosition() > 0) && (spinLocation.getSelectedItemPosition() > 0)
                        && (position > 0)) {
                    spinEmployee.setClickable(true);
                    nationVal = spinNation.getSelectedItem().toString();
                    zoneVal = spinZone.getSelectedItem().toString();
                    regionVal = spinRegion.getSelectedItem().toString();
                    clusterVal = parent.getSelectedItem().toString();
                    locationVal = spinLocation.getSelectedItem().toString();
                    new GetEmpList().execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ((spinNation.getSelectedItemPosition() > 0) && (spinZone.getSelectedItemPosition() > 0)
                        && (spinRegion.getSelectedItemPosition() > 0) && (spinCluster.getSelectedItemPosition() > 0)
                        && (position > 0)) {
                    spinEmployee.setClickable(true);
                    nationVal = spinNation.getSelectedItem().toString();
                    zoneVal = spinZone.getSelectedItem().toString();
                    regionVal = spinRegion.getSelectedItem().toString();
                    clusterVal = spinCluster.getSelectedItem().toString();
                    locationVal = parent.getSelectedItem().toString();
                    new GetEmpList().execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_status_table.setVisibility(View.VISIBLE);
                if (spinEmployee.getSelectedItem().toString().equals("All")) {
                    emp=empcode;
                    rm=sharedPref.getIsRM();
                    new GetStatusReport().execute();
                }else if (!spinEmployee.getSelectedItem().toString().equals("All")) {
                    emp=emp_id;
                    rm="4";
                    new GetStatusReport().execute();
                }
            }
        });
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

    private void setHeader() {
        try {
            TextView tvHeader = (TextView) findViewById(R.id.textTitle);
            ImageView ivHome = (ImageView) findViewById(R.id.iv_home);
            ImageView ivLogout = (ImageView) findViewById(R.id.iv_logout);
            tvHeader.setText(this.getResources().getString(R.string.status_report));

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

    public class GetGeoHierarchyNation extends AsyncTask<Void, Void, SoapObject> {

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

                object = webService.get_Geographicalhierarchy(empcode, "national");

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
                result.clear();
                for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                    SoapObject root = (SoapObject) soapObject.getProperty(i);

                    if (root.getProperty("national") != null) {
                        if (!root.getProperty("national").toString().equalsIgnoreCase("anyType{}")) {
                            nation = root.getProperty("national").toString();
                        } else {
                            nation = "";
                        }
                    } else {
                        nation = "";
                    }
                }
                result.add(nation);

                if (result.size() > 0) {
                    strResultArray = new String[result.size() + 1];
                    strResultArray[0] = "Select Nation";

                    for (int i = 0; i < result.size(); i++) {
                        strResultArray[i + 1] = result.get(i);
                    }
                }

                ArrayAdapter<String> adapterMaster = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, strResultArray) {
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

                spinNation.setAdapter(adapterMaster);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                new GetGeoHierarchyZone().execute();
            }

        }
    }

    public class GetGeoHierarchyZone extends AsyncTask<Void, Void, SoapObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected SoapObject doInBackground(Void... params) {

            SoapObject object = null;
            try {
                SOAPWebService webService = new SOAPWebService(mContext);

                object = webService.get_Geographicalhierarchy(empcode, "zone");

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
                result.clear();
                for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                    SoapObject root = (SoapObject) soapObject.getProperty(i);

                        if (root.getProperty("zone") != null) {

                            if (!root.getProperty("zone").toString().equalsIgnoreCase("anyType{}")) {
                                zone = root.getProperty("zone").toString();

                            } else {
                                zone = "";
                            }
                        } else {
                            zone = "";
                        }
                    result.add(zone);
                }

                if (result.size() > 0) {
                    strResultArray = new String[result.size() + 1];
                    strResultArray[0] = "Select Zone";

                    for (int i = 0; i < result.size(); i++) {
                        strResultArray[i + 1] = result.get(i);
                    }
                }

                ArrayAdapter<String> adapterMaster = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, strResultArray) {
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
                spinZone.setAdapter(adapterMaster);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                new GetGeoHierarchyRegion().execute();
            }

        }
    }

    public class GetGeoHierarchyRegion extends AsyncTask<Void, Void, SoapObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected SoapObject doInBackground(Void... params) {

            SoapObject object = null;
            try {
                SOAPWebService webService = new SOAPWebService(mContext);

                object = webService.get_Geographicalhierarchy(empcode, "region");

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
                result.clear();
                for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                    SoapObject root = (SoapObject) soapObject.getProperty(i);


                    if (root.getProperty("region") != null) {

                        if (!root.getProperty("region").toString().equalsIgnoreCase("anyType{}")) {
                            region = root.getProperty("region").toString();

                        } else {
                            region = "";
                        }
                    } else {
                        region = "";
                    }
                    result.add(region);
                }

                if (result.size() > 0) {
                    strResultArray = new String[result.size() + 1];
                    strResultArray[0] = "Select Region";

                    for (int i = 0; i < result.size(); i++) {
                        strResultArray[i + 1] = result.get(i);
                    }
                }

                ArrayAdapter<String> adapterMaster = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, strResultArray) {
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
                spinRegion.setAdapter(adapterMaster);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                new GetGeoHierarchyCluster().execute();
            }

        }
    }

    public class GetGeoHierarchyCluster extends AsyncTask<Void, Void, SoapObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected SoapObject doInBackground(Void... params) {

            SoapObject object = null;
            try {
                SOAPWebService webService = new SOAPWebService(mContext);

                object = webService.get_Geographicalhierarchy(empcode, "cluster");

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
                result.clear();
                for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                    SoapObject root = (SoapObject) soapObject.getProperty(i);


                    if (root.getProperty("cluster") != null) {

                        if (!root.getProperty("cluster").toString().equalsIgnoreCase("anyType{}")) {
                            cluster = root.getProperty("cluster").toString();

                        } else {
                            cluster = "";
                        }
                    } else {
                        cluster = "";
                    }
                    result.add(cluster);
                }

                if (result.size() > 0) {
                    strResultArray = new String[result.size() + 1];
                    strResultArray[0] = "Select Cluster";

                    for (int i = 0; i < result.size(); i++) {
                        strResultArray[i + 1] = result.get(i);
                    }
                }

                ArrayAdapter<String> adapterMaster = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, strResultArray) {
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
                spinCluster.setAdapter(adapterMaster);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                new GetGeoHierarchyLocation().execute();
            }

        }
    }

    public class GetGeoHierarchyLocation extends AsyncTask<Void, Void, SoapObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected SoapObject doInBackground(Void... params) {

            SoapObject object = null;
            try {
                SOAPWebService webService = new SOAPWebService(mContext);

                object = webService.get_Geographicalhierarchy(empcode, "location");

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
                result.clear();
                for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                    SoapObject root = (SoapObject) soapObject.getProperty(i);


                    if (root.getProperty("location") != null) {

                        if (!root.getProperty("location").toString().equalsIgnoreCase("anyType{}")) {
                            location = root.getProperty("location").toString();

                        } else {
                            location = "";
                        }
                    } else {
                        location = "";
                    }
                    result.add(location);
                }

                if (result.size() > 0) {
                    strResultArray = new String[result.size() + 1];
                    strResultArray[0] = "Select Location";

                    for (int i = 0; i < result.size(); i++) {
                        strResultArray[i + 1] = result.get(i);
                    }
                }

                ArrayAdapter<String> adapterMaster = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, strResultArray) {
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
                spinLocation.setAdapter(adapterMaster);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public class GetEmpList extends AsyncTask<Void, Void, SoapObject> {

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

                object = webService.get_emplist(empcode, nationVal, zoneVal, regionVal, locationVal, clusterVal);

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
                result.clear();
                for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                    SoapObject root = (SoapObject) soapObject.getProperty(i);


                    if (root.getProperty("employeename") != null) {

                        if (!root.getProperty("employeename").toString().equalsIgnoreCase("anyType{}")) {
                            employee = root.getProperty("employeename").toString();

                        } else {
                            employee = "";
                        }
                    } else {
                        employee = "";
                    }


                    if (root.getProperty("emp_id") != null) {

                        if (!root.getProperty("emp_id").toString().equalsIgnoreCase("anyType{}")) {
                            emp_id = root.getProperty("emp_id").toString();

                        } else {
                            emp_id = "";
                        }
                    } else {
                        emp_id = "";
                    }
                    result.add(employee);
                }

                if (result.size() > 0) {
                    strResultArray = new String[result.size() + 2];
                    strResultArray[0] = "Select Employee";
                    strResultArray[1] = "All";

                    for (int i = 0; i < result.size(); i++) {
                        strResultArray[i + 2] = result.get(i);
                    }
                }

                ArrayAdapter<String> adapterMaster = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, strResultArray) {
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
                        } else {
                            // Pass convertView as null to prevent reuse of special case views
                            v = super.getDropDownView(position, null, parent);
                        }
                        // Hide scroll bar because it appears sometimes unnecessarily, this does not prevent scrolling
                        parent.setVerticalScrollBarEnabled(false);
                        return v;
                    }
                };

                adapterMaster.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinEmployee.setAdapter(adapterMaster);

            } catch (Exception e) {
                e.printStackTrace();
            }

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

                object = webService.StatusReport(emp,rm);

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
