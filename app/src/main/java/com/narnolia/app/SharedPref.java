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
    private static final String KEY_Email = "key_email";
    private static final String KEY_FirstName = "key_firstname";
    private static final String KEY_LastName = "key_lastname";
    private static final String KEY_LoginId = "key_loginid";
    private static final String KEY_MiddleName = "key_middlename";
    private static final String KEY_Mobile = "key_mobile";
    private static final String KEY_Result = "key_result";
    private static final String KEY_Status = "key_status";
    private static final String KEY_UserId = "key_userid";
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
                                   String UserId) {

        editor.putString(KEY_Email, Email);
        editor.putString(KEY_LoginId, LoginId);
        editor.putString(KEY_Mobile, Mobile);
        editor.putString(KEY_Result, Result);
        editor.putString(KEY_Status, Status);
        editor.putString(KEY_UserId, UserId);
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
}
