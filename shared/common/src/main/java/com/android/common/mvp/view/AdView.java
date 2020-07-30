package com.android.common.mvp.view;

import com.android.common.mvp.model.AdBean;
import com.android.common.base.BaseView;

public interface AdView extends BaseView {
    /**
     * 倒计时
     * @param second
     */
    void onCountDown(int second);
    /**
     * 获取广告对象
     * @param adBean
     */
    void onGetAd(AdBean adBean);

}
