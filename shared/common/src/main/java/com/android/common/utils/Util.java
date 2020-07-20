package com.android.common.utils;

import android.text.TextUtils;

public class Util {

  public static boolean isInstanceof(Object instance, String className) {
    boolean result = false;
    if (TextUtils.isEmpty(className) || (null == instance)) {
      return result;
    }

    try {
      Class clazz = Class.forName(className);

      result = clazz.isInstance(instance);
      return result;
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return result;
  }
}
