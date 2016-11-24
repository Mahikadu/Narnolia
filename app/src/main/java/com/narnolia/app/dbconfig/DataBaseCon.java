package com.narnolia.app.dbconfig;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

/**
 * Created by Admin on 27-10-2016.
 */

public class DataBaseCon {

    private static DbHelper dbHelper;
    private static DataBaseCon dbConInstance = null;
    private Context context;

    private DataBaseCon(Context context) {
        try {
            this.context = context;
            open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static synchronized DataBaseCon getInstance(Context ctx) {

        try {
            if (dbConInstance == null) {
                dbConInstance = new DataBaseCon(ctx);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbConInstance;
    }

    public DataBaseCon open() {
        try {
            dbHelper = DbHelper.getInstance(context);
            dbHelper.open();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void closeCursor(Cursor cursor) {
        dbHelper.closeCursor(cursor);
    }


    public long insert(String tbl, String values[], String names[]) {
        return dbHelper.insert(values, names, tbl);
    }

    public long insert1(String tbl, String values[], String names[]) {
        return dbHelper.insert1(values, names, tbl);
    }

    public Cursor fetch(String tbl, String names[], String where, String args[], String order,
                        String limit, boolean isDistinct, String groupBy, String having) {

        return dbHelper.fetch(tbl, names, where, args, order, limit, isDistinct, groupBy, having);
    }

    public Cursor fetchLastRow(String tbl, String order, String args[]) {

        return dbHelper.fetchLastRow(tbl, order, args);
    }

    public Cursor fetchFromSelect(String tbl, String where) {
        String query = null;
        try {
            query = "select * from " + tbl + where;
            Log.i("TAG", "query :" + query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbHelper.rawQuery(query);
    }
    public Cursor fetchAlldata(String tbl) {
        String query = null;
        try {
            query = "select * from table_M_Category";

            Log.i("TAG", "query :" + query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbHelper.rawQuery(query);
    }

    public Cursor fetchDistictFromSelect(String colName,String tbl, String where) {
        String query = null;
        try {
            query = "select DISTINCT "+colName+" from " + tbl + where;
            Log.i("TAG", "query :" + query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbHelper.rawQuery(query);
    }
    public Cursor fetchAll2(String Category,String where1, String tbl) {
        String query = null;
        try {
            //    query = "select *  from table_M_Category where  trim(Produt_type_id) ='1014'";

            query = "select * from table_M_Category where trim(Category) ='"+Category+"'";

            //    query = "select * from table_M_Category where trim(Subcategory) ='"+colName+"'";

            Log.i("TAG", "query :" + query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbHelper.rawQuery(query);
    }

    public Cursor fetchAll(String tbl) {
        String query = null;
        try {
            query = "select * from " + tbl;
            Log.i("TAG", "query :" + query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbHelper.rawQuery(query);
    }
    public int getCountOfRows(String tbl) {
        int count = 0;
        try {
            String query = "select * from " + tbl;
            count = 0;
            count = (int) dbHelper.getCountOfRows(tbl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public Cursor getAllDataFromTable(String tbl) {
        Cursor cursor = null;
        try {
            String query = "select * from " + tbl;
            cursor = dbHelper.rawQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public boolean delete(String tbl, String where, String args[]) {
        return dbHelper.delete(tbl, where, args);
    }

    public boolean update(String tbl, String where, String values[], String names[], String args[]) {
        return dbHelper.update(where, values, names, tbl, args);
    }
    public boolean updateBulk(String tbl, String where, String values[], String names[], String args[]) {
        return dbHelper.updateBulk(where, values, names, tbl, args);
    }

    public boolean alterTable(String tbl) {
        return dbHelper.alterTable(tbl);
    }



    public SQLiteStatement beginDBTransaction(String tableName, String names[]) {
        SQLiteStatement statement = null;
        try {
            statement = null;
            statement = dbHelper.beginDBTransaction(tableName, names);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statement;
    }

    public void beginDBTransaction() {

        dbHelper.beginDBTransaction();
    }

    public void endDBTransaction() {

        dbHelper.endDBTransaction();
    }

    public void dbTransactionSucessFull() {
        dbHelper.dbTransactionSucessFull();
    }

    public void updateServerStatus(String status) {
        dbHelper.updateServerStatus(status);
    }

    public int getCountOfRows(String table, String whereClause, String[] whereArgs) {
        int count = 0;
        try {
            String query = "select * from " + table;
            count = 0;
            count = (int) dbHelper.getCountOfRows(table,whereClause,whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
}
