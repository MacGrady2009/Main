package com.android.common.network;

import com.android.common.BuildConfig;
import com.android.common.constant.Constant;
import com.android.common.base.BaseApi;
import com.android.common.utils.LogUtil;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager<S extends BaseApi> {

    public static final String TAG = "RetrofitManager";
    public static RetrofitManager instance;
    private Retrofit mRetrofit;
    Retrofit.Builder mBuilder;
    private S mApiService;
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
        mRetrofit = getRetrofitBuilder(baseUrl).build();
        return mRetrofit;
    }

    private Retrofit getRetrofit(String baseUrl,Interceptor... interceptors){
        mRetrofit = getRetrofitBuilder(baseUrl,interceptors).build();
        return mRetrofit;
    }

    private Retrofit getRetrofit(String baseUrl,long connectTimeout
        ,long readTimeout, long writeTimeout){
        mRetrofit = getRetrofitBuilder(baseUrl,connectTimeout,readTimeout,writeTimeout).build();
        return mRetrofit;
    }

    private Retrofit getRetrofit(String baseUrl,long connectTimeout
        ,long readTimeout, long writeTimeout, Interceptor... interceptors){
        mRetrofit = getRetrofitBuilder(baseUrl,connectTimeout,readTimeout,writeTimeout,interceptors).build();
        return mRetrofit;
    }

    private Retrofit.Builder getRetrofitBuilder(String baseUrl ,Interceptor... interceptors){
        mBuilder = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getOkHttpClient(interceptors))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create());
        return mBuilder;
    }

    private Retrofit.Builder getRetrofitBuilder(String baseUrl,long connectTimeout
        ,long readTimeout, long writeTimeout, Interceptor... interceptors){
        mBuilder = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getOkHttpClient(connectTimeout,readTimeout,writeTimeout,interceptors))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create());
        return mBuilder;
    }

    public S getApiService(Class<S> apiServiceClass){
        getRetrofit();
        mApiService = mRetrofit.create(apiServiceClass);
        return mApiService;
    }

    public S getApiService(String baseUrl ,Class<S> apiServiceClass){
        getRetrofit(baseUrl);
        mApiService = mRetrofit.create(apiServiceClass);
        return mApiService;
    }

    public S getApiService(String baseUrl , Interceptor interceptor ,Class<S> apiServiceClass){
        getRetrofit(baseUrl,interceptor);
        mApiService = mRetrofit.create(apiServiceClass);
        return mApiService;
    }

    public S getApiService(String baseUrl ,long connectTimeout
        ,long readTimeout, long writeTimeout,Class<S> apiServiceClass){
        getRetrofit(baseUrl,connectTimeout,readTimeout,writeTimeout);
        mApiService = mRetrofit.create(apiServiceClass);
        return mApiService;
    }

    public S getApiService(String baseUrl ,long connectTimeout
        ,long readTimeout, long writeTimeout, Class<S> apiServiceClass, Interceptor... interceptors){
        getRetrofit(baseUrl,connectTimeout,readTimeout,writeTimeout,interceptors);
        mApiService = mRetrofit.create(apiServiceClass);
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
    private static OkHttpClient getOkHttpClient(Interceptor... interceptors) {
        return getOkHttpClient(Constant.HTTP_CONNECT_TIMEOUT,Constant.HTTP_READ_TIMEOUT
            ,Constant.HTTP_WRITE_TIMEOUT,interceptors);
    }

    /**
     * 获取Okhttp
     *
     * @return
     */
    private static OkHttpClient getOkHttpClient(long connectTimeout
        ,long readTimeout, long writeTimeout) {
        return getOkHttpClient(connectTimeout, readTimeout, writeTimeout, new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                return null;
            }
        });
    }

    /**
     * 获取Okhttp
     *
     * @return
     */
    private static OkHttpClient getOkHttpClient(long connectTimeout
        ,long readTimeout, long writeTimeout, Interceptor... interceptors) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
            .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)//设置连接超时时间
            .readTimeout(readTimeout, TimeUnit.MILLISECONDS)//设置读取超时时间
            .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(logInterceptor());
        }

        //settings more interceptors
        if (null != interceptors && interceptors.length > 0){
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
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
                LogUtil.d(TAG,msg);
            });
            mLogInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        return mLogInterceptor;
    }
}
