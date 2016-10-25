package com.narnolia.app;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by USER on 10/24/2016.
 */

public class LeadActivity extends AbstractActivity {

    private Context mContext;
    EditText name, mobileno, location, city, pincode;
    TextView tv_source_of_lead, tv_sub_source_of_lead, tv_name,
            tv_mobile_no, tv_location, tv_city, tv_pincode, tv_prospective_products;
    Spinner spinner_source_of_lead, spinner_sub_source, spinner_prospective_product;
    Button btn_create, btn_cancel, btn_create_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead);

        initView();

    }
    private void initView(){

        try {
            mContext = LeadActivity.this;

            setHeader();

            //............Edit Text......
            name = (EditText) findViewById(R.id.edt1_name);
            mobileno = (EditText) findViewById(R.id.edt1_mobile_no);
            location = (EditText) findViewById(R.id.edt1_location);
            city = (EditText) findViewById(R.id.edt1_city);
            pincode = (EditText) findViewById(R.id.edt1_pincode);


            //..............Text View.....
            tv_source_of_lead = (TextView) findViewById(R.id.txt1_source_of_lead);
            tv_sub_source_of_lead = (TextView) findViewById(R.id.txt1_sub_source);
            tv_name = (TextView) findViewById(R.id.txt1_name);
            tv_mobile_no = (TextView) findViewById(R.id.txt1_mobile_no);
            tv_location = (TextView) findViewById(R.id.txt1_location);
            tv_city = (TextView) findViewById(R.id.txt1_city);
            tv_pincode = (TextView) findViewById(R.id.txt1_pincode);
            tv_prospective_products = (TextView) findViewById(R.id.txt1_prospective_product);

            //...............Spinner...
            spinner_source_of_lead = (Spinner) findViewById(R.id.spin1_source_of_lead);
            spinner_sub_source = (Spinner) findViewById(R.id.spin1_sub_source);
            spinner_prospective_product = (Spinner) findViewById(R.id.spin1_prospective_product);

            //..................Button.....
            btn_create = (Button) findViewById(R.id.btn1_create_lead);
            btn_cancel = (Button) findViewById(R.id.btn1_cancel_lead);
            btn_create_close = (Button) findViewById(R.id.btn1_create_close_lead);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setHeader() {
        try {
            TextView tvHeader = (TextView) findViewById(R.id.textTitle);
            ImageView ivHome = (ImageView) findViewById(R.id.iv_home);
            ImageView ivLogout = (ImageView) findViewById(R.id.iv_logout);

            tvHeader.setText(mContext.getResources().getString(R.string.header_text_create_lead));

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
