package com.narnolia.app;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.narnolia.app.dbconfig.DbHelper;
import com.narnolia.app.libs.Utils;
import com.narnolia.app.model.LeadInfoModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by USER on 10/24/2016.
 */

public class UpdateLeadActivity extends AbstractActivity {

    private Context mContext;
    private LeadInfoModel leadInfoModel;

    EditText fname,mname,lname, mobileno, email, date_of_birth, address, flat, street, location, city,
            pincode, next_metting_date, metting_agenda, lead_update_log;
    Spinner spinner_lead_name, spinner_source_of_lead, spinner_sub_source, spinner_age_group,
            spinner_occupation, spinner_annual_income, spinner_other_broker,spinner_lead_status,spinner_research_type,spinner_duration;
    TextView tv_lead_name, tv_source_of_lead, tv_sub_source, tv_fname,tv_mname,tv_lname, tv_mobile_no, tv_email,
            tv_age_group, tv_date_of_birth, tv_address, tv_flat, tv_street, tv_location, tv_city, tv_pincode,
            tv_occupation, tv_annual_income, tv_other_broker, tv_meeting_status, tv_meeting_agenda, tv_lead_update_log,tv_lead_status;
    RadioGroup rg_meeting_status;
    RadioButton rb_contact, rb_not_contact;
    LinearLayout connect,notconnect,linear_non_salaried,linear_remark,linear_competitor,linear_research;
    //.........Edit Text Strings
    String str_fname,str_mname,str_lname,str_mobile_no,str_email,str_date_of_birth,str_address,str_flat,str_street,str_laocion,str_city,
            str_pincode,str_next_meeting_date,str_metting_agenda,str_lead_update_log;
    //........Spineer Strings
    String str_spinner_lead_name, str_spinner_source_of_lead, str_spinner_sub_source, str_spinner_age_group,
            str_spinner_occupation,str_spinner_annual_income, str_spinner_other_broker,str_spinner_lead_status,str_spinner_research_type,str_spinner_duration;
    //......Radio Group String
    String str_rg_meeting_status;
    Button bt_update_lead, bt_close_lead;
    private DatePickerDialog datePickerDialog,datePickerDialog1;   //date picker declare
    private SimpleDateFormat dateFormatter;      //date format declare
    private List<String> spinAgeGroupArray = new ArrayList<String>(); //age group array
    private List<String> spinOccupationArray = new ArrayList<String>(); //Occupation array
    private List<String> spinAnnualInacomeArray = new ArrayList<String>(); //Annual Income array
    private List<String> spinOtherBrokersArray = new ArrayList<String>(); //Other Brokers array
    private List<String> spinLeadStatusArray = new ArrayList<String>(); //Other Brokers array
    private List<String> spinDurationArray = new ArrayList<String>(); //Other Brokers array
    private List<String> spinSourceLeadList;
    private List<String> spinSubSourceLeadList;
    String[] strLeadArray = null;
    String[] strSubLeadArray = null;
    private String empcode;
    private SharedPref sharedPref;
    private TextView admin;

    private String LeadId;
    int directLeadId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_update);

        sharedPref = new SharedPref(UpdateLeadActivity.this);
        empcode = sharedPref.getLoginId();

        initView();

    }
    private void hide_keyboard(Context context, View view) {
        InputMethodManager inputManager = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, 0);
    }
    private void initView() {

        try {
            mContext = UpdateLeadActivity.this;

            admin = (TextView) findViewById(R.id.admin);
            admin.setText(empcode);

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            setHeader();

            //............Edit Text Ref
            fname = (EditText) findViewById(R.id.edt_fname);
            mname=(EditText)findViewById(R.id.edt_mname);
            lname=(EditText)findViewById(R.id.edt_lname);
            mobileno = (EditText) findViewById(R.id.edt_mob_no);
            email = (EditText) findViewById(R.id.edt_email);
            date_of_birth = (EditText) findViewById(R.id.edt_date_of_birth);
            address = (EditText) findViewById(R.id.edt_address);
            flat = (EditText) findViewById(R.id.edt_flat);
            street = (EditText) findViewById(R.id.edt_street);
            location = (EditText) findViewById(R.id.edt_location);
            city = (EditText) findViewById(R.id.edt_city);
            pincode = (EditText) findViewById(R.id.edt_pincode);
            next_metting_date = (EditText) findViewById(R.id.edt_next_meeting);
            metting_agenda = (EditText) findViewById(R.id.edt_meeting_agenda);
            lead_update_log = (EditText) findViewById(R.id.edt_lead_update_log);
            //..................edit text validations.......
            fname.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (fname.length()>0){fname.setError(null);}
                }
            });
            mname.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (mname.length()>0){mname.setError(null);}
                }
            });
            lname.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (lname.length()>0){lname.setError(null);}
                }
            });
            city.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (city.length()>0){city
                            .setError(null);}
                }
            });

            leadInfoModel = (LeadInfoModel) getIntent().getSerializableExtra(Utils.KEY_LEAD_DATA);
            LeadId = leadInfoModel.getLead_id();
            directLeadId = leadInfoModel.getDirect_lead_id();

            String firstname = leadInfoModel.getFirstname();


            populateData();

            //.............TextView Ref
            tv_lead_name = (TextView) findViewById(R.id.txt_lead_name);
            tv_source_of_lead = (TextView) findViewById(R.id.txt_source_of_lead);
            tv_source_of_lead.setText(Html.fromHtml("<font color=\"red\">*</font>"+"<font color=\"black\">Source of Lead</font>\n"));
            tv_sub_source = (TextView) findViewById(R.id.txt_sub_source);
            tv_sub_source.setText(Html.fromHtml("<font color=\"red\">*</font>"+"<font color=\"black\">Sub Source</font>\n"));
            tv_fname = (TextView) findViewById(R.id.txt_fname);
            tv_fname.setText(Html.fromHtml("<font color=\"red\">*</font>"+"<font color=\"black\">First Name</font>\n"));
            tv_mname = (TextView) findViewById(R.id.txt_mname);
            tv_lname = (TextView) findViewById(R.id.txt_lname);
            tv_lname.setText(Html.fromHtml("<font color=\"red\">*</font>"+"<font color=\"black\">Last Name</font>\n"));
            tv_mobile_no = (TextView) findViewById(R.id.txt_mob_no);
            tv_mobile_no.setText(Html.fromHtml("<font color=\"red\">*</font>"+"<font color=\"black\">Mobile Number</font>\n"));
            tv_email = (TextView) findViewById(R.id.txt_email);
            tv_age_group = (TextView) findViewById(R.id.txt_age_group);
            tv_date_of_birth = (TextView) findViewById(R.id.txt_date_of_birth);
            tv_address = (TextView) findViewById(R.id.txt_address);
            tv_flat = (TextView) findViewById(R.id.txt_flat);
            tv_street = (TextView) findViewById(R.id.txt_street);
            tv_location = (TextView) findViewById(R.id.txt_location);
            tv_city = (TextView) findViewById(R.id.txt_city);
            tv_city.setText(Html.fromHtml("<font color=\"red\">*</font>"+"<font color=\"black\">City</font>\n"));
            tv_pincode = (TextView) findViewById(R.id.txt_pincode);
            tv_pincode.setText(Html.fromHtml("<font color=\"red\">*</font>"+"<font color=\"black\">Pincode</font>\n"));
            tv_occupation = (TextView) findViewById(R.id.txt_occupation);
            tv_annual_income = (TextView) findViewById(R.id.txt_annual_income);
            tv_other_broker = (TextView) findViewById(R.id.txt_other_broker);
            tv_meeting_status = (TextView) findViewById(R.id.txt_meeting_status);
            tv_meeting_status.setText(Html.fromHtml("<font color=\"red\">*</font>"+"<font color=\"black\">Metting Status</font>\n"));
            tv_meeting_agenda = (TextView) findViewById(R.id.txt_meeting_agenda);
            tv_lead_update_log = (TextView) findViewById(R.id.txt_lead_update_log);
            tv_lead_status=(TextView)findViewById(R.id.tvLeadStatus_connected);
            tv_lead_status.setText(Html.fromHtml("<font color=\"red\">*</font>"+"<font color=\"black\">Lead Status</font>\n"));


            //....................spinner Ref........
            spinner_lead_name = (Spinner) findViewById(R.id.spin_lead_name);
            spinner_source_of_lead = (Spinner) findViewById(R.id.spin_source_of_lead);
            spinner_sub_source = (Spinner) findViewById(R.id.spin_sub_source);
            spinner_age_group = (Spinner) findViewById(R.id.spin_age_group);
            spinner_occupation = (Spinner) findViewById(R.id.spin_occupation);
            spinner_annual_income = (Spinner) findViewById(R.id.spin_annual_income);
            spinner_other_broker = (Spinner) findViewById(R.id.spin_other_broker);
            spinner_lead_status=(Spinner)findViewById(R.id.spin_lead_status);
            spinner_duration=(Spinner)findViewById(R.id.spin_duration);
            spinner_research_type=(Spinner)findViewById(R.id.spin_research_type);
            //.................radio group layout......
            connect=(LinearLayout)findViewById(R.id.linear_meeting_status_connected);
            notconnect=(LinearLayout)findViewById(R.id.linear_meeting_status_not_connected);
            //...............intenal linear layouts
            linear_non_salaried=(LinearLayout)findViewById(R.id.linear_non_salaried);
            linear_remark=(LinearLayout)findViewById(R.id.linear_remark);
            linear_competitor=(LinearLayout)findViewById(R.id.linear_competitor);
            linear_research=(LinearLayout)findViewById(R.id.linear_research);

            //spinner OnItemSelectedListener
            spinner_source_of_lead.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if ( strLeadArray != null && strLeadArray.length > 0){
                        String strSourceofLead = spinner_source_of_lead.getSelectedItem().toString();
//                        fetchDistrCodeBranchName(strServiceBranchCode);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spinner_sub_source.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if ( strSubLeadArray != null && strSubLeadArray.length > 0){
                        String strSubSourceofLead = spinner_sub_source.getSelectedItem().toString();
//                        fetchDistrCodeBranchName(strServiceBranchCode);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
//.........................spinner occupation item selection
            spinner_occupation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent,
                                           View view, int pos, long id) {
                    String occuString;
                    occuString = parent.getItemAtPosition(pos).toString();
                    if (occuString != null) {
                        if (occuString.equalsIgnoreCase("Non-Salaried")) {

                            linear_non_salaried.setVisibility(View.VISIBLE);

                        } else {
                            linear_non_salaried.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }
            });
            //.........................lead status slection......
            spinner_lead_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent,
                                           View view, int pos, long id) {
                    String leadString,leadString1,leadString2;
                    leadString = parent.getItemAtPosition(pos).toString();
                    leadString1=parent.getItemAtPosition(pos).toString();
                    leadString2=parent.getItemAtPosition(pos).toString();
                    if (leadString != null) {
                        if (leadString.equalsIgnoreCase("Not Intersted")) {

                            linear_remark.setVisibility(View.VISIBLE);

                        } else {
                            linear_remark.setVisibility(View.GONE);
                        }
                    }
                    if (leadString1 != null) {
                        if (leadString1.equalsIgnoreCase("Lost to Competitor")) {

                            linear_competitor.setVisibility(View.VISIBLE);

                        } else {
                            linear_competitor.setVisibility(View.GONE);
                        }
                    }
                    if (leadString2 != null) {
                        if (leadString2.equalsIgnoreCase("Research Servicing")) {

                            linear_research.setVisibility(View.VISIBLE);

                        } else {
                            linear_research.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }
            });
            fetchSourcedata();
            fetchSubSourcedata();




            // ...............Radio Group.....
            rg_meeting_status = (RadioGroup) findViewById(R.id.rg_metting_status);

            //................Radio Button
            rb_contact = (RadioButton) findViewById(R.id.rb_contact);
            rb_not_contact = (RadioButton) findViewById(R.id.rb_not_contact);

            //................Button ........
            bt_update_lead = (Button) findViewById(R.id.btn_update);
            bt_close_lead = (Button) findViewById(R.id.btn_close);
            //....................update lead click event...
            bt_update_lead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    validateDetails();
                }
            });
            try {
                //.............date of birth date picker

                final Calendar newCalendar = Calendar.getInstance();
                dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
                datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int monthOfYear, int dayOfMonth, int year) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(monthOfYear, dayOfMonth, year);
                        date_of_birth.setText(dateFormatter.format(newDate.getTime()));

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                date_of_birth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hide_keyboard(mContext,v);
                        datePickerDialog.show();
                    }
                });
                //.......Next Date metting.........
                datePickerDialog1 = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int monthOfYear, int dayOfMonth, int year) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(monthOfYear, dayOfMonth, year);
                        next_metting_date.setText(dateFormatter.format(newDate.getTime()));
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                next_metting_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datePickerDialog1.show();
                    }
                });
                rg_meeting_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        View radiobutton = group.findViewById(checkedId);
                        int index = group.indexOfChild(radiobutton);
                        if (index == 0) {
                            connect.setVisibility(View.VISIBLE);
                            notconnect.setVisibility(View.GONE);

                        } else {
                            notconnect.setVisibility(View.VISIBLE);
                            connect.setVisibility(View.GONE);

                        }
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setHeader() {
        try {
            TextView tvHeader = (TextView) findViewById(R.id.textTitle);
            ImageView ivHome = (ImageView) findViewById(R.id.iv_home);
            ImageView ivLogout = (ImageView) findViewById(R.id.iv_logout);

            tvHeader.setText(mContext.getResources().getString(R.string.header_text_update_lead));

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

    private void fetchSourcedata() {
        try {

            spinSourceLeadList = new ArrayList<>();
            String SourceLead = "SourceLead";

            String where = " where type like " + "'" + SourceLead + "'";
            Cursor cursor2 = Narnolia.dbCon.fetchFromSelect(DbHelper.TABLE_M_PARAMETER, where);
            if (cursor2 != null && cursor2.getCount() > 0) {
                cursor2.moveToFirst();
                do {
                    String branch = "";
                    branch = cursor2.getString(cursor2.getColumnIndex("value"));
                    spinSourceLeadList.add(branch);
                } while (cursor2.moveToNext());
                cursor2.close();
            }
            if (spinSourceLeadList.size() > 0) {
                strLeadArray = new String[spinSourceLeadList.size() + 1];
                strLeadArray[0] = "Select Source Lead";
                for (int i = 0; i < spinSourceLeadList.size(); i++) {
                    strLeadArray[i + 1] = spinSourceLeadList.get(i);
                }
            }

           /* spinSourceLeadList.addAll(Arrays.asList(getResources().getStringArray(R.array.source_array)));
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinSourceLeadList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner_source_of_lead.setAdapter(adapter);*/


            if (spinSourceLeadList != null && spinSourceLeadList.size() > 0) {
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, strLeadArray);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner_source_of_lead.setAdapter(adapter1);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //...............validatations.....
    private void validateDetails(){
        try {
            String str_fname,str_mname,str_lname,str_mob,str_city,str_pincode;
            mobileno.setError(null);
            city.setError(null);
            pincode.setError(null);
            fname.setError(null);
            mname.setError(null);
            lname.setError(null);
            str_fname=fname.getText().toString().trim();
            str_mname=mname.getText().toString().trim();
            str_lname=lname.getText().toString().trim();
            str_mob=mobileno.getText().toString().trim();
            str_city=city.getText().toString().trim();
            str_pincode=pincode.getText().toString().trim();

            View focusView = null;
            if (TextUtils.isEmpty(str_fname)) {
                fname.setError(getString(R.string.name));
                focusView = fname;
                focusView.requestFocus();

                return;
            }
            if (TextUtils.isEmpty(str_mname)) {
                mname.setError(getString(R.string.name));
                focusView = mname;
                focusView.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(str_lname)) {
                lname.setError(getString(R.string.name));
                focusView = lname;
                focusView.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(str_mob)) {
                mobileno.setError(getString(R.string.reqmob));
                focusView = mobileno;
                focusView.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(str_city)){
                city.setError(getString(R.string.reqcity));
                focusView=city;
                focusView.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(str_pincode)) {

                pincode.setError(getString(R.string.reqpincode));
                focusView = pincode;
                focusView.requestFocus();
                return;
            }
            if (spinner_source_of_lead.getCount()==0){
                focusView=spinner_source_of_lead;
                focusView.requestFocus();
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void fetchSubSourcedata() {
        try {

            spinSubSourceLeadList = new ArrayList<>();
            String SubSourceLead = "SubSource ";

            String where = " where type like " + "'" + SubSourceLead + "'";
            Cursor cursor2 = Narnolia.dbCon.fetchFromSelect(DbHelper.TABLE_M_PARAMETER, where);
            if (cursor2 != null && cursor2.getCount() > 0) {
                cursor2.moveToFirst();
                do {
                    String sourcelead = "";
                    sourcelead = cursor2.getString(cursor2.getColumnIndex("value"));
                    spinSubSourceLeadList.add(sourcelead);
                } while (cursor2.moveToNext());
                cursor2.close();
            }
            if (spinSubSourceLeadList.size() > 0) {
                strSubLeadArray = new String[spinSubSourceLeadList.size() + 1];
                strSubLeadArray[0] = "Select Sub Source Lead";
                for (int i = 0; i < spinSubSourceLeadList.size(); i++) {
                    strSubLeadArray[i + 1] = spinSubSourceLeadList.get(i);
                }
            }

            if (spinSubSourceLeadList != null && spinSubSourceLeadList.size() > 0) {
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, strSubLeadArray);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner_sub_source.setAdapter(adapter1);
            }
            //........................Age Group spinner
            spinAgeGroupArray.addAll(Arrays.asList(getResources().getStringArray(R.array.age_group)));
            if (spinAgeGroupArray!=null&&spinAgeGroupArray.size()>0) {
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinAgeGroupArray);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_age_group.setAdapter(adapter2);
            }
            //........................Occupation Spinner
            spinOccupationArray.addAll(Arrays.asList(getResources().getStringArray(R.array.occupation_array)));
            if (spinOccupationArray!=null&&spinOccupationArray.size()>0) {
                ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinOccupationArray);
                adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_occupation.setAdapter(adapter3);
            }
            //............................Annual Income Spinner
            spinAnnualInacomeArray.addAll(Arrays.asList(getResources().getStringArray(R.array.annual_income_aaray)));
            if (spinAnnualInacomeArray!=null&&spinAnnualInacomeArray.size()>0) {
                ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinAnnualInacomeArray);
                adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_annual_income.setAdapter(adapter4);
            }
            //............................Other Brokers Spinner
            spinOtherBrokersArray.addAll(Arrays.asList(getResources().getStringArray(R.array.brokers_array)));
            if (spinOtherBrokersArray!=null&&spinOtherBrokersArray.size()>0) {
                ArrayAdapter<String> adapter5= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinOtherBrokersArray);
                adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_other_broker.setAdapter(adapter5);
            }
            //.............................Lead Staus Array
            spinLeadStatusArray.addAll(Arrays.asList(getResources().getStringArray(R.array.lead_status_array)));
            if (spinLeadStatusArray!=null&&spinLeadStatusArray.size()>0) {
                ArrayAdapter<String> adapter6= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinLeadStatusArray);
                adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_lead_status.setAdapter(adapter6);
            }
            //.........................Duration Array
            spinDurationArray.addAll(Arrays.asList(getResources().getStringArray(R.array.duration_array)));
            if (spinDurationArray!=null&&spinDurationArray.size()>0) {
                ArrayAdapter<String> adapter7= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinDurationArray);
                adapter7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_duration.setAdapter(adapter7);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateOrInsertInDb(){
        //........Edit text get Text
        str_fname=fname.getText().toString().toString();
        str_mname=mname.getText().toString().trim();
        str_lname=lname.getText().toString().trim();
        str_mobile_no=mobileno.getText().toString().trim();
        str_email=email.getText().toString().trim();
        str_date_of_birth=date_of_birth.getText().toString().trim();
        str_address=address.getText().toString().trim();
        str_flat=flat.getText().toString().trim();
        str_street=street.getText().toString().trim();
        str_laocion=location.getText().toString().trim();
        str_city=city.getText().toString().trim();
        str_pincode=pincode.getText().toString().trim();
        str_next_meeting_date=next_metting_date.getText().toString().trim();
        str_metting_agenda=metting_agenda.getText().toString().trim();
        str_lead_update_log=lead_update_log.getText().toString().trim();
        //.......spinner text get Text

        str_spinner_lead_name=spinner_lead_name.getSelectedItem().toString();
        str_spinner_source_of_lead=spinner_source_of_lead.getSelectedItem().toString();
        str_spinner_sub_source=spinner_sub_source.getSelectedItem().toString();
        str_spinner_age_group=spinner_age_group.getSelectedItem().toString();
        str_spinner_occupation=spinner_occupation.getSelectedItem().toString();
        str_spinner_annual_income=spinner_annual_income.getSelectedItem().toString();
        str_spinner_other_broker=spinner_other_broker.getSelectedItem().toString();
        str_spinner_lead_status=spinner_lead_status.getSelectedItem().toString();
        str_spinner_research_type=spinner_research_type.getSelectedItem().toString();
        str_spinner_duration=spinner_duration.getSelectedItem().toString();


        //......Radio Group get Text
        rg_meeting_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) rg_meeting_status.findViewById(checkedId);
                str_rg_meeting_status = rb.getText().toString();
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

    private void populateData(){
        try{

            fname.setText(leadInfoModel.getFirstname());
            mname.setText(leadInfoModel.getMiddlename());
            lname.setText(leadInfoModel.getLastname());
            mobileno.setText(leadInfoModel.getMobile_no());
            location.setText(leadInfoModel.getLocation());
            city.setText(leadInfoModel.getCity());
            pincode.setText(leadInfoModel.getPincode());

            if (leadInfoModel.getSource_of_lead() != null && leadInfoModel.getSource_of_lead().trim().length() > 0) {
                spinner_source_of_lead.setSelection(spinSourceLeadList.indexOf(leadInfoModel.getSource_of_lead()) + 1);
            }

            if (leadInfoModel.getSub_source() != null && leadInfoModel.getSub_source().trim().length() > 0) {
                spinner_sub_source.setSelection(spinSubSourceLeadList.indexOf(leadInfoModel.getSub_source()) + 1);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
