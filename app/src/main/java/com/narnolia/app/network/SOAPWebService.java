package com.narnolia.app.network;

import android.content.Context;
import android.util.Log;

import com.narnolia.app.libs.Utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by Admin on 03-11-2016.
 */

public class SOAPWebService {

    Context context;
    /**
     * Variable Decleration................
     */
    String NAMESPACE = "http://tempuri.org/";
    String SOAP_ACTION = "http://tempuri.org/IService1/";
    String SOAP_ACTION_METHOD = "IService1";

    public SOAPWebService(Context con) {
        context = con;
    }

    public SoapPrimitive SaveLead(String lead_source, String sub_source, String empcode, String fname, String mname,
                                  String lname, String mobileno, String location, String city,
                                  String pincode, String app_version, String Stages, String createdby, String app_date,
                                  String createdFrom, String L_flag, String allocated_user_id){

        SoapPrimitive result = null;

        try{
            SoapObject request = new SoapObject(NAMESPACE,"SaveLead");

            request.addProperty("lead_source",lead_source);
            request.addProperty("sub_source",sub_source);
            request.addProperty("empcode",empcode);
            request.addProperty("fname",fname);
            request.addProperty("mname",mname);
            request.addProperty("lname",lname);
            request.addProperty("mobileno",mobileno);
            request.addProperty("location",location);
            request.addProperty("city",city);
            request.addProperty("pincode",pincode);
            request.addProperty("app_version",app_version);
            request.addProperty("Stages",Stages);
            request.addProperty("createdby",createdby);
            request.addProperty("app_date",app_date);
            request.addProperty("createdFrom",createdFrom);
            request.addProperty("L_flag",L_flag);
            request.addProperty("allocated_user_id",allocated_user_id);



            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope.setOutputSoapObject(request); // set request object
            envelope.dotNet = true;

            HttpTransportSE androidHttpTransport = new HttpTransportSE(Utils.URL);// http
            // transport
            // call
            androidHttpTransport.call(SOAP_ACTION + "SaveLead", envelope);
            Log.i("TAG", "envelope" + envelope);

            // response soap object
            result = (SoapPrimitive) envelope.getResponse();
            Log.i("TAG", "response :" + result);

            return result;

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
//.........................
    public SoapPrimitive UpdateLead(String lead_id,String lead_source, String sub_source,String customer_id, String fname, String mname,
                                  String lname, String mobileno, String emailid,String age,String dob,String address1,String address2,String address3
            ,String location,String city,String pincode,String occupation,String non_salary_val,String annual_income,String other_broker_dealt_with,String meeting_status,String Lead_Status,String remarks,
                                    String competitor_name, String product,String typeofsearch,String duration,String pan_no,String reason,String b_margin,String b_aum,String b_sip,String b_number,
                                    String b_value,String b_premium,String next_meeting_date,String meeting_agenda,String lead_update_log,String status_flag,String updatedby, String empcode,String last_meeting_date,String last_meeting_update,String app_version,String allocated_user_id,String updated_date,String duration_date ){
        SoapPrimitive result = null;
        try{SoapObject request = new SoapObject(NAMESPACE,"UpdateLead");
            request.addProperty("lead_id",lead_id);
            request.addProperty("lead_source",lead_source);
            request.addProperty("sub_source",sub_source);
            request.addProperty("customer_id",customer_id);
            request.addProperty("fname",fname);
            request.addProperty("mname",mname);
            request.addProperty("lname",lname);
            request.addProperty("mobileno",mobileno);
            request.addProperty("emailid",emailid);
            request.addProperty("age",age);
            request.addProperty("dob",dob);
            request.addProperty("address1",address1);
            request.addProperty("address2",address2);
            request.addProperty("address3",address3);
            request.addProperty("location",location);
            request.addProperty("city",city);
            request.addProperty("pincode",pincode);
            request.addProperty("occupation",occupation);
            request.addProperty("non_salary_val",non_salary_val);
            request.addProperty("annual_income",annual_income);
            request.addProperty("other_broker_dealt_with",other_broker_dealt_with);
            request.addProperty("meeting_status",meeting_status);
            request.addProperty("Lead_Status",Lead_Status);
            request.addProperty("remarks",remarks);
            request.addProperty("competitor_name",competitor_name);
            request.addProperty("product",product);
            request.addProperty("typeofsearch",typeofsearch);
            request.addProperty("duration",duration);
            request.addProperty("pan_no",pan_no);
            request.addProperty("reason",reason);
            request.addProperty("b_margin",b_margin);
            request.addProperty("b_aum",b_aum);
            request.addProperty("b_sip",b_sip);
            request.addProperty("b_number",b_number);
            request.addProperty("b_value",b_value);
            request.addProperty("b_premium",b_premium);
            request.addProperty("next_meeting_date",next_meeting_date);
            request.addProperty("meeting_agenda",meeting_agenda);
            request.addProperty("lead_update_log",lead_update_log);
            request.addProperty("status_flag",status_flag);
            request.addProperty("updatedby",updatedby);
            request.addProperty("empcode",empcode);
            request.addProperty("last_meeting_date",last_meeting_date);
            request.addProperty("last_meeting_update",last_meeting_update);
            request.addProperty("app_version",app_version);
            request.addProperty("allocated_user_id",allocated_user_id);
            request.addProperty("updated_date",updated_date);
            request.addProperty("duration_date",duration_date);




            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope.setOutputSoapObject(request); // set request object
            envelope.dotNet = true;

            HttpTransportSE androidHttpTransport = new HttpTransportSE(Utils.URL);// http
            // transport
            // call
            androidHttpTransport.call(SOAP_ACTION + "UpdateLead", envelope);
            Log.i("TAG", "envelope" + envelope);

            // response soap object
            result = (SoapPrimitive) envelope.getResponse();
            Log.i("TAG", "response :" + result);

            return result;

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    //...........................................................................

    public SoapPrimitive SaveCategory(String lead_id,String catId, String empCode,String fromtype){
        SoapPrimitive result = null;
        try{

            SoapObject request = new SoapObject(NAMESPACE,"savecategory");
            request.addProperty("lead_id",lead_id);
            request.addProperty("categoryid",catId);
            request.addProperty("empcode",empCode);
            request.addProperty("fromtype",fromtype);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope.setOutputSoapObject(request); // set request object
            envelope.dotNet = true;

            HttpTransportSE androidHttpTransport = new HttpTransportSE(Utils.URL);// http
            // transport
            // call
            androidHttpTransport.call(SOAP_ACTION + "savecategory", envelope);
            Log.i("TAG", "envelope" + envelope);

            // response soap object
            result = (SoapPrimitive) envelope.getResponse();
            Log.i("TAG", "response :" + result);

            return result;

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    //.............................................................

//...............................................

    public SoapObject GetMasterDetails() {
        SoapObject result2 = null;

        try {
            SoapObject request = new SoapObject(NAMESPACE,
                    "Parameter");// soap object

           // request.addProperty("date", date);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope.setOutputSoapObject(request); // set request object
            envelope.dotNet = true;

            HttpTransportSE androidHttpTransport = new HttpTransportSE(Utils.URL);// http
            // transport
            // call
            androidHttpTransport.call(SOAP_ACTION + "Parameter",
                    envelope);
            Log.i("TAG", "envelope" + envelope);

            // response soap object
            result2 = (SoapObject) envelope.getResponse();
            Log.i("TAG", "response :" + result2);

            return result2;
        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }

    }

    public SoapObject GetLead(String empcode) {
        SoapObject result3 = null;

        try {
//            SoapObject request = new SoapObject(NAMESPACE,
//                    "GetLead_Empwise");// soap object

            SoapObject request = new SoapObject(NAMESPACE,
                    "GetALLLead_Empwise");// soap object

            request.addProperty("empcode", empcode);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope.setOutputSoapObject(request); // set request object
            envelope.dotNet = true;

            HttpTransportSE androidHttpTransport = new HttpTransportSE(Utils.URL);// http
            // transport
            // call
            androidHttpTransport.call(SOAP_ACTION + "GetALLLead_Empwise",
                    envelope);
            Log.i("TAG", "envelope" + envelope);

            // response soap object
            result3 = (SoapObject) envelope.getResponse();
            Log.i("TAG", "response :" + result3);

            return result3;
        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }

    }
    //.................Save Reserach type......
    public SoapPrimitive SaveResearch(String lead_id,String research_id, String empCode,String fromtype){
        SoapPrimitive result4 = null;
        try{
            SoapObject request = new SoapObject(NAMESPACE,"saveresearch");
            request.addProperty("lead_id",lead_id);
            request.addProperty("research_id",research_id);
            request.addProperty("empcode",empCode);
            request.addProperty("fromtype",fromtype);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            HttpTransportSE androidHttpTransport = new HttpTransportSE(Utils.URL);
            androidHttpTransport.call(SOAP_ACTION + "saveresearch", envelope);
            Log.i("TAG", "envelope" + envelope);
            result4 = (SoapPrimitive) envelope.getResponse();
            Log.i("TAG", "response :" + result4);
            return result4;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



    //................get All Client.........
    public SoapObject Get_ALL_Client(String RMCode) {
        SoapObject result5 = null;

        try {
//            SoapObject request = new SoapObject(NAMESPACE,
//                    "GetLead_Empwise");// soap object

            SoapObject request = new SoapObject(NAMESPACE,
                    "get_ALL_client");// soap object

            request.addProperty("RMCode", RMCode);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope.setOutputSoapObject(request); // set request object
            envelope.dotNet = true;

            HttpTransportSE androidHttpTransport = new HttpTransportSE(Utils.URL);// http
            // transport
            // call
            androidHttpTransport.call(SOAP_ACTION + "get_ALL_client",
                    envelope);
            Log.i("TAG", "envelope" + envelope);
            // response soap object
            result5 = (SoapObject) envelope.getResponse();
            Log.i("TAG", "response :" + result5);
            return result5;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    public SoapObject FillDetail(String lead_id) {
        SoapObject result6 = null;

        try {
//
            SoapObject request = new SoapObject(NAMESPACE,
                    "filldetails");// soap object

            request.addProperty("lead_id", lead_id);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope.setOutputSoapObject(request); // set request object
            envelope.dotNet = true;

            HttpTransportSE androidHttpTransport = new HttpTransportSE(Utils.URL);// http
            // transport
            // call
            androidHttpTransport.call(SOAP_ACTION + "filldetails",
                    envelope);
            Log.i("TAG", "envelope" + envelope);

            // response soap object
            result6 = (SoapObject) envelope.getResponse();
            Log.i("TAG", "response :" + result6);

            return result6;
        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }

    }
    //...............GetcategoryType_data...........
    public SoapObject GetcategoryType_data(String lead_id) {
        SoapObject result7 = null;

        try {
//
            SoapObject request = new SoapObject(NAMESPACE,
                    "GetcategoryType_data");// soap object

            request.addProperty("lead_id", lead_id);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope.setOutputSoapObject(request); // set request object
            envelope.dotNet = true;

            HttpTransportSE androidHttpTransport = new HttpTransportSE(Utils.URL);// http
            // transport
            // call
            androidHttpTransport.call(SOAP_ACTION + "GetcategoryType_data",
                    envelope);
            Log.i("TAG", "envelope" + envelope);

            // response soap object
            result7 = (SoapObject) envelope.getResponse();
            Log.i("TAG", "response :" + result7);

            return result7;
        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }

    }
    //..................Get Message ........
    public SoapObject GetMessages(String emp,String roleid,String dates) {
        SoapObject result8 = null;

        try {
//
            SoapObject request = new SoapObject(NAMESPACE,
                    "GetMessages");// soap object

            request.addProperty("emp", emp);
            request.addProperty("roleid","2");//roleid
            request.addProperty("dates",dates);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope.setOutputSoapObject(request); // set request object
            envelope.dotNet = true;

            HttpTransportSE androidHttpTransport = new HttpTransportSE(Utils.URL);// http
            // transport
            // call
            androidHttpTransport.call(SOAP_ACTION + "GetMessages",
                    envelope);
            Log.i("TAG", "envelope" + envelope);

            // response soap object
            result8 = (SoapObject) envelope.getResponse();
            Log.i("TAG", "response :" + result8);

            return result8;
        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }

    }
//.........................
//....................fill Details app...............
public SoapObject Filldetailsforapp(String lead_id) {
    SoapObject result9 = null;

    try {
//
        SoapObject request = new SoapObject(NAMESPACE,
                "filldetailsforapp");// soap object

        request.addProperty("lead_id", lead_id);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);// soap envelop with version
        envelope.setOutputSoapObject(request); // set request object
        envelope.dotNet = true;

        HttpTransportSE androidHttpTransport = new HttpTransportSE(Utils.URL);// http
        // transport
        // call
        androidHttpTransport.call(SOAP_ACTION + "filldetailsforapp",
                envelope);
        Log.i("TAG", "envelope" + envelope);

        // response soap object
        result9 = (SoapObject) envelope.getResponse();
        Log.i("TAG", "response :" + result9);

        return result9;
    } catch (Exception e) {

        e.printStackTrace();

        return null;
    }

}
    //..................Get Status Report ........
    public SoapObject StatusReport(String empcode,String roleid) {
        SoapObject result10 = null;

        try {
//
            SoapObject request = new SoapObject(NAMESPACE,
                    "statusreport");// soap object

            request.addProperty("empcode", empcode);
            request.addProperty("role_id",roleid);//roleid


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope.setOutputSoapObject(request); // set request object
            envelope.dotNet = true;

            HttpTransportSE androidHttpTransport = new HttpTransportSE(Utils.URL);// http
            // transport
            // call
            androidHttpTransport.call(SOAP_ACTION + "statusreport",
                    envelope);
            Log.i("TAG", "envelope" + envelope);

            // response soap object
            result10 = (SoapObject) envelope.getResponse();
            Log.i("TAG", "response :" + result10);

            return result10;
        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }

    }

    //..................Get Status Report ........
    public SoapObject SubStatusReport(String empcode,String lead_status,String CreatedDate,String role_id) {
        SoapObject result11 = null;

        try {
//
            SoapObject request = new SoapObject(NAMESPACE,
                    "sub_status_report");// soap object

            request.addProperty("empcode", empcode);
            request.addProperty("lead_status", "Lead Created");//"Lead Created"
            request.addProperty("created_date", CreatedDate);
            request.addProperty("role_id",role_id);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope.setOutputSoapObject(request); // set request object
            envelope.dotNet = true;

            HttpTransportSE androidHttpTransport = new HttpTransportSE(Utils.URL);// http
            // transport
            // call
            androidHttpTransport.call(SOAP_ACTION + "sub_status_report",
                    envelope);
            Log.i("TAG", "envelope" + envelope);

            // response soap object
            result11 = (SoapObject) envelope.getResponse();
            Log.i("TAG", "response :" + result11);

            return result11;
        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }

    }
    //..................frogot Password ........
    public SoapObject ForgotPassword(String user_id) {
        SoapObject result12 = null;

        try {
//
            SoapObject request = new SoapObject(NAMESPACE,
                    "get_Useremailid");// soap object

            request.addProperty("userid", user_id);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope.setOutputSoapObject(request); // set request object
            envelope.dotNet = true;

            HttpTransportSE androidHttpTransport = new HttpTransportSE(Utils.URL);// http
            // transport
            // call
            androidHttpTransport.call(SOAP_ACTION + "get_Useremailid",
                    envelope);
            Log.i("TAG", "envelope" + envelope);

            // response soap object
            result12 = (SoapObject) envelope.getResponse();
            Log.i("TAG", "response :" + result12);

            return result12;
        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }

    }
    //..................Get Attendence Report ........
    public SoapObject Attendence_Report_Monthwise(String role_id1,String emp_code1,String Month,String year,String attendance) {
        SoapObject result13 = null;

        try {
//
            SoapObject request = new SoapObject(NAMESPACE,
                    "attendance_report_monthwise");// soap object

            request.addProperty("role_id", role_id1);
            request.addProperty("emp_code",emp_code1);
            request.addProperty("Month", Month);
            request.addProperty("year",year);
            request.addProperty("attendance",attendance);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope.setOutputSoapObject(request); // set request object
            envelope.dotNet = true;

            HttpTransportSE androidHttpTransport = new HttpTransportSE(Utils.URL);// http
            // transport
            // call
            androidHttpTransport.call(SOAP_ACTION + "attendance_report_monthwise",
                    envelope);
            Log.i("TAG", "envelope" + envelope);

            // response soap object
            result13 = (SoapObject) envelope.getResponse();
            Log.i("TAG", "response :" + result13);

            return result13;
        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }

    }

    public SoapObject Attendence_Report_Datewise(String emp_code11,String current_date) {
        SoapObject result14 = null;

        try {
//
            SoapObject request = new SoapObject(NAMESPACE,
                    "attendance_report_datewise");// soap object
            request.addProperty("emp_code",emp_code11);
            request.addProperty("date", current_date);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope.setOutputSoapObject(request); // set request object
            envelope.dotNet = true;

            HttpTransportSE androidHttpTransport = new HttpTransportSE(Utils.URL);// http
            // transport
            // call
            androidHttpTransport.call(SOAP_ACTION + "attendance_report_datewise",
                    envelope);
            Log.i("TAG", "envelope" + envelope);

            // response soap object
            result14 = (SoapObject) envelope.getResponse();
            Log.i("TAG", "response :" + result14);

            return result14;
        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }

    }
//.............. This web servicee is used for getting status report spinner data........
    public SoapObject get_Geographicalhierarchy(String user_id,String param) {
        SoapObject result15 = null;

        try {
//
            SoapObject request = new SoapObject(NAMESPACE,
                    "get_Geographicalhierarchy");// soap object
            request.addProperty("userid",user_id);
            request.addProperty("param", param);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope.setOutputSoapObject(request); // set request object
            envelope.dotNet = true;

            HttpTransportSE androidHttpTransport = new HttpTransportSE(Utils.URL);// http
            // transport
            // call
            androidHttpTransport.call(SOAP_ACTION + "get_Geographicalhierarchy",
                    envelope);
            Log.i("TAG", "envelope" + envelope);

            // response soap object
            result15 = (SoapObject) envelope.getResponse();
            Log.i("TAG", "response :" + result15);

            return result15;
        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }

    }
    public SoapObject get_emplist(String login_id, String national, String zone, String region, String location, String cluster) {
        SoapObject result16 = null;

        try {
//
            SoapObject request = new SoapObject(NAMESPACE,
                    "get_emplist");// soap object
            request.addProperty("login_id",login_id);
            request.addProperty("national", national);
            request.addProperty("zone", zone);
            request.addProperty("region", region);
            request.addProperty("location", location);
            request.addProperty("cluster", cluster);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope.setOutputSoapObject(request); // set request object
            envelope.dotNet = true;

            HttpTransportSE androidHttpTransport = new HttpTransportSE(Utils.URL);// http
            // transport
            // call
            androidHttpTransport.call(SOAP_ACTION + "get_emplist",
                    envelope);
            Log.i("TAG", "envelope" + envelope);

            // response soap object
            result16 = (SoapObject) envelope.getResponse();
            Log.i("TAG", "response :" + result16);

            return result16;
        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }

    }
}
