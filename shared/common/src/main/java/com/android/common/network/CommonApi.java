package com.android.common.network;

import com.android.common.base.BaseInterface;
import com.android.common.mvp.model.AdBean;
import io.reactivex.rxjava3.core.Observable;
import java.util.Map;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface CommonApi extends BaseInterface {

    /**
     * 下载文件
     * @param url
     */
    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);//直接使用网址下载

    /**
     *
     * @param url
     * @return
     */

    /**
     * 断点下载下载文件
     * @param range(Range: bytes=10-)：第10个字节及最后个字节的数据
     * (Range: bytes=40-100)：第40个字节到第100个字节之间的数据.
     * @param url
     */
    @Streaming
    @GET
    Observable<ResponseBody> download(@Header("Range") String range, @Url String url);//直接使用网址下载


    /**
     * 单个文件上传
     */
    @Multipart
    @POST("")
    Observable<Response<ResponseBean>> uploadFile(@Part MultipartBody.Part file);

    /**
     * 单个文件上传和参数（混合式）
     */
    @Multipart
    @POST("")
    Observable<Response<ResponseBean>> uploadFile(@Part MultipartBody.Part file, @Part("type") RequestBody type);

    /**
     * 批量文件上传
     */
    @Multipart
    @POST("")
    Observable<Response<ResponseBean>> uploadFiles(@PartMap Map<String, RequestBody> params);

    /**
     * 广告页获取广告
     */
    @GET("/api/getAd")
    Observable<Response<ResponseBean<AdBean>>> getAd();


}
