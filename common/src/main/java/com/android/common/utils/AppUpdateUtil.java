package com.android.common.utils;

/**
 * App升级专用
 */
public class AppUpdateUtil {

  private static AppUpdateUtil instance;

  /**
   * single instance
   * @return
   */
  public static AppUpdateUtil getInstance() {
    if (instance == null) {
      instance = new AppUpdateUtil();
    }
    return instance;
  }



}
