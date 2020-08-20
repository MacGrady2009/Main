package com.android.guide.mvp.view;

import com.android.guide.mvp.model.AdBean;
import com.android.common.base.BaseView;

public interface AdView extends BaseView {
    /**
     * 倒计时
     * @param second
     */
    void onCountDown(long second);
    /**
     * 获取广告对象
     * @param adBean
     */
    void onGetAd(AdBean adBean);

}
