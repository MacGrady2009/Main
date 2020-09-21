package com.android.main.business.home.fragment;

import com.android.common.base.BaseFragment;
import com.android.main.R;

public class HomeThirdFragment extends BaseFragment {

    public static HomeThirdFragment instance;

    public static HomeThirdFragment getInstance(){
        if (null == instance){
            instance = new HomeThirdFragment();
        }
        return instance;
    }

    private HomeThirdFragment(){
    }

    @Override
    public int onSetRootViewId() {
        return R.layout.fragment_home_third;
    }

    @Override
    protected void onFindView() {
        super.onFindView();
    }

    @Override
    protected void onInitView() {
        super.onInitView();
    }

    @Override
    protected void onInitEvent() {
        super.onInitEvent();
    }
}
