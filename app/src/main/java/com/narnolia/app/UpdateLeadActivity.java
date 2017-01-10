package com.narnolia.app;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.narnolia.app.dbconfig.DbHelper;
import com.narnolia.app.libs.Utils;
import com.narnolia.app.model.CategoryDetailsModel;
import com.narnolia.app.model.ClientDetailsModel;
import com.narnolia.app.model.LeadInfoModel;
import com.narnolia.app.model.ProductCategory;
import com.narnolia.app.model.ProductDetailsModel;
import com.narnolia.app.model.SubCategoryDetailsModel;
import com.narnolia.app.multispinner.KeyPairBoolData;
import com.narnolia.app.multispinner.MultiSpinnerSearch;
import com.narnolia.app.multispinner.SpinnerListener;
import com.narnolia.app.network.SOAPWebService;

import org.ksoap2.serialization.SoapPrimitive;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by USER on 10/24/2016.
 */

public class UpdateLeadActivity extends AbstractActivity  implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    private Context mContext;
    private LeadInfoModel leadInfoModel;
    private List<LeadInfoModel>leadInfoModelList;
    private Map<String, List<String>> spinPincodeArray;
    private List<String> spinCityArray;
    private List<String> selItemsPosition;
    private ArrayAdapter<String> adapter9;
    private ProgressDialog progressDialog;
    private String responseId;
    public Utils utils;
    String lead__Id;
    String strStages,strFlag;
    boolean cliked=false;
    ClientDetailsModel clientDetailsModel;
    List<ClientDetailsModel>clientInfoModelList;
    private List<String> spinClientIDList;
    String[] strClientIDArray;
    String sourceString,subsource,ClinetId;
    SpinnerListener spineListener;

    boolean flag = false;

    public boolean[] selChkBoxArr = new boolean[50];

    //..................................................................
    private String strCreatedfrom, strAppVersion, strAppdt, strAllocated_userid,
            strCompitator_Name, strProduct, strRemark,
            strPanNo, strB_Margin, strB_aum, strB_sip, strB_number, strB_value, strB_premium,strEmpCode,
            strCreatedby, strCreateddt, strUpdateddt, strUpdatedby
            ,strBusiness_opp,strLastMeetingDate,strLastMeetingUpdate;
    //.......................................................................
    EditText customer_id,fname,mname,lname, mobileno, email, date_of_birth, address, flat, street, location,next_metting_date, metting_agenda, lead_update_log,reason,
            margin,aum,sip,number,value,premium,remark,compitator,product,last_meeting_dt,last_meeting_update,pan_no,duration;
    AutoCompleteTextView editCity,autoPincode;
    String lead_id_info,leadId;
    Spinner spinner_lead_name, spinner_source_of_lead, spinner_sub_source, spinner_age_group,
            spinner_occupation, spinner_annual_income, spinner_other_broker,spinner_lead_status,spinner_duration,spinner_custID;
    MultiSpinnerSearch spinner_research_type ;
    TextView tv_lead_name, tv_source_of_lead, tv_sub_source, tv_fname,tv_mname,tv_lname, tv_mobile_no, tv_email,
            tv_age_group, tv_date_of_birth, tv_address, tv_flat, tv_street, tv_location, tv_city, tv_pincode,
            tv_occupation, tv_annual_income, tv_other_broker, tv_meeting_status, tv_meeting_agenda, tv_lead_update_log,tv_lead_status,tv_pan_no;
    RadioGroup rg_meeting_status;
    RadioButton rb_contact, rb_not_contact;
    LinearLayout connect,notconnect,linear_non_salaried,linear_remark,linear_competitor,linear_research,linear_lead_details_hidden,linear_customer_id,linear_pan_no,linear_duration,linear_spin_customer_id;
    //.........Edit Text Strings
    String str_cust_id,str_fname,str_mname,str_lname,str_mobile_no,str_email,str_date_of_birth,str_address,str_flat,str_street,str_laocion,str_city,
            str_pincode,str_next_meeting_date,str_metting_agenda,str_lead_update_log,str_reason;
    //........Spineer Strings
    String str_spinner_lead_name, str_spinner_source_of_lead, str_spinner_sub_source, str_spinner_age_group,
            str_spinner_occupation,str_spinner_annual_income, str_spinner_other_broker,str_spinner_lead_status,str_spinner_research_type = "",str_spinner_duration;
    //......Radio Group String
    String str_rg_meeting_status;
    Button bt_update_lead, bt_close_lead,btn1_opportunity_pitched2;
    private DatePickerDialog datePickerDialog,datePickerDialog1,datePickerDialog2;   //date picker declare
    private SimpleDateFormat dateFormatter;      //date format declare
    private List<String> spinAgeGroupArray = new ArrayList<String>(); //age group array
    String spinOccupationArray[] = {"Select Occupation","Salaried", "Non-Salaried"};//Occupation array
    String spinAnnualInacomeArray[]={"Select Annual Income","0-5,00,000", "5,00,000-10,00,000","10,00,000-25,00,00","25,00,000 and above"};
    String spinOtherBrokersArray[]={"Select Other Brokers Dealt with","TOP 20","Others","None"};//Other Brokers array
    String spinDurationArray[]={"Select Duration","1 month","3 month","6 month","Others"}; //duration Array
    String spinLeadStatusArray[]={"Select Lead Status","Hot","Warm","Cold","Not Intersted","Wrong Contact Details","Lost","Lost to Competitor","Research Servicing","On-boarding"};
    private List<String> spinSourceLeadList;
    private List<String> spinSubSourceLeadList;
    private List<String> spinLeadNameList;
    private List<String> spinResearchTypeList;
    String [] strResearchArray=null;
    private List<String> spinOccupationList=new ArrayList<String>(Arrays.asList(spinOccupationArray));
    private List<String>spinAnnualIncomeList=new ArrayList<String>(Arrays.asList(spinAnnualInacomeArray));
    private List<String>spinOtherBrokerList=new ArrayList<String>(Arrays.asList(spinOtherBrokersArray));
    private List<String>spinDurationList=new ArrayList<String>(Arrays.asList(spinDurationArray));
    private List<String>spinLeadStatusList=new ArrayList<String>(Arrays.asList(spinLeadStatusArray));
    String[] strLeadArray = null;
    String[] strSubLeadArray = null;
    String[] strAgeArray=null;
    String[] strLeadNameArray;
    private String empcode,fullname;
    private SharedPref sharedPref;
    private TextView admin;
    private ArrayAdapter<String> adapter8 = null;

    AlertDialog.Builder DialogProduct;
    AlertDialog showProduct;
    int icount =0;
    int id;

    private LinearLayout productLayout;
    private  CheckBox checkbox;
    String selectedCatId = "";
    String selectedCatIdpopulate = "";
    String str_CheckedItems;
    public ProductDetailsModel productDetailsModel;
    private CategoryDetailsModel categoryDetailsModel;
    private SubCategoryDetailsModel subCategoryDetailsModel;

    private List<ProductDetailsModel> spinProductList;
    private List<CategoryDetailsModel> categoryDetailsModelList;
    private List<SubCategoryDetailsModel> subCategoryDetailsModelList;

    LinearLayout CategoryLinearLayout = null;
    LinearLayout productInfoLinearLayout = null;

    private boolean flagSelected = false;
    private int selCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_update);

        leadInfoModelList = new ArrayList<LeadInfoModel>();
        spinLeadNameList = new ArrayList<>();
        fetchDataOfLeadDetails();

        sharedPref = new SharedPref(UpdateLeadActivity.this);
        empcode = sharedPref.getLoginId();
        spinPincodeArray = new HashMap<>();
        spinCityArray = new ArrayList<>();
        selItemsPosition = new ArrayList<>();
        lead__Id = getIntent().getStringExtra("lead__Id");

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
            utils = new Utils(mContext);
            admin = (TextView) findViewById(R.id.admin);
            admin.setText(empcode);

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            setHeader();
//............Auto complete text view
            editCity = (AutoCompleteTextView) findViewById(R.id.edt_city);
            autoPincode = (AutoCompleteTextView) findViewById(R.id.edt_pincode);

            //............Edit Text Ref
            customer_id=(EditText)findViewById(R.id.edt_cust_id);
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
            next_metting_date = (EditText) findViewById(R.id.edt_next_meeting);
            metting_agenda = (EditText) findViewById(R.id.edt_meeting_agenda);
            lead_update_log = (EditText) findViewById(R.id.edt_lead_update_log);
            reason=(EditText)findViewById(R.id.edt_reason);  //........when radio button is not connected
            //.....inside table edit text
            margin=(EditText)findViewById(R.id.edt_margin);
            aum=(EditText)findViewById(R.id.edt_aum);
            sip=(EditText)findViewById(R.id.edt_sip);
            number=(EditText)findViewById(R.id.edt_number);
            value=(EditText)findViewById(R.id.edt_value);
            premium=(EditText)findViewById(R.id.edt_premium);
            //......spinner slection not intersted
            remark=(EditText)findViewById(R.id.edt_remark);
            compitator=(EditText)findViewById(R.id.edt_competitor);
            product=(EditText)findViewById(R.id.edt_product);
            last_meeting_dt=(EditText)findViewById(R.id.edt_last_meeting);
            last_meeting_update=(EditText)findViewById(R.id.edt_meeting_update);
            pan_no=(EditText)findViewById(R.id.edt_pan_no);
            duration=(EditText)findViewById(R.id.edt_duration);
            /*tv_prospective_products2 = (TextView) findViewById(R.id.txt1_prospective_product2);
            tv_prospective_products2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    icount = 0;
                    showProductDialog();
                }
            });*/

            new LoadCityData().execute();//........................................Load city data
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
            editCity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editCity.length()>0){editCity
                            .setError(null);}
                }
            });
            //.........................pan no validation......
            pan_no.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (pan_no.length()==0){
                        pan_no.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                    }
                    if (pan_no.length()==5){
                        pan_no.setInputType(InputType.TYPE_CLASS_NUMBER);
                    }
                    if (pan_no.length()==9){
                        pan_no.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    try {
                        if ((pan_no.length() == 10)||(pan_no.length()==11)) {

                            Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
                            Matcher matcher = pattern.matcher(editable);
                            if (matcher.matches()) {
                                pan_no.setError(null);
                            } else {
                                int pos = pan_no.length();
                                if (pos == 0) {
                                    pan_no.setError(null);
                                } else {
                                    pan_no.setError("PAN is not matching");
                                }
                            }
                        } else {
                            if (pan_no.length() == 0) {
                                pan_no.setError(null);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

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
            tv_pan_no=(TextView)findViewById(R.id.txt_pan_no);
            tv_pan_no.setText(Html.fromHtml("<font color=\"red\">*</font>"+"<font color=\"black\">Pan No</font>\n"));

            //....................spinner Ref........
            spinner_lead_name = (Spinner) findViewById(R.id.spin_lead_name);
            spinner_source_of_lead = (Spinner) findViewById(R.id.spin_source_of_lead);
            spinner_sub_source = (Spinner) findViewById(R.id.spin_sub_source);
            spinner_custID=(Spinner)findViewById(R.id.spin2_cust_id);
            spinner_age_group = (Spinner) findViewById(R.id.spin_age_group);
            spinner_occupation = (Spinner) findViewById(R.id.spin_occupation);
            spinner_annual_income = (Spinner) findViewById(R.id.spin_annual_income);
            spinner_other_broker = (Spinner) findViewById(R.id.spin_other_broker);
            spinner_lead_status=(Spinner)findViewById(R.id.spin_lead_status);
            spinner_duration=(Spinner)findViewById(R.id.spin_duration);
            spinner_research_type=(MultiSpinnerSearch) findViewById(R.id.spin_research_type);




            //.................radio group layout......
            connect=(LinearLayout)findViewById(R.id.linear_meeting_status_connected);
            notconnect=(LinearLayout)findViewById(R.id.linear_meeting_status_not_connected);
            linear_lead_details_hidden=(LinearLayout)findViewById(R.id.linear_lead_details_hidden);
            //...............intenal linear layouts
            linear_non_salaried=(LinearLayout)findViewById(R.id.linear_non_salaried);
            linear_remark=(LinearLayout)findViewById(R.id.linear_remark);
            linear_competitor=(LinearLayout)findViewById(R.id.linear_competitor);
            linear_research=(LinearLayout)findViewById(R.id.linear_research);
            linear_customer_id=(LinearLayout)findViewById(R.id.linear_cust_id);
            linear_pan_no=(LinearLayout)findViewById(R.id.linear_pan_no);
            linear_duration=(LinearLayout)findViewById(R.id.linear_duration);
            linear_spin_customer_id=(LinearLayout)findViewById(R.id.linear1_spin_cust_id);

            //spinner OnItemSelectedListener
            spinner_lead_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if(position==0){
                        linear_lead_details_hidden.setVisibility(View.GONE);

                    }
                    if (position != 0){

                        if ( strLeadNameArray != null && strLeadNameArray.length > 0){
                            String leadData = spinner_lead_name.getSelectedItem().toString();
                            String [] strLead = leadData.split("\\(");
                            leadId = strLead[1].substring(0,strLead[1].length()-1);

                            try {
                                // WHERE clause
                                String where = " where lead_id = '"+leadId+"'";
                                Cursor cursor = Narnolia.dbCon.fetchFromSelect(DbHelper.TABLE_DIRECT_LEAD,where);
                                if (cursor != null && cursor.getCount() > 0) {
                                    cursor.moveToFirst();
                                    do {
                                        leadInfoModel = createLeadInfoModel(cursor);
//                                        leadInfoModelList.add(leadInfoModel);

                                    } while (cursor.moveToNext());
                                    cursor.close();

                                }else {
                                    Toast.makeText(mContext, "No data found..!",Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            linear_lead_details_hidden.setVisibility(View.VISIBLE);

                            try {
                                populateData(leadInfoModel);
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

            spinner_source_of_lead.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if ( strLeadArray != null && strLeadArray.length > 0){
                        str_spinner_source_of_lead = spinner_source_of_lead.getSelectedItem().toString();

                        sourceString = parent.getItemAtPosition(position).toString();

                        if (sourceString != null) {
                            try {
                                if (sourceString.equalsIgnoreCase("In- house Leads (Existing)")&& subsource.equals("Existing Client")){
                                    fname.setText("");
                                    customer_id.setText("");
                                    getAllClientData();//.............Client data method
                                    linear_spin_customer_id.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    fname.setEnabled(true);


                                    customer_id.setEnabled(true);
                             //       customer_id.setText("");
                                    linear_spin_customer_id.setVisibility(View.GONE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                if (!sourceString.equalsIgnoreCase("In- house Leads (Existing)")&& !subsource.equals("Existing Client")){
                                    fname.setText(leadInfoModel.getFirstname());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (sourceString.equalsIgnoreCase("Client Reference")) {
                                linear_customer_id.setVisibility(View.VISIBLE);
                            }else if (sourceString.equalsIgnoreCase("In- house Leads (Existing)"))
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
                    if ( strSubLeadArray != null && strSubLeadArray.length > 0){
                     //   strSubSourceofLead = spinner_sub_source.getSelectedItem().toString();
                        subsource=parent.getItemAtPosition(position).toString();
                        if (subsource!=null){
                            if (subsource.equalsIgnoreCase("Existing Client")&& sourceString.equals("In- house Leads (Existing)")){
                                fname.setText("");
                                customer_id.setText("");
                                getAllClientData();//.............Client data method
                                linear_spin_customer_id.setVisibility(View.VISIBLE);
                            }
                            else
                            {   fname.setEnabled(true);
                           //     fname.setText("");
                                customer_id.setEnabled(true);
                             //   customer_id.setText("");
                                linear_spin_customer_id.setVisibility(View.GONE);
                            }
                        }
                        if (!subsource.equalsIgnoreCase("Existing Client")&& !sourceString.equals("In- house Leads (Existing)")){
                            fname.setText(leadInfoModel.getFirstname());
                        }

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
                    String leadString,leadString1,leadString2,leadString3;
                    leadString = parent.getItemAtPosition(pos).toString();
                    leadString1=parent.getItemAtPosition(pos).toString();
                    leadString2=parent.getItemAtPosition(pos).toString();
                    leadString3=parent.getItemAtPosition(pos).toString();
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
                    if (leadString3 != null) {
                        if (leadString3.equalsIgnoreCase("On-boarding")) {

                            linear_pan_no.setVisibility(View.VISIBLE);

                        } else {
                            linear_pan_no.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }

            });
            spinner_duration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent,
                                           View view, int pos1, long id) {
                    String leadString,leadString1,leadString2,leadString3;
                    leadString = parent.getItemAtPosition(pos1).toString();
                    leadString1=parent.getItemAtPosition(pos1).toString();
                    leadString2=parent.getItemAtPosition(pos1).toString();
                    leadString3=parent.getItemAtPosition(pos1).toString();
                    if (leadString != null) {
                        int selMonth=0;
                        Calendar cal = Calendar.getInstance();
                        Date current = cal.getTime();
                        cal.setTime(current);
                        if (leadString.equalsIgnoreCase("1 month")) {
                            selMonth = 1;
                            cal.add(Calendar.MONTH, selMonth);
                            String curDate = getDate(cal);
                            linear_duration.setVisibility(View.VISIBLE);
                            duration.setText(""+curDate);
                        }else if (leadString1.equalsIgnoreCase("3 month")) {
                            selMonth = 3;
                            cal.add(Calendar.MONTH, selMonth);
                            String curDate = getDate(cal);
                            linear_duration.setVisibility(View.VISIBLE);
                            duration.setText(""+curDate);
                        }else if (leadString2.equalsIgnoreCase("6 month")) {
                            selMonth = 6;
                            cal.add(Calendar.MONTH, selMonth);
                            String curDate = getDate(cal);
                            linear_duration.setVisibility(View.VISIBLE);
                            duration.setText(""+curDate);
                        }else if (leadString3.equalsIgnoreCase("Others")) {
                            duration.setText("");
                        }

                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }

            });
            //........................spinner client id
            spinner_custID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                    if (position != 0){

                        if ( strClientIDArray != null && strClientIDArray.length > 0){
                            String clientData = spinner_custID.getSelectedItem().toString();
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

            fetchResearchTypedata();

            try {
                //  fetchResearchTypedata();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Set Lead to the Lead Spinner
            try {
                if (lead__Id != null && lead__Id.length() > 0){
                    if (adapter8 != null){
                        for (int i = 0;i<strLeadNameArray.length;i++){
                            if (strLeadNameArray[i].contains(lead__Id)){
                                int selLeadPos = adapter8.getPosition(strLeadNameArray[i]);
                                spinner_lead_name.setSelection(selLeadPos);
                            }

                        }
                        //                     int selLeadPos = adapter8.getgetPosition(lead__Id);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            // ...............Radio Group.....
            rg_meeting_status = (RadioGroup) findViewById(R.id.rg_metting_status);

            //................Radio Button
            rb_contact = (RadioButton) findViewById(R.id.rb_contact);
            rb_not_contact = (RadioButton) findViewById(R.id.rb_not_contact);

            //................Button ........
            bt_update_lead = (Button) findViewById(R.id.btn_update);
            bt_close_lead = (Button) findViewById(R.id.btn_close);

            btn1_opportunity_pitched2 = (Button) findViewById(R.id.btn1_opportunity_pitched2);

            btn1_opportunity_pitched2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    icount = 0;

                    //Toast.makeText(mContext, "Selected chkbox COUNT value is:" +icount, Toast.LENGTH_SHORT).show();

                    showProductDialog();

                }
            });
            //....................update lead click event...
            bt_update_lead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    validateDetails();
                }
            });
            last_meeting_update.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }
                @Override
                public void afterTextChanged(Editable editable) {
                    String text=last_meeting_dt.getText().toString().trim();
                    String text1=last_meeting_update.getText().toString().trim();
                    lead_update_log.setText(text+"\n"+text1);
                }
            });
            last_meeting_dt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String text=last_meeting_dt.getText().toString().trim();
                    String text1=last_meeting_update.getText().toString().trim();
                    lead_update_log.setText(text+"\n"+text1);
                }
            });
            bt_close_lead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cliked=true;

                    if (isConnectingToInternet()) {

                        new UpdateLeadData().execute();

                    } else {
                        updateInDb();
                    }
                    pushActivity(mContext, HomeActivity.class, null, true);
                }
            });
            try {
                //.............date of birth date picker

                final Calendar newCalendar = Calendar.getInstance();
                dateFormatter = new SimpleDateFormat("MM-dd-yyyy");
                datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int monthOfYear, int dayOfMonth, int year) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(monthOfYear, dayOfMonth, year);
                        date_of_birth.setText(dateFormatter.format(newDate.getTime()));

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setCalendarViewShown(false);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
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
                datePickerDialog1.getDatePicker().setCalendarViewShown(false);
                datePickerDialog1.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                next_metting_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datePickerDialog1.show();
                    }
                });
                datePickerDialog2 = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int monthOfYear, int dayOfMonth, int year) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(monthOfYear, dayOfMonth, year);
                        last_meeting_dt.setText(dateFormatter.format(newDate.getTime()));
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog2.getDatePicker().setCalendarViewShown(false);
                datePickerDialog2.getDatePicker().setMaxDate(System.currentTimeMillis());
                last_meeting_dt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datePickerDialog2.show();
                    }
                });


                rg_meeting_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        View radiobutton = group.findViewById(checkedId);

                        RadioButton rb = (RadioButton) findViewById(checkedId);
                        str_rg_meeting_status = rb.getText().toString();
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

            spineListener = new SpinnerListener() {
                @Override
                public void onItemsSelected(List<KeyPairBoolData> items) {
                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).isSelected()) {
                            Log.i("TAG item", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                            str_spinner_research_type += items.get(i).getId()+"#";
//                            str_spinner_research_type=i+items.get(i).getName()+items.get(i).isSelected();
                        }
                    }
                    str_spinner_research_type = str_spinner_research_type.length() > 0 ? str_spinner_research_type.substring(0, str_spinner_research_type.length() - 1) : "";

                }
            };

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getDate(Calendar cal) {
        return "" + (cal.get(Calendar.MONTH)+1) + "/"
                + cal.get(Calendar.DATE) +"/"
                + cal.get(Calendar.YEAR);

    }

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

        if (selItemsPosition != null && selItemsPosition.size() > 0){
            for (int i = 0; i < selItemsPosition.size(); i++){
                selChkBoxArr[Integer.parseInt(selItemsPosition.get(i))] = true;
//                selectedCatIdpopulate +=selItemsPosition.get(i)+"#";
            }
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

                //        submit.setVisibility(View.INVISIBLE);
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(icount>0){

                    String name = + icount + " is Selected";
                    //    tv_prospective_products2.setText(name);
                    selectedCatId = selectedCatId.length() > 0 ? selectedCatId.substring(0,selectedCatId.length() - 1) : "";

                    btn1_opportunity_pitched2.setText(name);
                    showProduct.dismiss();
                }else
                {
                    //    tv_prospective_products2.setText("Prospective Products");

                    btn1_opportunity_pitched2.setText("Opportunity Pitched");
                    showProduct.dismiss();
                }

            /*    String name = +icount+ "is Selected";
                tv_prospective_products1.setText(name);*/
            }
        });

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
                    //spinProductList.add(productDetailsModel);
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
//                producttxt.setOnClickListener(this);

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
                                                        selectedCatId += v.getId()+"#";
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else if (selCounter % 2 == 0) {//even
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

                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }


                            Log.e("Selected id =>", "" + id);
                            //                          Toast.makeText(mContext, "Selected Text is:" + textview.getText().toString(), Toast.LENGTH_SHORT).show();

                                    /*if (prodInfoLayout.getChildCount() > 0) {

                                        for (int j = 2; j < prodInfoLayout.getChildCount(); j++) {
                                            LinearLayout catLayout = (LinearLayout) prodInfoLayout.getChildAt(j);
                                            if (catLayout.getChildCount() > 0) {
                                                for (int k = 0; k < catLayout.getChildCount(); k++) {
                                                    View v = catLayout.getChildAt(k);

                                                    CheckBox checkbox1 = (CheckBox) v;
                                                    if (checkbox1.isChecked()) {

                                                        String selChkBoxValue = checkbox1.getText().toString();
                                                        Toast.makeText(mContext, "Selected chkbox value is:" + selChkBoxValue, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        }
                                    }*/

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
//                          spinProductList.add(productvalue);
                            // cursor=null;
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

                                selChkBoxArr[v.getId()] = true;
                                selectedCatId += v.getId()+"#";
                            }
                        });

                        checkbox.setOnCheckedChangeListener(this);

                        if (selChkBoxArr[checkbox.getId()]){
                            checkbox.setChecked(true);
                            selectedCatId += checkbox.getId()+"#";
                        }/*else if (selItemsPosition != null && selItemsPosition.size() > 0){
                            if (selChkBoxArr[checkbox.getId()]){
                                checkbox.setChecked(true);
                            }
                        }*/

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
    public void onClick(View view) {
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
                                    if (checkbox1.isChecked()) {

                                        String selChkBoxValue = checkbox1.getText().toString();
                                        //        Toast.makeText(mContext, "Selected chkbox value is:" + selChkBoxValue, Toast.LENGTH_SHORT).show();
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


    private int countCheck(boolean isChecked) {

        icount += isChecked ? 1 : -1 ;
        //  Toast.makeText(mContext, "Selected chkbox Count is:" + icount, Toast.LENGTH_SHORT).show();

        return  icount;
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        countCheck(b);
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

                                    if (checkbox1.isChecked()) {

                                        /*int selChkBoxId = checkbox1.getId();
                                        selectedCatId += selChkBoxId+"#";*/
                                        if(str_CheckedItems == null)
                                        {
                                            str_CheckedItems = compoundButton.getText().toString();
                                            //      Toast.makeText(mContext, "Selected chkbox :" + str_CheckedItems, Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            str_CheckedItems = str_CheckedItems+","+ compoundButton.getText().toString();
                                            //         Toast.makeText(mContext, "Selected chkbox :" + str_CheckedItems, Toast.LENGTH_SHORT).show();
                                        }

                                        String selChkBoxValue = checkbox1.getText().toString();
//                                        Toast.makeText(mContext, "Selected chkbox value is:" + selChkBoxValue, Toast.LENGTH_SHORT).show();

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


    private void  fetchDataOfLeadDetails(){
        try {
            // WHERE clause
            String where = " where flag NOT IN('D')";
            Cursor cursor = Narnolia.dbCon.fetchFromSelect(DbHelper.TABLE_DIRECT_LEAD, where);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    leadInfoModel = createLeadInfoModel(cursor);
                    leadInfoModelList.add(leadInfoModel);

                } while (cursor.moveToNext());
                cursor.close();

            }else {
                Toast.makeText(mContext, "No data found..!",Toast.LENGTH_SHORT).show();
            }
            setLeadDetailValue();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setLeadDetailValue(){
        try{
            if(leadInfoModelList.size()>0) {

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
            }
        }catch (Exception e){
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
            Collections.sort(spinSourceLeadList);
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
    public boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public boolean isName(String name) {
        if (name == null) {
            return false;
        } else {
            if (name.length() < 3 ) {
                return false;
            } else {
                return true;
            }
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
    //...............validatations.....
    private void validateDetails(){
        try {

            mobileno.setError(null);
            editCity.setError(null);
            autoPincode.setError(null);
            fname.setError(null);
            lname.setError(null);
            str_fname=fname.getText().toString().trim();
            str_lname=lname.getText().toString().trim();
            str_city=editCity.getText().toString().trim();
            str_pincode=autoPincode.getText().toString().trim();
            //...............on button click refrences........
            str_cust_id=customer_id.getText().toString().trim();
            str_mname=mname.getText().toString().trim();
            str_mobile_no=mobileno.getText().toString().trim();
            str_email=email.getText().toString().trim();
            str_date_of_birth=date_of_birth.getText().toString().trim();
            str_address=address.getText().toString().trim();
            str_flat=flat.getText().toString().trim();
            str_laocion=location.getText().toString().trim();
            str_next_meeting_date=next_metting_date.getText().toString().trim();
            str_metting_agenda=metting_agenda.getText().toString().trim();
            str_lead_update_log=lead_update_log.getText().toString().trim();
            //.......spinner text get Text
            str_spinner_lead_name=spinner_lead_name.getSelectedItem().toString();
            str_spinner_sub_source=spinner_sub_source.getSelectedItem().toString();
            str_spinner_age_group=spinner_age_group.getSelectedItem().toString();
            str_spinner_occupation=spinner_occupation.getSelectedItem().toString();
            str_spinner_annual_income=spinner_annual_income.getSelectedItem().toString();
            str_spinner_other_broker=spinner_other_broker.getSelectedItem().toString();
            str_spinner_lead_status=spinner_lead_status.getSelectedItem().toString();
            //str_spinner_research_type=spinner_research_type.getSelectedItem().toString();
            str_spinner_duration=spinner_duration.getSelectedItem().toString();

            str_street=street.getText().toString().trim();
            str_reason=reason.getText().toString().trim();
            strB_Margin=margin.getText().toString().trim();
            strB_aum=aum.getText().toString().trim();
            strB_sip=sip.getText().toString().trim();
            strB_number=number.getText().toString().trim();
            strB_value=value.getText().toString().trim();
            strB_premium=premium.getText().toString().trim();
            strRemark=remark.getText().toString().trim();
            strCompitator_Name=compitator.getText().toString().trim();
            strProduct=product.getText().toString().trim();
            strLastMeetingDate=last_meeting_dt.getText().toString().trim();
            strLastMeetingUpdate=last_meeting_update.getText().toString().trim();
            strPanNo=pan_no.getText().toString().trim();




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
            if (!isName(str_lname))
            {
                lname.setError(getString(R.string.name));
                focusView = lname;
                focusView.requestFocus();
                return;
            }
            if (!isPhoneValid(str_mobile_no)) {
                mobileno.setError(getString(R.string.reqmob));
                focusView = mobileno;
                focusView.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(str_city)){
                editCity.setError(getString(R.string.reqcity));
                focusView=editCity;
                focusView.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(str_pincode)) {
                autoPincode.setError(getString(R.string.reqpincode));
                focusView = autoPincode;
                focusView.requestFocus();
                return;
            }


            if (!TextUtils.isEmpty(str_email)) {
                if (!isEmailValid(str_email)) {
                    email.setError("Invalid email Id");
                    focusView = email;
                    focusView.requestFocus();
                    return;
                }
            }


            if (isConnectingToInternet()) {

                new UpdateLeadData().execute();

            } else {
                updateInDb();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void fetchResearchTypedata() {
        try {

            spinResearchTypeList = new ArrayList<>();
            //  final List<KeyPairBoolData> spinResearchTypeList = new ArrayList<>();
          /*  String SubSourceLead = "SubSource ";

            String where = " where type like " + "'" + SubSourceLead + "'";
            Cursor cursor2 = Narnolia.dbCon.fetchFromSelect(DbHelper.TABLE_M_PARAMETER, where);*/
            String ST = "ST";

            String where1 = " where trim(type) like " + "'" + ST + "'";
            Cursor cursor3 = Narnolia.dbCon.fetchFromSelect(DbHelper.TABLE_M_PARAMETER, where1);
            if (cursor3 != null && cursor3.getCount() > 0) {
                cursor3.moveToFirst();
                do {
                    String research = "";
                    research = cursor3.getString(cursor3.getColumnIndex("value")).trim();
                    spinResearchTypeList.add(research);
                } while (cursor3.moveToNext());
                cursor3.close();
            }
            /*Collections.sort(spinResearchTypeList);
            if (spinResearchTypeList.size() > 0) {
                strResearchArray = new String[spinResearchTypeList.size() + 1];
                strResearchArray[0] = "Select an Option";
                for (int i = 0; i < spinResearchTypeList.size(); i++) {
                    strResearchArray[i + 1] = spinResearchTypeList.get(i);
                }
            }*/


            final List<KeyPairBoolData> listArray = new ArrayList<>();

            for (int i = 0; i < spinResearchTypeList.size(); i++) {
                KeyPairBoolData h = new KeyPairBoolData();
                h.setId(i + 1);
                h.setName(spinResearchTypeList.get(i));
                h.setSelected(false);
                listArray.add(h);
            }

            spinner_research_type.setItems(listArray, -1, new SpinnerListener() {

                @Override
                public void onItemsSelected(List<KeyPairBoolData> items) {

                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).isSelected()) {
                            Log.i("TAG item", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                            str_spinner_research_type += items.get(i).getId()+"#";
//                            str_spinner_research_type=i+items.get(i).getName()+items.get(i).isSelected();
                        }
                    }
                    str_spinner_research_type = str_spinner_research_type.length() > 0 ? str_spinner_research_type.substring(0, str_spinner_research_type.length() - 1) : "";
                }
            });

            spinner_research_type.setLimit(listArray.size(), new MultiSpinnerSearch.LimitExceedListener() {
                @Override
                public void onLimitListener(KeyPairBoolData data) {
                    Toast.makeText(getApplicationContext(),
                            "Limit exceed ", Toast.LENGTH_LONG).show(); // 2
                }
            });



          /*  if (spinResearchTypeList != null && spinResearchTypeList.size() > 0) {
                ArrayAdapter<String> researchtype = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strResearchArray) {
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

                researchtype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_research_type.setAdapter(researchtype);
            }*/
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

            //........................Age Group spinner
            spinAgeGroupArray.addAll(Arrays.asList(getResources().getStringArray(R.array.age_group)));
            if (spinAgeGroupArray!=null&&spinAgeGroupArray.size()>0) {
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinAgeGroupArray) {
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

                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_age_group.setAdapter(adapter2);
            }
            //........................Occupation Spinner

            ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinOccupationList) {
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
            spinner_occupation.setAdapter(adapter3);
            //............................Annual Income Spinner
            ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinAnnualIncomeList) {
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

            adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_annual_income.setAdapter(adapter4);
            //............................Other Brokers Spinner
            ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinOtherBrokerList) {
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

            adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_other_broker.setAdapter(adapter5);
            //.............................Lead Staus Array
            ArrayAdapter<String> adapter6 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinLeadStatusList) {
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

            adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_lead_status.setAdapter(adapter6);
            //.........................Duration Array
            ArrayAdapter<String> adapter7 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinDurationList) {
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

            adapter7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_duration.setAdapter(adapter7);

            if (spinLeadNameList != null && spinLeadNameList.size() > 0) {
                adapter8 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strLeadNameArray) {
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

                adapter8.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_lead_name.setAdapter(adapter8);
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

    private void populateData(LeadInfoModel leadInfoModel){
        try{

            if(leadInfoModel != null){
                customer_id.setText(leadInfoModel.getCustomerID());
                fname.setText(leadInfoModel.getFirstname());
                mname.setText(leadInfoModel.getMiddlename());
                lname.setText(leadInfoModel.getLastname());
                mobileno.setText(leadInfoModel.getMobile_no());
                email.setText(leadInfoModel.getEmail_id());
                location.setText(leadInfoModel.getLocation());
                editCity.setText(leadInfoModel.getCity());
                autoPincode.setText(leadInfoModel.getPincode());
                date_of_birth.setText(leadInfoModel.getDob());
                address.setText(leadInfoModel.getAddress1());
                flat.setText(leadInfoModel.getAddress2());
                street.setText(leadInfoModel.getAddress3());
                margin.setText(leadInfoModel.getB_margin());
                aum.setText(leadInfoModel.getB_aum());
                sip.setText(leadInfoModel.getB_sip());
                number.setText(leadInfoModel.getB_number());
                value.setText(leadInfoModel.getB_value());
                premium.setText(leadInfoModel.getB_value());

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                String lead__id1 = sharedPref.getString("lead__Id","");
                String meeting_date=sharedPref.getString("meeting_date","");
                String meeting_agenda=sharedPref.getString("meeting_agenda", "");
                if(leadId.equals(lead__id1)){
                    next_metting_date.setText(meeting_date);
                    metting_agenda.setText(meeting_agenda);
                }else {
                    next_metting_date.setText(leadInfoModel.getMeetingdt());
                    metting_agenda.setText(leadInfoModel.getMeetingagenda());
                }

                lead_update_log.setText(leadInfoModel.getLead_updatelog());
                reason.setText(leadInfoModel.getReason());
                remark.setText(leadInfoModel.getRemark());
                compitator.setText(leadInfoModel.getCompitatorname());
                product.setText(leadInfoModel.getProduct());
                last_meeting_dt.setText(leadInfoModel.getLast_meeting_date());
                last_meeting_update.setText(leadInfoModel.getLast_meeting_update());
                pan_no.setText(leadInfoModel.getPanno());

                if (leadInfoModel.getSource_of_lead() != null && leadInfoModel.getSource_of_lead().trim().length() > 0) {
                    spinner_source_of_lead.setSelection(spinSourceLeadList.indexOf(leadInfoModel.getSource_of_lead()) + 1);
                }

                if (leadInfoModel.getSub_source() != null && leadInfoModel.getSub_source().trim().length() > 0) {
                    spinner_sub_source.setSelection(spinSubSourceLeadList.indexOf(leadInfoModel.getSub_source()) + 1);
                }
                if (leadInfoModel.getAge() != null && leadInfoModel.getAge().trim().length() > 0) {
                    spinner_age_group.setSelection(spinAgeGroupArray.indexOf(leadInfoModel.getAge()));
                }
                if (leadInfoModel.getOccupation() != null && leadInfoModel.getOccupation().trim().length() > 0) {
                    spinner_occupation.setSelection(spinOccupationList.indexOf(leadInfoModel.getOccupation()));
                }
                if (leadInfoModel.getAnnual_income() != null && leadInfoModel.getAnnual_income().trim().length() > 0) {
                    spinner_annual_income.setSelection(spinAnnualIncomeList.indexOf(leadInfoModel.getAnnual_income()));
                }
                if (leadInfoModel.getBrokerdetls() != null && leadInfoModel.getBrokerdetls().trim().length() > 0) {
                    spinner_other_broker.setSelection(spinOtherBrokerList.indexOf(leadInfoModel.getBrokerdetls()));
                }
                if (leadInfoModel.getLeadstatus() != null && leadInfoModel.getLeadstatus().trim().length() > 0) {
                    spinner_lead_status.setSelection(spinLeadStatusList.indexOf(leadInfoModel.getLeadstatus()));
                }
                if (leadInfoModel.getDuration() != null && leadInfoModel.getDuration().trim().length() > 0) {
                    spinner_duration.setSelection(spinDurationList.indexOf(leadInfoModel.getDuration()));
                }


                if (leadInfoModel.getMeetingstatus() != null && leadInfoModel.getMeetingstatus().equalsIgnoreCase("Contacted")) {
                    rb_contact.setChecked(true);
                } else if (leadInfoModel.getMeetingstatus() != null && leadInfoModel.getMeetingstatus().equalsIgnoreCase("Not Contacted")) {
                    rb_not_contact.setChecked(true);
                }
                String typeofsearch = leadInfoModel.getTypeofsearch();
                List<String> selItemsPos = new ArrayList<>();
                if (leadInfoModel.getTypeofsearch() != null && leadInfoModel.getTypeofsearch().length() > 0){
                    if (typeofsearch.contains("#")){
                        String selectedItem[] = typeofsearch.split("#");
                        if (selectedItem != null && selectedItem.length > 0){
                            for (int j = 0; j<selectedItem.length;j++){
                                selItemsPos.add(selectedItem[j]);
                            }
                        }

                    }else{
                        selItemsPos.add(typeofsearch);
                    }


                    final List<KeyPairBoolData> listArray = new ArrayList<>();

                    for (int i = 0; i < categoryDetailsModelList.size(); i++) {
                        KeyPairBoolData h = new KeyPairBoolData();
                        h.setId(i + 1);
                        h.setName(spinResearchTypeList.get(i));
                        for (int j = 0 ; j < selItemsPos.size(); j++){
                            if (selItemsPos.get(j).trim().equals(String.valueOf(i+1))){
                                h.setSelected(true);
                                break;
                             }else{
                                h.setSelected(false);
                             }

                        }
                        listArray.add(h);

                    }

                    spinner_research_type.setItems(listArray, -1, spineListener);


                }
                String businessOpp = leadInfoModel.getBusiness_opp();
                if (leadInfoModel.getBusiness_opp() != null && leadInfoModel.getBusiness_opp().length() > 0) {
                    if (businessOpp.contains("#")) {
                        String selectedItem[] = businessOpp.split("#");
                        if (selectedItem != null && selectedItem.length > 0) {
                            for (int j = 0; j < selectedItem.length; j++) {
                                selItemsPosition.add(selectedItem[j]);
                            }
                            String name = + selItemsPosition.size() + " is Selected";
                            btn1_opportunity_pitched2.setText(name);
                        }

                    } else {
                        selItemsPosition.add(businessOpp);
                    }

                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public LeadInfoModel createLeadInfoModel(Cursor cursor) {

        leadInfoModel = new LeadInfoModel();
        try {
            leadInfoModel.setStages(cursor.getString(cursor.getColumnIndex("stages")));
            leadInfoModel.setLead_id(cursor.getString(cursor.getColumnIndex("lead_id")));
            leadInfoModel.setDirect_lead_id(cursor.getInt(cursor.getColumnIndex("direct_lead_id")));
            leadInfoModel.setSource_of_lead(cursor.getString(cursor.getColumnIndex("source_of_lead")));
            leadInfoModel.setSub_source(cursor.getString(cursor.getColumnIndex("sub_source")));
            leadInfoModel.setCustomerID(cursor.getString(cursor.getColumnIndex("customer_id")));
            leadInfoModel.setFirstname(cursor.getString(cursor.getColumnIndex("fname")));
            leadInfoModel.setMiddlename(cursor.getString(cursor.getColumnIndex("mname")));
            leadInfoModel.setLastname(cursor.getString(cursor.getColumnIndex("lname")));
            leadInfoModel.setDob(cursor.getString(cursor.getColumnIndex("dob")));
            leadInfoModel.setAge(cursor.getString(cursor.getColumnIndex("age")));
            leadInfoModel.setMobile_no(cursor.getString(cursor.getColumnIndex("mobile_no")));
            leadInfoModel.setAddress1(cursor.getString(cursor.getColumnIndex("address_1")));
            leadInfoModel.setAddress2(cursor.getString(cursor.getColumnIndex("address_2")));
            leadInfoModel.setAddress3(cursor.getString(cursor.getColumnIndex("address_3")));
            leadInfoModel.setLocation(cursor.getString(cursor.getColumnIndex("location")));
            leadInfoModel.setCity(cursor.getString(cursor.getColumnIndex("city")));
            leadInfoModel.setPincode(cursor.getString(cursor.getColumnIndex("pincode")));
            leadInfoModel.setEmail_id(cursor.getString(cursor.getColumnIndex("email_id")));
            leadInfoModel.setAnnual_income(cursor.getString(cursor.getColumnIndex("annual_income")));
            leadInfoModel.setOccupation(cursor.getString(cursor.getColumnIndex("occupation")));
            leadInfoModel.setCreatedfrom(cursor.getString(cursor.getColumnIndex("created_from")));
            leadInfoModel.setAppversion(cursor.getString(cursor.getColumnIndex("app_version")));
            leadInfoModel.setAppdt(cursor.getString(cursor.getColumnIndex("app_dt")));
            leadInfoModel.setFlag(cursor.getString(cursor.getColumnIndex("flag")));
            leadInfoModel.setAllocateduserid(cursor.getString(cursor.getColumnIndex("allocated_user_id")));
            leadInfoModel.setBrokerdetls(cursor.getString(cursor.getColumnIndex("other_broker_dealt_with")));
            leadInfoModel.setMeetingstatus(cursor.getString(cursor.getColumnIndex("meeting_status")));
            leadInfoModel.setLeadstatus(cursor.getString(cursor.getColumnIndex("lead_status")));
            leadInfoModel.setCompitatorname(cursor.getString(cursor.getColumnIndex("competitor_name")));
            leadInfoModel.setProduct(cursor.getString(cursor.getColumnIndex("product")));
            leadInfoModel.setRemark(cursor.getString(cursor.getColumnIndex("remarks")));
            leadInfoModel.setTypeofsearch(cursor.getString(cursor.getColumnIndex("typeofsearch")));
            leadInfoModel.setDuration(cursor.getString(cursor.getColumnIndex("duration")));
            leadInfoModel.setPanno(cursor.getString(cursor.getColumnIndex("pan_no")));
            leadInfoModel.setB_margin(cursor.getString(cursor.getColumnIndex("b_margin")));
            leadInfoModel.setB_aum(cursor.getString(cursor.getColumnIndex("b_aum")));
            leadInfoModel.setB_sip(cursor.getString(cursor.getColumnIndex("b_sip")));
            leadInfoModel.setB_number(cursor.getString(cursor.getColumnIndex("b_number")));
            leadInfoModel.setB_value(cursor.getString(cursor.getColumnIndex("b_value")));
            leadInfoModel.setB_premium(cursor.getString(cursor.getColumnIndex("b_premium")));
            leadInfoModel.setReason(cursor.getString(cursor.getColumnIndex("reason")));
            leadInfoModel.setMeetingdt(cursor.getString(cursor.getColumnIndex("next_meeting_date")));
            leadInfoModel.setMeetingagenda(cursor.getString(cursor.getColumnIndex("meeting_agenda")));
            leadInfoModel.setLead_updatelog(cursor.getString(cursor.getColumnIndex("lead_update_log")));
            leadInfoModel.setCreatedby(cursor.getString(cursor.getColumnIndex("created_by")));
            leadInfoModel.setCreateddt(cursor.getString(cursor.getColumnIndex("created_dt")));
            leadInfoModel.setUpdateddt(cursor.getString(cursor.getColumnIndex("updated_dt")));
            leadInfoModel.setUpdatedby(cursor.getString(cursor.getColumnIndex("updated_by")));
            leadInfoModel.setLast_meeting_date(cursor.getString(cursor.getColumnIndex("last_meeting_date")));
            leadInfoModel.setEmpcode(cursor.getString(cursor.getColumnIndex("emp_code")));
            leadInfoModel.setLast_meeting_update(cursor.getString(cursor.getColumnIndex("last_meeting_update")));
            leadInfoModel.setBusiness_opp(cursor.getString(cursor.getColumnIndex("business_opportunity")));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return leadInfoModel;

    }
    //.......................Client Details Model
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
                spinner_custID.setAdapter(adapter3);
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
                customer_id.setText(clientDetailsModel.getClientID());
                customer_id.setEnabled(false);
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
    //..........................product data....................
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
                object = webService.SaveCategory(leadId, selectedCatId, empcode,fromType);

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
    private void updateInDb() {

        try {
            int directLeadId;
//            lead_id_info = "";
            String strLastSync = "0";
            if (cliked){
                strStages="closer of lead";
                strFlag="D";
            }else {
                strStages = "Lead Updated";
                strFlag = "U";}

            directLeadId = leadInfoModel.getDirect_lead_id();

            // WHERE   clause
            String selection = "lead_id" + " = ?";

            // WHERE clause arguments
            String[] selectionArgs = {leadId};

            // for now strcurrency becomes blank as " "; please change it later
            String valuesArray[] = { "" + directLeadId,leadId,strStages, str_spinner_source_of_lead, str_spinner_sub_source, str_cust_id, str_fname, str_mname, str_lname,
                    str_date_of_birth, str_spinner_age_group, str_mobile_no, str_address, str_flat, str_street, str_laocion, str_city, str_pincode, str_email, str_spinner_annual_income,
                    str_spinner_occupation, strCreatedfrom, strAppVersion, strAppdt, strFlag, strAllocated_userid, str_spinner_other_broker,
                    str_rg_meeting_status, str_spinner_lead_status, strCompitator_Name, strProduct, strRemark, str_spinner_research_type,
                    str_spinner_duration, strPanNo, strB_Margin, strB_aum, strB_sip, strB_number, strB_value, strB_premium, str_reason,
                    str_next_meeting_date, str_metting_agenda, str_lead_update_log, strCreatedby, strCreateddt, strUpdateddt, strUpdatedby,strEmpCode,
                    strLastMeetingDate,strLastMeetingUpdate,selectedCatId};


            boolean result = Narnolia.dbCon.update(DbHelper.TABLE_DIRECT_LEAD, selection, valuesArray, utils.columnNamesLeadUpdate, selectionArgs);


            if (result) {
                if (strFlag=="D"){
                  //  displayMessage("Close Lead Data inserted");
                }else if (strFlag=="U") {

                    displayMessage("Data Inserted Succesfully");
                }



                pushActivity(mContext, HomeActivity.class, null, true);
            }

        } catch (
                Exception e
                )

        {
            e.printStackTrace();
        }
    }
    //............................................................................................
    public class UpdateLeadData extends AsyncTask<String, Void, SoapPrimitive> {

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
            if (cliked){
                strStages="closer of lead";
                strFlag="D";
            }else {
                strStages = "Lead Updated";
                strFlag = "U";
            }

            SoapPrimitive object = webService.UpdateLead(leadId,str_spinner_source_of_lead, str_spinner_sub_source,str_cust_id, str_fname, str_mname, str_lname, str_mobile_no,
                    str_email, str_spinner_age_group, str_date_of_birth,str_address,str_flat,str_street,str_laocion ,str_city,str_pincode,str_spinner_occupation,str_spinner_annual_income,str_spinner_other_broker,str_rg_meeting_status,str_spinner_lead_status,strRemark,strCompitator_Name,strProduct,
                    str_spinner_research_type,str_spinner_duration,strPanNo,str_reason,strB_Margin,strB_aum,strB_sip,strB_number
                    ,strB_value,strB_premium,str_next_meeting_date,str_metting_agenda,str_lead_update_log,strFlag,"",empcode,strLastMeetingDate,strLastMeetingUpdate,"1","","","");

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
                    if (strFlag=="D"){
                    displayMessage("Lead Closed Sucessfully");
                }else if (strFlag=="U") {

                    displayMessage("Lead Updated Successfully");
                }
                    updateInDb();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                new SaveResearch().execute();
                new SaveCategory().execute();
            }

        }
    }

    public class SaveResearch extends AsyncTask <String, Void, SoapPrimitive> {

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
                object = webService.SaveResearch(leadId,str_spinner_research_type, empcode,fromType);

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
}

