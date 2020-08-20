package com.android.guide.mvp.model;

import com.android.common.base.BaseApi;
import com.android.common.network.ResponseBean;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;
import retrofit2.http.GET;

public interface GuideApi extends BaseApi {
    /**
     * 广告页获取广告
     */
    @GET("/api/getAd")
    Observable<Response<ResponseBean<AdBean>>> getAd();
}
