package com.android.guide.mvp.present;

import android.util.Log;
import com.android.common.base.BasePresentImpl;
import com.android.guide.mvp.model.AdBean;
import com.android.guide.mvp.model.GuideApi;
import com.android.guide.mvp.view.AdView;
import com.android.common.network.BusinessObserver;
import com.android.common.network.ResponseBean;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.functions.Consumer;
import org.reactivestreams.Subscription;

public class AdPresent extends BasePresentImpl<AdView, GuideApi> {

    final Subscription[] mSubscription = new Subscription[1];

    volatile boolean mIsPause4Activity = false;
    volatile boolean mIsWait = false;

    private Object lock = new Object();

    public AdPresent(AdView adView) {
        super(adView);
    }

    /**
     * 倒计时
     * @param countSecond
     */
    public void countDown(Long countSecond) {
        //用来在执行子线程中的Flowable OnSubscribe
        // 的subscribe方法之前执行
        Consumer consumer = o -> {};
        Flowable<Long> flowAble = Flowable.create(e -> {
            long count = countSecond;
            while (count > 0) {
                synchronized (lock){
                    if (mIsPause4Activity){
                        mIsWait = true;
                        lock.wait();
                    }else {
                        e.onNext(count);
                        count--;
                        Thread.sleep(1000);
                    }
                }
            }
            e.onComplete();
        }, BackpressureStrategy.BUFFER);

        AdPresent.compose(flowAble).doOnSubscribe(consumer)
            .subscribe(new FlowableSubscriber<Long>() {

                @Override
                public void onSubscribe(@NonNull Subscription s) {
                    mSubscription[0] = s;
                    s.request(countSecond + 1);
                }

                @Override
                public void onNext(Long countSecond) {
                    mBaseView.onCountDown(countSecond);
                }

                @Override
                public void onError(Throwable t) {
                    mBaseView.onFailed(null);
                }

                @Override
                public void onComplete() {
                    stopCountDown();
                }
            });
    }

    /**
     * 获取广告对象
     */
    public void getAd() {
        compose(getService(GuideApi.class).getAd())
            .subscribe(new BusinessObserver<AdBean>() {
                @Override
                public void onSucceed(AdBean adBean) {
                    mBaseView.onSucceed();
                    mBaseView.onGetAd(adBean);
                }

                @Override
                public void onFailed(ResponseBean responseBean) {
                    mBaseView.onFailed(responseBean);
                }
            });
    }

    public void setActivityState(boolean isPause) {
        mIsPause4Activity = isPause;
        if (mIsWait && (!mIsPause4Activity)){
            synchronized (lock){
                lock.notify();
                mIsWait = false;
            }
        }
    }

    private void stopCountDown() {
        if (mSubscription[0] != null) {
            mSubscription[0].cancel();
            mSubscription[0] = null;
        }
    }

}
