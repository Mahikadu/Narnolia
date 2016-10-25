package com.narnolia.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by USER on 10/24/2016.
 */

public class UpdateLeadActivity extends Activity {
    EditText name,mobileno,email,date_of_birth,address,flat,street,location,city,pincode,next_metting_date,metting_agenda,lead_update_log;
    Spinner spinner_lead_name,spinner_source_of_lead,spinner_sub_source,spinner_age_group,spinner_occupation,spinner_annual_income,spinner_other_broker;
    TextView tv_lead_name,tv_source_of_lead,tv_sub_source,tv_name,tv_mobile_no,tv_email,tv_age_group,tv_date_of_birth,tv_address,tv_flat,tv_street,tv_location,tv_city,tv_pincode,tv_occupation,tv_annual_income,tv_other_broker,tv_meeting_status,tv_meeting_agenda,tv_lead_update_log;
    RadioGroup rg_meeting_status;
    RadioButton rb_contact,rb_not_contact;
    Button bt_update_lead,bt_close_lead;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_update);

        //............Edit Text Ref
        name=(EditText)findViewById(R.id.edt_name);
        mobileno=(EditText)findViewById(R.id.edt_mob_no);
        email=(EditText)findViewById(R.id.edt_email);
        date_of_birth=(EditText)findViewById(R.id.edt_date_of_birth);
        address=(EditText)findViewById(R.id.edt_address);
        flat=(EditText)findViewById(R.id.edt_flat);
        street=(EditText)findViewById(R.id.edt_street);
        location=(EditText)findViewById(R.id.edt_location);
        city=(EditText)findViewById(R.id.edt_city);
        pincode=(EditText)findViewById(R.id.edt_pincode);
        next_metting_date=(EditText)findViewById(R.id.edt_next_meeting);
        metting_agenda=(EditText)findViewById(R.id.edt_meeting_agenda);
        lead_update_log=(EditText)findViewById(R.id.edt_lead_update_log);

//.............TextView Ref
        tv_lead_name=(TextView)findViewById(R.id.txt_lead_name);
        tv_source_of_lead=(TextView)findViewById(R.id.txt_source_of_lead);
        tv_sub_source=(TextView)findViewById(R.id.txt_sub_source);
        tv_name=(TextView)findViewById(R.id.txt_name);
        tv_mobile_no=(TextView)findViewById(R.id.txt_mob_no);
        tv_email=(TextView)findViewById(R.id.txt_email);
        tv_age_group=(TextView)findViewById(R.id.txt_age_group);
        tv_date_of_birth=(TextView)findViewById(R.id.txt_date_of_birth);
        tv_address=(TextView)findViewById(R.id.txt_address);
        tv_flat=(TextView)findViewById(R.id.txt_flat);
        tv_street=(TextView)findViewById(R.id.txt_street);
        tv_location=(TextView)findViewById(R.id.txt_location);
        tv_city=(TextView)findViewById(R.id.txt_city);
        tv_pincode=(TextView)findViewById(R.id.txt_pincode);
        tv_occupation=(TextView)findViewById(R.id.txt_occupation);
        tv_annual_income=(TextView)findViewById(R.id.txt_annual_income);
        tv_other_broker=(TextView)findViewById(R.id.txt_other_broker);
        tv_meeting_status=(TextView)findViewById(R.id.txt_meeting_status);
        tv_meeting_agenda=(TextView)findViewById(R.id.txt_meeting_agenda);
        tv_lead_update_log=(TextView)findViewById(R.id.txt_lead_update_log);
//....................spinner Ref........
        spinner_lead_name=(Spinner)findViewById(R.id.spin_lead_name);
        spinner_source_of_lead=(Spinner)findViewById(R.id.spin_source_of_lead);
        spinner_sub_source=(Spinner)findViewById(R.id.spin_sub_source);
        spinner_age_group=(Spinner)findViewById(R.id.spin_age_group);
        spinner_occupation=(Spinner)findViewById(R.id.spin_occupation);
        spinner_annual_income=(Spinner)findViewById(R.id.spin_annual_income);
        spinner_other_broker=(Spinner)findViewById(R.id.spin_other_broker);
        //...............Radio Group.....
        rg_meeting_status=(RadioGroup)findViewById(R.id.rg_metting_status);
        //................Radio Button
        rb_contact=(RadioButton)findViewById(R.id.rb_contact);
        rb_not_contact=(RadioButton)findViewById(R.id.rb_not_contact);
        //................Button ........
        bt_update_lead=(Button)findViewById(R.id.btn_update);
        bt_close_lead=(Button)findViewById(R.id.btn_close);



    }
}
