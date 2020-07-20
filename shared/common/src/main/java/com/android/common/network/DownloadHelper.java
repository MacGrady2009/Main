package com.android.common.network;

import com.android.common.utils.FileUtil;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.io.File;

public class DownloadHelper {

    ProgressListener  mListener;

    public DownloadHelper(ProgressListener  listener){
        mListener = listener;
    }

    /**
     * 文件下载
     * @param url
     * @param destDir
     * @param fileName
     */
    public void downloadFile(String url, final String destDir, final String fileName){
        if (null != mListener){
            mListener.onStart();
        }
        RetrofitManager<CommonApi> retrofitManager = RetrofitManager.getInstance();
        retrofitManager.getApiService(CommonApi.class)
            .download(url)
            .subscribeOn(Schedulers.io())//请求网络 在调度者的io线程
            .observeOn(Schedulers.io()) //指定线程保存文件
            .observeOn(Schedulers.computation())
            .map(responseBody-> FileUtil.saveFileWithCallback(responseBody, destDir, fileName,mListener))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new BaseObserver<File>() {
                @Override
                public void onSuccess(File file, ResponseBean responseBean) {
                    if (null != mListener){
                        mListener.onFinish(true,file,"");
                    }
                }

                @Override
                public void onFailed(ResponseBean responseBean) {
                    if (null != mListener){
                        mListener.onFinish(false,null,responseBean.getMessage());
                    }
                }
            });


    }

    public void cancelDownLoad(){

    }
}
