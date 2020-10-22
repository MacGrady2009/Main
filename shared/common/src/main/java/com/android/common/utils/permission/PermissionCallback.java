package com.android.common.utils.permission;

public interface PermissionCallback {
    /**成功获取权限、拒绝、不再询问、不再询问解释说明、*/
    int SUCCESS = 1, DENIED = 2,  NEVER_ASK_AGAIN = 3, EXPLAIN = 4;

    void onPermission(int request, @PermissionState int state);
}
