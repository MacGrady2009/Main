package com.android.common.view.webview;

import static com.android.common.constant.Constant.REQUEST_FILE_CHOOSER;
import static com.android.common.constant.Constant.URL_KEY;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.android.common.base.BaseWebView;
import com.android.common.base.BaseWebViewActivity;
import com.android.common.base.BaseWebViewFragment;

/**
 * 默认WebChromeClient
 */
public class DefaultWebChromeClient extends WebChromeClient {
    //进度条百分之百
    public static final int A_HUNDRED_PER_CENT = 100;
    private Fragment mFragment;
    private String mTitle;
    private TextView mTvTopTitle;
    private ProgressBar mProgressBar;

    public DefaultWebChromeClient(Fragment fragment, String title, TextView tvTopTitle, ProgressBar progressBar) {
        mFragment = fragment;
        mTitle = title;
        mTvTopTitle = tvTopTitle;
        mProgressBar = progressBar;
    }

    @Override
    public void onReceivedTitle(WebView webView, String s) {
        super.onReceivedTitle(webView, s);
        if (!TextUtils.isEmpty(mTitle)) {
            mTvTopTitle.setText(mTitle);
        } else {
            mTvTopTitle.setText(mTitle != null && !mTitle.isEmpty() ? mTitle : webView.getTitle());
        }
    }

    @Override
    public void onProgressChanged(WebView webView, int i) {
        if (i == A_HUNDRED_PER_CENT) {
            mProgressBar.setVisibility(View.GONE);//加载完网页进度条消失
        } else {
            mProgressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
            mProgressBar.setProgress(i);//设置进度值
        }
        super.onProgressChanged(webView, i);
    }

    @Override
    public boolean onJsAlert(WebView webView, String s, String s1, JsResult jsResult) {
        return super.onJsAlert(webView, s, s1, jsResult);
    }

    @Override
    public boolean onShowFileChooser(WebView webView,
                                     ValueCallback<Uri[]> valueCallback,
                                     FileChooserParams fileChooserParams) {
        if (mFragment!=null && mFragment instanceof BaseWebViewFragment){
            BaseWebViewFragment webViewFragment = (BaseWebViewFragment) mFragment;
            webViewFragment.setUploadCallbackAboveL(valueCallback);
        }
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        mFragment.startActivityForResult(Intent.createChooser(i, "File Browser"), REQUEST_FILE_CHOOSER);
        return true;

    }
    /**
     * 打开和关闭Window(多窗口)
     * 超级链接标签a里有一个target属性，其意义是决定"是否在新窗口/标签页中打开链接"，如果不写target=”_blank”那么就是在相同的标签页打开，如果写了，就是在新的空白标签页中打开。比如：
     *
     * <a href="https://www.taobao.com/" title="淘宝" target="_blank">新窗口打开链接</a>
     * <p>
     * 而我们WebView默认是不支持target这个属性的
     *
     * @param view
     * @param isDialog
     * @param isUserGesture
     * @param resultMsg
     * @return
     */
    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        BaseWebView newWebView = new BaseWebView(view.getContext());
        newWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                // 在此处进行跳转URL的处理, 一般情况下_black需要重新打开一个页面,
                Intent intent = new Intent(mFragment.getActivity(), BaseWebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(URL_KEY, request.getUrl().toString());
                intent.putExtras(bundle);
                mFragment.startActivity(intent);
                return true;
            }

        });
        WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
        transport.setWebView(newWebView);
        resultMsg.sendToTarget();
        return true;
    }
}
