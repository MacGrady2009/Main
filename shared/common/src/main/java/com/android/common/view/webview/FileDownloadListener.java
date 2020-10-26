package com.android.common.view.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.DownloadListener;

/**
 * 实现webview下载文件
 */
public class FileDownloadListener implements DownloadListener {
    private Context mContext;

    public FileDownloadListener(Activity context) {
        mContext = context;

    }

    @Override
    public void onDownloadStart(final String url, String userAgent, String contentDisposition, String mimetype, final long contentLength) {
        Log.d("onDownloadStart", (String.format("url: %s, mimetype: %s, contentDisposition: %s, contentLength: %d", url, mimetype, contentDisposition, contentLength)));

//        Intent i = new Intent(mContext, DownloadActivity.class);
//        i.putExtra(DownloadActivity.EXTRA_URL, url);
//        i.putExtra(DownloadActivity.EXTRA_DISPOSITION, contentDisposition);
//        i.putExtra(DownloadActivity.EXTRA_MIMETYPE, mimetype);
//        i.putExtra(DownloadActivity.EXTRA_LENGTH, contentLength);
//        mContext.startActivity(i);
    }
}
