package com.narnolia.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.narnolia.app.dbconfig.DbHelper;

/**
 * Created by Admin on 03-11-2016.
 */

public class SharedPref {

    private Context context;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private static final String KEY_CHANNEL = "key_channel";
    private static final String KEY_Designation = "key_designation";
    private static final String KEY_DusraEmail = "key_dusraemail";
    private static final String KEY_DusraMobile = "key_dusramobile";
    private static final String KEY_UserPass = "key_pass";
    private static final String KEY_Email = "key_email";

    private static final String KEY_versionName = "key_versionName";
    private static final String KEY_UserFrom = "key_userfrom";
    private static final String KEY_lat = "key_lat";
    private static final String KEY_lang = "key_lang";
    private static final String KEY_attendence = "key_attendence";
    private static final String KEY_fromDate = "key_fromDate";
    private static final String Key_Location="key_location";
    private static final String KEY_LoginId = "key_loginid";
    private static final String KEY_Mobile = "key_mobile";
    private static final String KEY_Result = "key_result";
    private static final String KEY_Status = "key_status";
    private static final String KEY_UserId = "key_userid";
    private static final String KEY_RM_ID= "key_rm_id";
    private static final String KEY_NODATA = "key_nodata";
    private static final String KEY_ProsalRefId="key_proposalrefid";
    private static final String  KEY_PersonId="key_personId";
    private static final String IS_SD_CARD_PERMISSION = "permission_granted";

    public SharedPref(Context _ctx) {
        context = _ctx;
        sharedPref = _ctx.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void setSharedPrefLogin(String Email,String LoginId, String Mobile, String Result, String Status,
                                   String UserId,String rm_id) {

        editor.putString(KEY_Email, Email);
        editor.putString(KEY_LoginId, LoginId);
        editor.putString(KEY_Mobile, Mobile);
        editor.putString(KEY_Result, Result);
        editor.putString(KEY_Status, Status);
        editor.putString(KEY_UserId, UserId);
        editor.putString(KEY_RM_ID,rm_id);
        editor.commit();
    }


    public void clearPref() {
        try {
            editor.remove(KEY_Result);
            editor.remove(KEY_LoginId);
            editor.remove(KEY_CHANNEL);
            editor.remove((KEY_ProsalRefId));
            editor.remove(KEY_PersonId);
            editor.commit();
            editor.clear();
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearResult() {
        try {
            editor.remove(KEY_Result);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setResult(String result) {
        try {
            editor.putString(KEY_Result, result);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public boolean checkForLoginData(Context mContext, String userName) {
        Boolean result = false;
        try {

            String whereClause = " where " + mContext.getString(R.string.column_userId) + " = '" + getUserId() + "' AND " + mContext.getString(R.string.column_username) + " = '" + userName + "'";
            Cursor cursor = Narnolia.dbCon.fetchFromSelect(DbHelper.TABLE_USER_DETAIL, whereClause);


            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                setResult(cursor.getString(cursor.getColumnIndex(mContext.getString(R.string.column_result))));
                result = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public void setChannel(String channel) {
        editor.putString(KEY_CHANNEL, channel);
        editor.commit();
    }

    public String getChannel() {
        String result = sharedPref.getString(KEY_CHANNEL, "");
        return result;
    }

    public String getLoginResult() {
        return sharedPref.getString(KEY_Result, "false");
    }

    public String getLoginId() {
        String loginId = sharedPref.getString(KEY_LoginId, "");
        return loginId;
    }

    public String getUserId() {
        String userId = sharedPref.getString(KEY_UserId, "");
        return userId;
    }

    public String getEmail() {
        String email = sharedPref.getString(KEY_Email, "");
        return email;
    }
    public String getMobile() {
        String mobile = sharedPref.getString(KEY_Mobile, "");
        return mobile;
    }


    public String getUserPass() {
        String pass = sharedPref.getString(KEY_UserPass, "");
        return pass;
    }

    public String getStatus() {
        String status = sharedPref.getString(KEY_Status, "");
        return status;
    }

    public String getApp() {
        String from = sharedPref.getString(KEY_UserFrom, "");
        return from;
    }
    public String getVersion_name() {
        String version = sharedPref.getString(KEY_versionName, "");
        return version;
    }
    public String getLat() {
        String lat = sharedPref.getString(KEY_lat, "");
        return lat;
    }
    public String getLang() {
        String lang = sharedPref.getString(KEY_lang, "");
        return lang;
    }
    public String getAttendence() {
        String attend = sharedPref.getString(KEY_attendence, "");
        return attend;
    }
    public String getCurrentDate() {
        String currDate = sharedPref.getString(KEY_fromDate, "");
        return currDate;
    }
    public String getKey_Location(){
        String loc=sharedPref.getString(Key_Location,"");
        return loc;
    }

    public void setKeyNodata(boolean isData) {
        editor.putBoolean(KEY_NODATA, isData);
        editor.commit();
    }

    public boolean getKeyNoData() {
        boolean result = sharedPref.getBoolean(KEY_NODATA, false);
        return result;
    }

    public void setProposerId(String result) {
        try {
            editor.putString(KEY_ProsalRefId, result);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String getProposerId() {
        String userId = sharedPref.getString(KEY_ProsalRefId, "");
        return userId;
    }

    public void setPersonId(String result) {
        try {
            editor.putString(KEY_PersonId, result);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPersonId() {
        String personId = sharedPref.getString(KEY_PersonId, "");
        return personId;
    }

    public void setSDCardPermission(boolean flag_sd_card_permission_granted) {
        editor.putBoolean(IS_SD_CARD_PERMISSION, flag_sd_card_permission_granted);
        editor.commit();
    }

    public boolean isSDCardPermission() {
        return sharedPref.getBoolean(IS_SD_CARD_PERMISSION, false);
    }

    public String getIsRM() {
        String isRM = sharedPref.getString(KEY_RM_ID, "");
        return isRM;
    }



    public void setSharedPrefLoginWithPass(String loginId,String pass, String status, String userFrom, String versionName, String lat, String lang, String attendence, String fromDate,String loc) {
        editor.putString(KEY_LoginId, loginId);
        editor.putString(KEY_UserPass, pass);
        editor.putString(KEY_Status, status);
        editor.putString(KEY_UserFrom, userFrom);
        editor.putString(KEY_versionName, versionName);
        editor.putString(KEY_lat, lat);
        editor.putString(KEY_lang, lang);
        editor.putString(KEY_attendence, attendence);
        editor.putString(KEY_fromDate, fromDate);
        editor.putString(Key_Location,loc);
        editor.commit();
    }
}
