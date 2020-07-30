package com.android.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

/**
 * @author roof 2018/6/8.
 * @email lyj@yhcs.com
 * @detail 屏幕utils
 */

public class DisplayUtil {
    private static float density = 0.0F;
    private static float scaledDensity = 0.0F;
    private static int screenWidth = 0;
    private static int screenHeight = 0;
    private static int navigationBarHeight;
    private static int maxTextureSize = -1;
    private static int statusHeight = 0;
    private static Boolean IsPad = null;
    private static Point realSize = null;

    static {
        getScreenWidth();
        getScreenHeight();
    }

    public static int getScreenWidth() {
        if (screenWidth <= 0) {
            screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        }
        return screenWidth;
    }

    public static int getScreenHeight() {
        if (screenHeight <= 0) {
            screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        }
        return screenHeight;
    }

    public static float getDensity() {
        if (density <= 0.0F) {
            density = Resources.getSystem().getDisplayMetrics().density;
        }
        return density;
    }

    public static float getScaledDensity() {
        if (scaledDensity <= 0.0F) {
            scaledDensity = Resources.getSystem().getDisplayMetrics().scaledDensity;
        }
        return scaledDensity;
    }

    public static int dp2px(float dpValue) {
        return (int) (dpValue * getDensity() + 0.5F);
    }

    public static int px2dp(float pxValue) {
        return (int) (pxValue / getDensity() + 0.5F);
    }

    public static int sp2px(float pxValue) {
        return (int) (pxValue * getScaledDensity() + 0.5F);
    }

    public static int px2sp(float pxValue) {
        return (int) (pxValue / getScaledDensity() + 0.5F);
    }

    public static int getNavigationBarHeight() {
        if (navigationBarHeight <= 0) {
            Resources resources = Resources.getSystem();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            navigationBarHeight = resources.getDimensionPixelSize(resourceId);
        }
        return navigationBarHeight;
    }

    /**
     * 获得状态栏的高度
     * @param context Context
     */
    public static int getStatusHeight(Context context) {
        if (statusHeight == 0) {
            try {
                int resourceId = context.getResources().getIdentifier("status_bar_height",
                        "dimen", "android");
                if (resourceId > 0) {
                    //根据资源ID获取响应的尺寸值
                    statusHeight = context.getResources().getDimensionPixelSize(resourceId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    /**
     * 最大可显示图片宽高
     * @return size
     */
    public static int getMaxTextureSize() {
        if (maxTextureSize <= 0) {
            // Safe minimum default size
            final int IMAGE_MAX_BITMAP_DIMENSION = 2048;

            // Get EGL Display
            EGL10 egl = (EGL10) EGLContext.getEGL();
            EGLDisplay display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

            // Initialise
            int[] version = new int[2];
            egl.eglInitialize(display, version);

            // Query total number of configurations
            int[] totalConfigurations = new int[1];
            egl.eglGetConfigs(display, null, 0, totalConfigurations);

            // Query actual list configurations
            EGLConfig[] configurationsList = new EGLConfig[totalConfigurations[0]];
            egl.eglGetConfigs(display, configurationsList, totalConfigurations[0], totalConfigurations);

            int[] textureSize = new int[1];
            int maximumTextureSize = 0;

            // Iterate through all the configurations to located the maximum texture size
            for (int i = 0; i < totalConfigurations[0]; i++) {
                // Only need to check for width since opengl textures are always squared
                egl.eglGetConfigAttrib(display, configurationsList[i], EGL10.EGL_MAX_PBUFFER_WIDTH, textureSize);

                // Keep track of the maximum texture size
                if (maximumTextureSize < textureSize[0])
                    maximumTextureSize = textureSize[0];
            }

            // Release
            egl.eglTerminate(display);

            // Return largest texture size found, or default
            maxTextureSize = Math.max(maximumTextureSize, IMAGE_MAX_BITMAP_DIMENSION);
        }
        return maxTextureSize;
    }

    /**
     * 判断当前设备是平板还是pad，平板返回true,手机返回false
     * @return true or false
     */
    public static boolean isPad() {
        if (IsPad == null) {
            IsPad = (Resources.getSystem().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                    >= Configuration.SCREENLAYOUT_SIZE_LARGE;
        }
        return IsPad;
    }

    /**
     * 屏幕真正大小
     * @param context context
     * @return Point
     */
    public static Point getRealScreenSize(Context context) {
        if (realSize == null) {
            Display display = null;
            WindowManager manager = getWindowManager(context);
            if (manager != null) {
                display = manager.getDefaultDisplay();
            }
            if (display != null) {
                realSize = new Point();
                display.getRealSize(realSize);
            }
        }
        return realSize;
    }

    private static WindowManager getWindowManager(Context context) {
        return (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

}
