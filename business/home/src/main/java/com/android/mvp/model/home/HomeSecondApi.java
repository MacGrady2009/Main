package com.android.mvp.model.home;

import com.android.common.base.BaseApi;
import com.android.common.network.ResponseBean;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;
import retrofit2.http.GET;

public interface HomeSecondApi extends BaseApi {
    /**
     * 第二个tab的列表
     */
    @GET("/api/getHomeSecondList")
    Observable<Response<ResponseBean<HomeSecondBean>>> getHomeSecondList();
}
