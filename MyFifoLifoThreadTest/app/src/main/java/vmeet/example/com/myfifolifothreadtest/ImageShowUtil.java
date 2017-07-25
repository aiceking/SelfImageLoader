package vmeet.example.com.myfifolifothreadtest;

import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import vmeet.example.com.myfifolifothreadtest.ImgThread.ImageRunnable;
import vmeet.example.com.myfifolifothreadtest.bean.ImgViewBean;
import vmeet.example.com.myfifolifothreadtest.enumeration.LoadType;
import vmeet.example.com.myfifolifothreadtest.helper.DiskLruCacheHelper;
import vmeet.example.com.myfifolifothreadtest.helper.ImageHelper;
import vmeet.example.com.myfifolifothreadtest.helper.LruCacheHelper;

/**
 * Created by Vmmet on 2017/4/6.
 */
public class ImageShowUtil {
    private static ImageShowUtil imageUtil;
    private  ExecutorService imgThreadPool;/**加载图片线程池*/
    private Handler handler;/**线程间通信*/
    private ImageHelper imageHelper;/**加载图片帮助类*/
    private LruCacheHelper lruCacheHelper;/**内存缓存帮助类*/
    private DiskLruCacheHelper diskLruCacheHelper;/**磁盘缓存帮助类*/
    private ImageShowUtil(){
        initThreadPool();
        imageHelper=new ImageHelper();
        lruCacheHelper=new LruCacheHelper();
        diskLruCacheHelper=new DiskLruCacheHelper();
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //显示图片，防止错位
                ImgViewBean imgViewBean=(ImgViewBean)msg.obj;
                if (imgViewBean.getImageView().getTag().toString().equals(imgViewBean.getUrl())){
                    imgViewBean.getImageView().setImageBitmap(imgViewBean.getBitmap());
                }
            }
        };
    }
    private void initThreadPool() {
        int number=Runtime.getRuntime().availableProcessors()+1;
        imgThreadPool = new ThreadPoolExecutor(number, number, 0L, TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>());
    }
    /**单例模式*/
    public static ImageShowUtil getInstance(){
        if (imageUtil == null) {
            synchronized (ImageShowUtil.class) {
                if (imageUtil == null) {
                    imageUtil = new ImageShowUtil();
                }
            }
        }
        return imageUtil;
    }
    public long getDiskSize(){
        return diskLruCacheHelper.getSize();
    }
    public void deleteCacheWithUrl(String url){
        lruCacheHelper.removeBitmapFromMem(url);
        diskLruCacheHelper.removeBitmapFromDiskMem(url);
    }
    public void deleteAllCache(){
        lruCacheHelper.removeBitmapAll();
        diskLruCacheHelper.removeBitmapAll();
    }
    /**加载本地图片指定了任务队列的加载方式，且使用占位图*/
    public void loadImg(LoadType loadType,String path,ImageView imageView,int placeHolder){
        if (imageView.getTag()==null){
            imageView.setTag(path);
            imageView.setImageResource(placeHolder);
        }else if (!imageView.getTag().toString().equals(path)) {
            imageView.setTag(path);
            imageView.setImageResource(placeHolder);
        }
        imgThreadPool.execute(new ImageRunnable(loadType, imageView, path, handler, imageHelper, lruCacheHelper, diskLruCacheHelper));
    }
    /**加载本地图片未指定任务队列的加载方式（默认LIFO），且使用占位图*/
    public void loadImg(String path,ImageView imageView,int placeHolder){
        if (imageView.getTag()==null){
            imageView.setTag(path);
            imageView.setImageResource(placeHolder);
        }else if (!imageView.getTag().toString().equals(path)) {
            imageView.setTag(path);
            imageView.setImageResource(placeHolder);
        }
            imgThreadPool.execute(new ImageRunnable(imageView, path,handler,imageHelper,lruCacheHelper,diskLruCacheHelper));
    }
    /**加载本地图片指定了任务队列的加载方式，未使用占位图*/
    public void loadImg(LoadType loadType,String path,ImageView imageView){
        if (imageView.getTag()==null){
            imageView.setTag(path);
        }else if (!imageView.getTag().toString().equals(path)) {
            imageView.setTag(path);
        }
        imgThreadPool.execute(new ImageRunnable(loadType,imageView, path,handler,imageHelper, lruCacheHelper,diskLruCacheHelper));
    }
    /**加载本地图片未指定任务队列的加载方式（默认LIFO），未使用占位图*/
    public void loadImg(String path,ImageView imageView){
        if (imageView.getTag()==null){
            imageView.setTag(path);
        }else if (!imageView.getTag().toString().equals(path)) {
            imageView.setTag(path);
        }
        imgThreadPool.execute(new ImageRunnable(imageView, path,handler,imageHelper,lruCacheHelper,diskLruCacheHelper));
    }
}
