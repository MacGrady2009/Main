package com.android.common.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

public class ResolverHelper {

    public static boolean insert(Context context, String tableName, ContentValues values){
        if ((values == null) || TextUtils.isEmpty(tableName)){
            return false;
        }

        boolean result = false;

        try {
            Uri uri = Uri.parse("content://" + DBConfig.getAuthUrl(context) +"/"+ tableName);
            ContentResolver resolver = context.getContentResolver();
            if (null != resolver.insert(uri,values)){
                result = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static String query(Context context, String tableName, String columnName) {
        Uri uri = Uri.parse("content://" + DBConfig.getAuthUrl(context) +"/"+ tableName);
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(uri, null, null, null, null);
        String result = "";
        if (cursor != null && cursor.moveToFirst()) {
            do {
                result = cursor.getString(cursor.getColumnIndex(columnName));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return result;
    }

    public static boolean delete(Context context, String tableName) {
        boolean result = false;
        Uri uri = Uri.parse("content://" + DBConfig.getAuthUrl(context) +"/"+ tableName);
        ContentResolver resolver = context.getContentResolver();
        int num = resolver.delete(uri,null,null);
        result = (num > 0) ? true : false;
        return result ;
    }
}
