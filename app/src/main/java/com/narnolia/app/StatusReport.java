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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
    private String param, nation, zone, region, cluster, location;
    private String responseId;
    private List<String> result, empList;
    String[] strResultArray = null;

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


                    if(param.equalsIgnoreCase("national")) {
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
                }

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

//                object = webService.get_emplist(empcode, param);

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



                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
