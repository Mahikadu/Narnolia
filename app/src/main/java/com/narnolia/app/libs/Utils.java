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

    public String[] columnNamesMasterDetails = new String[50];


    public Utils(Context mContext) {
        this.mContext = mContext;

        String[] masterDetailsArray = {mContext.getString(R.string.column_masterdetails_id),mContext.getString(R.string.column_masterdetails_flag),
                mContext.getString(R.string.column_masterdetails_transfer),mContext.getString(R.string.column_masterdetails_lmd),
                mContext.getString(R.string.column_masterdetails_type),mContext.getString(R.string.column_masterdetails_value)};


        columnNamesMasterDetails= Arrays.copyOf(masterDetailsArray,masterDetailsArray.length);
    }



    public void logout(Context mContext) {
        SharedPref sharedPref = new SharedPref(mContext);
        sharedPref.clearResult();
    }
}
