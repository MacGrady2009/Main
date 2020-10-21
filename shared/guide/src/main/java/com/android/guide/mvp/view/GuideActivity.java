package com.android.guide.mvp.view;

import android.view.KeyEvent;
import com.android.common.base.BaseActivity;
import com.android.guide.R;

/**
 * 引导页面
 */
public class GuideActivity extends BaseActivity {

    @Override
    protected int onSetRootViewId() {
        return R.layout.activity_guide;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
