package com.android.guide.mvp.present;

import com.android.common.base.BaseObserver;
import com.android.common.base.BasePresentImpl;
import com.android.guide.mvp.model.AdBean;
import com.android.guide.mvp.model.GuideApi;
import com.android.guide.mvp.view.AdView;
import com.android.common.network.BusinessObserver;
import com.android.common.network.ResponseBean;
import io.reactivex.rxjava3.core.Observable;
import java.util.concurrent.TimeUnit;

public class AdPresent extends BasePresentImpl<AdView,GuideApi> {

    public AdPresent(AdView adView){
        super(adView);
    }



    /**
     * 倒计时
     * @param countSecond
     */
    public void countDown(Long countSecond){
        AdPresent.compose(Observable.interval(0, 1, TimeUnit.SECONDS).take(countSecond))
            .subscribe(new BaseObserver<Long>() {
                @Override
                public void onSuccess(Long result) {
                    mBaseView.onCountDown(countSecond - result);
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
        compose(getService(GuideApi.class).getAd())
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
