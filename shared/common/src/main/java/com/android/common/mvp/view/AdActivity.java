package com.android.common.mvp.view;


import com.android.common.Constant;
import com.android.common.R;
import com.android.common.base.BaseActivity;
import com.android.common.base.BaseObserver;
import com.android.common.mvp.model.AdBean;
import com.android.common.mvp.present.AdPresent;

/**
 * 广告页
 */
public class AdActivity extends BaseActivity implements AdView {

    private AdPresent present;

    @Override
    protected int onSetRootViewId() {
        return R.layout.activity_ad;
    }

    @Override
    protected void onInitView() {
        super.onInitView();
        present = new AdPresent(this);
        present.getAd();
    }

    @Override
    public void onCountDown(int second) {
        if (second <= 0) return;

    }

    @Override
    public void onGetAd(AdBean adBean) {
        present.countDown(Constant.AD_TIME);
    }
}
