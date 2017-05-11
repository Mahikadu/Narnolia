package com.narnolia.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.narnolia.app.adapter.AttendanceReportAdapter;
import com.narnolia.app.adapter.CustomSpinnerAdapter;
import com.narnolia.app.adapter.StatusReportAdapter;
import com.narnolia.app.adapter.SubStatusReportAdapter;
import com.narnolia.app.model.AttendenceReportModel;
import com.narnolia.app.model.LoginDetailsModel;
import com.narnolia.app.model.StatusReportModel;
import com.narnolia.app.network.SOAPWebService;

import org.ksoap2.serialization.SoapObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static com.narnolia.app.adapter.StatusReportAdapter.subStatusReportModelList;

public class StatusReport extends AbstractActivity {

    private Context mContext;
    private Spinner spinNation, spinZone, spinRegion, spinCluster, spinLocation, spinEmployee, spinSearchBy, spinYear, spinMonth;
    private ProgressDialog progressDialog;
    EditText from_date, to_date;
    private String empcode, attendanceVal, searchByVal,to_date_Val,from_date_Val;
    private SharedPref sharedPref;
    private String param, nation, zone, region, cluster, location, employee, emp_id;
    public static String nationVal, zoneVal, regionVal, clusterVal, locationVal, monthVal, yearVal;
    private String responseId;
    String fromHomeKey, fromHomeKey1;
    private List<String> result, empList;
    String[] strResultArray = null;
    String emp, rm;
    private RadioGroup rg_attendence;
    private RadioButton rb_present, rb_absent;
    private RadioButton radioButton;
    LinearLayout attendence_report_menu, date_wise_report, main_menu, attendence_table;
    private List<StatusReportModel> getStatusReportModelList;
    private List<AttendenceReportModel> attendanceReportModelList;
    StatusReportModel statusReportModel;
    AttendenceReportModel attendanceReportModel;
    private List<LoginDetailsModel> loginDetailsModels;
    public LoginDetailsModel loginDetailsModel;
    private ListView lvStatusReport, lvAttendanceReport;
    private ListView lvSubStatusReport;
    String spinMonthList[] = {"select Month", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};// {"select Month", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    String spinYearList[] = {"select Year", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027"};
    LinearLayout linear_sub_status1, layout_status_table, to_date_from_date;
    Button btn_search;
    String spinSearchByList[] = {"--select--", "Location", "Date"};
    //    public List<SubStatusReportModel> subStatusReportModelList;
    public StatusReportAdapter statusReportAdapter;
    public AttendanceReportAdapter attendanceReportAdapter;
    public SubStatusReportAdapter subStatusReportAdapter;
    private TextView report_today1, report_t1_1, report_t2_1, report_t3_1, report_t4_1, report_t5_1, report_t6_1, report_t7_1, report_t_month_1, report_t_quarter_1;
    private TextView report_today, report_t1, report_t2, report_t3, report_t4, report_t5, report_t6, report_t7, report_t_month, report_t_quarter;
    String t1_1, t2_1, t3_1, t4_1, t5_1, t6_1, t7_1, t_month_1, t_quater_1, today_report_1, status_1;
    String ws_attendance, ws_empcode, ws_insertdate, ws_lat, ws_long, ws_name;
    String t1, t2, t3, t4, t5, t6, t7, t_month, t_quater, today_report;
    String[] arrayForSpinner = {""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_status_reports);
        mContext = StatusReport.this;
        if (getIntent() != null) {
            fromHomeKey = getIntent().getStringExtra("from_status");
            fromHomeKey1 = getIntent().getStringExtra("form_Attendence");
        }
        setHeader();

        sharedPref = new SharedPref(mContext);
        empcode = sharedPref.getLoginId();
        rm = sharedPref.getIsRM();
        result = new ArrayList<>();
        progressDialog = new ProgressDialog(mContext);
        //   progressDialog.setTitle("Login Status");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        spinNation = (Spinner) findViewById(R.id.spin_nation);
        spinZone = (Spinner) findViewById(R.id.spin_zone);
        spinRegion = (Spinner) findViewById(R.id.spin_region);
        spinCluster = (Spinner) findViewById(R.id.spin_cluster);
        spinLocation = (Spinner) findViewById(R.id.spin_location);
        spinEmployee = (Spinner) findViewById(R.id.spin_employee);
        spinSearchBy = (Spinner) findViewById(R.id.search_by);
        rg_attendence = (RadioGroup) findViewById(R.id.rg_attendence);
        rb_present = (RadioButton) findViewById(R.id.rb_present);
        rb_absent = (RadioButton) findViewById(R.id.rb_absent);
        spinYear = (Spinner) findViewById(R.id.year);
        spinMonth = (Spinner) findViewById(R.id.month);
        from_date = (EditText) findViewById(R.id.from_date);
        to_date = (EditText) findViewById(R.id.to_date);

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
        lvStatusReport = (ListView) findViewById(R.id.list_Status_Report);
        lvAttendanceReport = (ListView) findViewById(R.id.list_attendance_report);

        lvSubStatusReport = (ListView) findViewById(R.id.list_sub_status_Report);
        linear_sub_status1 = (LinearLayout) findViewById(R.id.linear_sub_status);
        layout_status_table = (LinearLayout) findViewById(R.id.status_table);
        to_date_from_date = (LinearLayout) findViewById(R.id.to_date_from_date);

        getStatusReportModelList = new ArrayList<>();
        attendanceReportModelList = new ArrayList<>();
        loginDetailsModel = new LoginDetailsModel();

        attendence_report_menu = (LinearLayout) findViewById(R.id.attendence_report_menu);
        date_wise_report = (LinearLayout) findViewById(R.id.date_wise_report);
        main_menu = (LinearLayout) findViewById(R.id.main_menu);
        attendence_table = (LinearLayout) findViewById(R.id.attendence_table);

        if (fromHomeKey != null) {
            if (fromHomeKey.equals("FromStatus")) {
                attendence_report_menu.setVisibility(View.GONE);
                date_wise_report.setVisibility(View.GONE);
                to_date_from_date.setVisibility(View.GONE);
                main_menu.setVisibility(View.VISIBLE);
                btn_search.setVisibility(View.VISIBLE);
            }
        } else if (fromHomeKey1 != null) {
            if (fromHomeKey1.equals("FromAttendence")) {
                attendence_report_menu.setVisibility(View.VISIBLE);
                date_wise_report.setVisibility(View.GONE);
                to_date_from_date.setVisibility(View.GONE);
                main_menu.setVisibility(View.GONE);
            }
        }


        spinSearchBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (spinSearchBy.getSelectedItem().equals("Location") && rg_attendence.getCheckedRadioButtonId() != -1) {
                    main_menu.setVisibility(View.VISIBLE);
                    btn_search.setVisibility(View.VISIBLE);
                    date_wise_report.setVisibility(View.GONE);
                }
                if (spinSearchBy.getSelectedItem().equals("Date")) {
                    date_wise_report.setVisibility(View.VISIBLE);
                    main_menu.setVisibility(View.GONE);
                    btn_search.setVisibility(View.GONE);
                    to_date_from_date.setVisibility(View.GONE);
                    layout_status_table.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, spinYearList) {
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
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinYear.setAdapter(adapterYear);
        ArrayAdapter<String> adapterMonth = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, spinMonthList) {
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

        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinMonth.setAdapter(adapterMonth);

        spinMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                monthVal = spinMonth.getSelectedItem().toString();
                yearVal = spinYear.getSelectedItem().toString();
                int month1, year1;

                if (spinYear.getSelectedItemPosition() != 0 && spinMonth.getSelectedItemPosition() != 0) {
                    month1 = Integer.parseInt(monthVal);
                    year1 = Integer.parseInt(yearVal);
                    final java.util.Date calculatedDate = calculateMonthEndDate(month1, year1);
                    SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
                    String lastDate = format.format(calculatedDate);
                    to_date_from_date.setVisibility(View.VISIBLE);
                    from_date.setText("0" + month1 + "-01-" + year1);
                    to_date.setText(lastDate);
                    btn_search.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                monthVal = spinMonth.getSelectedItem().toString();
                yearVal = spinYear.getSelectedItem().toString();
                int month1, year1;

                if (spinYear.getSelectedItemPosition() != 0 && spinMonth.getSelectedItemPosition() != 0) {
                    month1 = Integer.parseInt(monthVal);
                    year1 = Integer.parseInt(yearVal);
                    final java.util.Date calculatedDate = calculateMonthEndDate(month1, year1);
                    SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
                    String lastDate = format.format(calculatedDate);
                    to_date_from_date.setVisibility(View.VISIBLE);
                    from_date.setText("0" + month1 + "-01-" + year1);
                    to_date.setText(lastDate);
                    btn_search.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        rg_attendence.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked) {
                    if (fromHomeKey1 != null) {
                        if (fromHomeKey1.equals("FromAttendence")) {

                            if (spinSearchBy.getSelectedItem().equals("Location")) {
                                main_menu.setVisibility(View.VISIBLE);
                                btn_search.setVisibility(View.VISIBLE);
                                date_wise_report.setVisibility(View.GONE);
                            } else if (spinSearchBy.getSelectedItem().equals("Date")) {
                                date_wise_report.setVisibility(View.VISIBLE);
                                main_menu.setVisibility(View.GONE);
                                btn_search.setVisibility(View.GONE);

                            }
                        }

                    }
                }
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
                // layout_status_table.setVisibility(View.VISIBLE);
                View focusView = null;
                if (fromHomeKey != null) {
                    if (fromHomeKey.equals("FromStatus")) {
                        if (spinNation.getSelectedItemPosition() == 0) {
                            Toast.makeText(mContext, "Please select Nation", Toast.LENGTH_SHORT).show();
                            focusView = spinNation;
                            focusView.requestFocus();
                            spinNation.setFocusable(true);
                            spinNation.requestFocusFromTouch();
                            return;
                        } else if (spinZone.getSelectedItemPosition() == 0) {
                            Toast.makeText(mContext, "Please select Zone", Toast.LENGTH_SHORT).show();
                            focusView = spinZone;
                            focusView.requestFocus();
                            spinZone.setFocusable(true);
                            spinZone.requestFocusFromTouch();
                            return;
                        } else if (spinRegion.getSelectedItemPosition() == 0) {
                            Toast.makeText(mContext, "Please select Region", Toast.LENGTH_SHORT).show();
                            focusView = spinRegion;
                            focusView.requestFocus();
                            spinRegion.setFocusable(true);
                            spinRegion.requestFocusFromTouch();
                            return;
                        } else if (spinCluster.getSelectedItemPosition() == 0) {
                            Toast.makeText(mContext, "Please select Cluster", Toast.LENGTH_SHORT).show();
                            focusView = spinCluster;
                            focusView.requestFocus();
                            spinCluster.setFocusable(true);
                            spinCluster.requestFocusFromTouch();
                            return;
                        } else if (spinLocation.getSelectedItemPosition() == 0) {
                            Toast.makeText(mContext, "Please select Location", Toast.LENGTH_SHORT).show();
                            focusView = spinLocation;
                            focusView.requestFocus();
                            spinLocation.setFocusable(true);
                            spinLocation.requestFocusFromTouch();
                            return;
                        } else if (spinEmployee.getSelectedItemPosition() == 0) {
                            Toast.makeText(mContext, "Please select Employee", Toast.LENGTH_SHORT).show();
                            focusView = spinEmployee;
                            focusView.requestFocus();
                            spinEmployee.setFocusable(true);
                            spinEmployee.requestFocusFromTouch();
                            return;
                        } else if (spinEmployee.getSelectedItem().toString().equals("All")) {
                            emp = empcode;
                            layout_status_table.setVisibility(View.VISIBLE);
                            new GetStatusReport().execute();
                        } else if (!spinEmployee.getSelectedItem().toString().equals("All")) {
                            emp = emp_id;
                            rm = "4";
                            layout_status_table.setVisibility(View.VISIBLE);
                            new GetStatusReport().execute();
                        }
                    }
                } else if (fromHomeKey1 != null) {
                    if (fromHomeKey1.equals("FromAttendence")) {
                        int selectedId = rg_attendence.getCheckedRadioButtonId();
                        // find the radiobutton by returned id
                        radioButton = (RadioButton) findViewById(selectedId);
                        attendanceVal = radioButton.getText().toString();

                            searchByVal = spinSearchBy.getSelectedItem().toString();

                        if (spinSearchBy.getSelectedItem().equals("Location")) {

                            searchByVal = spinSearchBy.getSelectedItem().toString();
                            if (rg_attendence.getCheckedRadioButtonId() == -1) {
                                Toast.makeText(mContext, "Please Select the Present or Absent", Toast.LENGTH_SHORT).show();
                                focusView = rg_attendence;
                                focusView.requestFocus();
                                return;
                            } else if (spinSearchBy.getSelectedItemPosition() == 0) {
                                Toast.makeText(mContext, "Please select Search By", Toast.LENGTH_SHORT).show();
                                focusView = spinSearchBy;
                                focusView.requestFocus();
                                spinSearchBy.setFocusable(true);
                                spinSearchBy.requestFocusFromTouch();
                                return;
                            } else if (spinNation.getSelectedItemPosition() == 0) {
                                Toast.makeText(mContext, "Please select Nation", Toast.LENGTH_SHORT).show();
                                focusView = spinNation;
                                focusView.requestFocus();
                                spinNation.setFocusable(true);
                                spinNation.requestFocusFromTouch();
                                return;
                            } else if (spinZone.getSelectedItemPosition() == 0) {
                                Toast.makeText(mContext, "Please select Zone", Toast.LENGTH_SHORT).show();
                                focusView = spinZone;
                                focusView.requestFocus();
                                spinZone.setFocusable(true);
                                spinZone.requestFocusFromTouch();
                                return;
                            } else if (spinRegion.getSelectedItemPosition() == 0) {
                                Toast.makeText(mContext, "Please select Region", Toast.LENGTH_SHORT).show();
                                focusView = spinRegion;
                                focusView.requestFocus();
                                spinRegion.setFocusable(true);
                                spinRegion.requestFocusFromTouch();
                                return;
                            } else if (spinCluster.getSelectedItemPosition() == 0) {
                                Toast.makeText(mContext, "Please select Cluster", Toast.LENGTH_SHORT).show();
                                focusView = spinCluster;
                                focusView.requestFocus();
                                spinCluster.setFocusable(true);
                                spinCluster.requestFocusFromTouch();
                                return;
                            } else if (spinLocation.getSelectedItemPosition() == 0) {
                                Toast.makeText(mContext, "Please select Location", Toast.LENGTH_SHORT).show();
                                focusView = spinLocation;
                                focusView.requestFocus();
                                spinLocation.setFocusable(true);
                                spinLocation.requestFocusFromTouch();
                                return;
                            } else if (spinEmployee.getSelectedItemPosition() == 0) {
                                Toast.makeText(mContext, "Please select Employee", Toast.LENGTH_SHORT).show();
                                focusView = spinEmployee;
                                focusView.requestFocus();
                                spinEmployee.setFocusable(true);
                                spinEmployee.requestFocusFromTouch();
                                return;
                            } else {
                                attendence_table.setVisibility(View.VISIBLE);
                                new GetAttendanceReport().execute();
                            }
                        } else if (spinSearchBy.getSelectedItem().equals("Date")) {
                            to_date.setError(null);
                            String to_date1 =to_date.getText().toString();
                            DateFormat inputDF = new SimpleDateFormat("MM-dd-yyyy");
                            DateFormat outputDF = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = null;
                            try {
                                date = inputDF.parse(to_date1);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            to_date_Val = outputDF.format(date);
                            String from_date1 =from_date.getText().toString();
                            DateFormat inputDF1 = new SimpleDateFormat("MM-dd-yyyy");
                            DateFormat outputDF1 = new SimpleDateFormat("yyyy-MM-dd");
                            Date date1 = null;
                            try {
                                date1 = inputDF1.parse(from_date1);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            from_date_Val=outputDF1.format(date1);
                            if (rg_attendence.getCheckedRadioButtonId() == -1) {
                                Toast.makeText(mContext, "Please Select the Present or Absent", Toast.LENGTH_SHORT).show();
                                focusView = rg_attendence;
                                focusView.requestFocus();
                                return;
                            } else if (spinMonth.getSelectedItemPosition() == 0) {
                                Toast.makeText(mContext, "Please select Month", Toast.LENGTH_SHORT).show();
                                focusView = spinMonth;
                                focusView.requestFocus();
                                spinMonth.setFocusable(true);
                                spinMonth.requestFocusFromTouch();
                                return;
                            } else if (spinYear.getSelectedItemPosition() == 0) {
                                Toast.makeText(mContext, "Please select Year", Toast.LENGTH_SHORT).show();
                                focusView = spinYear;
                                focusView.requestFocus();
                                spinYear.setFocusable(true);
                                spinYear.requestFocusFromTouch();
                                return;
                            }else if (TextUtils.isEmpty(to_date.getText().toString())){
                                to_date.setError("Please Select month and year");
                                focusView = to_date;
                                focusView.requestFocus();
                                return;
                            }else if (TextUtils.isEmpty(from_date.getText().toString())){
                                from_date.setError("Please Select month and year");
                                focusView = from_date;
                                focusView.requestFocus();
                                return;
                            }else {
                                attendence_table.setVisibility(View.VISIBLE);
                                new GetAttendanceReport().execute();
                            }

                        }
                    }
                }
            }
        });
    }

    public void setSubStatusData() {
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
            if (fromHomeKey != null) {
                if (fromHomeKey.equals("FromStatus")) {
                    tvHeader.setText(this.getResources().getString(R.string.status_report));
                }
            } else if (fromHomeKey1 != null) {
                if (fromHomeKey1.equals("FromAttendence")) {
                    tvHeader.setText(this.getResources().getString(R.string.attendance));
                }
            }


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

                adapterMaster.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinCluster.setAdapter(adapterMaster);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                new GetGeoHierarchyLocation().execute();
            }

        }
    }

    //..........For Gettting last date of Month.........
    public static java.util.Date calculateMonthEndDate(int month, int year) {
        int[] daysInAMonth = {29, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int day = daysInAMonth[month];
        boolean isLeapYear = new GregorianCalendar().isLeapYear(year);

        if (isLeapYear && month == 2) {
            day++;
        }
        GregorianCalendar gc = new GregorianCalendar(year, month - 1, day);
        java.util.Date monthEndDate = new java.util.Date(gc.getTime().getTime());
        return monthEndDate;
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

                object = webService.StatusReport(emp, rm);

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
                    statusReportAdapter = new StatusReportAdapter(mContext, getStatusReportModelList);
                    lvStatusReport.setAdapter(statusReportAdapter);
                    setListViewHeightBasedOnItems(lvStatusReport);
                    statusReportAdapter.notifyDataSetChanged();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public class GetAttendanceReport extends AsyncTask<Void, Void, SoapObject> {

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
                if (!searchByVal.equals("")){
                    if (searchByVal.equals("Location")){
                        object = webService.AttendanceReport("", "", attendanceVal, empcode, rm, searchByVal, nationVal,
                                zoneVal, regionVal, locationVal, clusterVal);
                    }else if (searchByVal.equals("Date")){
                        object = webService.AttendanceReport(from_date_Val,to_date_Val, attendanceVal, empcode, rm, searchByVal, "",
                                "", "", "", "");
                    }
                }


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
            attendanceReportModelList.clear();
            for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                SoapObject root = (SoapObject) soapObject.getProperty(i);


                if (root.getProperty("attendance1") != null) {

                    if (!root.getProperty("attendance1").toString().equalsIgnoreCase("anyType{}")) {
                        ws_attendance = root.getProperty("attendance1").toString();


                    } else {
                        ws_attendance = "";
                    }
                } else {
                    ws_attendance = "";
                }

                if (root.getProperty("emp_code") != null) {

                    if (!root.getProperty("emp_code").toString().equalsIgnoreCase("anyType{}")) {
                        ws_empcode = root.getProperty("emp_code").toString();
                    } else {
                        ws_empcode = "";
                    }
                } else {
                    ws_empcode = "";
                }

                if (root.getProperty("insertdate") != null) {

                    if (!root.getProperty("insertdate").toString().equalsIgnoreCase("anyType{}")) {
                        ws_insertdate = root.getProperty("insertdate").toString();
                    } else {
                        ws_insertdate = "";
                    }
                } else {
                    ws_insertdate = "";
                }

                if (root.getProperty("latitude") != null) {

                    if (!root.getProperty("latitude").toString().equalsIgnoreCase("anyType{}")) {
                        ws_lat = root.getProperty("latitude").toString();
                    } else {
                        ws_lat = "";
                    }
                } else {
                    ws_lat = "";
                }

                if (root.getProperty("longitude") != null) {

                    if (!root.getProperty("longitude").toString().equalsIgnoreCase("anyType{}")) {
                        ws_long = root.getProperty("longitude").toString();
                    } else {
                        ws_long = "";
                    }
                } else {
                    ws_long = "";
                }

                Double latitude = Double.parseDouble(ws_lat);
                Double longitude = Double.parseDouble(ws_long);
                String loc = "";
                try {
                    Geocoder geo = new Geocoder(mContext, Locale.getDefault());
                    List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);
                    loc = addresses.get(0).getLocality();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (root.getProperty("name") != null) {

                    if (!root.getProperty("name").toString().equalsIgnoreCase("anyType{}")) {
                        ws_name = root.getProperty("name").toString();
                    } else {
                        ws_name = "";
                    }
                } else {
                    ws_name = "";
                }
                attendanceReportModel = new AttendenceReportModel();
                attendanceReportModel.setAttendance(ws_attendance);
                attendanceReportModel.setLatitude("");
                attendanceReportModel.setLongitude("");
                attendanceReportModel.setEmpId1(ws_empcode);
                attendanceReportModel.setInsertdate(ws_insertdate);
                attendanceReportModel.setLocation(loc);
                attendanceReportModel.setNameAttendence(ws_name);

                attendanceReportModelList.add(attendanceReportModel);
            }

            if (attendanceReportModelList != null && attendanceReportModelList.size() > 0) {
                attendanceReportAdapter = new AttendanceReportAdapter(mContext, attendanceReportModelList);
                lvAttendanceReport.setAdapter(attendanceReportAdapter);
                setListViewHeightBasedOnItems(lvAttendanceReport);
                attendanceReportAdapter.notifyDataSetChanged();

            }
        }
    }
}
