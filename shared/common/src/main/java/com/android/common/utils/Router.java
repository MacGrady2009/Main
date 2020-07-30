package com.android.common.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.common.Constant;


public class Router {

    private String scheme = "";
    private String host = "";
    private String port = "";

    private static Router instance;
    public static final int NEW_TASK = Intent.FLAG_ACTIVITY_NEW_TASK;
    public static final int CLEAR_TOP = Intent.FLAG_ACTIVITY_CLEAR_TOP;
    public static final int SINGLE_TOP = Intent.FLAG_ACTIVITY_SINGLE_TOP;
    public static final int CLEAR_TASK = Intent.FLAG_ACTIVITY_CLEAR_TASK;
    public static final int NO_TASK = -100;
    public static final String TAG = Router.class.getSimpleName();
    private Router() {
    }

    public static Router getInstance() {
        if (instance == null) {
            instance = new Router();
        }
        return instance;
    }


    public void startActivity(@NonNull Context context, @Nullable Bundle bundle, @NonNull String path) {
        startActivity(context,bundle,path,NEW_TASK);
    }


    public void startActivity(@NonNull Context context, @Nullable Bundle bundle, @NonNull String path, int flag) {
        try {
            Intent intent = createIntent(context, bundle, path, flag);
            if (intent!=null){
                context.startActivity(intent, bundle);  //bundle
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * design for activity
     */
    public void startActivityForResult(@NonNull Activity activity, int requestCode, @Nullable Bundle options, @NonNull String path) {
        startActivityForResult(activity,requestCode,options,path,NO_TASK);
    }

    /**
     * design for activity
     */
    public void startActivityForResult(@NonNull Activity activity, int requestCode, @Nullable Bundle options, @NonNull String path, int flag) {
        try {
            Intent intent = createIntent(activity, options, path, flag);
            if (intent !=null){
                activity.startActivityForResult(intent, requestCode, options);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * design for fragment
     */
    public void startActivityForResult(@NonNull Fragment fragment, int requestCode, @Nullable Bundle options, @NonNull String path) {
        try {
            Intent intent = createIntent(fragment.getActivity(), options, path, 0x00);
            if (intent !=null){
                fragment.startActivityForResult(intent, requestCode, options);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建隐式跳转Intent
     * @param context
     * @param bundle
     * @param path
     * @param flag
     * @return
     */
    private Intent createIntent(@NonNull Context context, @Nullable Bundle bundle, @NonNull String path, int flag) {
        scheme = AppUtils.getAppMetaData(context, Constant.META_DATA_SCHEME_KEY);
        host = AppUtils.getAppMetaData(context, Constant.META_DATA_HOST_KEY);
        port = AppUtils.getAppMetaData(context, Constant.META_DATA_PORT_KEY);

        if (null != context && (!TextUtils.isEmpty(path)) && (!TextUtils.isEmpty(port)) &&
            (!TextUtils.isEmpty(scheme)) && (!TextUtils.isEmpty(host))) {
            Intent intent = new Intent();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(scheme).append(host).append(":").append(port).append("/").append(path);
            intent.setData(Uri.parse(stringBuilder.toString()));
            if (flag > 0) {
                intent.setFlags(flag);
            }
            if(bundle!=null) {
                intent.putExtras(bundle);
            }

            return intent;
        }
        return null;
    }

    public String getScheme() {
        return scheme;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

}
