package com.android.common.base;

import android.view.KeyEvent;
import androidx.fragment.app.FragmentManager;
import com.android.common.R;

public class BaseWebViewActivity extends BaseActivity{

    @Override
    protected int onSetRootViewId() {
        return R.layout.activity_base_webview;
    }

    private FragmentManager mFragmentManager;
    private BaseWebViewFragment mBaseWebViewFragment;

    @Override
    protected void onInitFragment() {
        mFragmentManager = getSupportFragmentManager();
        mBaseWebViewFragment = (BaseWebViewFragment)mFragmentManager.findFragmentById(R.id.fragment_base_web_view);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mBaseWebViewFragment.onKeyDown(keyCode,event);
    }
}