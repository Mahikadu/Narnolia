package com.narnolia.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.narnolia.app.dbconfig.DbHelper;
import com.narnolia.app.libs.Utils;
import com.narnolia.app.model.CategoryDetailsModel;
import com.narnolia.app.model.ProductDetailsModel;
import com.narnolia.app.network.SOAPWebService;

import org.ksoap2.serialization.SoapPrimitive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by USER on 10/24/2016.
 */

public class LeadActivity extends AbstractActivity {

    private Context mContext;
    protected Utils utils;
    private ProgressDialog progressDialog;
    EditText fname, mname, lname, mobileno, location;
    private AutoCompleteTextView editCity, autoPincode;
    private Map<String, List<String>> spinPincodeArray;
    private List<String> spinCityArray;
    TextView tv_source_of_lead, tv_sub_source_of_lead, tv_fname, tv_mname, tv_lname,
            tv_mobile_no, tv_location, tv_city, tv_pincode, tv_prospective_products, tv_prospective_products1;

    private String strStages, strSourceofLead, strSubSourceofLead, strCustomerID, str_fname, str_mname, str_lname,
            strDob, strAge, str_mob, strAddr1, strAddr2, strAddr3, strlocation, str_city, str_pincode, strEmail, strIncome,
            strOccupation, strCreatedfrom, strAppVersion, strAppdt, strFlag, strAllocated_userid, strBrokerDelts,
            strMeetingStatus, strLeadStatus, strCompitator_Name, strProduct, strRemark, strTypeofSearch,
            strDuration, strPanNo, strB_Margin, strB_aum, strB_sip, strB_number, strB_value, strB_premium, strReason,
            strMeetingdt, strMeetingAgenda, strLead_Updatelog, strCreatedby, strCreateddt, strUpdateddt, strUpdatedby,
            strBusiness_opp;
    private ArrayAdapter<String> adapter9;
    Spinner spinner_source_of_lead, spinner_sub_source;
    Button btn_create, btn_cancel, btn_create_close;
    private List<String> spinSourceLeadList;
    private List<String> spinSubSourceLeadList;
    private List<String> spinProductList;
    // private List<ProductDetailsModel> spinProductList;
    private List<CategoryDetailsModel> categoryDetailsModelList;
    String[] strLeadArray = null;
    String[] strSubLeadArray = null;
    String[] strProductArray = null;
    private String responseId;
    String LeadId;

    private String empcode;
    private SharedPref sharedPref;
    private TextView admin;

    private LinearLayout productLayout,customer_id;
    private  CheckBox checkbox;
    public ProductDetailsModel productDetailsModel;
    private CategoryDetailsModel categoryDetailsModel;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead);

        sharedPref = new SharedPref(LeadActivity.this);
        empcode = sharedPref.getLoginId();

        spinPincodeArray = new HashMap<>();
        spinCityArray = new ArrayList<>();

        initView();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initView() {

        try {
            mContext = LeadActivity.this;
            utils = new Utils(mContext);
            progressDialog = new ProgressDialog(mContext);

            admin = (TextView) findViewById(R.id.admin);
            admin.setText(empcode);

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            setHeader();


            //............Edit Text......
            fname = (EditText) findViewById(R.id.edt1_fname);
            mname = (EditText) findViewById(R.id.edt1_mname);
            lname = (EditText) findViewById(R.id.edt1_lname);
            mobileno = (EditText) findViewById(R.id.edt1_mobile_no);
            location = (EditText) findViewById(R.id.edt1_location);

            //................Auto Complete Text View......
            editCity=(AutoCompleteTextView)findViewById(R.id.edt1_city);
            autoPincode=(AutoCompleteTextView) findViewById(R.id.edt1_pincode);
            //..............linear layout...........
            customer_id=(LinearLayout)findViewById(R.id.linear1_cust_id);

            //..............Text View.....
            tv_source_of_lead = (TextView) findViewById(R.id.txt1_source_of_lead);
            tv_source_of_lead.setText(Html.fromHtml("<font color=\"red\">*</font>" + "<font color=\"black\">Source of Lead</font>\n"));
            tv_sub_source_of_lead = (TextView) findViewById(R.id.txt1_sub_source);
            tv_sub_source_of_lead.setText(Html.fromHtml("<font color=\"red\">*</font>" + "<font color=\"black\">Sub Source</font>\n"));
            tv_fname = (TextView) findViewById(R.id.txt1_fname);
            tv_fname.setText(Html.fromHtml("<font color=\"red\">*</font>" + "<font color=\"black\">First Name</font>\n"));
            tv_mname = (TextView) findViewById(R.id.txt1_mname);
            tv_lname = (TextView) findViewById(R.id.txt1_lname);
            tv_lname.setText(Html.fromHtml("<font color=\"red\">*</font>" + "<font color=\"black\">Last Name</font>\n"));
            tv_mobile_no = (TextView) findViewById(R.id.txt1_mobile_no);
            tv_mobile_no.setText(Html.fromHtml("<font color=\"red\">*</font>" + "<font color=\"black\">Mobile Number</font>\n"));
            tv_location = (TextView) findViewById(R.id.txt1_location);
            tv_city = (TextView) findViewById(R.id.txt1_city);
            tv_city.setText(Html.fromHtml("<font color=\"red\">*</font>" + "<font color=\"black\">City</font>\n"));
            tv_pincode = (TextView) findViewById(R.id.txt1_pincode);
            tv_pincode.setText(Html.fromHtml("<font color=\"red\">*</font>" + "<font color=\"black\">Pincode</font>\n"));
            tv_prospective_products = (TextView) findViewById(R.id.txt1_prospective_product);
            tv_prospective_products1 = (TextView) findViewById(R.id.txt1_prospective_product1);

            tv_prospective_products1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProductDialog();
                }
            });

            new LoadCityData().execute();//........................................Load city data
            //...............Spinner...
            spinner_source_of_lead = (Spinner) findViewById(R.id.spin1_source_of_lead);
            spinner_sub_source = (Spinner) findViewById(R.id.spin1_sub_source);
            // spinner_prospective_product = (Spinner) findViewById(R.id.spin1_prospective_product);




            //spinner OnItemSelectedListener
            spinner_source_of_lead.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (strLeadArray != null && strLeadArray.length > 0) {
                        strSourceofLead = spinner_source_of_lead.getSelectedItem().toString();
//                        fetchDistrCodeBranchName(strServiceBranchCode);
                        String occuString;
                        occuString = parent.getItemAtPosition(position).toString();
                        if (occuString != null) {
                            if (occuString.equalsIgnoreCase("Client Reference")) {

                                customer_id.setVisibility(View.VISIBLE);

                            } else {
                                customer_id.setVisibility(View.GONE);
                            }
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spinner_sub_source.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (strSubLeadArray != null && strSubLeadArray.length > 0) {
                        strSubSourceofLead = spinner_sub_source.getSelectedItem().toString();
//                        fetchDistrCodeBranchName(strServiceBranchCode);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            //......................madnetary Edit text validation.
            fname.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (fname.length() > 0) {
                        fname.setError(null);
                    }
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
                    if (lname.length() > 0) {
                        lname.setError(null);
                    }
                }
            });
            editCity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editCity.length() > 0) {
                        editCity.setError(null);
                    }
                }
            });
            //..........................................City Pincode
            editCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    try {
                        if (spinPincodeArray.containsKey(parent.getItemAtPosition(position))) {

                            autoPincode.setText("");
                            String city1 = (String) parent.getItemAtPosition(position);

                            editCity.setError(null);


                            String selection = mContext.getString(R.string.column_m_pin_district) + " = ?";

                            // WHERE clause arguments
                            String[] selectionArgs = {(String) parent.getItemAtPosition(position)};
                            String pincodeColNames[] = {getString(R.string.column_m_pin_pincode)};
                            Cursor cursor = Narnolia.dbCon.fetch(DbHelper.TABLE_M_PINCODE_TABLE, pincodeColNames, selection, selectionArgs, getString(R.string.column_m_pin_pincode), null, true, null, null);
                            String pincode, city;
                            List<String> pincodeList;
                            if (cursor != null && cursor.getCount() > 0) {
                                cursor.moveToFirst();
                                do {
                                    pincode = cursor.getString(cursor.getColumnIndex(getString(R.string.column_m_pin_pincode)));
                                    //
                                    pincodeList = new ArrayList<>();
                                    if (spinPincodeArray.containsKey(city1)) {
                                        pincodeList.addAll(spinPincodeArray.get(city1));
                                        pincodeList.add(pincode);
                                    } else {
                                        pincodeList.add(pincode);
                                        //
                                    }
                                    if (pincodeList.size() > 0) {
                                        spinPincodeArray.put(city1, pincodeList);
                                    }

                                } while (cursor.moveToNext());
                                cursor.close();
                                if (pincodeList.size() > 0) {
                                    final String[] strPinArr = new String[pincodeList.size()];
                                    strPinArr[0] = getString(R.string.select_pincode);
                                    pincodeList.remove(0);

                                    for (int i = 0; i < pincodeList.size(); i++) {
                                        strPinArr[i + 1] = pincodeList.get(i);
                                    }

                                    adapter9 = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, strPinArr);
                                    adapter9.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    autoPincode.setAdapter(adapter9);
                                    pincodeList.clear();

                                    autoPincode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                        @Override
                                        public void onFocusChange(View view, boolean hasFocus) {
                                            if (!hasFocus) {
                                                String val = autoPincode.getText() + "";

                                                if (Arrays.asList(strPinArr).contains(val)) {
                                                    System.out.println("CITY CITY CITY");
                                                } else {
                                                    autoPincode.setError("Invalid Pincode");
                                                }
                                            }
                                        }
                                    });

                                } else {
                                    autoPincode.setError(null);
                                    String[] strPinArr = new String[pincodeList.size()];
                                    strPinArr[0] = getString(R.string.select_pincode);
                                    adapter9 = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, strPinArr);
                                    adapter9.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    autoPincode.setAdapter(adapter9);
                                    pincodeList.clear();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            autoPincode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        autoPincode.setError(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            editCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    try {
                        if (!hasFocus) {
                            String val = editCity.getText() + "";

                            if (spinPincodeArray.containsKey(val)) {
                                System.out.println("CITY CITY CITY");
                            } else {
                                editCity.setError("Invalid City");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });







            fetchSourcedata();
            fetchSubSourcedata();
            // fetchProductata();

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

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetFields();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchSourcedata() {
        try {

            spinSourceLeadList = new ArrayList<>();
            String SourceLead = "SourceLead";

            String where = " where type like " + "'" + SourceLead + "'";
            Cursor cursor1 = Narnolia.dbCon.fetchFromSelect(DbHelper.TABLE_M_PARAMETER, where);
            if (cursor1 != null && cursor1.getCount() > 0) {
                cursor1.moveToFirst();
                do {
                    String branch = "";
                    branch = cursor1.getString(cursor1.getColumnIndex("value"));
                    spinSourceLeadList.add(branch);
                } while (cursor1.moveToNext());
                cursor1.close();
            }
            Collections.sort(spinSourceLeadList);
            if (spinSourceLeadList.size() > 0) {
                strLeadArray = new String[spinSourceLeadList.size() + 1];
                strLeadArray[0] = "Select Source Lead";
                for (int i = 0; i < spinSourceLeadList.size(); i++) {
                    strLeadArray[i + 1] = spinSourceLeadList.get(i);
                }
            }
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
            Collections.sort(spinSubSourceLeadList);
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


    private void validateDetails() {
        try {
            mobileno.setError(null);
            editCity.setError(null);
            autoPincode.setError(null);
            fname.setError(null);
            mname.setError(null);
            lname.setError(null);

            str_fname = fname.getText().toString().trim();
            str_mname = mname.getText().toString().trim();
            str_lname = lname.getText().toString().trim();
            str_mob = mobileno.getText().toString().trim();
            str_city = editCity.getText().toString().trim();
            str_pincode = autoPincode.getText().toString().trim();
            strlocation = location.getText().toString().trim();

            View focusView = null;
            if (TextUtils.isEmpty(str_fname)) {
                fname.setError(getString(R.string.name));
                focusView = fname;
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
            if (TextUtils.isEmpty(str_city)) {
                editCity.setError(getString(R.string.reqcity));
                focusView = editCity;
                focusView.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(str_pincode)) {

                autoPincode.setError(getString(R.string.reqpincode));
                focusView = autoPincode;
                focusView.requestFocus();
                return;
            }
            if (spinner_source_of_lead.getCount() == 0) {
                focusView = spinner_source_of_lead;
                focusView.requestFocus();
                return;
            }
            if (isConnectingToInternet()) {

                new InsertLeadData().execute();

            } else {
                updateOrInsertInDb();
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

    private void showProductDialog() {

        // spinSourceLeadList = new ArrayList<>();
        final AlertDialog.Builder DialogProduct = new AlertDialog.Builder(this);

        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogViewproduct = li.inflate(R.layout.product_custom_dialog, null);
        DialogProduct.setView(dialogViewproduct);

        final AlertDialog showProduct = DialogProduct.show();

        productLayout = (LinearLayout) showProduct.findViewById(R.id.productLinearLayout);


        fillUI();

    }

    void fillUI() {
        try {
            productLayout.removeAllViews();

            spinProductList = new ArrayList<>();

            categoryDetailsModelList = new ArrayList<>();

            String Product = "Product   ";

            String where = " where type like " + "'" + Product + "'";
            Cursor cursor3 = Narnolia.dbCon.fetchFromSelect(DbHelper.TABLE_M_PARAMETER, where);
            Set<ProductDetailsModel> hs = new HashSet<>();

            if (cursor3 != null && cursor3.getCount() > 0) {
                cursor3.moveToFirst();
                do {
                    String productvalue = "";
                    //  productDetailsModel = new ProductDetailsModel();
                    productvalue = cursor3.getString(cursor3.getColumnIndex("value"));
                    // productDetailsModel.setProdName(productvalue);
                    //spinProductList.add(productDetailsModel);
                    spinProductList.add(productvalue);
                } while (cursor3.moveToNext());
                cursor3.close();
            }

          /*  hs.addAll(spinProductList);
            spinProductList.clear();
            spinProductList.addAll(hs);*/

            Cursor cursor = Narnolia.dbCon.fetchAll(DbHelper.TABLE_M_CATEGORY);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    String category = "";
                    categoryDetailsModel = new CategoryDetailsModel();
                    category = cursor.getString(cursor.getColumnIndex("Category"));
                    categoryDetailsModel.setCategory(category);
                    categoryDetailsModelList.add(categoryDetailsModel);
//                    spinProductList.add(productvalue);
                } while (cursor.moveToNext());
                cursor.close();
            }


            for (int count = 0; count < spinProductList.size(); count++) {
                String productName = spinProductList.get(count);

                LinearLayout productInfoLinearLayout = new LinearLayout(mContext);
                productInfoLinearLayout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams merchantInfoLinearLayout_LayoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                merchantInfoLinearLayout_LayoutParams.setMargins(5, 5, 5, 5);
                productInfoLinearLayout
                        .setLayoutParams(merchantInfoLinearLayout_LayoutParams);

                // Merchant name textview
                TextView producttxt = new TextView(mContext);
                producttxt.setTypeface(null, Typeface.BOLD);
                producttxt.setText(productName);
                producttxt.setTextSize(20);
                producttxt.setGravity(Gravity.CENTER_HORIZONTAL);
                LinearLayout.LayoutParams merchantnameTextView_LayoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                merchantnameTextView_LayoutParams.setMargins(5, 0, 0, 5);
                producttxt
                        .setLayoutParams(merchantnameTextView_LayoutParams);

                // line view layout
                LinearLayout lineviewlayout = new LinearLayout(mContext);
                LinearLayout.LayoutParams lineview = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 2);
                TextView line = new TextView(mContext);
                line.setLayoutParams(lineview);
                lineview.setMargins(3, 0, 3, 0);
                line.setBackgroundColor(Color.GRAY);
                lineviewlayout.addView(line);

               /* // merchant description text view
                TextView categorytxt = new TextView(mContext);
                categorytxt.setTypeface(null, Typeface.BOLD);
                categorytxt.setText("category");
                LinearLayout.LayoutParams merchantdescriptionTextView_LayoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                merchantdescriptionTextView_LayoutParams.setMargins(5, 0, 0, 5);
                categorytxt.setLayoutParams(merchantdescriptionTextView_LayoutParams);*/

                productInfoLinearLayout.addView(producttxt);
                productInfoLinearLayout.addView(lineviewlayout);

                LinearLayout CategoryLinearLayout = null;
                for (int i = 0; i < categoryDetailsModelList.size(); i++) {
                    String category = categoryDetailsModelList.get(i).getCategory();
                    CategoryLinearLayout = new LinearLayout(mContext);
                    CategoryLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams CategoryLinearLayout_LayoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    CategoryLinearLayout
                            .setLayoutParams(CategoryLinearLayout_LayoutParams);

                    checkbox = new CheckBox(mContext);
                    LinearLayout.LayoutParams childParam2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    childParam2.weight = 0.9f;
                    checkbox.setLayoutParams(childParam2);

                    TextView categorytxt = new TextView(mContext);
                    categorytxt.setTypeface(null, Typeface.BOLD);
                    categorytxt.setText(category);
                    LinearLayout.LayoutParams childParam1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    childParam1.weight = 0.1f;
                    categorytxt.setTextSize(18);
                    categorytxt.setGravity(Gravity.LEFT);
                    categorytxt.setLayoutParams(childParam1);


                    CategoryLinearLayout.addView(checkbox);
                    CategoryLinearLayout.addView(categorytxt);

                }


                productInfoLinearLayout.addView(CategoryLinearLayout);

                productLayout.addView(productInfoLinearLayout);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Lead Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public class InsertLeadData extends AsyncTask<String, Void, SoapPrimitive> {

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
        protected SoapPrimitive doInBackground(String... params) {

            PackageManager manager = mContext.getPackageManager();
            String versionName = "";
            try {
                PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
                String packageName = info.packageName;
                int versionCode = info.versionCode;
                versionName = info.versionName;
              /*  status = params[0];
                stages = params[1];
                stagesTobeSend = params[1];
                if (isUpdate && leadInfoModel != null && LeadId.contains(String.valueOf(leadInfoModel.getDirect_lead_id()))) {
                    stagesTobeSend = mContext.getString(R.string.text_lead_created);
                } else {

                }*/
            } catch (Exception e) {
                // TODO Auto-generated catch block
            }
            // strOccupation  = 1 and strDesignation = 1  also currency = "" these are hardcoded values we need to change when we get master data
            SOAPWebService webService = new SOAPWebService(mContext);
            String stages = "Lead Created";
            String flag = "C";

            SoapPrimitive object = webService.SaveLead(strSourceofLead, strSubSourceofLead, "", str_fname, str_mname, str_lname, str_mob,
                    strlocation, str_city, str_pincode, "1",stages, "", "", "", flag, "");

            return object;

        }

        @Override
        protected void onPostExecute(SoapPrimitive soapObject) {
            super.onPostExecute(soapObject);
            try {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                responseId = String.valueOf(soapObject);
                if (responseId.contains("ERROR") || responseId.contains("null")) {
                    Toast.makeText(mContext, "Please check Internet Connection", Toast.LENGTH_LONG).show();
                } else {
                    displayMessage("Lead Created Successfully");
                    updateOrInsertInDb();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void updateOrInsertInDb() {

        try {
            int directLeadId = 0;
            LeadId = "";
            String strLastSync = "0";
            String strStages = "Lead Created";
            String strFlag = "C";

            if (isConnectingToInternet()) {
                if (responseId.equalsIgnoreCase("TRUE")) {
                    //  LeadId = leadInfoModel.getLead_id();
                } else {
                    LeadId = responseId;
                }
                // need to change once web service implementation is done
                directLeadId = (Narnolia.dbCon.getCountOfRows(DbHelper.TABLE_DIRECT_LEAD) + 1);


            } /*else {
                    LeadId = "LL";
                    directLeadId = (Narnolia.dbCon.getCountOfRows(DbHelper.TABLE_DIRECT_LEAD) + 1);
                    LeadId = LeadId.concat("" + directLeadId);
                }*/

            // WHERE   clause
            String selection = "lead_id" + " = ?";

            // WHERE clause arguments
            String[] selectionArgs = {LeadId};

            // for now strcurrency becomes blank as " "; please change it later
            String valuesArray[] = { "" + directLeadId,LeadId,strStages, strSourceofLead, strSubSourceofLead, strCustomerID, str_fname, str_mname, str_lname,
                    strDob, strAge, str_mob, strAddr1, strAddr2, strAddr3, strlocation, str_city, str_pincode, strEmail, strIncome,
                    strOccupation, strCreatedfrom, strAppVersion, strAppdt, strFlag, strAllocated_userid, strBrokerDelts,
                    strMeetingStatus, strLeadStatus, strCompitator_Name, strProduct, strRemark, strTypeofSearch,
                    strDuration, strPanNo, strB_Margin, strB_aum, strB_sip, strB_number, strB_value, strB_premium, strReason,
                    strMeetingdt, strMeetingAgenda, strLead_Updatelog, strCreatedby, strCreateddt, strUpdateddt, strUpdatedby,
                    strBusiness_opp};


            boolean result = Narnolia.dbCon.update(DbHelper.TABLE_DIRECT_LEAD, selection, valuesArray, utils.columnNamesLead, selectionArgs);


            if (result) {

                displayMessage("Insert Data Succesfully");

                pushActivity(mContext, HomeActivity.class, null, true);
            }

        } catch (
                Exception e
                )

        {
            e.printStackTrace();
        }
    }
    private void fetchDataFromDB() {
        try {
            String pincodeColumnNames[] = {getString(R.string.column_m_pin_pincode), getString(R.string.column_m_pin_district)};

            Cursor cursor = Narnolia.dbCon.fetch(DbHelper.TABLE_M_PINCODE_TABLE, pincodeColumnNames, null, null, getString(R.string.column_m_pin_district), null, false, getString(R.string.column_m_pin_district), null);

            String pincode, city;

            try {
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {
                        pincode = cursor.getString(cursor.getColumnIndex(getString(R.string.column_m_pin_pincode)));
                        city = cursor.getString(cursor.getColumnIndex(getString(R.string.column_m_pin_district)));
                        List<String> pincodeList = new ArrayList<>();
                        if (spinPincodeArray.containsKey(city)) {


                            pincodeList.addAll(spinPincodeArray.get(city));
                            pincodeList.add(pincode);
                        } else {
                            pincodeList.add(pincode);
                            spinCityArray.add(city);

                        }
                        if (pincodeList.size() > 0) {
                            spinPincodeArray.put(city, pincodeList);
                        }

                    } while (cursor.moveToNext());
                    cursor.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void resetFields(){
        try{
            fname.setText("");
            mname.setText("");
            lname.setText("");
            mobileno.setText("");
            location.setText("");
            editCity.setText("");
            autoPincode.setText("");
            spinner_source_of_lead.setSelection(0);
            spinner_sub_source.setSelection(0);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public class LoadCityData extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (progressDialog != null && !progressDialog.isShowing()) {
                progressDialog.show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            fetchDataFromDB();


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {

// Create the adapter and set it to the AutoCompleteTextView
                if (spinCityArray != null && spinCityArray.size() > 0) {
                    ArrayAdapter<String> adapterPin =
                            new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, spinCityArray);
                    editCity.setAdapter(adapterPin);
                }


                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }



        }


    }


}