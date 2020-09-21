package com.android.main.business.home.fragment;

import com.android.common.base.BaseFragment;
import com.android.main.R;

public class HomeSecondFragment extends BaseFragment {

    public static HomeSecondFragment instance;

    public static HomeSecondFragment getInstance(){
        if (null == instance){
            instance = new HomeSecondFragment();
        }

        return instance;
    }

    private HomeSecondFragment(){
    }

    @Override
    public int onSetRootViewId() {
        return R.layout.fragment_home_second;
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
