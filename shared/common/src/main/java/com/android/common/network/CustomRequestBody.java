package com.android.common.network;

import androidx.annotation.Nullable;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

public class CustomRequestBody extends RequestBody {
    private final ProgressListener mListener;
    private final RequestBody requestBody;
    private BufferedSink bufferedSink;

    public CustomRequestBody(ProgressListener listener, RequestBody requestBody) {
        this.mListener = listener;
        this.requestBody = requestBody;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    //关键方法
    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (null == bufferedSink) bufferedSink = Okio.buffer(sink(sink));
        requestBody.writeTo(bufferedSink);
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();
    }

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            long bytesWriting = 0l;
            long contentLength = 0l;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (0 == contentLength) contentLength = contentLength();
                bytesWriting += byteCount;
                if (null != mListener){
                    mListener.onProgress(contentLength, bytesWriting);
                }
            }
        };
    }
}
