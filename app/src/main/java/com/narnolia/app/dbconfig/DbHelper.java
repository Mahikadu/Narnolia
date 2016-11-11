package com.narnolia.app.dbconfig;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.narnolia.app.R;

/**
 * Created by Admin on 27-10-2016.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Narnolia";

    public static final String TABLE_USER_DETAIL = "table_userDetail";
    public static final String TABLE_M_PARAMETER = "table_M_Parameter";
    public static final String TABLE_DIRECT_LEAD_CATEGORY_DTLS = "tabel_direct_lead_category_dtls";
    public static final String TABLE_DIRECT_LEAD = "table_Direct_lead";
    public static final String TABLE_M_CATEGORY = "table_M_Category";
    public static final String TABLE_DIRECT_LEAD_ST_DTLS = "table_direct_lead_st_dtls";
    public static  final String TABLE_M_PINCODE_TABLE = "pincode";

    public static final int DATABASE_VERSION = 2;
    private static DbHelper dbInstance = null;
    private static SQLiteDatabase db;

    private Context _ctxt;

    //execute db string

    public String strUserDetail = "CREATE TABLE " + TABLE_USER_DETAIL + " (userId VARCHAR(50), loginId VARCHAR(100)," +
            " email VARCHAR(50),mobile VARCHAR(50),result VARCHAR(50),status VARCHAR(50),username VARCHAR(50), " +
            "password VARCHAR(50))";

    private String strM_Parameter = "CREATE TABLE " + TABLE_M_PARAMETER + "(m_id INTEGER primary key, id VARCHAR(50)," +
            "flag VARCHAR(50),transfer VARCHAR(50),lmd VARCHAR(50), type VARCHAR(50), value VARCHAR(100))";

    private String strdirect_lead_category_dtls = "CREATE TABLE" + TABLE_DIRECT_LEAD_CATEGORY_DTLS + "(id INTEGER primary key," +
            "lead_id VARCHAR(50),producttype_id VARCHAR(50),category_id VARCHAR(50))";

    public String strDirect_lead = "CREATE TABLE " + TABLE_DIRECT_LEAD + " ( direct_lead_id INTEGER primary key," +
            "lead_id VARCHAR(50), stages VARCHAR(50), source_of_lead VARCHAR(500), sub_source VARCHAR(300)," +
            "customer_id VARCHAR(50),fname VARCHAR(50), mname VARCHAR(50), lname VARCHAR(50)," +
            "dob VARCHAR(50), age VARCHAR(50), mobile_no VARCHAR(50), address_1 VARCHAR(300), address_2 VARCHAR(300)," +
            "address_3 VARCHAR(300), location VARCHAR(300),city VARCHAR(100),pincode VARCHAR(50), email_id VARCHAR(50)," +
            "annual_income VARCHAR(50), occupation VARCHAR(50), " +
            "created_from VARCHAR(50), app_version VARCHAR(50), app_dt VARCHAR(50), channel VARCHAR(50), allocated_user_id VARCHAR(50)," +
            "other_broker_dealt_with VARCHAR(100), meeting_status VARCHAR(100), lead_status VARCHAR(50), competitor_name VARCHAR(50), product VARCHAR(50)," +
            "remarks VARCHAR(50), typeofsearch VARCHAR(50), duration VARCHAR(10), pan_no VARCHAR(50), b_margin VARCHAR(50), b_aum VARCHAR(50)," +
            "b_sip VARCHAR(50), b_number VARCHAR(50),b_value VARCHAR(50), b_premium VARCHAR(50),reason VARCHAR(50)),next_meeting_date VARCHAR(50)," +
            "meeting_agenda VARCHAR(50),lead_update_log VARCHAR(50),created_by VARCHAR(50),created_dt VARCHAR(50)," +
            "updated_dt VARCHAR(50),updated_by VARCHAR(50),business_opportunity VARCHAR(50))";

    private String strdirect_lead_st_dtls = "CREATE TABLE" + TABLE_M_CATEGORY + "(id INTEGER primary key," +
            "Produt_type_id VARCHAR(50),Category VARCHAR(50),Subcategory VARCHAR(50),Status VARCHAR(50))";

    private String strM_Category = "CREATE TABLE" + TABLE_DIRECT_LEAD_ST_DTLS + "(id INTEGER primary key," +
            "lead_id VARCHAR(50),ST_type_id VARCHAR(50))";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this._ctxt = context;
    }

    static synchronized DbHelper getInstance(Context ctx) {

        if (dbInstance == null) {
            dbInstance = new DbHelper(ctx);
        }
        return dbInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("TAG", "In on create db");
       /* db.execSQL(strM_Parameter);
        db.execSQL(strdirect_lead_category_dtls);
        db.execSQL(strDirect_lead);
        db.execSQL(strdirect_lead_st_dtls);
        db.execSQL(strM_Category);
        db.execSQL(strUserDetail);*/
    }

    // Open the database connection.
    public void open() {
        try {
            db = this.getWritableDatabase();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void close() {
        try {
            if (db != null && db.isOpen())
                db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
    }

    void closeCursor(Cursor cursor) {
        try {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ContentValues createContentValues(String values[], String names[]) {
        ContentValues values1 = null;
        try {
            values1 = new ContentValues();

            Log.e("name.length", String.valueOf(names.length));
            Log.e("values.length", String.valueOf(values.length));

            for (int i = 0; i < names.length; i++) {
                try {
                    if ((names[i].equalsIgnoreCase(_ctxt.getString(R.string.column_masterdetails_id)))) {
                        try {
                            int value = Integer.parseInt(values[i]);
                            values1.put(names[i], value);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        values1.put(names[i], values[i]);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return values1;
    }

    private ContentValues createContentValues1(String[] values, String names[]) {
        Log.e("prachi", "inside create content values");
        ContentValues values1 = null;
        try {
            values1 = new ContentValues();

            Log.e("Abc", "length" + names.length);


            for (int i = 0; i < names.length; i++) {

                String valueArray = values[i];
                Log.e("Prachi", "$$-->" + valueArray);
              /*  String nameArray1=names[i];
                Log.e("Prachi","inserted names"+nameArray1);*/
                values1.put(names[i], values[i]);
                Log.v("prachi", "value inserted");


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return values1;
    }

    long insert1(String values[], String names[], String tbl) {
        if (db != null && !db.isOpen())
            open();
        for (int i = 0; i < values.length; i++) {
            String values1 = values[i];
            Log.e("Prachi", "-->" + values1);
        }

        for (int i = 0; i < names.length; i++) {
            String names1 = names[i];
            Log.e("Prachi", "values" + names1);
        }

        ContentValues initialValues = createContentValues1(values, names);
        long inserted = 0;
        try {
            inserted = db.insert(tbl, null, initialValues);
            Log.e("prachi", "values inserted" + inserted);
        } catch (Exception e) {
        }
        return inserted;
    }

    long insert(String values[], String names[], String tbl) {
        if (db != null && !db.isOpen())
            open();

        ContentValues initialValues = createContentValues(values, names);
        long inserted = 0;
        try {
            inserted = db.insert(tbl, null, initialValues);
        } catch (Exception e) {
        }
        return inserted;
    }

    Cursor fetch(String tbl, String names[], String where, String args[], String order, String limit,
                 boolean isDistinct, String groupBy, String having) {

        if (db != null && !db.isOpen())
            open();
        //Cursor cur = null;
        //try {
        return db.query(true, tbl, names, where, args, groupBy, having, order, limit);
        /*} catch (Exception e) {
            return null;
        }*/
    }

    Cursor fetchLastRow(String tbl, String orderByCol, String args[]) {

        if (db != null && !db.isOpen())
            open();
        String where = null;
        String limit = "1";
        String order = orderByCol + " DESC";
        String groupBy = null;
        String having = null;
        String names[] = null;

       /* SELECT *
                FROM food_table
        ORDER BY _id DESC
        LIMIT 1*/

        //Cursor cur = null;
        //try {
        return db.query(false, tbl, names, where, args, groupBy, having, order, limit);
        /*} catch (Exception e) {
            return null;
        }*/
    }

    Cursor rawQuery(String query) {

        if (db != null && !db.isOpen())
            open();
        //Cursor cur = null;
        //try {
        return db.rawQuery(query, null);
        /*} catch (Exception e) {
            return null;
        }*/
    }

    boolean delete(String tbl, String where, String args[]) {

        if (db != null && !db.isOpen())
            open();

        boolean isDeleted = false;
        try {
            isDeleted = db.delete(tbl, where, args) > 0;
        } catch (Exception e) {
        }
        return isDeleted;
    }

    boolean update(String where, String values[], String names[], String tbl, String args[]) {

        if (db != null && !db.isOpen())
            open();

        ContentValues updateValues = createContentValues(values, names);

        boolean isUpdated = false;
        try {
            isUpdated = db.update(tbl, updateValues, where, args) > 0;

            if (!isUpdated) {
                long result = db.insert(tbl, null, updateValues);
                if (result > 0) {
                    isUpdated = true;
                }
            }
        } catch (Exception e) {
        }
        return isUpdated;
    }

    boolean alterTable(String tbl) {
        boolean isAlter = false;
        try {
            if (db != null && !db.isOpen())
                open();
            db.execSQL("DELETE FROM " + tbl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isAlter;
    }


    boolean updateBulk(String where, String values[], String names[], String tbl, String args[]) {

        if (db != null && !db.isOpen())
            open();
        ContentValues updateValues = createContentValues(values, names);
        Log.d("Update Query=>", updateValues.toString());

        boolean isUpdated = false;
        try {
            isUpdated = db.update(tbl, updateValues, where, args) > 0;

            if (!isUpdated) {

                long result = db.insert(tbl, null, updateValues);

                if (result > 0) {
                    isUpdated = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isUpdated;
    }

    public SQLiteStatement beginDBTransaction(String tableName, String names[]) {
        SQLiteStatement statement = null;
        try {
            if (db != null && !db.isOpen())
                open();
            String values = "";
            for (int i = 0; i < names.length; i++) {
                values = values + "?,";
            }

            if (values != null && values.length() > 0 && !(values.equalsIgnoreCase(""))) {
                values = values.substring(0, values.lastIndexOf(","));
            }
            String sql = "INSERT INTO " + tableName + " VALUES (" + values + ")";
            statement = db.compileStatement(sql);
            db.beginTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statement;
    }

    public void beginDBTransaction() {

        try {
            if (db != null && !db.isOpen())
                open();

            db.beginTransaction();
            Log.i("DB_APP", "In beginDBTransaction");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void endDBTransaction() {
        try {
            if (db == null || (db != null && !db.isOpen()))
                open();


            db.endTransaction();
            Log.i("DB_APP", "In endDBTransaction");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dbTransactionSucessFull() {
        try {
            if (db != null && !db.isOpen())
                open();
            db.setTransactionSuccessful();
            Log.i("DB_APP", "In dbTransactionSucessFull");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateServerStatus(String status) {
        try {
            if (db != null && !db.isOpen())
                open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getCountOfRows(String table, String whereClause, String[] whereArgs) {
        if (db != null && !db.isOpen())
            if (db != null && !db.isOpen())
                open();
        return DatabaseUtils.queryNumEntries(db, table, whereClause, whereArgs);
    }

    public long getCountOfRows(String tableName) {

        if (db != null && !db.isOpen())
            open();
        return DatabaseUtils.queryNumEntries(db, tableName);
    }

}
