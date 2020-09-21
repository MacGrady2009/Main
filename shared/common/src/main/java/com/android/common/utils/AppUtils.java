package com.android.common.utils;

import static com.android.common.constant.Constant.PACKAGE_ARCHIVE_DATA_TYPE;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import com.android.common.constant.Constant;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;


public class AppUtils {


    private static int versionCode = -1;

    private static String versionName = "";

    /**
     * 获取meta data
     * @param key key
     * @return String result
     */
    public static String getAppMetaData(Context context, String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager =context.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo info = packageManager.getApplicationInfo(
                        context.getPackageName(), PackageManager.GET_META_DATA);
                if ((info != null) && (info.metaData != null)) {
                    if (info.metaData.containsKey(key)){
                        Object obj = info.metaData.get(key);
                        if (obj != null){
                            resultData = obj.toString();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultData;
    }


    /**
     * 获取VersionName,VersionCode
     */
    private static void getAppVersionInfo(Context context) {
        String name = "";
        int code = -1;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            name = pi.versionName;
            code = pi.versionCode;
            if (TextUtils.isEmpty(name)) {
                name = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        versionName = name;
        versionCode = code;
    }

    public static int getVersionCode(Context context) {
        if (versionCode < 0) {
            getAppVersionInfo(context);
        }
        return versionCode;
    }

    public static String getVersionName(Context context) {
        if (TextUtils.isEmpty(versionName)) {
            getAppVersionInfo(context);
        }
        return versionName;
    }

    /**
     *判断当前应用程序处于前台还是后台
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    return true;
                }else{
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 本地安装apk
     * @param context
     * @param apkPath
     */
    public static  void installApk(Context context, String apkPath) {
        if (null == context || (TextUtils.isEmpty(apkPath))) {
            return;
        }
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            File file = new File(apkPath);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri uri = FileProvider.getUriForFile(context,
                  getAppMetaData(context, Constant.FILE_PROVIDER_AUTH), file);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uri, PACKAGE_ARCHIVE_DATA_TYPE);
            } else {
                intent.setDataAndType(Uri.fromFile(file), PACKAGE_ARCHIVE_DATA_TYPE);
            }
            context.startActivity(intent);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static boolean isAlive(Object object) {
        if (object == null) return false;
        if (object instanceof Activity) {
            return isAlive((Activity) object);
        } else if (object instanceof Fragment) {
            return isAlive((Fragment) object);
        } else if (object instanceof Service) {
            return isAlive((Service) object);
        } else if (object instanceof View){
            return isAlive(((View) object).getContext());
        } else if (object instanceof Context){
            try{
                return isAlive(getActivity((Context)object));
            } catch (Exception ignore){}
        }
        return true;
    }

    public static boolean isAlive(Activity activity) {
        return activity != null && !activity.isFinishing() && !activity.isDestroyed();
    }

    public static boolean isAlive(Fragment fragment) {
        return fragment != null && isAlive(fragment.getActivity()) && fragment.isAdded() && !fragment.isDetached();
    }

    public static boolean isAlive(Service service) {
        return isAlive(service, service.getClass().getName());
    }

    public static boolean isAlive(Context context, String serviceClassName) {
        if (context != null) {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (manager == null) return false;
            List<ActivityManager.RunningServiceInfo> services = manager.getRunningServices(Integer.MAX_VALUE);
            if (services != null) {
                for (ActivityManager.RunningServiceInfo s : services) {
                    if (TextUtils.equals(serviceClassName, s.service.getClassName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * context 转为Activity
     * @param context context
     */
    public static Activity getActivity(Context context) {
        Context in = context;
        if (in instanceof ContextWrapper) {
            while (in instanceof ContextWrapper) {
                if (in instanceof Activity) {
                    return (Activity) in;
                }
                in = ((ContextWrapper) in).getBaseContext();
            }
        }
        throw new IllegalStateException("The context is not an Activity.");
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取进程号对应的进程名
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public static boolean isMainProcess(Context context) {
        boolean isMainProcess = false;
        // 获取当前进程名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        isMainProcess = processName == null || TextUtils.equals(processName, packageName);
        return isMainProcess;
    }

    public static void closeAndroidPDialog() {
        if (android.os.Build.VERSION.SDK_INT >= 28) {
            try {
                Class cls = Class.forName("android.app.ActivityThread");
                Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
                declaredMethod.setAccessible(true);
                Object activityThread = declaredMethod.invoke(null);
                Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
                mHiddenApiWarningShown.setAccessible(true);
                mHiddenApiWarningShown.setBoolean(activityThread, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

}
