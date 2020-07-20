package com.android.common.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDB;
    private Context mContext;

    private static DBHelper mDatabaseOperator;

    public static DBHelper getInstance(Context context){
        if (null == mDatabaseOperator)
            mDatabaseOperator = new DBHelper(context);
        return mDatabaseOperator;
    }
    private DBHelper(Context context) {
        mContext = context;
        mDBHelper = new DatabaseHelper(context);
    }
    public void openWritableDatabase() {
        mDB = mDBHelper.getWritableDatabase();
    }

    public void openReadableDatabase() {
        mDB = mDBHelper.getReadableDatabase();
    }

    public void closeDatabase() {
        if (mDB.isOpen()){
            mDBHelper.close();
        };
    }

    public long insert(String table, String nullColumnHack, ContentValues values) {
        long result = -1;
        try{
            openWritableDatabase();
            result = mDB.insert(table,nullColumnHack,values);
        }catch (Exception exception){
            exception.printStackTrace();
        }finally {
            closeDatabase();
        }
        return result;
    }
    public Cursor query(String table, String[] columns, String selection,
                        String[] selectionArgs, String groupBy, String having,
                        String orderBy) {
        Cursor cursor = null;
        try{
            openReadableDatabase();
            cursor = mDB.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return cursor;
    }
    public int delete(String table, String whereClause, String[] whereArgs) {
        int result = 0;
        try{
            openWritableDatabase();
            result = mDB.delete(table, whereClause, whereArgs);
        }catch (Exception exception){

        }finally {
            closeDatabase();
        }
        return result;
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        int result = 0;
        try{
            openWritableDatabase();
            result = mDB.update(table, values, whereClause, whereArgs);
        }catch (Exception exception){

        }finally {
            closeDatabase();
        }
        return result;
    }

    class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DBConfig.DATABASE_NAME, null, DBConfig.DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + DBConfig.LOGIN_TABLE_NAME +"(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                Columns.USER_CREDENTIAL_KEY + " TEXT," +
                Columns.APP_CREDENTIAL_KEY + " TEXT" +
                ");");
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DBConfig.LOGIN_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DBConfig.TMS_TABLE_NAME);
            onCreate(db);
        }
    }


}
