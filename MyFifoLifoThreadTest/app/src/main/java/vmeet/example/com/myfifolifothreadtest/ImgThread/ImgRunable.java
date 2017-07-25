package vmeet.example.com.myfifolifothreadtest.ImgThread;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import vmeet.example.com.myfifolifothreadtest.bean.ImgSize;
import vmeet.example.com.myfifolifothreadtest.bean.ImgViewBean;
import vmeet.example.com.myfifolifothreadtest.helper.DiskLruCacheHelper;
import vmeet.example.com.myfifolifothreadtest.helper.ImageHelper;
import vmeet.example.com.myfifolifothreadtest.helper.LruCacheHelper;

/**
 * Created by Vmmet on 2017/4/7.
 */
public class ImgRunable implements Runnable {
private ImageView imageView;
    private String url;
    private Handler handler;
    private ImageHelper imageHelper;
    private LruCacheHelper lruCacheHelper;
    private DiskLruCacheHelper diskLruCacheHelper;
    public ImgRunable(ImageView imageView, String url,Handler handler,
                      ImageHelper imageHelper,LruCacheHelper lruCacheHelper,
                      DiskLruCacheHelper diskLruCacheHelper){
        this.imageView=imageView;
        this.url=url;
        this.handler=handler;
        this.imageHelper=imageHelper;
        this.lruCacheHelper=lruCacheHelper;
        this.diskLruCacheHelper=diskLruCacheHelper;
    }
    @Override
    public void run() {
        Bitmap bitmap;
        bitmap=lruCacheHelper.getBitmapFromMem(url);
        if (bitmap!=null){
            sendImgToUiThread(bitmap);
        }else{
            bitmap=diskLruCacheHelper.getBitmapFromDisk(url);
            if (bitmap!=null){
                lruCacheHelper.putBitmapToMem(url, bitmap);
                sendImgToUiThread(bitmap);
            }else{
                ImgSize imgSize= imageHelper.getImageViewSize(imageView);
                bitmap= imageHelper.getLocalImg(url, imgSize.getWidth(), imgSize.getHeight());
                if (bitmap!=null){
                    lruCacheHelper.putBitmapToMem(url, bitmap);
                    diskLruCacheHelper.putBitmapToDiskMem(url,bitmap);
                    sendImgToUiThread(bitmap);
                }else {
                    //去网络上加载
                }
            }
    }
    }
    private void sendImgToUiThread(Bitmap bitmap) {
        ImgViewBean imgViewBean=new ImgViewBean(imageView,url,bitmap);
        Message message = Message.obtain();
        message.obj = imgViewBean;
        handler.sendMessage(message);
    }
}
