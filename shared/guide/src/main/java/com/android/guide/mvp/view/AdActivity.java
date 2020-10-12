package com.android.guide.mvp.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.android.common.constant.Constant;
import com.android.common.base.BaseActivity;
import com.android.common.constant.ModuleConstant;
import com.android.common.utils.Router;
import com.android.common.utils.SpUtils;
import com.android.guide.BuildConfig;
import com.android.guide.R;
import com.android.guide.mvp.model.AdBean;
import com.android.guide.mvp.present.AdPresent;

/**
 * 广告页
 */
public class AdActivity extends BaseActivity implements AdView
    , View.OnClickListener {

    private AdPresent present;

    private ImageView mIvAd;

    private TextView mTvAdTime;

    private TextView mTvRight;

    @Override
    protected int onSetRootViewId() {
        return R.layout.activity_ad;
    }

    @Override
    protected void onFindView() {
        super.onFindView();
        mIvAd = findViewById(R.id.img_ad);
        mTvAdTime = findViewById(R.id.tv_ad_time);
        mTvRight = findViewById(R.id.tv_right);
    }

    @Override
    protected void onInitView() {
        if (!isTaskRoot()){
            finish();
            return;
        };
        String right = String.format(getString(R.string.right), "2020");
        mTvRight.setText(right);
        present = new AdPresent(this);
        present.getAd();
    }

    @Override
    public void onInitEvent(){
        mIvAd.setOnClickListener(this);
        mTvAdTime.setOnClickListener(this);
    }

    @Override
    public void onCountDown(long second) {
        String skip = String.format(getString(R.string.skip), second);
        mTvAdTime.setText(skip);
        if (second <= 1){
            skip();
        }
    }

    @Override
    public void onGetAd(AdBean adBean) {
        present.countDown(Constant.AD_TIME);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.img_ad) {

        } else if (view.getId() == R.id.tv_ad_time) {
            skip();
        }
    }

    private void skip(){
        if (!SpUtils.getBoolean(this, ModuleConstant.IS_GUIDED,false)){
            Router.getInstance().startActivity(this,null, BuildConfig.GUIDE);
            finish();
        }else if (!SpUtils.getBoolean(this,ModuleConstant.IS_LOGIN,false)){
            Router.getInstance().startActivity(this,null, BuildConfig.LOGIN);
            finish();
        }else {
            Router.getInstance().startActivity(this,null, BuildConfig.MAIN);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        present.setActivityState(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        present.setActivityState(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        present = null;
    }
}
