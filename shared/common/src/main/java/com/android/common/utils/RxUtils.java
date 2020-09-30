package com.android.common.utils;


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableTransformer;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;


public class RxUtils {

    /**
     * subscribeOn(Schedulers.io())的意思是在IO线程上注册对被观察者的监听
     * ，也就是让我们的网络操作在io线程上执行，并被观察
     * observeOn(AndroidSchedulers.mainThread())的意思是在主线程监听数据变化
     * ，并可以执行UI相关操作
     * @param observable
     * @param <T>
     * @return
     */
    public static <T> Observable<T> subscribe(Observable<T> observable){
        return observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> Flowable<T> subscribe(Flowable<T> flowAble){
        return flowAble.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }


    public static void dispose(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public static void dispose(Subscription subscription) {
        if (subscription != null) {
            subscription.cancel();
        }
    }

}