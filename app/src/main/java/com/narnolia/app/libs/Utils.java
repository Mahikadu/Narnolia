package com.narnolia.app.libs;

import android.content.Context;

import com.narnolia.app.R;
import com.narnolia.app.SharedPref;

import java.util.Arrays;

/**
 * Created by Admin on 25-10-2016.
 */

public class Utils {
    private Context mContext;

    public static String URL = "http://sudesi.in/narnoliaws/Service1.svc";

    public static String KEY_LEAD_DATA = "lead_data";

    public String[] columnNamesMasterDetails = new String[50];
    public String[] columnNamesLead = new String[50];
    public String[] columnNamesLeadUpdate = new String[50];
    public String[] columnNamesClientDetails = new String[50];
    public String[] columnNamesDashboardUpdate=new String[50];


    public Utils(Context mContext) {
        this.mContext = mContext;

        String[] masterDetailsArray = {mContext.getString(R.string.column_masterdetails_id),mContext.getString(R.string.column_masterdetails_flag),
                mContext.getString(R.string.column_masterdetails_transfer),mContext.getString(R.string.column_masterdetails_lmd),
                mContext.getString(R.string.column_masterdetails_type),mContext.getString(R.string.column_masterdetails_value)};


        columnNamesMasterDetails= Arrays.copyOf(masterDetailsArray,masterDetailsArray.length);


        String[] leadArray = {mContext.getString(R.string.column_direct_lead_id),mContext.getString(R.string.column_lead_id),
                mContext.getString(R.string.column_stages),
                mContext.getString(R.string.column_source_of_lead),
                mContext.getString(R.string.column_sub_source), mContext.getString(R.string.column_customer_id),
                mContext.getString(R.string.column_firstname), mContext.getString(R.string.column_middlename),
                mContext.getString(R.string.column_lastname), mContext.getString(R.string.column_dob),
                mContext.getString(R.string.column_age), mContext.getString(R.string.column_mobile_no),
                mContext.getString(R.string.column_address1), mContext.getString(R.string.column_address2),
                mContext.getString(R.string.column_address3), mContext.getString(R.string.column_location),
                mContext.getString(R.string.column_city), mContext.getString(R.string.column_pincode),
                mContext.getString(R.string.column_email_id), mContext.getString(R.string.column_annual_income),
                mContext.getString(R.string.column_occupation), mContext.getString(R.string.column_created_from),
                mContext.getString(R.string.column_app_version), mContext.getString(R.string.column_app_dt),
                mContext.getString(R.string.column_flag), mContext.getString(R.string.column_allocated_user_id),
                mContext.getString(R.string.column_other_broker_dealt_with),
                mContext.getString(R.string.column_meeting_status), mContext.getString(R.string.column_lead_status),
                mContext.getString(R.string.column_competitor_name), mContext.getString(R.string.column_product),
                mContext.getString(R.string.column_remarks), mContext.getString(R.string.column_typeofsearch),
                mContext.getString(R.string.column_duration), mContext.getString(R.string.column_pan_no),
                mContext.getString(R.string.column_b_margin), mContext.getString(R.string.column_b_aum),
                mContext.getString(R.string.column_b_sip), mContext.getString(R.string.column_b_number),
                mContext.getString(R.string.column_b_value),
                mContext.getString(R.string.column_b_premium), mContext.getString(R.string.column_reason),
                mContext.getString(R.string.column_next_meeting_date), mContext.getString(R.string.column_meeting_agenda),
                mContext.getString(R.string.column_lead_update_log), mContext.getString(R.string.column_created_by),
                mContext.getString(R.string.column_created_dt), mContext.getString(R.string.column_updated_dt),
                mContext.getString(R.string.column_updated_by),mContext.getString(R.string.column_emp_code),
                mContext.getString(R.string.column_last_meeting_date), mContext.getString(R.string.column_last_meeting_update),mContext.getString(R.string.column_business_opportunity), mContext.getString(R.string.column_customer_id_name),};

        columnNamesLead = Arrays.copyOf(leadArray, leadArray.length);

        String[] updateLeadArray = {mContext.getString(R.string.column_direct_lead_id),mContext.getString(R.string.column_lead_id),
                mContext.getString(R.string.column_stages),
                mContext.getString(R.string.column_source_of_lead),
                mContext.getString(R.string.column_sub_source), mContext.getString(R.string.column_customer_id),
                mContext.getString(R.string.column_firstname), mContext.getString(R.string.column_middlename),mContext.getString(R.string.column_lastname), mContext.getString(R.string.column_dob),mContext.getString(R.string.column_age),
                mContext.getString(R.string.column_mobile_no),mContext.getString(R.string.column_address1), mContext.getString(R.string.column_address2),mContext.getString(R.string.column_address3), mContext.getString(R.string.column_location),mContext.getString(R.string.column_city), mContext.getString(R.string.column_pincode),
                mContext.getString(R.string.column_email_id), mContext.getString(R.string.column_annual_income),
                mContext.getString(R.string.column_occupation), mContext.getString(R.string.column_created_from),
                mContext.getString(R.string.column_app_version), mContext.getString(R.string.column_app_dt),
                mContext.getString(R.string.column_flag), mContext.getString(R.string.column_allocated_user_id),
                mContext.getString(R.string.column_other_broker_dealt_with),
                mContext.getString(R.string.column_meeting_status), mContext.getString(R.string.column_lead_status),
                mContext.getString(R.string.column_competitor_name), mContext.getString(R.string.column_product),
                mContext.getString(R.string.column_remarks), mContext.getString(R.string.column_typeofsearch),
                mContext.getString(R.string.column_duration), mContext.getString(R.string.column_pan_no),
                mContext.getString(R.string.column_b_margin), mContext.getString(R.string.column_b_aum),
                mContext.getString(R.string.column_b_sip), mContext.getString(R.string.column_b_number),
                mContext.getString(R.string.column_b_value),
                mContext.getString(R.string.column_b_premium), mContext.getString(R.string.column_reason),
                mContext.getString(R.string.column_next_meeting_date), mContext.getString(R.string.column_meeting_agenda),
                mContext.getString(R.string.column_lead_update_log), mContext.getString(R.string.column_created_by),
                mContext.getString(R.string.column_created_dt), mContext.getString(R.string.column_updated_dt),
                mContext.getString(R.string.column_updated_by),mContext.getString(R.string.column_emp_code),
                mContext.getString(R.string.column_last_meeting_date), mContext.getString(R.string.column_last_meeting_update),
                mContext.getString(R.string.column_business_opportunity),
                mContext.getString(R.string.column_customer_id_name),
        };

        columnNamesLeadUpdate = Arrays.copyOf(leadArray, updateLeadArray.length);
        String[] clientDetailsArray = {mContext.getString(R.string.Address),
                mContext.getString(R.string.AnnualIncome),
                mContext.getString(R.string.BirthDate),
                mContext.getString(R.string.Branchid),
                mContext.getString(R.string.City),
                mContext.getString(R.string.ClientCat),
                mContext.getString(R.string.ClientID),
                mContext.getString(R.string.ClientName),
                mContext.getString(R.string.EmailAddress),
                mContext.getString(R.string.MobileNumber),
                mContext.getString(R.string.PanNumber),
                mContext.getString(R.string.PinCode),
                mContext.getString(R.string.RMCode),
                mContext.getString(R.string.RMName),
                mContext.getString(R.string.StateId),
                mContext.getString(R.string.Telephonenumber),
                mContext.getString(R.string.Ucc),
                mContext.getString(R.string.bankaccount),
                mContext.getString(R.string.bankname),
                mContext.getString(R.string.cStatus),
                mContext.getString(R.string.dopeningDate),
                mContext.getString(R.string.dpac),
                mContext.getString(R.string.dpid),
                mContext.getString(R.string.ifsc),
                mContext.getString(R.string.micr),
                mContext.getString(R.string.result)};
        columnNamesClientDetails = Arrays.copyOf(clientDetailsArray, clientDetailsArray.length);
        String[] updateDashboardLeadArray = {mContext.getString(R.string.column_direct_lead_id),mContext.getString(R.string.column_lead_id),
                mContext.getString(R.string.column_stages),
                mContext.getString(R.string.column_source_of_lead),
                mContext.getString(R.string.column_sub_source), mContext.getString(R.string.column_customer_id),
                mContext.getString(R.string.column_firstname), mContext.getString(R.string.column_middlename),mContext.getString(R.string.column_lastname), mContext.getString(R.string.column_dob),mContext.getString(R.string.column_age),
                mContext.getString(R.string.column_mobile_no),mContext.getString(R.string.column_address1), mContext.getString(R.string.column_address2),mContext.getString(R.string.column_address3), mContext.getString(R.string.column_location),mContext.getString(R.string.column_city), mContext.getString(R.string.column_pincode),
                mContext.getString(R.string.column_email_id), mContext.getString(R.string.column_annual_income),
                mContext.getString(R.string.column_occupation), mContext.getString(R.string.column_created_from),
                mContext.getString(R.string.column_app_version), mContext.getString(R.string.column_app_dt),
                mContext.getString(R.string.column_flag), mContext.getString(R.string.column_allocated_user_id),
                mContext.getString(R.string.column_other_broker_dealt_with),
                mContext.getString(R.string.column_meeting_status), mContext.getString(R.string.column_lead_status),
                mContext.getString(R.string.column_competitor_name), mContext.getString(R.string.column_product),
                mContext.getString(R.string.column_remarks), mContext.getString(R.string.column_typeofsearch),
                mContext.getString(R.string.column_duration), mContext.getString(R.string.column_pan_no),
                mContext.getString(R.string.column_b_margin), mContext.getString(R.string.column_b_aum),
                mContext.getString(R.string.column_b_sip), mContext.getString(R.string.column_b_number),
                mContext.getString(R.string.column_b_value),
                mContext.getString(R.string.column_b_premium), mContext.getString(R.string.column_reason),
                mContext.getString(R.string.column_next_meeting_date), mContext.getString(R.string.column_meeting_agenda),
                mContext.getString(R.string.column_lead_update_log), mContext.getString(R.string.column_created_by),
                mContext.getString(R.string.column_created_dt), mContext.getString(R.string.column_updated_dt),
                mContext.getString(R.string.column_updated_by),mContext.getString(R.string.column_emp_code),
                mContext.getString(R.string.column_last_meeting_date), mContext.getString(R.string.column_last_meeting_update),
                mContext.getString(R.string.column_business_opportunity),
                mContext.getString(R.string.column_customer_id_name),
        };
        columnNamesDashboardUpdate = Arrays.copyOf(leadArray, updateDashboardLeadArray.length);
    }



    public void logout(Context mContext) {
        SharedPref sharedPref = new SharedPref(mContext);
        sharedPref.clearResult();
    }
}
