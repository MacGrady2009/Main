package com.android.common.network;

import com.android.common.base.BaseEvent;
import org.greenrobot.eventbus.EventBus;

/**
 * @param <T>
 */
public abstract class BusinessObserver<T> extends ResponseObserver<ResponseBean<T>>{

    @Override
    public void onSuccess(ResponseBean<T> responseBean) {
        if (responseBean.getCode() == Constant.SUCCESS){
            onSucceed(responseBean.getData());
        }
        if (responseBean.getCode() == Constant.LOGOUT){
            EventBus.getDefault().post(new BaseEvent());
        }

    }

    public abstract void onSucceed(T t);
}
