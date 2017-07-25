package vmeet.example.com.myfifolifothreadtest.helper;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by Vmmet on 2017/4/11.
 */
public class LruCacheHelper {
    private static LruCache<String, Bitmap> memCache;
    public LruCacheHelper(){
        initMemCache();
    }
    /**
     * 初始化内存缓存
     */
    public void initMemCache() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory()/1024);
        int cacheSize = maxMemory / 8;
        memCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount()/1024;
            }
        };
    }
    /**
     * 从内存缓存中拿
     */
    public  Bitmap getBitmapFromMem(String url) {
        return memCache.get(url);
    }

    /**
     * 加入到内存缓存中
     */
    public  void putBitmapToMem(String url, Bitmap bitmap) {
        if (getBitmapFromMem(url) == null)
        {
            if (bitmap != null){
                memCache.put(url, bitmap);
            }
        }
    }
    public void removeBitmapFromMem(String url){
        memCache.remove(url);
    }
    public void removeBitmapAll(){
        memCache.evictAll();
    }
}
