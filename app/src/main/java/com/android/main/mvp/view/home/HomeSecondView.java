package com.android.main.mvp.view.home;

import com.android.common.base.BaseView;
import com.android.main.mvp.model.home.HomeSecondBean;

public interface HomeSecondView extends BaseView {
    /**
     * 获取列表
     */
    void onGetHomeSecondList(HomeSecondBean homeSecondBean);

}
