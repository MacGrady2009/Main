package com.android.common.utils;

import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;

/**
 * FragmentStack
 */
public class FragmentStack {

    private List<Fragment> fragments = new ArrayList<>();

    static FragmentStack instance;

    private FragmentStack() {
    }

    public static FragmentStack getInstance() {
        if (instance == null) {
            instance = new FragmentStack();
        }
        return instance;
    }

    public void add(Fragment fragment) {
        if (fragment != null) {
            fragments.add(fragment);
        }
    }

    public void remove(Fragment fragment) {
        if (fragment != null) {
            fragments.remove(fragment);
        }
    }

    public void clear() {
        fragments.clear();
    }

    public boolean contains(Fragment fragment) {
        boolean result = false;
        if (fragment != null) {
            result = fragments.contains(fragment);
        }
        return result;
    }
}
