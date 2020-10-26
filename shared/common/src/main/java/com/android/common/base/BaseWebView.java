package com.android.common.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.android.common.R;

public class BaseWebView extends WebView {
    /*布局算法*/
    public static final int NORMAL = 0;
    public static final int SINGLE_COLUMN = 1;
    public static final int NARROW_COLUMNS = 2;
    /*5.1以上默认禁止了https和http混用，通过该设置可以控制是否开启*/
    public static final int ALWAYS_ALLOW = 0;
    public static final int NEVER_ALLOW = 1;
    public static final int COMPATIBILITY_MODE = 2;
    /*是否与Javascript交互*/
    private boolean javaScriptEnabled;
    /*否支持多窗口，默认值false*/
    private boolean supportMultipleWindows;
    /*是否将图片调整到适合webview的大小*/
    private boolean useWideViewPort;
    /*是否缩放至屏幕的大小*/
    private boolean loadWithOverviewMode;
    /*是否支持缩放*/
    private boolean supportZoom;
    /*布局算法*/
    private int layoutAlgorithm;
    /*WebView是否可以缩放*/
    private boolean builtInZoomControls;
    /*是否显示原生的缩放控件*/
    private boolean displayZoomControls;
    /*文本的缩放倍数*/
    private int textZoom;
    /*字体的大小*/
    private int defaultFontSize;
    /*支持的最小字体大小*/
    private int minimumFontSize;
    /*5.1以上默认禁止了https和http混用，通过该设置可以控制是否开启*/
    private int mixedContentMode;
    /*缓存模式*/
    private int cacheMode;
    /*是否允许WebView使用File协议*/
    private boolean allowFileAccess;
    /*是否可访问Content Provider的资源，默认值 true*/
    private boolean allowContentAccess;
    /*是否允许通过file url加载的Javascript读取其他的本地文件*/
    private boolean allowFileAccessFromFileURLs;
    /*是否允许通过file url加载的Javascript可以访问其他的源，包括其他的文件和http,https等其他的源*/
    private boolean allowUniversalAccessFromFileURLs;
    /*是否支持通过JS打开新窗口*/
    private boolean javaScriptCanOpenWindowsAutomatically;
    /*是否支持自动加载图片*/
    private boolean loadsImagesAutomatically;
    /*是否允许网页执行定位操作*/
    private boolean geolocationEnabled;
    /*是否开启 DOM storage API 功能*/
    private boolean domStorageEnabled;
    /*数据库存储API是否可用*/
    private boolean databaseEnabled;
    /*是否开启 Application Caches 功能 方便构建离线APP*/
    private boolean appCacheEnabled;
    /*缓存路径*/
    private String appCachePath;
    /*是否设置cookie*/
    private boolean cookie;
    private Context context;

    /**
     * @param context
     */
    public BaseWebView(Context context) {
        super(context);
        init(context,null);
    }
    /**
     * @param context
     * @param attributeSet
     */
    public BaseWebView(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
        init(context,attributeSet);
    }

    public BaseWebView(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context,attributeSet,defStyleAttr);
        init(context,attributeSet);
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context,attrs,defStyleAttr,defStyleRes);
        init(context, attrs);
    }

    /**
     * 初始化数据
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        obtainAttr(context, attrs);
        setDefaultAttr();
    }
    /**
     * 获取组件自定义属性
     *
     * @param context
     * @param attrs
     */
    private void obtainAttr(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TXWebView);
        javaScriptEnabled = ta.getBoolean(R.styleable.TXWebView_javaScriptEnabled, true);
        supportMultipleWindows = ta.getBoolean(R.styleable.TXWebView_supportMultipleWindows, true);
        useWideViewPort = ta.getBoolean(R.styleable.TXWebView_useWideViewPort, true);
        loadWithOverviewMode = ta.getBoolean(R.styleable.TXWebView_loadWithOverviewMode, true);
        supportZoom = ta.getBoolean(R.styleable.TXWebView_supportZoom, true);
        layoutAlgorithm = ta.getInt(R.styleable.TXWebView_layoutAlgorithm, 0);
        builtInZoomControls = ta.getBoolean(R.styleable.TXWebView_builtInZoomControls, true);
        displayZoomControls = ta.getBoolean(R.styleable.TXWebView_displayZoomControls, false);
        textZoom = ta.getInt(R.styleable.TXWebView_textZoom, 100);
        defaultFontSize = ta.getInt(R.styleable.TXWebView_defaultFontSize, 16);
        minimumFontSize = ta.getInt(R.styleable.TXWebView_minimumFontSize, 8);
        mixedContentMode = ta.getInt(R.styleable.TXWebView_mixedContentMode, 0);
        cacheMode = ta.getInt(R.styleable.TXWebView_cacheMode, 2);
        allowFileAccess = ta.getBoolean(R.styleable.TXWebView_allowFileAccess, true);
        allowContentAccess = ta.getBoolean(R.styleable.TXWebView_allowContentAccess, true);
        allowFileAccessFromFileURLs = ta.getBoolean(R.styleable.TXWebView_allowFileAccessFromFileURLs, false);
        allowUniversalAccessFromFileURLs = ta.getBoolean(R.styleable.TXWebView_allowUniversalAccessFromFileURLs, true);
        javaScriptCanOpenWindowsAutomatically = ta.getBoolean(R.styleable.TXWebView_javaScriptCanOpenWindowsAutomatically, true);
        loadsImagesAutomatically = ta.getBoolean(R.styleable.TXWebView_loadsImagesAutomatically, true);
        geolocationEnabled = ta.getBoolean(R.styleable.TXWebView_geolocationEnabled, false);
//        userAgentString = ta.getString(R.styleable.TXWebView_userAgentString);
        domStorageEnabled = ta.getBoolean(R.styleable.TXWebView_domStorageEnabled, true);
        databaseEnabled = ta.getBoolean(R.styleable.TXWebView_databaseEnabled, false);
        appCacheEnabled = ta.getBoolean(R.styleable.TXWebView_appCacheEnabled, false);
        appCachePath = ta.getString(R.styleable.TXWebView_appCachePath);
//        appCacheMaxSize = ta.getInt(R.styleable.TXWebView_appCacheMaxSize, 0);
        cookie = ta.getBoolean(R.styleable.TXWebView_cookie, false);
        ta.recycle();
    }

    /**
     * 设置默认属性值
     */
    private void setDefaultAttr() {
        WebSettings settings = getSettings();
        // 存储(storage)
        // 启用HTML5 DOM storage API，默认值 false
        settings.setDomStorageEnabled(domStorageEnabled);
        // 启用Web SQL Database API，这个设置会影响同一进程内的所有WebView，默认值 false
        // 此API已不推荐使用，参考：https://www.w3.org/TR/webdatabase/
        settings.setDatabaseEnabled(databaseEnabled);
        // 启用Application Caches API，必需设置有效的缓存路径才能生效，默认值 false
        // 此API已废弃，参考：https://developer.mozilla.org/zh-CN/docs/Web/HTML/Using_the_application_cache
        if (appCacheEnabled) {
            settings.setAppCacheMaxSize(1024 * 1024 * 10);
            settings.setAppCacheEnabled(appCacheEnabled);
            settings.setAppCachePath(context.getCacheDir().getAbsolutePath());
        }

        // 定位(location)
        settings.setGeolocationEnabled(geolocationEnabled);

        //TODO 是否保存表单数据
        settings.setSaveFormData(true);
        // 是否当webview调用requestFocus时为页面的某个元素设置焦点，默认值 true
        settings.setNeedInitialFocus(true);

        // 是否支持viewport属性，默认值 false
        // 页面通过`<meta name="viewport" ... />`自适应手机屏幕
        settings.setUseWideViewPort(useWideViewPort);
        // 是否使用overview mode加载页面，默认值 false
        // 当页面宽度大于WebView宽度时，缩小使页面宽度等于WebView宽度
        settings.setLoadWithOverviewMode(loadWithOverviewMode);
        //布局算法
        if (layoutAlgorithm == NORMAL) {
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        } else if (layoutAlgorithm == SINGLE_COLUMN) {
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        } else if (layoutAlgorithm == NARROW_COLUMNS) {
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        }

        // 是否支持Javascript，默认值false
        settings.setJavaScriptEnabled(javaScriptEnabled);
        //是否支持多窗口，默认值false
        settings.setSupportMultipleWindows(supportMultipleWindows);
        // 是否可用Javascript(window.open)打开窗口，默认值 false
        settings.setJavaScriptCanOpenWindowsAutomatically(javaScriptCanOpenWindowsAutomatically);

        //资源访问
        settings.setAllowContentAccess(allowContentAccess); // 是否可访问Content Provider的资源，默认值 true
        settings.setAllowFileAccess(allowFileAccess);    // 是否可访问本地文件，默认值 true
        // 是否允许通过file url加载的Javascript读取本地文件，默认值 false
        settings.setAllowFileAccessFromFileURLs(allowFileAccessFromFileURLs);
        // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
        settings.setAllowUniversalAccessFromFileURLs(allowUniversalAccessFromFileURLs);

        // 资源加载
        settings.setLoadsImagesAutomatically(loadsImagesAutomatically); // 是否自动加载图片
        //直接定义死了，不对外暴露，如需要可以再做修改，参考链接https://zhidao.baidu.com/question/555580626372493932.html
        settings.setBlockNetworkImage(false);       // 禁止加载网络图片
        settings.setBlockNetworkLoads(false);       // 禁止加载所有网络资源

        settings.setSupportZoom(supportZoom);          // 是否支持缩放
        settings.setBuiltInZoomControls(builtInZoomControls); // 是否使用内置缩放机制
        settings.setDisplayZoomControls(displayZoomControls);  // 是否显示内置缩放控件

        //默认文本编码，默认值 "UTF-8",编码格式未对外暴露
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setDefaultFontSize(defaultFontSize);        // 默认文字尺寸，默认值16，取值范围1-72
        //TODO 待确认
        settings.setDefaultFixedFontSize(16);   // 默认等宽字体尺寸，默认值16
        settings.setMinimumFontSize(minimumFontSize);         // 最小文字尺寸，默认值 8
        //TODO 待确认
        settings.setMinimumLogicalFontSize(8);  // 最小文字逻辑尺寸，默认值 8
        settings.setTextZoom(textZoom);              // 文字缩放百分比，默认值 100


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //TODO 用户是否需要通过手势播放媒体(不会自动播放)，默认值 true
            settings.setMediaPlaybackRequiresUserGesture(true);
        }
        //5.0以上允许加载http和https混合的页面(5.0以下默认允许，5.0+默认禁止)
        if (mixedContentMode == ALWAYS_ALLOW) {
            settings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        } else if (mixedContentMode == NEVER_ALLOW) {
            settings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_NEVER_ALLOW);
        } else if (mixedContentMode == COMPATIBILITY_MODE) {
            settings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
    }
}
