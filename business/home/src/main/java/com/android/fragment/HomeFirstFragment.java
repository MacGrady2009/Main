package com.android.fragment;

import com.android.common.base.BaseFragment;
import com.android.home.R;

public class HomeFirstFragment extends BaseFragment {

    public static HomeFirstFragment instance;

    public static HomeFirstFragment getInstance(){
        if (null == instance){
            instance = new HomeFirstFragment();
        }
        return instance;
    }

    public HomeFirstFragment(){
    }


    @Override
    public int onSetRootViewId() {
        return R.layout.fragment_home_first;
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
        return getString(R.string.home);
    }
}
