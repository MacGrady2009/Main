package com.android.common.utils;

import android.content.Context;
import com.bumptech.glide.Glide;

public class GlideUtils {

    public void clearCache(Context context){
        Glide.get(context).clearMemory();
        Glide.get(context).clearDiskCache();
    }

}
