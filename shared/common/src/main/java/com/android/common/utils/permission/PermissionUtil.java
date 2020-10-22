package com.android.common.utils.permission;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

public class PermissionUtil {

    public static boolean checkPermission(Context context, String permission) {
        int targetSdkVersion = getTargetVersion(context);
        boolean result = false;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (targetSdkVersion >= Build.VERSION_CODES.M) {
                    result = ActivityCompat.checkSelfPermission(context,permission) == PackageManager.PERMISSION_GRANTED;
                } else {
                    result = PermissionChecker.checkSelfPermission(context, permission) == PermissionChecker.PERMISSION_GRANTED;
                }
            } else {
                result = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    private static int version = 0;

    private static int getTargetVersion(Context context) {
        if (version == 0) {
            try {
                final PackageInfo info = context.getPackageManager().getPackageInfo(
                        context.getPackageName(), 0);
                version = info.applicationInfo.targetSdkVersion;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return version;
    }
}
