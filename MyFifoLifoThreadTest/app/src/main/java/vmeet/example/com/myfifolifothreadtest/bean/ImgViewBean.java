package vmeet.example.com.myfifolifothreadtest.bean;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by Vmmet on 2017/4/7.
 */
public class ImgViewBean {
    public ImageView imageView;
    public String url;
    public Bitmap bitmap;
    public ImgViewBean(ImageView imageView, String url, Bitmap bitmap){
        this.imageView=imageView;
        this.url=url;
        this.bitmap=bitmap;
    }
    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
