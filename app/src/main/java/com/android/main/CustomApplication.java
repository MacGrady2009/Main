package com.android.main;

import android.app.Application;
import android.content.res.Configuration;
import androidx.annotation.NonNull;
import com.android.common.utils.ActivityStack;
import com.android.common.utils.AppUtils;

public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //app启动会有多个进程
        if (AppUtils.isMainProcess(this)) {
            AppUtils.closeAndroidPDialog();
            registerActivityLifecycleCallbacks(ActivityLifecycle.getInstance());
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterActivityLifecycleCallbacks(ActivityLifecycle.getInstance());
        ActivityStack.getInstance().clear();
    }
}
