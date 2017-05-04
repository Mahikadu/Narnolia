package com.narnolia.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.narnolia.app.adapter.StatusReportAdapter;
import com.narnolia.app.model.StatusReportModel;
import com.narnolia.app.network.SOAPWebService;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

public class StatusReport extends AbstractActivity {

    private Context mContext;
    private Spinner spinNation, spinZone, spinRegion, spinCluster, spinLocation, spinEmployee;
    private ProgressDialog progressDialog;
    private String empcode;
    private SharedPref sharedPref;
    private String param, nation, zone, region, cluster, location, employee;
    private String nationVal, zoneVal, regionVal, clusterVal, locationVal;
    private String responseId;
    String fromHomeKey;
    private List<String> result, empList;
    String[] strResultArray = null;
    LinearLayout attendence_report_menu,date_wise_report,main_menu;

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
        attendence_report_menu=(LinearLayout)findViewById(R.id.attendence_report_menu);
        date_wise_report=(LinearLayout)findViewById(R.id.date_wise_report);
        if (getIntent() != null) {
            fromHomeKey = getIntent().getStringExtra("from_status");
            if (fromHomeKey.equals("FromStatus")) {
                attendence_report_menu.setVisibility(View.GONE);
                date_wise_report.setVisibility(View.GONE);
            }
        }

        new GetGeoHierarchyNation().execute();

        spinLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ((spinNation.getSelectedItemPosition() > 0) && (spinZone.getSelectedItemPosition() > 0)
                        && (spinRegion.getSelectedItemPosition() > 0) && (spinCluster.getSelectedItemPosition() > 0)
                        && (position > 0)) {
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
                    result.add(employee);
                }

                if (result.size() > 0) {
                    strResultArray = new String[result.size() + 1];
                    strResultArray[0] = "Select Employee";

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
                spinEmployee.setAdapter(adapterMaster);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
