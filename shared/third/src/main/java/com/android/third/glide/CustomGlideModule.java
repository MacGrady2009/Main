package com.android.third.glide;

import android.content.Context;
import android.os.Environment;
import androidx.annotation.NonNull;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;
import java.io.File;

@GlideModule
public class CustomGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        String filePath = context.getCacheDir() + "/glide";
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }
       //重新设置内存限制为10M
        builder.setMemoryCache(new LruResourceCache(10 * 1024 * 1024));
        //设置缓存目录、大小100M
        builder.setDiskCache(new DiskLruCacheFactory(filePath, 100 * 1024 * 1024));
    }

}
