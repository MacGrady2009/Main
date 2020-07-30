package com.android.common.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.android.common.utils.AppUtils;

public class AppProvider extends ContentProvider
{
    private DBHelper dbHelper;
    // 若不匹配采用UriMatcher.NO_MATCH(-1)返回
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    @Override
    public boolean onCreate()
    {
        String authUrl = AppUtils.getAppMetaData(this.getContext(),"auth_url");
        MATCHER.addURI(authUrl, DBConfig.LOGIN_TABLE_NAME, DBConfig.LOGIN_TABLE_SUFFIX);
        MATCHER.addURI(authUrl, DBConfig.TMS_TABLE_NAME, DBConfig.TMS_TABLE_SUFFIX);
        dbHelper = DBHelper.getInstance(getContext());
        return true;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        Uri result = null;
        switch (MATCHER.match(uri))
        {
            case DBConfig.LOGIN_TABLE_SUFFIX:
                // 若主键值是自增长的id值则返回值为主键值，否则为行号，但行号并不是RecNo列
                long loginId = dbHelper.insert(DBConfig.LOGIN_TABLE_NAME, null, values);
                result = ContentUris.withAppendedId(uri, loginId);
                getContext().getContentResolver().notifyChange(uri,null);
                return result;
            case DBConfig.TMS_TABLE_SUFFIX:
                // 若主键值是自增长的id值则返回值为主键值，否则为行号，但行号并不是RecNo列
                long tmsID = dbHelper.insert(DBConfig.TMS_TABLE_NAME, null, values);
                result = ContentUris.withAppendedId(uri, tmsID);
                getContext().getContentResolver().notifyChange(uri,null);
                return result;
            default:
                throw new IllegalArgumentException("this is unkown uri:" + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        int result = 0;
        switch (MATCHER.match(uri))
        {
            case DBConfig.LOGIN_TABLE_SUFFIX:
                result = dbHelper.delete(DBConfig.LOGIN_TABLE_NAME, selection, selectionArgs); // 删除所有记录;
                getContext().getContentResolver().notifyChange(uri,null);
                return result;
            case DBConfig.TMS_TABLE_SUFFIX:
                result =  dbHelper.delete(DBConfig.TMS_TABLE_NAME, selection, selectionArgs); // 删除所有记录
                getContext().getContentResolver().notifyChange(uri,null);
                return result;
            case DBConfig.OTHER_TABLE_SUFFIX:
                long id = ContentUris.parseId(uri); // 取得跟在URI后面的数字
                String where = "id = " + id;
                if (null != selection && !"".equals(selection.trim()))
                {
                    where += " and " + selection;
                }
                result =  dbHelper.delete(DBConfig.LOGIN_TABLE_NAME, where, selectionArgs);
                getContext().getContentResolver().notifyChange(uri,null);
                return result;
            default:
                throw new IllegalArgumentException("this is unkown uri:" + uri);
        }
    }
    /**
     * 外部应用向本应用更新数据
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        int result = 0;
        switch (MATCHER.match(uri))
        {
            case DBConfig.LOGIN_TABLE_SUFFIX:
                result = dbHelper.update(DBConfig.LOGIN_TABLE_NAME,values,selection, selectionArgs); // 更新所有记录
                getContext().getContentResolver().notifyChange(uri,null);
                return result;
            case DBConfig.TMS_TABLE_SUFFIX:
                result = dbHelper.update(DBConfig.TMS_TABLE_NAME,values,selection, selectionArgs); // 更新所有记录
                getContext().getContentResolver().notifyChange(uri,null);
                return result;
            case DBConfig.OTHER_TABLE_SUFFIX:
                long id = ContentUris.parseId(uri); // 取得跟在URI后面的数字
                String where = "id = " + id;
                if (null != selection && !"".equals(selection.trim()))
                {
                    where += " and " + selection;
                }
                result = dbHelper.update(DBConfig.LOGIN_TABLE_NAME,values,where,selectionArgs);
                getContext().getContentResolver().notifyChange(uri,null);
                return result;
            default:
                throw new IllegalArgumentException("this is unkown uri:" + uri);
        }
    }
    /**
     * 返回对应的内容类型
     * 如果返回集合的内容类型，必须以vnd.android.cursor.dir开头
     * 如果是单个元素，必须以vnd.android.cursor.item开头
     */
    @Override
    public String getType(Uri uri)
    {
        switch(MATCHER.match(uri))
        {
            case DBConfig.LOGIN_TABLE_SUFFIX:
                return "vnd.android.cursor.dir/login";
            case DBConfig.TMS_TABLE_SUFFIX:
                return "vnd.android.cursor.dir/tms";
            case DBConfig.OTHER_TABLE_SUFFIX:
                return "vnd.android.cursor.item/login";
            default:
                throw new IllegalArgumentException("this is unkown uri:" + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        switch (MATCHER.match(uri))
        {
            case DBConfig.LOGIN_TABLE_SUFFIX:
                return dbHelper.query(DBConfig.LOGIN_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
            case DBConfig.TMS_TABLE_SUFFIX:
                return dbHelper.query(DBConfig.TMS_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
            case DBConfig.OTHER_TABLE_SUFFIX:
                long id = ContentUris.parseId(uri); // 取得跟在URI后面的数字
                String where = "id = " + id;
                if (null != selection && !"".equals(selection.trim()))
                {
                    where += " and " + selection;
                }
                return dbHelper.query(DBConfig.LOGIN_TABLE_NAME, projection, where, selectionArgs, null, null, sortOrder);
            default:
                throw new IllegalArgumentException("this is unkown uri:" + uri);
        }
    }

    @Nullable
    @Override
    public Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
        Bundle bundle = new Bundle();
        if ("Test".equals(method)){
            bundle.putString("if","if");
        }else {
            bundle.putString("else","else");
        }
        return bundle;
    }
}