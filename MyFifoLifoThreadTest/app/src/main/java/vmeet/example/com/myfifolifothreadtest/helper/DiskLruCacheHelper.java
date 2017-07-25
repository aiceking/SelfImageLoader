package vmeet.example.com.myfifolifothreadtest.helper;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import vmeet.example.com.myfifolifothreadtest.application.MyApplication;
import vmeet.example.com.myfifolifothreadtest.bean.DiskLruCache;

/**
 * Created by Vmmet on 2017/4/11.
 */
public class DiskLruCacheHelper {
    private  DiskLruCache diskLruCache;
    private   int DISK_CACHE_DEFAULT_SIZE = 100 * 1024 * 1024;// 文件缓存默认 100M
    public DiskLruCacheHelper(){
        initDiskLruCache();
    }
    /**
     * 初始化文件缓存
     */
    public void initDiskLruCache() {
        try {
            File cacheDir = getDiskCacheDir(MyApplication.context, "vmeet_bitmap");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            diskLruCache = DiskLruCache.open(cacheDir, getAppVersion(MyApplication.context), 1, DISK_CACHE_DEFAULT_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public   File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }
    public  int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }
    public  String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    public  String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
    /**
     * 从文件缓存中拿
     */
    public  Bitmap getBitmapFromDisk(String url) {
        try {
            String key = hashKeyForDisk(url);
            DiskLruCache.Snapshot snapShot = diskLruCache.get(key);
            if (snapShot != null) {
                InputStream is = snapShot.getInputStream(0);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void putBitmapToDiskMem(String url,Bitmap bitmap){
        try {
            String key = hashKeyForDisk(url);
            DiskLruCache.Editor editor = diskLruCache.edit(key);
            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(0);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                editor.commit();
            }
            diskLruCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void removeBitmapFromDiskMem(String url){
        try {
            String key = hashKeyForDisk(url);
            diskLruCache.remove(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void removeBitmapAll(){
        try {
            diskLruCache.delete();
            /**重新初始化次磁盘缓存*/
            initDiskLruCache();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public long getSize(){
        return diskLruCache.size();
    }
}
