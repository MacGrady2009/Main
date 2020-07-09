package com.android.common.network;

import android.util.Log;
import com.android.common.BuildConfig;
import com.android.common.Constant;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager<T extends CommonApi> {

    public static final String TAG = "RetrofitManager";
    public static RetrofitManager instance;
    private Retrofit mRetrofit;
    private T mApiService;
    private static HttpLoggingInterceptor mLogInterceptor;

    public static RetrofitManager getInstance(){
        if (null == instance){
            instance = new RetrofitManager();
        }
        return instance;
    }

    private Retrofit getRetrofit(){
        return getRetrofit(Constant.BASE_URL);
    }

    private Retrofit getRetrofit(String baseUrl){
        mRetrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build();
        return mRetrofit;
    }

    private Retrofit getRetrofit(String baseUrl,Interceptor interceptor){
        mRetrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getOkHttpClient(interceptor))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build();
        return mRetrofit;
    }

    private Retrofit getRetrofit(String baseUrl,long connectTimeout
        ,long readTimeout, long writeTimeout){
        mRetrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getOkHttpClient(connectTimeout,readTimeout,writeTimeout))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build();
        return mRetrofit;
    }

    private Retrofit getRetrofit(String baseUrl,long connectTimeout
        ,long readTimeout, long writeTimeout, Interceptor interceptor){
        mRetrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getOkHttpClient(connectTimeout,readTimeout,writeTimeout,interceptor))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build();
        return mRetrofit;
    }

    public T getApiService(Class<T> apiServiceClass){
        getRetrofit();
        if (null == mApiService){
            mApiService = mRetrofit.create(apiServiceClass);
        }
        return mApiService;
    }

    public T getApiService(String baseUrl ,Class<T> apiServiceClass){
        getRetrofit(baseUrl);
        if (null == mApiService){
            mApiService = mRetrofit.create(apiServiceClass);
        }
        return mApiService;
    }

    public T getApiService(String baseUrl , Interceptor interceptor ,Class<T> apiServiceClass){
        getRetrofit(baseUrl,interceptor);
        if (null == mApiService){
            mApiService = mRetrofit.create(apiServiceClass);
        }
        return mApiService;
    }

    public T getApiService(String baseUrl ,long connectTimeout
        ,long readTimeout, long writeTimeout,Class<T> apiServiceClass){
        getRetrofit(baseUrl,connectTimeout,readTimeout,writeTimeout);
        if (null == mApiService){
            mApiService = mRetrofit.create(apiServiceClass);
        }
        return mApiService;
    }

    public T getApiService(String baseUrl ,long connectTimeout
        ,long readTimeout, long writeTimeout,Interceptor interceptor, Class<T> apiServiceClass){
        getRetrofit(baseUrl,connectTimeout,readTimeout,writeTimeout,interceptor);
        if (null == mApiService){
            mApiService = mRetrofit.create(apiServiceClass);
        }
        return mApiService;
    }

    /**
     * 获取Okhttp
     *
     * @return
     */
    private static OkHttpClient getOkHttpClient() {
        return getOkHttpClient(Constant.HTTP_CONNECT_TIMEOUT,Constant.HTTP_READ_TIMEOUT
            ,Constant.HTTP_WRITE_TIMEOUT);
    }

    /**
     * 获取Okhttp
     *
     * @return
     */
    private static OkHttpClient getOkHttpClient(Interceptor interceptor) {
        return getOkHttpClient(Constant.HTTP_CONNECT_TIMEOUT,Constant.HTTP_READ_TIMEOUT
            ,Constant.HTTP_WRITE_TIMEOUT,interceptor);
    }

    /**
     * 获取Okhttp
     *
     * @return
     */
    private static OkHttpClient getOkHttpClient(long connectTimeout
        ,long readTimeout, long writeTimeout) {
        return getOkHttpClient(connectTimeout,readTimeout,writeTimeout, null);
    }

    /**
     * 获取Okhttp
     *
     * @return
     */
    private static OkHttpClient getOkHttpClient(long connectTimeout
        ,long readTimeout, long writeTimeout, Interceptor interceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
            .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)//设置连接超时时间
            .readTimeout(readTimeout, TimeUnit.MILLISECONDS)//设置读取超时时间
            .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(logInterceptor());
        }

        if (null != interceptor){
            builder.addInterceptor(interceptor);
        }

        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        if (sslParams != null) {
            builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        }
        builder.hostnameVerifier(HttpsUtils.getHttpsVerifier());

        OkHttpClient okHttpClient= builder.build();
        return okHttpClient;
    }

    private static HttpLoggingInterceptor logInterceptor() {
        if (null == mLogInterceptor){
            mLogInterceptor = new HttpLoggingInterceptor((msg) -> {
                Log.d(TAG,msg);
            });
            mLogInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        return mLogInterceptor;
    }
}
