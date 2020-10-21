package com.android.guide.business.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.common.base.BaseFragment;
import com.android.common.constant.Constant;
import com.android.common.constant.ModuleConstant;
import com.android.common.utils.Router;
import com.android.common.utils.SpUtils;
import com.android.guide.BuildConfig;
import com.android.guide.R;
import com.android.guide.mvp.model.AdBean;
import com.android.guide.mvp.present.AdPresent;
import com.android.guide.mvp.view.AdView;

public class AdFragment extends BaseFragment implements AdView
    , View.OnClickListener{

    private AdPresent present;

    private ImageView mIvAd;

    private TextView mTvAdTime;

    private TextView mTvRight;

    @Override
    protected int onSetRootViewId() {
        return R.layout.fragment_ad;
    }

    @Override
    protected void onFindView() {
        super.onFindView();
        mIvAd = mRootView.findViewById(R.id.img_ad);
        mTvAdTime = mRootView.findViewById(R.id.tv_ad_time);
        mTvRight = mRootView.findViewById(R.id.tv_right);
    }

    @Override
    protected void onInitView() {
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
        if (!SpUtils.getBoolean(this.getContext(), ModuleConstant.IS_GUIDED,false)){
            Router.getInstance().startActivity(this.getContext(),null, BuildConfig.GUIDE);
            this.getActivity().finish();
        }else if (!SpUtils.getBoolean(this.getContext(),ModuleConstant.IS_LOGIN,false)){
            Router.getInstance().startActivity(this.getContext(),null, BuildConfig.LOGIN);
            this.getActivity().finish();
        }else {
            Router.getInstance().startActivity(this.getContext(),null, BuildConfig.MAIN);
            this.getActivity().finish();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        present.setActivityState(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        present.setActivityState(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        present = null;
    }

}
