package com.android.common.mvp.present;

import com.android.common.base.BaseObserver;
import com.android.common.base.BasePresentImpl;
import com.android.common.mvp.model.AdBean;
import com.android.common.mvp.view.AdView;
import com.android.common.network.BusinessObserver;
import com.android.common.network.ResponseBean;
import io.reactivex.rxjava3.core.Observable;
import java.util.concurrent.TimeUnit;

public class AdPresent extends BasePresentImpl<AdView> {

    public AdPresent(AdView adView){
        super(adView);
    }

    /**
     * 倒计时
     * @param second
     */
    public void countDown(int second){
        AdPresent.compose(Observable.interval(0, 1, TimeUnit.SECONDS).take(second))
            .subscribe(new BaseObserver<Long>() {
                @Override
                public void onSuccess(Long result) {
                    mBaseView.onCountDown(second);
                }

                @Override
                public void onFailed(ResponseBean responseBean) {

                }
            });
    }
    /**
     * 获取广告对象
     */
    public void getAd(){
        compose(getService().getAd())
            .subscribe(new BusinessObserver<AdBean>() {

                @Override
                public void onSucceed(AdBean adBean) {
                    mBaseView.onGetAd(adBean);
                }

                @Override
                public void onFailed(ResponseBean responseBean) {
                }
            });
    }
}
