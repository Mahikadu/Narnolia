package com.narnolia.app;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.narnolia.app.dbconfig.DbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 10/24/2016.
 */

public class LeadActivity extends AbstractActivity {

    private Context mContext;
    EditText fname,mname,lname,mobileno, location, city, pincode;
    TextView tv_source_of_lead, tv_sub_source_of_lead, tv_name,
            tv_mobile_no, tv_location, tv_city, tv_pincode, tv_prospective_products;
    Spinner spinner_source_of_lead, spinner_sub_source, spinner_prospective_product;
    Button btn_create, btn_cancel, btn_create_close;
    private List<String> spinSourceLeadList;
    private List<String> spinSubSourceLeadList;
    private List<String> spinProductList;
    String[] strLeadArray = null;
    String[] strSubLeadArray = null;
    String[] strProductArray = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead);

        initView();

    }
    private void initView(){

        try {
            mContext = LeadActivity.this;

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            setHeader();


            //............Edit Text......
            fname = (EditText) findViewById(R.id.edt1_fname);
            mname=(EditText)findViewById(R.id.edt1_mname);
            lname=(EditText)findViewById(R.id.edt1_lname);
            mobileno = (EditText) findViewById(R.id.edt1_mobile_no);
            location = (EditText) findViewById(R.id.edt1_location);
            city = (EditText) findViewById(R.id.edt1_city);
            pincode = (EditText) findViewById(R.id.edt1_pincode);


            //..............Text View.....
            tv_source_of_lead = (TextView) findViewById(R.id.txt1_source_of_lead);
            tv_source_of_lead.setText(Html.fromHtml("<font color=\"red\">*</font>"+"<font color=\"black\">Source of Lead</font>\n"));
            tv_sub_source_of_lead = (TextView) findViewById(R.id.txt1_sub_source);
            tv_sub_source_of_lead.setText(Html.fromHtml("<font color=\"red\">*</font>"+"<font color=\"black\">Sub Source</font>\n"));
            tv_name = (TextView) findViewById(R.id.txt1_fname);
            tv_name.setText(Html.fromHtml("<font color=\"red\">*</font>"+"<font color=\"black\">First Name</font>\n"));
            tv_mobile_no = (TextView) findViewById(R.id.txt1_mobile_no);
            tv_mobile_no.setText(Html.fromHtml("<font color=\"red\">*</font>"+"<font color=\"black\">Mobile Number</font>\n"));
            tv_location = (TextView) findViewById(R.id.txt1_location);
            tv_city = (TextView) findViewById(R.id.txt1_city);
            tv_city.setText(Html.fromHtml("<font color=\"red\">*</font>"+"<font color=\"black\">City</font>\n"));
            tv_pincode = (TextView) findViewById(R.id.txt1_pincode);
            tv_pincode.setText(Html.fromHtml("<font color=\"red\">*</font>"+"<font color=\"black\">Pincode</font>\n"));
            tv_prospective_products = (TextView) findViewById(R.id.txt1_prospective_product);

            //...............Spinner...
            spinner_source_of_lead = (Spinner) findViewById(R.id.spin1_source_of_lead);
            spinner_sub_source = (Spinner) findViewById(R.id.spin1_sub_source);
            spinner_prospective_product = (Spinner) findViewById(R.id.spin1_prospective_product);

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

            spinner_prospective_product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if ( strLeadArray != null && strLeadArray.length > 0){
                        String strProduct = spinner_prospective_product.getSelectedItem().toString();
//                        fetchDistrCodeBranchName(strServiceBranchCode);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            fetchSourcedata();
            fetchSubSourcedata();
            fetchProductata();

            //..................Button.....
            btn_create = (Button) findViewById(R.id.btn1_create_lead);
            btn_cancel = (Button) findViewById(R.id.btn1_cancel_lead);
            btn_create_close = (Button) findViewById(R.id.btn1_create_close_lead);
            btn_create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    validateDetails();
                }
            });
        }catch (Exception e){
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

    private void fetchProductata() {
        try {

            spinProductList = new ArrayList<>();
            String Product = "Product";

            String where = " where type like " + "'" + Product + "'";
            Cursor cursor2 = Narnolia.dbCon.fetchFromSelect(DbHelper.TABLE_M_PARAMETER, where);
            if (cursor2 != null && cursor2.getCount() > 0) {
                cursor2.moveToFirst();
                do {
                    String productvalue = "";
                    productvalue = cursor2.getString(cursor2.getColumnIndex("value"));
                    spinProductList.add(productvalue);
                } while (cursor2.moveToNext());
                cursor2.close();
            }
            if (spinProductList.size() > 0) {
                strProductArray = new String[spinProductList.size() + 1];
                strProductArray[0] = "Select option";
                for (int i = 0; i < spinProductList.size(); i++) {
                    strProductArray[i + 1] = spinProductList.get(i);
                }
            }

            if (spinProductList != null && spinProductList.size() > 0) {
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, strProductArray);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner_prospective_product.setAdapter(adapter1);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void validateDetails(){
        try {
            String str_mob,str_city,str_pincode;
            mobileno.setError(null);
            city.setError(null);
            pincode.setError(null);
            str_mob=mobileno.getText().toString().trim();
            str_city=city.getText().toString().trim();
            str_pincode=pincode.getText().toString().trim();
            View focusView = null;
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
