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

public class LoginWebService {


    Context context;

    /**
     * Variable Decleration................
     */

    public LoginWebService(Context con) {
        context = con;
    }

    public SoapObject LoginLead(String username, String password, String loginfrom, String version, String latitude, String longitude, String attendance, String date, String location, String na) {
        SoapObject result1 = null;
        try {
            SoapObject request1 = new SoapObject("http://tempuri.org/",
                    "Login");// soap object

            request1.addProperty("username", username);
            request1.addProperty("password", password);
            request1.addProperty("loginfrom", loginfrom);
            request1.addProperty("version", version);
            request1.addProperty("latitude", latitude);
            request1.addProperty("longitude", longitude);
            request1.addProperty("attendance", attendance);
            request1.addProperty("date", date);
            request1.addProperty("Lat_location", location);
            request1.addProperty("Lon_Location", na);
            SoapSerializationEnvelope envelope1 = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope1.setOutputSoapObject(request1); // set request object
            envelope1.dotNet = true;

            HttpTransportSE androidHttpTransport = new HttpTransportSE(Utils.URL);// http
            // transport
            // call
            androidHttpTransport.call("http://tempuri.org/IService1/Login",
                    envelope1);
            Log.i("TAG", "envelope" + envelope1);

            // response soap object
            result1 = (SoapObject) envelope1.getResponse();


            return result1;
        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }
}
