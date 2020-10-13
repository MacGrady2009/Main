package com.android.common.base;

import com.android.common.network.ResponseBean;

public interface BaseView extends BaseInterface {
    /**
     * 网络接口请求失败统一回调
     * @param responseBean
     */
    void onFailed(ResponseBean responseBean);
    /**
     * 网络接口请求成功统一回调
     */
    void onSucceed();
}
