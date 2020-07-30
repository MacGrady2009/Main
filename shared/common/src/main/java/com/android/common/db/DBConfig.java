package com.android.common.db;

import android.content.Context;
import com.android.common.Constant;
import com.android.common.utils.AppUtils;

public class DBConfig {

    public static String getAuthUrl(Context context){
        return AppUtils.getAppMetaData(context, Constant.META_DATA_AUTH_URL_KEY);
    }

    public static final String DATABASE_NAME = "App.db";
    public static final int DATABASE_VERSION = 1;
    //db table name config
    public static final String LOGIN_TABLE_NAME = "login";
    public static final int LOGIN_TABLE_SUFFIX = 1;
    public static final String TMS_TABLE_NAME = "tms";
    public static final int TMS_TABLE_SUFFIX = LOGIN_TABLE_SUFFIX + 1;
    public static final int OTHER_TABLE_SUFFIX = TMS_TABLE_SUFFIX + 1;
}
