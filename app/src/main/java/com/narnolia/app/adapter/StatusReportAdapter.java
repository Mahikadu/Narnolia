package com.narnolia.app.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.narnolia.app.R;
import com.narnolia.app.SharedPref;
import com.narnolia.app.StatusReportActivity;
import com.narnolia.app.libs.Utils;
import com.narnolia.app.model.GetMessagesModel;
import com.narnolia.app.model.StatusReportModel;
import com.narnolia.app.model.SubStatusReportModel;
import com.narnolia.app.network.SOAPWebService;

import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.narnolia.app.R.id.report_t1;
import static com.narnolia.app.R.id.report_t2;
import static com.narnolia.app.R.id.report_t3;
import static com.narnolia.app.R.id.report_t4;
import static com.narnolia.app.R.id.report_t5;
import static com.narnolia.app.R.id.report_t6;
import static com.narnolia.app.R.id.report_t7;
import static com.narnolia.app.R.id.report_today;

/**
 * Created by Sudesi on 23/03/2017.
 */

public class StatusReportAdapter extends BaseAdapter implements View.OnClickListener {
    public Context context;
    private static LayoutInflater inflater = null;
    private StatusReportModel statusReportModel;
    private ProgressDialog progressDialog;
    public SubStatusReportAdapter subStatusReporAdapter;
    public static List<SubStatusReportModel> subStatusReportModelList;
    private SharedPref sharedPref;
    private SubStatusReportModel subStatusReportModel;
    private List<StatusReportModel> statusReportModels = new ArrayList<>();
    private String leadStatus = "", createdDate = "", responseId = "";
    String meeting_status="";
    private String sub_lead_id,sub_name,sub_Mobile_no,sub_city,sub_pincode,sub_last_meeting_date,sub_last_meeting_update,sub_meeting_status;

    public StatusReportAdapter(Context mContext, List<StatusReportModel> statusReportModels) {
        this.context = mContext;
        progressDialog = new ProgressDialog(mContext);
        subStatusReportModelList = new ArrayList<>();
        sharedPref = new SharedPref(mContext);
        this.statusReportModels = statusReportModels;
        /*Calendar now = Calendar.getInstance();
        // SimpleDateFormat df = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        now.setTime(new Date());
        now.add(Calendar.DAY_OF_YEAR, -1);
        Date prevDate = now.getTime();
        selDate = df.format(prevDate);*/


    }

    @Override
    public int getCount() {
        return statusReportModels.size();
    }

    @Override
    public Object getItem(int position) {
        return statusReportModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final StatusReportAdapter.ViewHolder viewHolder;
        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_status_report, null);

            viewHolder = new StatusReportAdapter.ViewHolder();

            viewHolder.report_status = (TextView) convertView.findViewById(R.id.report_status);
            viewHolder.report_today = (TextView) convertView.findViewById(report_today);
            viewHolder.report_today.setOnClickListener(this);

            viewHolder.report_t1 = (TextView) convertView.findViewById(report_t1);
            viewHolder.report_t1.setOnClickListener(this);

            viewHolder.report_t2 = (TextView) convertView.findViewById(report_t2);
            viewHolder.report_t2.setOnClickListener(this);

            viewHolder.report_t3 = (TextView) convertView.findViewById(R.id.report_t3);
            viewHolder.report_t3.setOnClickListener(this);
            viewHolder.report_t4 = (TextView) convertView.findViewById(R.id.report_t4);
            viewHolder.report_t4.setOnClickListener(this);
            viewHolder.report_t5 = (TextView) convertView.findViewById(R.id.report_t5);
            viewHolder.report_t5.setOnClickListener(this);
            viewHolder.report_t6 = (TextView) convertView.findViewById(R.id.report_t6);
            viewHolder.report_t6.setOnClickListener(this);
            viewHolder.report_t7 = (TextView) convertView.findViewById(R.id.report_t7);
            viewHolder.report_t7.setOnClickListener(this);
            viewHolder.report_t_month = (TextView) convertView.findViewById(R.id.report_t_month);

            viewHolder.report_t_quarter = (TextView) convertView.findViewById(R.id.report_t_quarter);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (StatusReportAdapter.ViewHolder) convertView.getTag();
        }
        statusReportModel = statusReportModels.get(position);
        viewHolder.report_status.setText(statusReportModel.getStatus_1());
        meeting_status=statusReportModel.getStatus_1();
        viewHolder.report_today.setText(statusReportModel.getToday_report_1());
        viewHolder.report_t1.setText(statusReportModel.getT1_1());
        viewHolder.report_t2.setText(statusReportModel.getT2_1());
        viewHolder.report_t3.setText(statusReportModel.getT3_1());
        viewHolder.report_t4.setText(statusReportModel.getT4_1());
        viewHolder.report_t5.setText(statusReportModel.getT5_1());
        viewHolder.report_t6.setText(statusReportModel.getT6_1());
        viewHolder.report_t7.setText(statusReportModel.getT7_1());
        viewHolder.report_t_month.setText(statusReportModel.getT_month_1());
        viewHolder.report_t_quarter.setText(statusReportModel.getT_quater_1());


        return convertView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case report_today:
                createdDate = Utils.getSelectedDate(0);
              //  Toast.makeText(context, "createdDate is :" + createdDate, Toast.LENGTH_SHORT).show();
                new SubStatusReport().execute();
                break;
            case report_t1:
                createdDate = Utils.getSelectedDate(-1);
             //   Toast.makeText(context, "createdDate is :" + createdDate, Toast.LENGTH_SHORT).show();
                new SubStatusReport().execute();
                break;
            case report_t2:
                createdDate = Utils.getSelectedDate(-2);
              //  Toast.makeText(context, "createdDate is :" + createdDate, Toast.LENGTH_SHORT).show();
                new SubStatusReport().execute();
                break;
            case report_t3:
                createdDate = Utils.getSelectedDate(-3);
               // Toast.makeText(context, "createdDate is :" + createdDate, Toast.LENGTH_SHORT).show();
                new SubStatusReport().execute();
                break;
            case report_t4:
                createdDate = Utils.getSelectedDate(-4);
              //  Toast.makeText(context, "createdDate is :" + createdDate, Toast.LENGTH_SHORT).show();
                new SubStatusReport().execute();
                break;
            case report_t5:
                createdDate = Utils.getSelectedDate(-5);
               // Toast.makeText(context, "createdDate is :" + createdDate, Toast.LENGTH_SHORT).show();
                new SubStatusReport().execute();
                break;
            case report_t6:
                createdDate = Utils.getSelectedDate(-6);
              //  Toast.makeText(context, "createdDate is :" + createdDate, Toast.LENGTH_SHORT).show();
                new SubStatusReport().execute();
                break;
            case report_t7:
                createdDate = Utils.getSelectedDate(-7);
               // Toast.makeText(context, "createdDate is :" + createdDate, Toast.LENGTH_SHORT).show();
                new SubStatusReport().execute();
                break;
        }

    }

    public class ViewHolder {
        private TextView report_status, report_today, report_t1, report_t2, report_t3, report_t4, report_t5, report_t6, report_t7, report_t_month, report_t_quarter;

    }

    public class SubStatusReport extends AsyncTask<Void, Void, SoapObject> {

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
                SOAPWebService webService = new SOAPWebService(context);
                object = webService.SubStatusReport(sharedPref.getLoginId(),meeting_status, createdDate, sharedPref.getIsRM());
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
            subStatusReportModelList.clear();

            try {
                for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                    SoapObject root = (SoapObject) soapObject.getProperty(i);

                    subStatusReportModel = new SubStatusReportModel();
                    if (root.getProperty("lead_id") != null) {

                        if (!root.getProperty("lead_id").toString().equalsIgnoreCase("anyType{}")) {
                            sub_lead_id = root.getProperty("lead_id").toString();
                            subStatusReportModel.setSub_lead_id(sub_lead_id);

                        } else {
                            sub_lead_id = "";
                        }
                    } else {
                        sub_lead_id = "";
                    }

                    if (root.getProperty("fname") != null) {

                        if (!root.getProperty("fname").toString().equalsIgnoreCase("anyType{}")) {
                            sub_name = root.getProperty("fname").toString();
                            subStatusReportModel.setSub_name(sub_name);
                        } else {
                            sub_name = "";
                        }
                    } else {
                        sub_name = "";
                    }

                    if (root.getProperty("mobile_no") != null) {

                        if (!root.getProperty("mobile_no").toString().equalsIgnoreCase("anyType{}")) {
                            sub_Mobile_no = root.getProperty("mobile_no").toString();
                            subStatusReportModel.setSub_mobile_no(sub_Mobile_no);
                        } else {
                            sub_Mobile_no = "";
                        }
                    } else {
                        sub_Mobile_no = "";
                    }
                    if (root.getProperty("city") != null) {

                        if (!root.getProperty("city").toString().equalsIgnoreCase("anyType{}")) {
                            sub_city = root.getProperty("city").toString();
                            subStatusReportModel.setSub_city(sub_city);

                        } else {
                            sub_city = "";
                        }
                    } else {
                        sub_city = "";
                    }
                    if (root.getProperty("pincode") != null) {

                        if (!root.getProperty("pincode").toString().equalsIgnoreCase("anyType{}")) {
                            sub_pincode = root.getProperty("pincode").toString();
                            subStatusReportModel.setSub_pincode(sub_pincode);

                        } else {
                            sub_pincode = "";
                        }
                    } else {
                        sub_pincode = "";
                    }
                    if (root.getProperty("last_meeting_date") != null) {

                        if (!root.getProperty("last_meeting_date").toString().equalsIgnoreCase("anyType{}")) {
                            sub_last_meeting_date = root.getProperty("last_meeting_date").toString();
                            subStatusReportModel.setSub_last_meeting_date(sub_last_meeting_date);
                        } else {
                            sub_last_meeting_date = "";
                        }
                    } else {
                        sub_last_meeting_date = "";
                    }
                    if (root.getProperty("last_meeting_update") != null) {

                        if (!root.getProperty("last_meeting_update").toString().equalsIgnoreCase("anyType{}")) {
                            sub_last_meeting_update = root.getProperty("last_meeting_update").toString();
                            subStatusReportModel.setSub_last_meeting_update(sub_last_meeting_update);
                        } else {
                            sub_last_meeting_update = "";
                        }
                    } else {
                        sub_last_meeting_update = "";
                    }
                    if (root.getProperty("lead_status") != null) {

                        if (!root.getProperty("lead_status").toString().equalsIgnoreCase("anyType{}")) {
                            sub_meeting_status = root.getProperty("lead_status").toString();
                            subStatusReportModel.setSub_lead_status(sub_meeting_status);
                        } else {
                            sub_meeting_status = "";
                        }
                    } else {
                        sub_meeting_status = "";
                    }




                    subStatusReportModelList.add(subStatusReportModel);

                }
            /*    if (subStatusReportModelList != null && subStatusReportModelList.size() > 0) {
                    subStatusReporAdapter = new SubStatusReportAdapter(context, subStatusReportModelList);
//                    lvStatusReport.setAdapter(subStatusReporAdapter);
                }*/
                if (subStatusReportModelList != null && subStatusReportModelList.size() > 0)
                    ((StatusReportActivity)context).setSubStatusData();

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}

