package com.android.guide.mvp.view;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.android.common.base.BaseActivity;
import com.android.guide.R;

/**
 * 广告页
 */
public class AdActivity extends BaseActivity {

    @Override
    protected int onSetRootViewId() {
        return R.layout.activity_ad;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()){
            finish();
            return;
        };
    }
}
