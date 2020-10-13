package com.android.main.mvp.present.home;

import com.android.common.base.BasePresentImpl;
import com.android.common.network.BusinessObserver;
import com.android.common.network.ResponseBean;
import com.android.main.mvp.model.home.HomeSecondApi;
import com.android.main.mvp.model.home.HomeSecondBean;
import com.android.main.mvp.view.home.HomeSecondView;

public class HomeSecondPresent extends BasePresentImpl<HomeSecondView, HomeSecondApi> {

    public HomeSecondPresent(HomeSecondView homeSecondView) {
        super(homeSecondView);
    }

    /**
     * 获取列表
     */
    public void getHomeSecondList() {
        compose(getService(HomeSecondApi.class).getHomeSecondList())
            .subscribe(new BusinessObserver<HomeSecondBean>() {
                @Override
                public void onSucceed(HomeSecondBean homeSecondBean) {
                    mBaseView.onSucceed();
                    mBaseView.onGetHomeSecondList(homeSecondBean);
                }

                @Override
                public void onFailed(ResponseBean responseBean) {
                    mBaseView.onFailed(responseBean);
                }
            });
    }

}
