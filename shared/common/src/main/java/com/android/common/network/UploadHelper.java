package com.android.common.network;

import com.android.common.base.BaseObserver;
import com.android.common.utils.RxUtils;
import io.reactivex.rxjava3.observers.ResourceObserver;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class UploadHelper {
    ProgressListener  mListener;

    /**
     *单个文件上传
     * @param fileType type/subType(image/png)
     * 请参照https://www.w3school.com.cn/media/media_mimeref.asp
     * @param file
     */
    public void uploadFile(String fileType, File file ) {
        if (null != mListener){
            mListener.onStart();
        }
        RequestBody fileBody = RequestBody.create(MediaType.parse(fileType), file);
        CustomRequestBody progressRequestBody  = new CustomRequestBody(mListener, fileBody);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), progressRequestBody);
        RetrofitManager<CommonApi> retrofitManager = RetrofitManager.getInstance();
        RxUtils.subscribe(retrofitManager.getApiService(CommonApi.class).uploadFile(part))
            .subscribe(new BaseObserver<Response<ResponseBean>>() {
                @Override
                public void onSuccess(Response<ResponseBean> responseBean) {
                    if (null != mListener){
                        mListener.onFinish(true,responseBean);
                    }
                }

                @Override
                public void onFailed(ResponseBean responseBean) {
                    if (null != mListener){
                        mListener.onFinish(false,responseBean);
                    }
                }
            });

    }

    /**
     * 单个文件上传并且带有参数
     * @param fileType type/subType(image/png)
     * 请参照https://www.w3school.com.cn/media/media_mimeref.asp
     * @param file
     * @param otherType type/subType(image/png)
     * 请参照https://www.w3school.com.cn/media/media_mimeref.asp
     * @param otherBody
     */
    public void uploadFile(String fileType, File file, String otherType, String otherBody) {
        if (null != mListener){
            mListener.onStart();
        }
        RequestBody fileBody = RequestBody.create(MediaType.parse(fileType), file);
        CustomRequestBody progressRequestBody  = new CustomRequestBody(mListener, fileBody);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), progressRequestBody);
        RequestBody requestBody =RequestBody.create(MediaType.parse(otherType), otherBody);
        RetrofitManager<CommonApi> retrofitManager = RetrofitManager.getInstance();
        RxUtils.subscribe(retrofitManager.getApiService(CommonApi.class).uploadFile(part,requestBody))
            .subscribe(new BaseObserver<Response<ResponseBean>>() {
                @Override
                public void onSuccess(Response<ResponseBean> responseBean) {
                    if (null != mListener){
                        mListener.onFinish(true,responseBean);
                    }
                }

                @Override
                public void onFailed(ResponseBean responseBean) {
                    if (null != mListener){
                        mListener.onFinish(false,responseBean);
                    }
                }
            });

    }
    /**
     * 多个文件上传
     * @param fileTypes type/subType(image/png)
     * 请参照https://www.w3school.com.cn/media/media_mimeref.asp
     * @param files
     */
    public void uploadFile(String[] fileTypes, File[] files) {
        if (null != mListener){
            mListener.onStart();
        }

        Map<String, RequestBody> map = new HashMap<>();

        int index = 0;
        for (File file : files){
            RequestBody fileBody = RequestBody.create(MediaType.parse(fileTypes[index]), file);
            map.put("file\"; filename=\""+ file.getName(), fileBody);
            index++;
        }

        RetrofitManager<CommonApi> retrofitManager = RetrofitManager.getInstance();
        RxUtils.subscribe(retrofitManager.getApiService(CommonApi.class).uploadFiles(map))
            .subscribe(new BaseObserver<Response<ResponseBean>>() {
                @Override
                public void onSuccess(Response<ResponseBean> responseBean) {
                    if (null != mListener){
                        mListener.onFinish(true,responseBean);
                    }
                }

                @Override
                public void onFailed(ResponseBean responseBean) {
                    if (null != mListener){
                        mListener.onFinish(false,responseBean);
                    }
                }
            });

    }

}


