package com.android.common.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.FileUtils;
import android.os.RecoverySystem;
import android.text.TextUtils;
import com.android.common.network.ProgressListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import okhttp3.ResponseBody;

public class FileUtil {


    public static File saveFile(ResponseBody responseBody, String destFileDir
        , String destFileName , ProgressListener listener) throws IOException {
        return saveFileWithCallback(responseBody,destFileDir,destFileName,null);
    }

    /**
     * 将文件写入本地
     *
     * @param destFileDir  目标文件夹
     * @param destFileName 目标文件名
     * @return 写入完成的文件
     * @throws IOException IO异常
     */
    public static File saveFileWithCallback(ResponseBody responseBody, String destFileDir
        , String destFileName , ProgressListener listener) throws IOException {
        InputStream is = responseBody.byteStream();
        long contentLength = responseBody.contentLength();
        long totalBytesRead = 0L;

        byte[] buf = new byte[2048];
        int len;
        FileOutputStream fos = null;
        try {
            File dir = new File(destFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, destFileName);
            if (file.exists()) {
                file.delete();
            }
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
                totalBytesRead += len != -1 ? len : 0;
                if (listener != null ){
                    listener.onProgress(contentLength, totalBytesRead);
                }
            }
            fos.flush();
            return file;

        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean deleteFile(String url) {
        boolean result = false;
        File file = new File(url);
        if (file.exists()) {
            result = file.delete();
        }
        return result;
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }


    /**
     * 建立目录
     * @param dir 目录
     * @return 是否成功创建
     */
    public static boolean makeDirs(String dir) {
        if (dir.startsWith(Environment.getExternalStorageDirectory().getAbsolutePath())) {
            //判断sd卡状态
            if (checkSdcard()) {
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    boolean flag = fDir.mkdirs();
                    return flag;
                }
                return true;
            } else {
                return false;
            }
        } else {
            File fDir = new File(dir);
            if (!fDir.exists()) {
                boolean flag = fDir.mkdirs();
                return flag;
            }
            return true;
        }
    }

    public static boolean makeDirs(File dir) {
        return makeDirs(dir.getAbsolutePath());
    }

    public static boolean createNoMedia(String parent) {
        if (!makeDirs(parent)) return false;
        File file = new File(parent, ".nomedia");
        if (file.exists()) {
            if (file.isFile()) {
                return true;
            } else {
                FileUtil.deleteFile(file.getAbsolutePath());
            }
        }
        try {
            return file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     *check apk 的完整性
     */
    public static boolean checkApk(Context context, String filePath) {
        boolean result = false;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
            if (info != null) {
                result = true;//完整
            }
        } catch (Exception ignore) {
        }
        return result;
    }

    /**
     * 通知扫描文件
     * @param context context
     * @param file file
     */
    public static void scanMediaFile(Context context, File file){
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }

    private static boolean checkSdcard() {
        return TextUtils.equals(Environment.MEDIA_MOUNTED, Environment.getExternalStorageState());
    }
}
