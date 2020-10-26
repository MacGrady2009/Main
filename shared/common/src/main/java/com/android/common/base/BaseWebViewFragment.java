package com.android.common.base;

import static android.app.Activity.RESULT_OK;
import static com.android.common.constant.Constant.REQUEST_FILE_CHOOSER;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.KeyEvent;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.android.common.R;
import com.android.common.constant.Constant;
import com.android.common.view.webview.DefaultWebChromeClient;
import com.android.common.view.webview.DefaultWebViewClient;
import com.android.common.view.webview.FileDownloadListener;

public class BaseWebViewFragment extends BaseFragment{

    private String mUrl;
    private String mTitle;
    private BaseWebView mWebView;
    private TextView mTvTitle;
    private ProgressBar mProgressBar;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;

    @Override
    protected int onSetRootViewId() {
        return R.layout.fragment_base_webview;
    }

    @Override
    protected void onFindView() {
        super.onFindView();
        mTvTitle = mToolBar.findViewById(R.id.title);
        mProgressBar = mContentView.findViewById(R.id.progressBar);
        mWebView = mContentView.findViewById(R.id.webView);
    }

    @Override
    protected void onInitView() {
        super.onInitView();
        mUrl = (args != null) ? args.getString(Constant.URL_KEY) : "";
        mTitle = (args != null) ? args.getString(Constant.TITLE_KEY) : "";
        initClient();
        loadUrl();
    }

    private void initClient() {
        mWebView.setWebViewClient(new DefaultWebViewClient());
        mWebView.setWebChromeClient(new DefaultWebChromeClient(this, mTitle, mTvTitle, mProgressBar));
        mWebView.setDownloadListener(new FileDownloadListener(this.getActivity()));
    }

    private void loadUrl(){
        mWebView.loadUrl(mUrl);
    }


    public void setUploadCallbackAboveL(ValueCallback<Uri[]> valueCallback){
        this.mUploadCallbackAboveL = valueCallback;
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goBack();
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FILE_CHOOSER){
            onFileChooserResult(requestCode, resultCode, data);
        }
    }

    private void onFileChooserResult(int requestCode, int resultCode, Intent data) {
        if (null == mUploadCallbackAboveL) return;
        if (mUploadCallbackAboveL != null) {
            onActivityResultAboveL(requestCode, resultCode, data);
        } else {
            mUploadCallbackAboveL.onReceiveValue(null);
        }
    }

    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_FILE_CHOOSER || mUploadCallbackAboveL == null) {
            return;
        }
        Uri[] results = null;
        if (resultCode == RESULT_OK) {
            if (data == null) {

            } else {
                String dataString = data.getDataString();
                ClipData clipData = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    clipData = data.getClipData();
                }

                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }

                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        mUploadCallbackAboveL.onReceiveValue(results);
        mUploadCallbackAboveL = null;
        return;
    }

    @Override
    public void onDestroy() {
        if (mWebView != null) {
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.clearHistory();
            mWebView.stopLoading();
            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);
            mWebView.setDownloadListener(null);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    public void goBack(){
        if (mWebView.canGoBack()) {
            //当WebView不是处于第一页面时，返回上一个页面
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            mWebView.goBack();
        } else {
            //当WebView处于第一页面时,直接退出
            getActivity().finish();
        }
    }
}
