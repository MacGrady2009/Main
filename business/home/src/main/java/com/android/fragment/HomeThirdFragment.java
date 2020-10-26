package com.android.fragment;

import com.android.common.base.BaseFragment;
import com.android.home.R;

public class HomeThirdFragment extends BaseFragment {

    public static HomeThirdFragment instance;

    public static HomeThirdFragment getInstance(){
        if (null == instance){
            instance = new HomeThirdFragment();
        }
        return instance;
    }

    public HomeThirdFragment(){
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

    @Override
    protected boolean onNeedBack() {
        return false;
    }

    @Override
    protected String onSetTitleText() {
        return getString(R.string.my);
    }
}
