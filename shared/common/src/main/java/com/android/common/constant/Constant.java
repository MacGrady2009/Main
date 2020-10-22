package com.android.common.constant;


public class Constant {
    /**
     * http 连接超时
     */
    public static final long HTTP_CONNECT_TIMEOUT = 10000;
    /**
     * http 读超时
     */
    public static final long HTTP_READ_TIMEOUT = 10000;
    /**
     * http 写超时
     */
    public static final long HTTP_WRITE_TIMEOUT = 15000;

    /**
     * META_DATA SCHEME
     */
    public static final String META_DATA_SCHEME_KEY = "scheme";

    /**
     * META_DATA HOST
     */
    public static final String META_DATA_HOST_KEY = "host";
    /**
     * META_DATA PORT
     */
    public static final String META_DATA_PORT_KEY = "port";
    /**
     * META_DATA auth_url
     */
    public static final String META_DATA_AUTH_URL_KEY = "auth_url";
    /**
     * META_DATA file_provider_auth
     */
    public static final String META_DATA_FILE_PROVIDER_AUTH_KEY = "file_provider_auth";

    /**
     * http 请求地址
     */
    public static final String BASE_URL = "https://e937e576-bb1a-47df-862b-2bfa2fbcacc7.mock.pstmn.io";
    /**
     * file provider auth
     */
    public static final String FILE_PROVIDER_AUTH = "file_provider_auth";
    /**
     * apk 解压类型
     */
    public static final String PACKAGE_ARCHIVE_DATA_TYPE = "application/vnd.android.package-archive";
    /**
     * ad页面广告播放时间
     */
    public static final long AD_TIME = 3;

    /**
     * 分页每页条目数
     */
    public static final long ITEMS_EVERY_PAGE = 20;

    /**
     * 权限请求码
     */
    public static final int PERMISSIONS_REQUEST_CODE = 1;
    /**
     * 写存储权限
     */
    public static final int PERMISSION_STORAGE_REQUEST_CODE = PERMISSIONS_REQUEST_CODE + 1;
    /**
     * 相机、相册权限
     */
    public static final int PERMISSION_CAMERA_REQUEST_CODE = PERMISSION_STORAGE_REQUEST_CODE + 1;
    /**
     * 读取电话状态和识别码
     */
    public static final int PERMISSION_PHONE_REQUEST_CODE = PERMISSION_CAMERA_REQUEST_CODE + 1;
    /**
     * 地理位置
     */
    public static final int PERMISSION_LOCATION_REQUEST_CODE = PERMISSION_PHONE_REQUEST_CODE  + 1;
    /**
     * 麦克风位置
     */
    public static final int PERMISSION_MICROPHONE_REQUEST_CODE = PERMISSION_LOCATION_REQUEST_CODE + 1;
    /**
     * 安装apk权限
     */
    public static final int PERMISSION_INSTALL_REQUEST_CODE = PERMISSION_MICROPHONE_REQUEST_CODE + 1;
}
