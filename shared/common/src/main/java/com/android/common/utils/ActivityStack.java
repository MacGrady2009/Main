package com.android.common.utils;

import android.app.Activity;
import java.util.ArrayList;
import java.util.List;

/**
 * ActivityStack
 */
public class ActivityStack {

    private List<Activity> activities = new ArrayList<>();

    static ActivityStack instance;

    private ActivityStack() {
    }

    public static ActivityStack getInstance() {
        if (instance == null) {
            instance = new ActivityStack();
        }
        return instance;
    }

    public void add(Activity activity) {
        if (activity != null) {
            activities.add(activity);
        }
    }

    public void remove(Activity activity) {
        if (activity != null) {
            activities.remove(activity);
        }
    }

    public void clear() {
        activities.clear();
    }

    public boolean contains(Activity activity) {
        boolean result = false;
        if (activity != null) {
            result = activities.contains(activity);
        }
        return result;
    }


}
