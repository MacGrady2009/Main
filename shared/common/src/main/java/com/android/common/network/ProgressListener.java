package com.android.common.network;

public interface ProgressListener<T> {

    void onStart();

    void onProgress(long total ,long progress);

    void onFinish(boolean success, T result);
}
