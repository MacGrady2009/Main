package com.android.common.network;

import com.android.common.base.BaseBean;
import com.android.common.base.BaseEvent;
import com.android.common.base.BaseObserver;
import org.greenrobot.eventbus.EventBus;
import retrofit2.Response;

public abstract class ResponseObserver<T extends BaseBean>
    extends BaseObserver<Response<T>> {

    @Override
    public void onSuccess(Response<T> response) {
        if (response.isSuccessful()) {
            onSuccess(response.body());
        } else {
            failed(response.code(), response.message());
        }
    }

    public abstract void onSuccess(T t);

}
