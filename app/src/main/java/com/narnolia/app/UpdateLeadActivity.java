package com.narnolia.app;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.narnolia.app.dbconfig.DbHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by USER on 10/24/2016.
 */

public class UpdateLeadActivity extends AbstractActivity {

    private Context mContext;

    EditText name, mobileno, email, date_of_birth, address, flat, street, location, city,
            pincode, next_metting_date, metting_agenda, lead_update_log;
    Spinner spinner_lead_name, spinner_source_of_lead, spinner_sub_source, spinner_age_group,
            spinner_occupation, spinner_annual_income, spinner_other_broker;
    TextView tv_lead_name, tv_source_of_lead, tv_sub_source, tv_name, tv_mobile_no, tv_email,
            tv_age_group, tv_date_of_birth, tv_address, tv_flat, tv_street, tv_location, tv_city, tv_pincode,
            tv_occupation, tv_annual_income, tv_other_broker, tv_meeting_status, tv_meeting_agenda, tv_lead_update_log;
    RadioGroup rg_meeting_status;
    RadioButton rb_contact, rb_not_contact;
    Button bt_update_lead, bt_close_lead;
    private DatePickerDialog datePickerDialog,datePickerDialog1;   //date picker declare
    private SimpleDateFormat dateFormatter;      //date format declare

    private List<String> spinSourceLeadList;
    private List<String> spinSubSourceLeadList;
    String[] strLeadArray = null;
    String[] strSubLeadArray = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_update);
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

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            setHeader();

            //............Edit Text Ref
            name = (EditText) findViewById(R.id.edt_name);
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

            //.............TextView Ref
            tv_lead_name = (TextView) findViewById(R.id.txt_lead_name);
            tv_source_of_lead = (TextView) findViewById(R.id.txt_source_of_lead);
            tv_sub_source = (TextView) findViewById(R.id.txt_sub_source);
            tv_name = (TextView) findViewById(R.id.txt_name);
            tv_mobile_no = (TextView) findViewById(R.id.txt_mob_no);
            tv_email = (TextView) findViewById(R.id.txt_email);
            tv_age_group = (TextView) findViewById(R.id.txt_age_group);
            tv_date_of_birth = (TextView) findViewById(R.id.txt_date_of_birth);
            tv_address = (TextView) findViewById(R.id.txt_address);
            tv_flat = (TextView) findViewById(R.id.txt_flat);
            tv_street = (TextView) findViewById(R.id.txt_street);
            tv_location = (TextView) findViewById(R.id.txt_location);
            tv_city = (TextView) findViewById(R.id.txt_city);
            tv_pincode = (TextView) findViewById(R.id.txt_pincode);
            tv_occupation = (TextView) findViewById(R.id.txt_occupation);
            tv_annual_income = (TextView) findViewById(R.id.txt_annual_income);
            tv_other_broker = (TextView) findViewById(R.id.txt_other_broker);
            tv_meeting_status = (TextView) findViewById(R.id.txt_meeting_status);
            tv_meeting_agenda = (TextView) findViewById(R.id.txt_meeting_agenda);
            tv_lead_update_log = (TextView) findViewById(R.id.txt_lead_update_log);

            //....................spinner Ref........
            spinner_lead_name = (Spinner) findViewById(R.id.spin_lead_name);
            spinner_source_of_lead = (Spinner) findViewById(R.id.spin_source_of_lead);
            spinner_sub_source = (Spinner) findViewById(R.id.spin_sub_source);
            spinner_age_group = (Spinner) findViewById(R.id.spin_age_group);
            spinner_occupation = (Spinner) findViewById(R.id.spin_occupation);
            spinner_annual_income = (Spinner) findViewById(R.id.spin_annual_income);
            spinner_other_broker = (Spinner) findViewById(R.id.spin_other_broker);

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

    private void fetchSubSourcedata() {
        try {

            spinSubSourceLeadList = new ArrayList<>();
            String SubSourceLead = "SubSource";

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


        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
