package com.narnolia.app.network;

import android.content.Context;
import android.util.Log;

import com.narnolia.app.libs.Utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
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

    public SoapObject SaveLead(String lead_source,String sub_source, String fname, String mname, String lname,
                               String mobileno, String city, String pincode, String empcode, String app_date, String createdFrom,
                               String app_version, String createdby, String location){

       SoapObject result = null;

        try{
            SoapObject request = new SoapObject(NAMESPACE,"SaveLead");

            request.addProperty("lead_source",lead_source);
            request.addProperty("sub_source",sub_source);
            request.addProperty("fname",fname);
            request.addProperty("mname",mname);
            request.addProperty("lname",lname);
            request.addProperty("mobileno",mobileno);
            request.addProperty("city",city);
            request.addProperty("pincode",pincode);
            request.addProperty("empcode",empcode);
            request.addProperty("app_date",app_date);
            request.addProperty("createdFrom",createdFrom);
            request.addProperty("app_version",app_version);
            request.addProperty("createdby",createdby);
            request.addProperty("location",location);

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
            result = (SoapObject) envelope.getResponse();
            Log.i("TAG", "response :" + result);

            return result;

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

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

}
