package com.android.guide.mvp.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.common.Constant;
import com.android.common.base.BaseActivity;
import com.android.common.utils.Router;
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
        if (isTaskRoot()){};
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
            Router.getInstance().startActivity(this,null, BuildConfig.GUIDE);
            finish();
        }
    }

    @Override
    public void onGetAd(AdBean adBean) {
        present.countDown(Constant.AD_TIME);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.img_ad){

        }else if (view.getId() == R.id.tv_ad_time){
            Router.getInstance().startActivity(this,null, BuildConfig.GUIDE);
            finish();
        }
    }
}
