package com.android.common.utils.permission;

import androidx.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({PermissionCallback.SUCCESS, PermissionCallback.DENIED,
        PermissionCallback.EXPLAIN,PermissionCallback.NEVER_ASK_AGAIN})
@Retention(RetentionPolicy.SOURCE)
public @interface PermissionState {
}
