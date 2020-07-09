package com.android.common.network;

import com.android.common.base.BaseEvent;
import org.greenrobot.eventbus.EventBus;

public abstract class BusinessObserver<T>
    extends BaseObserver<T> {

    @Override
    public void onSuccess(T t, ResponseBean responseBean) {
        if (null != responseBean && responseBean.getCode() == Constant.LOGOUT){
            EventBus.getDefault().post(new BaseEvent());
        }else {
            onSucceed(responseBean);
        }
    }

    public abstract void onSucceed(ResponseBean responseBean);

}
