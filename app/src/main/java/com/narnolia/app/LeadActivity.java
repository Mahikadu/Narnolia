package com.narnolia.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.narnolia.app.model.ClientDetailsModel;
import com.narnolia.app.model.LeadInfoModel;
import com.narnolia.app.model.ProductDetailsModel;
import com.narnolia.app.model.SubCategoryDetailsModel;
import com.narnolia.app.network.SOAPWebService;

import org.ksoap2.serialization.SoapPrimitive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by USER on 10/24/2016.
 */


public class LeadActivity extends AbstractActivity implements CompoundButton.OnCheckedChangeListener {

    private Context mContext;
    protected Utils utils;
    public ProgressDialog progressDialog;
    EditText fname, mname, lname, mobileno, location,customer_id;
    private AutoCompleteTextView editCity, autoPincode;
    private Map<String, List<String>> spinPincodeArray;
    private List<String> pincodeList;
    String selectedCatId = "";
    private List<String> selCatId;
    boolean uncheckall = false;
    String sourceString,subsource;
    String ClinetId;
    private List<String> spinCityArray;
    private List<String> spinClientIDList;
    String[] strClientIDArray;
    List<ClientDetailsModel>clientInfoModelList;
    ClientDetailsModel clientDetailsModel;
    TextView tv_source_of_lead, tv_sub_source_of_lead, tv_fname, tv_mname, tv_lname,
            tv_mobile_no, tv_location, tv_city, tv_pincode, tv_prospective_products,txt1_cust_id,txt2_cust_id;

    private String strStages, strSourceofLead, strSubSourceofLead, strCustomerID, str_fname, str_mname, str_lname,
            strDob, strAge, str_mob, strAddr1, strAddr2, strAddr3, strlocation, str_city, str_pincode, strEmail, strIncome,
            strOccupation, strCreatedfrom, strAppVersion, strAppdt, strFlag, strAllocated_userid, strBrokerDelts,
            strMeetingStatus, strLeadStatus, strCompitator_Name, strProduct, strRemark, strTypeofSearch,
            strDuration, strPanNo, strB_Margin, strB_aum, strB_sip, strB_number, strB_value, strB_premium, strReason,
            strMeetingdt, strMeetingAgenda, strLead_Updatelog, strCreatedby, strCreateddt, strUpdateddt, strUpdatedby,strEmpCode,strLastMeetingDate,StrLastMeetingUpdate,
            strBusiness_opp,strCustomer_id_name,str_duration;
    private ArrayAdapter<String> adapter9;
    Spinner spinner_source_of_lead, spinner_sub_source,spinner_cust_id;
    Button btn_create, btn_cancel, btn_create_close,btn1_opportunity_pitched2;
    private List<String> spinSourceLeadList;
    private List<String> spinSubSourceLeadList;
    //private List<String> spinProductList;
    private List<ProductDetailsModel> spinProductList;
    private List<CategoryDetailsModel> categoryDetailsModelList;
    private List<SubCategoryDetailsModel> subCategoryDetailsModelList;
    private List<String> selItemsPosition;
    String[] strLeadArray = null;
    String[] strSubLeadArray = null;
    String[] strProductArray = null;
    public String responseId ="";
    String LeadId;

    private String empcode;
    private SharedPref sharedPref;
    private TextView admin;
    String str_CheckedItems;

    AlertDialog.Builder DialogProduct;
    AlertDialog showProduct;
    int icount =0;
    boolean cliked=false;
    boolean create=false;


    int id;

    private boolean flagSelected = false;
    private int selCounter = 0;
    private int checkCounter = 0;
    public boolean[] selChkBoxArr = new boolean[50];

    private LinearLayout productLayout,linear_customer_id,linear_spin_customer_id;
    private  CheckBox checkbox;
    public ProductDetailsModel productDetailsModel;
    private CategoryDetailsModel categoryDetailsModel;
    private SubCategoryDetailsModel subCategoryDetailsModel;

    LinearLayout CategoryLinearLayout = null;
    LinearLayout productInfoLinearLayout = null;
    ArrayList<Integer> checkedPositions = new ArrayList<Integer>();
    boolean flag = false;


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
        selItemsPosition = new ArrayList<>();
        selCatId = new ArrayList<>();
        pincodeList = new ArrayList<>();

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
            customer_id=(EditText)findViewById(R.id.edt1_cust_id);

            //................Auto Complete Text View......
            editCity=(AutoCompleteTextView)findViewById(R.id.edt1_city);
            autoPincode=(AutoCompleteTextView) findViewById(R.id.edt1_pincode);
            //..............linear layout...........
            linear_customer_id=(LinearLayout)findViewById(R.id.linear1_cust_id);
            linear_spin_customer_id=(LinearLayout)findViewById(R.id.linear_spin_cust_id);

            //..............Text View.....
            tv_source_of_lead = (TextView) findViewById(R.id.txt1_source_of_lead);
            tv_source_of_lead.setText(Html.fromHtml("<font color=\"red\">*</font>" + "<font color=\"black\">Source of Lead</font>\n"));
            tv_sub_source_of_lead = (TextView) findViewById(R.id.txt1_sub_source);
            tv_sub_source_of_lead.setText(Html.fromHtml("<font color=\"red\">*</font>" + "<font color=\"black\">Sub Source</font>\n"));
            txt1_cust_id=(TextView)findViewById(R.id.txt1_cust_id);
            txt1_cust_id.setText(Html.fromHtml("<font color=\"red\">*</font>" + "<font color=\"black\">Customer ID</font>\n"));
            txt2_cust_id=(TextView)findViewById(R.id.txt2_cust_id);
            txt2_cust_id.setText(Html.fromHtml("<font color=\"red\">*</font>" + "<font color=\"black\">Customer ID</font>\n"));
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
/*            tv_prospective_products1 = (TextView) findViewById(R.id.txt1_prospective_product1);

            tv_prospective_products1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    icount = 0;
                    showProductDialog();
                }
            });*/

            new LoadCityData().execute();//........................................Load city data
            //...............Spinner...
            spinner_source_of_lead = (Spinner) findViewById(R.id.spin1_source_of_lead);
            spinner_sub_source = (Spinner) findViewById(R.id.spin1_sub_source);
            spinner_cust_id=(Spinner)findViewById(R.id.spin1_cust_id);
            // spinner_prospective_product = (Spinner) findViewById(R.id.spin1_prospective_product);




            //spinner OnItemSelectedListener
            spinner_source_of_lead.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (strLeadArray != null && strLeadArray.length > 0) {
                        strSourceofLead = spinner_source_of_lead.getSelectedItem().toString();
//                        fetchDistrCodeBranchName(strServiceBranchCode);

                        sourceString = parent.getItemAtPosition(position).toString();
                        if (sourceString != null && sourceString.length() > 0 &&subsource!=null) {
                            try {
                                if (sourceString.equalsIgnoreCase("In- house Leads (Existing)")&& subsource.equals("Existing Client")){
                                    getAllClientData();//.............Client data method
                                    linear_customer_id.setVisibility(View.GONE);
                                    linear_spin_customer_id.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    fname.setEnabled(true);
                                    fname.setText("");
                                    mname.setEnabled(true);
                                    mname.setText("");
                                    lname.setEnabled(true);
                                    lname.setText("");
                                    editCity.setEnabled(true);
                                    editCity.setText("");
                                    autoPincode.setEnabled(true);
                                    autoPincode.setText("");
                                    mobileno.setEnabled(true);
                                    mobileno.setText("");
                                    linear_spin_customer_id.setVisibility(View.GONE);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (sourceString.equalsIgnoreCase("Client Reference")) {
                                linear_customer_id.setVisibility(View.VISIBLE);
                            }else if (sourceString.equalsIgnoreCase("In- house Leads (Existing)")&&!subsource.equals("Existing Client"))
                            {
                                linear_customer_id.setVisibility(View.VISIBLE);

                            }else if (sourceString.equalsIgnoreCase("In- house Leads (New)"))
                            {
                                linear_customer_id.setVisibility(View.VISIBLE);
                            }else {
                                linear_customer_id.setVisibility(View.GONE);
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
                        subsource=parent.getItemAtPosition(position).toString();
                        if (subsource!=null && subsource.length() > 0&&sourceString!=null&&sourceString.length()>0){
                            if (subsource.equalsIgnoreCase("Existing Client")&& sourceString.equals("In- house Leads (Existing)")){
                                getAllClientData();//.............Client data method
                                linear_customer_id.setVisibility(View.GONE);
                                linear_spin_customer_id.setVisibility(View.VISIBLE);
                            }
                            else
                            {    fname.setEnabled(true);
                                fname.setText("");
                                mname.setEnabled(true);
                                mname.setText("");
                                lname.setEnabled(true);
                                lname.setText("");
                                editCity.setEnabled(true);
                                editCity.setText("");
                                autoPincode.setEnabled(true);
                                autoPincode.setText("");
                                mobileno.setEnabled(true);
                                mobileno.setText("");
                                linear_spin_customer_id.setVisibility(View.GONE);
                            }
                        }

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
//...............................Spinner Client Id..........
            //spinner_cust_id
            spinner_cust_id.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                    if (position != 0){

                        if ( strClientIDArray != null && strClientIDArray.length > 0){
                            strCustomer_id_name = spinner_cust_id.getSelectedItem().toString();
                            String clientData = spinner_cust_id.getSelectedItem().toString();
                            String [] strClient = clientData.split(" ");
                            ClinetId = strClient[0];

                            try {
                                // WHERE clause
                                String where = " where ClientID = '"+ClinetId+"'";
                                Cursor cursor = Narnolia.dbCon.fetchFromSelect(DbHelper.TABLE_CLIENT_DETAILS,where);
                                if (cursor != null && cursor.getCount() > 0) {
                                    cursor.moveToFirst();
                                    do {
                                        clientDetailsModel = createClientDetailsModel(cursor);
//                                        leadInfoModelList.add(leadInfoModel);

                                    } while (cursor.moveToNext());
                                    cursor.close();

                                }else {
                                    Toast.makeText(mContext, "No data found..!",Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            try {
                                populateClientData(clientDetailsModel);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
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
                    if (charSequence.length()==0){
                    }
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
            //................mobile no validation........
            mobileno.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (mobileno.getText().toString().length()>0){
                        mobileno.setError(null);
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

            btn1_opportunity_pitched2 = (Button) findViewById(R.id.btn1_prospective_product1);

            btn1_opportunity_pitched2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    icount = 0;

                    //Toast.makeText(mContext, "Selected chkbox COUNT value is:" +icount, Toast.LENGTH_SHORT).show();

                    showProductDialog();

                }
            });

            btn_create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    create=true;
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    validateDetails();

                    // pushActivity(mContext, HomeActivity.class, null, true);
                }
            });

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fname.setText("");
                    mname.setText("");
                    lname.setText("");
                    mobileno.setText("");
                    location.setText("");
                    editCity.setText("");
                    autoPincode.setText("");
                    spinner_source_of_lead.setSelection(0);
                    spinner_sub_source.setSelection(0);
                    spinner_cust_id.setSelection(0);
                    customer_id.setText("");
                    if (v != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    btn1_opportunity_pitched2.setText(getString(R.string.prospectiveproduct1));
                    uncheck();
                }
            });
            btn_create_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cliked=true;
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    validateDetails();
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
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strLeadArray) {
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
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strSubLeadArray) {
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

                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_source.setAdapter(adapter1);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*  if(leadInfoModelList.size()>0) {

          for(int i = 0; i<leadInfoModelList.size(); i++) {
              final LeadInfoModel leadInfoModel = leadInfoModelList.get(i);
              str_fname = leadInfoModel.getFirstname();
              str_mname = leadInfoModel.getMiddlename();
              str_lname = leadInfoModel.getLastname();
              lead_id_info = leadInfoModel.getLead_id();
              fullname = str_fname + " " + str_mname + " " + str_lname + " " + "(" + lead_id_info + ")";
              Collections.sort(spinLeadNameList);
              spinLeadNameList.add(fullname);
          }

          strLeadNameArray = new String[spinLeadNameList.size() + 1];
          strLeadNameArray[0] = "Select Lead Name";
          for (int i = 0; i < spinLeadNameList.size(); i++) {
              strLeadNameArray[i + 1] = spinLeadNameList.get(i);
          }
      }*/
    private void setClientDetailValue(){
        spinClientIDList = new ArrayList<>();
        try {
            if (clientInfoModelList.size()>0){
                for (int i=0;i<clientInfoModelList.size();i++){
                    final ClientDetailsModel clientDetailsModel=clientInfoModelList.get(i);
                    String custid=clientDetailsModel.getClientID();
                    String custname=clientDetailsModel.getClientName();
                    String cust_id_name=custid+" "+custname +" ";
                    Collections.sort(spinClientIDList);
                    spinClientIDList.add(cust_id_name);
                }
                strClientIDArray=new String[spinClientIDList.size()+1];
                strClientIDArray[0]="Select Client ID";
                for (int i=0;i<spinClientIDList.size();i++){
                    strClientIDArray[i+1]=spinClientIDList.get(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isPhoneValid(String phoneNumber) {
        if (phoneNumber == null) {
            return false;
        } else {
            if (phoneNumber.length() < 6 || phoneNumber.length() > 10) {
                return false;
            } else {
                return android.util.Patterns.PHONE.matcher(phoneNumber).matches();
            }
        }
    }
    public boolean isName(String name) {
        if (name == null) {
            return false;
        } else {
            if (name.length() < 1 ) {
                return false;
            } else {
                return true;
            }
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
            str_city = editCity.getText().toString();
            str_pincode = autoPincode.getText().toString().trim();
            strlocation = location.getText().toString().trim();
            strCustomerID=customer_id.getText().toString().trim();

            View focusView = null;
            if (spinner_source_of_lead.getSelectedItemPosition()==0){
                Toast.makeText(mContext, "Please select Source of Lead", Toast.LENGTH_SHORT).show();
                focusView=spinner_source_of_lead;
                focusView.requestFocus();
                spinner_source_of_lead.setFocusable(true);
                spinner_source_of_lead.requestFocusFromTouch();
                return;
            }
            if (spinner_sub_source.getSelectedItemPosition()==0){
                Toast.makeText(mContext, "Please select Sub Source of Lead", Toast.LENGTH_SHORT).show();
                focusView=spinner_sub_source;
                focusView.requestFocus();
                spinner_sub_source.setFocusable(true);
                spinner_sub_source.requestFocusFromTouch();
                return;
            }
            if (!isName(str_fname))
            {
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
            if (!isName(str_lname))
            {
                lname.setError(getString(R.string.name));
                focusView = fname;
                focusView.requestFocus();
                return;
            }
//            if (TextUtils.isEmpty(str_mob)) {
            if (!isPhoneValid(str_mob)) {
                mobileno.setError(getString(R.string.reqmob));
                focusView = mobileno;
                focusView.requestFocus();
                return;
            }

            String text0 =String.valueOf(mobileno.getText().toString().charAt(0));
            if (text0.equals("0")){
                mobileno.setError("Please enter correct mobile no");
                focusView=mobileno;
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
           /* String val = autoPincode.getText() + "";

            if (!Arrays.asList(pincodeList).contains(val)) {
                    autoPincode.setError("Invalid Pincode");
                    focusView = autoPincode;
                    focusView.requestFocus();
                    return;
                }*/
            if(!pincodeList.contains(autoPincode.getText().toString().trim())){
                autoPincode.setError("Invalid Pincode");
                focusView = autoPincode;
                focusView.requestFocus();
                return;
            }
            if (isConnectingToInternet()) {

                new InsertLeadData().execute();

            } else {
                updateOrInsertInDb();
            }

            if (create){
                pushActivity(mContext, HomeActivity.class, null, true);
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
    private void resetProduct(){
        icount = 0;
        try {
            if (productLayout.getChildCount() > 0) {
                for (int i = 0; i < productLayout.getChildCount(); i++) {

                    LinearLayout prodInfoLayout = (LinearLayout) productLayout.getChildAt(i);

                    if (prodInfoLayout.getChildCount() > 0) {
                        for (int j = 2; j < prodInfoLayout.getChildCount(); j++) {
                            LinearLayout catLayout = (LinearLayout) prodInfoLayout.getChildAt(j);
                            if (catLayout.getChildCount() > 0) {
                                for (int k = 0; k < catLayout.getChildCount(); k++) {
                                    View v = catLayout.getChildAt(k);

                                    CheckBox checkbox1 = (CheckBox) v;

                                    checkbox1.setChecked(false);
                                    selChkBoxArr[v.getId()] = false;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //.....................................................product data......................
    private void showProductDialog() {

        // spinSourceLeadList = new ArrayList<>();
        DialogProduct = new AlertDialog.Builder(this);

        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogViewproduct = li.inflate(R.layout.product_custom_dialog, null);
        DialogProduct.setView(dialogViewproduct);

        showProduct = DialogProduct.show();

        productLayout = (LinearLayout) showProduct.findViewById(R.id.productLinearLayout);

        final Button submit = (Button) showProduct.findViewById(R.id.btn1_submit);

        final TextView textcheck = (TextView) showProduct.findViewById(R.id.textcheck);
        final TextView textuncheck = (TextView) showProduct.findViewById(R.id.textuncheck);
        selChkBoxArr = new boolean[50];
//        if(!uncheckall) {
        // from DB
        if (selItemsPosition != null && selItemsPosition.size() > 0) {
            for (int i = 0; i < selItemsPosition.size(); i++) {
                selChkBoxArr[Integer.parseInt(selItemsPosition.get(i))] = true;
            }
            selItemsPosition.clear();
        }else {
            // from Local variable
            if (selectedCatId != null && selectedCatId.length() > 0) {
                if (selectedCatId.contains("#")) {
                    selCatId = Arrays.asList(selectedCatId.split("#"));

                } else {
                    selCatId.add(selectedCatId);
                }
                for (int j = 0; j < selCatId.size(); j++) {
                    selChkBoxArr[Integer.parseInt(selCatId.get(j))] = true;
                }
                Log.e("selChkBoxArr value =>",selectedCatId.toString());
            }
//        }else{
//            }


        }

        fillUI();

        categoryDetailsModelList = new ArrayList<>();

        Cursor cursor = null;
        cursor = Narnolia.dbCon.fetchAlldata(DbHelper.TABLE_M_CATEGORY);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String category = "";
                categoryDetailsModel = new CategoryDetailsModel();
                category = cursor.getString(cursor.getColumnIndex("Category"));
                categoryDetailsModel.setCategory(category);
                categoryDetailsModelList.add(categoryDetailsModel);
                //      spinProductList.add(category);

            } while (cursor.moveToNext());
        }
        cursor.close();

        textcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //    submit.setVisibility(View.VISIBLE);
                icount = 0;
                selectedCatId = "";
                selItemsPosition.clear();
                selectedCatId = "";
                try {

                    if (productLayout.getChildCount() > 0) {
                        for (int i = 0; i < productLayout.getChildCount(); i++) {

                            LinearLayout prodInfoLayout = (LinearLayout) productLayout.getChildAt(i);

                            if (prodInfoLayout.getChildCount() > 0) {
                                for (int j = 2; j < prodInfoLayout.getChildCount(); j++) {
                                    LinearLayout catLayout = (LinearLayout) prodInfoLayout.getChildAt(j);
                                    if (catLayout.getChildCount() > 0) {
                                        for (int k = 0; k < catLayout.getChildCount(); k++) {
                                            View v = catLayout.getChildAt(k);

                                            CheckBox checkbox1 = (CheckBox) v;

                                            checkbox1.setChecked(true);

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        textuncheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uncheck();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submitClick();
            }
        });

    }
    private void submitClick(){
        selectedCatId = "";
        icount = 0;
        if (productLayout.getChildCount() > 0) {
            for (int i = 0; i < productLayout.getChildCount(); i++) {
                LinearLayout prodInfoLayout = (LinearLayout) productLayout.getChildAt(i);
                if (prodInfoLayout.getChildCount() > 0) {
                    for (int j = 2; j < prodInfoLayout.getChildCount(); j++) {
                        LinearLayout catLayout = (LinearLayout) prodInfoLayout.getChildAt(j);
                        if (catLayout.getChildCount() > 0) {
                            for (int k = 0; k < catLayout.getChildCount(); k++) {
                                View v = catLayout.getChildAt(k);

                                CheckBox checkbox1 = (CheckBox) v;

                                if (checkbox1.isChecked()) {

                                    int selChkBoxId = checkbox1.getId();
                                    selectedCatId += selChkBoxId+"#";
                                    icount += 1;


                                }
                            }
                        }
                    }
                }
            }
        }
        selectedCatId = selectedCatId.length() > 0 ? selectedCatId.substring(0,selectedCatId.length() - 1) : "";
        Log.e("Selected Cate Cont = > ",""+icount);
        Log.e("Selected Cate Ids = > ","" +selectedCatId);

        if (icount > 0){
            String countStr = + icount + " is Selected";
            btn1_opportunity_pitched2.setText(countStr);
            showProduct.dismiss();
        }else{

            btn1_opportunity_pitched2.setText(getString(R.string.prospectiveproduct1));
            showProduct.dismiss();
        }
    }
    private void uncheck(){
        //        submit.setVisibility(View.INVISIBLE);
        uncheckall = true;
        icount = 0;
        selItemsPosition.clear();
        selectedCatId = "";
        try {

            if (productLayout.getChildCount() > 0) {
                for (int i = 0; i < productLayout.getChildCount(); i++) {

                    LinearLayout prodInfoLayout = (LinearLayout) productLayout.getChildAt(i);

                    if (prodInfoLayout.getChildCount() > 0) {
                        for (int j = 2; j < prodInfoLayout.getChildCount(); j++) {
                            LinearLayout catLayout = (LinearLayout) prodInfoLayout.getChildAt(j);
                            if (catLayout.getChildCount() > 0) {
                                for (int k = 0; k < catLayout.getChildCount(); k++) {
                                    View v = catLayout.getChildAt(k);

                                    CheckBox checkbox1 = (CheckBox) v;

                                    checkbox1.setChecked(false);
//                                            selChkBoxArr[v.getId()] = false;
                                }
                            }
                        }
                    }
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    void fillUI() {
        try {

            productLayout.removeAllViews();

            spinProductList = new ArrayList<>();

            categoryDetailsModelList = new ArrayList<>();

            subCategoryDetailsModelList = new ArrayList<>();

            String Product = "Product   ";
            String colName = "value,id";

            String where = " where type like " + "'" + Product + "'";

            Cursor cursor3=null;
            cursor3 = Narnolia.dbCon.fetchDistictFromSelect(colName,DbHelper.TABLE_M_PARAMETER, where);


            if (cursor3 != null && cursor3.getCount() > 0) {
                cursor3.moveToFirst();
                do {
                    String productName = "";
                    String productId = "";
                    productDetailsModel = new ProductDetailsModel();
                    productName = cursor3.getString(cursor3.getColumnIndex("value"));
                    productId = cursor3.getString(cursor3.getColumnIndex("id"));
                    productDetailsModel.setProdName(productName);
                    productDetailsModel.setProdId(productId);
                    spinProductList.add(productDetailsModel);
                } while (cursor3.moveToNext());

            }
            cursor3.close();


            int selCount = 0;
            String selTextview = "";

            for (int count = 0; count < spinProductList.size(); count++) {
                final String productName = spinProductList.get(count).getProdName();
                selCount = count;
                productInfoLinearLayout = new LinearLayout(mContext);
                productInfoLinearLayout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams merchantInfoLinearLayout_LayoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                merchantInfoLinearLayout_LayoutParams.setMargins(5, 5, 5, 5);
                productInfoLinearLayout.setLayoutParams(merchantInfoLinearLayout_LayoutParams);

                // Merchant name textview
                final TextView producttxt = new TextView(mContext);
                producttxt.setTypeface(null, Typeface.BOLD);
                producttxt.setText(productName);
                producttxt.setId(count);
                producttxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            selCounter++;
                            flagSelected = true;
                            if (selCounter % 2 == 1) {//odd
                                TextView textview = (TextView) view;
                                id = textview.getId();

                                for (int i = 0; i < productLayout.getChildCount(); i++) {

                                    LinearLayout prodInfoLayout = (LinearLayout) productLayout.getChildAt(i);

                                    if (prodInfoLayout.getChildCount() > 0) {

                                        View view1 = prodInfoLayout.getChildAt(0);
                                        TextView tvLayout = (TextView) view1;
                                        if (tvLayout.getText().toString().equalsIgnoreCase(textview.getText().toString())) {
                                            for (int j = 2; j < prodInfoLayout.getChildCount(); j++) {
                                                LinearLayout subLayout = (LinearLayout) prodInfoLayout.getChildAt(j);
                                                if (subLayout.getChildCount() > 0) {
                                                    for (int k = 0; k < subLayout.getChildCount(); k++) {
                                                        View v = subLayout.getChildAt(k);
                                                        final CheckBox checkbox1 = (CheckBox) v;

                                                        checkbox1.setChecked(true);
                                                        selChkBoxArr[v.getId()] = true;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else if (selCounter % 2 == 0) {//even

                                uncheckall = true;
                                TextView textview = (TextView) view;
                                id = textview.getId();

                                for (int i = 0; i < productLayout.getChildCount(); i++) {

                                    LinearLayout prodInfoLayout = (LinearLayout) productLayout.getChildAt(i);

                                    if (prodInfoLayout.getChildCount() > 0) {

                                        View view1 = prodInfoLayout.getChildAt(0);
                                        TextView tvLayout = (TextView) view1;
                                        if (tvLayout.getText().toString().equalsIgnoreCase(textview.getText().toString())) {
                                            for (int j = 2; j < prodInfoLayout.getChildCount(); j++) {
                                                LinearLayout subLayout = (LinearLayout) prodInfoLayout.getChildAt(j);
                                                if (subLayout.getChildCount() > 0) {
                                                    for (int k = 0; k < subLayout.getChildCount(); k++) {
                                                        View v = subLayout.getChildAt(k);
                                                        final CheckBox checkbox1 = (CheckBox) v;
                                                        checkbox1.setChecked(false);
                                                        selChkBoxArr[v.getId()] = false;

                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
                producttxt.setTextSize(20);
                producttxt.setGravity(Gravity.CENTER_HORIZONTAL);
                LinearLayout.LayoutParams merchantnameTextView_LayoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                merchantnameTextView_LayoutParams.setMargins(5, 0, 0, 5);
                producttxt.setLayoutParams(merchantnameTextView_LayoutParams);

                // line view layout
                LinearLayout lineviewlayout = new LinearLayout(mContext);
                LinearLayout.LayoutParams lineview = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 2);
                TextView line = new TextView(mContext);
                line.setLayoutParams(lineview);
                lineview.setMargins(3, 0, 3, 0);
                line.setBackgroundColor(Color.GRAY);
                lineviewlayout.addView(line);

                productInfoLinearLayout.addView(producttxt);
                productInfoLinearLayout.addView(lineviewlayout);

                String prodId = spinProductList.get(count).getProdId();
                try{
                    Cursor cursor=null;

                    categoryDetailsModelList.clear();
                    where = " where Produt_type_id like " + "'" + prodId + "'";
                    String product_type_id = "Category";
                    cursor = Narnolia.dbCon.fetchDistictFromSelect(product_type_id,DbHelper.TABLE_M_CATEGORY, where);

                    if (cursor != null && cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        do {
                            String category = "";
                            categoryDetailsModel = new CategoryDetailsModel();
                            category = cursor.getString(cursor.getColumnIndex("Category"));
                            categoryDetailsModel.setCategory(category);
                            categoryDetailsModelList.add(categoryDetailsModel);

                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                for (int i = 0; i < categoryDetailsModelList.size(); i++) {

                    String category = categoryDetailsModelList.get(i).getCategory();

                    try {
                        Cursor cursor2 = null;

                        subCategoryDetailsModelList.clear();
                        String where1 = " where trim(Category) = '"+category + "'";
                        cursor2 = Narnolia.dbCon.fetchAll2(category,DbHelper.TABLE_M_CATEGORY, where1);
                        if (cursor2 != null && cursor2.getCount() > 0) {
                            cursor2.moveToFirst();
                            do {
                                String subcategory = "";
                                int id;
                                subCategoryDetailsModel = new SubCategoryDetailsModel();
                                subcategory = cursor2.getString(cursor2.getColumnIndex("Subcategory"));
                                id = Integer.parseInt(cursor2.getString(cursor2.getColumnIndex("id")));
                                subCategoryDetailsModel.setSubCategory(subcategory);
                                subCategoryDetailsModel.setId(id);
                                subCategoryDetailsModelList.add(subCategoryDetailsModel);

                            } while (cursor2.moveToNext());
                        }
                        cursor2.close();

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    for (int k = 0; k < subCategoryDetailsModelList.size(); k++) {

                        String subcategory = subCategoryDetailsModelList.get(k).getSubCategory();

                        CategoryLinearLayout = new LinearLayout(mContext);
                        CategoryLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                        LinearLayout.LayoutParams CategoryLinearLayout_LayoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        CategoryLinearLayout.setLayoutParams(CategoryLinearLayout_LayoutParams);

                        checkbox = new CheckBox(mContext);

                        checkbox.setId(subCategoryDetailsModelList.get(k).getId());

                        checkbox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        });

                        checkbox.setOnCheckedChangeListener(this);

                        if (selChkBoxArr[checkbox.getId()]){
                            checkbox.setChecked(true);
                        }else {
                            checkbox.setChecked(false);
                        }

                        final LinearLayout.LayoutParams childParam2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        childParam2.weight = 0.9f;
                        checkbox.setLayoutParams(childParam2);

                        checkbox.setTypeface(null, Typeface.BOLD);
                        checkbox.setText(category +"-"+ subcategory);
                        LinearLayout.LayoutParams childParam1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        childParam1.weight = 0.1f;

                        CategoryLinearLayout.addView(checkbox);

                        productInfoLinearLayout.addView(CategoryLinearLayout);
                    }
                }
                productLayout.addView(productInfoLinearLayout);
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//........................................................................................................
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
//        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
//        client.disconnect();
    }

    private int countCheck(boolean isChecked) {

        icount += isChecked ? 1 : -1 ;
        //  Toast.makeText(mContext, "Selected chkbox Count is:" + icount, Toast.LENGTH_SHORT).show();

        return  icount;
    }





    public class InsertLeadData extends AsyncTask<String, Void, SoapPrimitive> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
               /* if (progressDialog != null && !progressDialog.isShowing()) {
                    progressDialog.show();
                }*/
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

            } catch (Exception e) {
                // TODO Auto-generated catch block
            }
            // strOccupation  = 1 and strDesignation = 1  also currency = "" these are hardcoded values we need to change when we get master data
            SOAPWebService webService = new SOAPWebService(mContext);
            if (cliked){
                strStages="closer of lead";
                strFlag="D";
            }else {
                strStages = "Lead Created";
                strFlag = "C";
            }
            strCreatedfrom="APP";
            strAllocated_userid="App";

            SoapPrimitive object = webService.SaveLead(strSourceofLead, strSubSourceofLead,empcode, str_fname, str_mname, str_lname, str_mob,
                    strlocation, str_city, str_pincode,"1",strStages, "", "",strCreatedfrom, strFlag, empcode);

            return object;

        }

        @Override
        protected void onPostExecute(SoapPrimitive soapObject) {
            super.onPostExecute(soapObject);
            String output = "";
            try {
               /* if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }*/
                output = (String) soapObject.toString();
                Log.d("Output =>",output);
                responseId = String.valueOf(soapObject.toString());
                if (responseId.contains("ERROR") || responseId.contains("null")) {
                    Toast.makeText(mContext, "Error in getting lead id", Toast.LENGTH_LONG).show();
                } else {
                    displayMessage("Lead Created Successfully");
                    //       updateOrInsertInDb();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                updateOrInsertInDb();
                new SaveCategory().execute();
            }

        }
    }

    private void updateOrInsertInDb() {

        try {
            int directLeadId = 0;
            LeadId = "";
            String strLastSync = "0";
            if (cliked){
                strStages="closer of lead";
                strFlag="D";
            }else {
                strStages = "Lead Created";
                strFlag = "C";
            }

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
            String[] selectionArgs = {LeadId};// LeadId

            // for now strcurrency becomes blank as " "; please change it later
            String valuesArray[] = { "" + directLeadId,LeadId,strStages, strSourceofLead, strSubSourceofLead, strCustomerID, str_fname, str_mname, str_lname,
                    strDob, strAge, str_mob, strAddr1, strAddr2, strAddr3, strlocation, str_city, str_pincode, strEmail, strIncome,
                    strOccupation, strCreatedfrom, strAppVersion, strAppdt, strFlag, strAllocated_userid, strBrokerDelts,
                    strMeetingStatus, strLeadStatus, strCompitator_Name, strProduct, strRemark, strTypeofSearch,
                    strDuration, strPanNo, strB_Margin, strB_aum, strB_sip, strB_number, strB_value, strB_premium, strReason,
                    strMeetingdt, strMeetingAgenda, strLead_Updatelog, strCreatedby, strCreateddt, strUpdateddt, strUpdatedby,strEmpCode,strLastMeetingDate,StrLastMeetingUpdate,
                    selectedCatId,strCustomer_id_name,str_duration};


            boolean result = Narnolia.dbCon.update(DbHelper.TABLE_DIRECT_LEAD, selection, valuesArray, utils.columnNamesLead, selectionArgs);


            if (result) {

                displayMessage("Data Inserted Succesfully");

                if (cliked){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                    builder1.setTitle("Lead");
                    builder1.setMessage("Lead "+ LeadId +" Created and Closed Sucessfully!");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    pushActivity(mContext, HomeActivity.class, null, true);
                                    dialog.cancel();
                                }
                            });

              /*  builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });*/

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }


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
    public class SaveCategory extends AsyncTask <String, Void, SoapPrimitive> {

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
            SoapPrimitive object = null;
            SOAPWebService webService = new SOAPWebService(mContext);
            String fromType="APP";
            try {
                object = webService.SaveCategory(LeadId, selectedCatId, empcode,fromType);

            } catch (Exception e) {
                // TODO Auto-generated catch block
            }
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
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    //.......................Client Details Model
    public void getAllClientData() {

        clientInfoModelList = new ArrayList<>();
        try {
            // WHERE clause
            String Branch = "CH";

            String where = " where Branchid = '"+Branch+"'";
            Cursor cursor = Narnolia.dbCon.fetchFromSelect(DbHelper.TABLE_CLIENT_DETAILS, where);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    clientDetailsModel = createClientDetailsModel(cursor);
                    clientInfoModelList.add(clientDetailsModel);

                } while (cursor.moveToNext());
                cursor.close();

            }
            setClientDetailValue();
//  spinner_cust_id
            if (spinClientIDList != null && spinClientIDList.size() > 0) {
                ArrayAdapter<String> adapter3= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strClientIDArray) {
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

                adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_cust_id.setAdapter(adapter3);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void populateClientData(ClientDetailsModel clientDetailsModel){
        try {
            if(clientDetailsModel != null){
                fname.setText(clientDetailsModel.getClientName());
                fname.setEnabled(false);
                //   customer_id.setText(clientDetailsModel.getClientID());
                //  customer_id.setEnabled(false);
                String first = "", middle = "", last = "";
                String nm=clientDetailsModel.getClientName();
                String[] name=nm.split(" ");
                if (!name.equals(null) && name.length >= 3){
                    first=name[0];
                    middle=name[1];
                    last=name[2];
                    fname.setText(first);
                    fname.setEnabled(false);
                    mname.setText(middle);
                    mname.setEnabled(false);
                    lname.setText(last);
                    lname.setEnabled(false);
                }else if(!name.equals(null) && name.length >= 2){
                    first=name[0];
                    last=name[1];
                    fname.setText(first);
                    fname.setEnabled(false);
                    mname.setText("");
                    lname.setText(last);
                    lname.setEnabled(false);
                }
                editCity.setText(clientDetailsModel.getCity());
                editCity.setEnabled(false);
                autoPincode.setText(clientDetailsModel.getPinCode());
                autoPincode.setEnabled(false);
                //   mobileno.setText(clientDetailsModel.getMobileNumber());
                //   mobileno.setEnabled(false);



            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public ClientDetailsModel createClientDetailsModel(Cursor cursor) {
        clientDetailsModel = new ClientDetailsModel();
        try {
            clientDetailsModel.setAddress(cursor.getString(cursor.getColumnIndex("Address")));
            clientDetailsModel.setAnnualIncome(cursor.getString(cursor.getColumnIndex("AnnualIncome")));
            clientDetailsModel.setBirthDate(cursor.getString(cursor.getColumnIndex("BirthDate")));
            clientDetailsModel.setBranchid(cursor.getString(cursor.getColumnIndex("Branchid")));
            clientDetailsModel.setCity(cursor.getString(cursor.getColumnIndex("City")));
            clientDetailsModel.setClientCat(cursor.getString(cursor.getColumnIndex("ClientCat")));
            clientDetailsModel.setClientID(cursor.getString(cursor.getColumnIndex("ClientID")));
            clientDetailsModel.setClientName(cursor.getString(cursor.getColumnIndex("ClientName")));
            clientDetailsModel.setEmailAddress(cursor.getString(cursor.getColumnIndex("EmailAddress")));
            clientDetailsModel.setMobileNumber(cursor.getString(cursor.getColumnIndex("MobileNumber")));
            clientDetailsModel.setPanNumber(cursor.getString(cursor.getColumnIndex("PanNumber")));
            clientDetailsModel.setPinCode(cursor.getString(cursor.getColumnIndex("PinCode")));
            clientDetailsModel.setRMCode(cursor.getString(cursor.getColumnIndex("RMCode")));
            clientDetailsModel.setRMName(cursor.getString(cursor.getColumnIndex("RMName")));
            clientDetailsModel.setStateId(cursor.getString(cursor.getColumnIndex("StateId")));
            clientDetailsModel.setTelephonenumber(cursor.getString(cursor.getColumnIndex("Telephonenumber")));
            clientDetailsModel.setUcc(cursor.getString(cursor.getColumnIndex("Ucc")));
            clientDetailsModel.setBankaccount(cursor.getString(cursor.getColumnIndex("bankaccount")));
            clientDetailsModel.setBankname(cursor.getString(cursor.getColumnIndex("bankname")));
            clientDetailsModel.setcStatus(cursor.getString(cursor.getColumnIndex("cStatus")));
            clientDetailsModel.setDopeningDate(cursor.getString(cursor.getColumnIndex("dopeningDate")));
            clientDetailsModel.setDpac(cursor.getString(cursor.getColumnIndex("dpac")));
            clientDetailsModel.setDpid(cursor.getString(cursor.getColumnIndex("dpid")));
            clientDetailsModel.setIfsc(cursor.getString(cursor.getColumnIndex("ifsc")));
            clientDetailsModel.setMicr(cursor.getString(cursor.getColumnIndex("micr")));
            clientDetailsModel.setResult(cursor.getString(cursor.getColumnIndex("result")));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return clientDetailsModel;

    }

    //................................................................


}