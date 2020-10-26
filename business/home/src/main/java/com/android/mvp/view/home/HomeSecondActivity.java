package com.android.mvp.view.home;

import androidx.fragment.app.FragmentManager;
import com.android.common.base.BaseActivity;
import com.android.fragment.HomeSecondFragment;
import com.android.home.R;

public class HomeSecondActivity extends BaseActivity {

    private FragmentManager mFragmentManager;
    private HomeSecondFragment mHomeSecondFragment;

    @Override
    protected int onSetRootViewId() {
        return R.layout.activity_home_second;
    }

    @Override
    protected void onInitFragment() {
        mFragmentManager = getSupportFragmentManager();
        mHomeSecondFragment = (HomeSecondFragment)mFragmentManager.findFragmentById(R.id.fragment_home_second);
        mHomeSecondFragment.setNeedBack(true);
    }
}
