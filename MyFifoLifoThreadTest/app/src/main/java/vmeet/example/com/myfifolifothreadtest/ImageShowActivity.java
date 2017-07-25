package vmeet.example.com.myfifolifothreadtest;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import vmeet.example.com.myfifolifothreadtest.adapter.ImageAdapter;
import vmeet.example.com.myfifolifothreadtest.view.MyImgGridView;

public class ImageShowActivity extends AppCompatActivity implements View.OnClickListener{
    private MyImgGridView gridView;
    private int IMAGE_OK=10;
    private List<String> list_images;
    private Button btn_number;
    public static int width;
    private ImageView iv;
    private ImageAdapter adapter;
    private Handler ImageHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==IMAGE_OK){
                Log.v("ss=", list_images.get(list_images.size() - 1));
                btn_number.setText(list_images.size() + "张");
                adapter=new ImageAdapter(ImageShowActivity.this, list_images);
                gridView.setAdapter(adapter);
                Log.v("diskSize=", ImageShowUtil.getInstance().getDiskSize()/1024/1024+" M");
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        iv.setVisibility(View.VISIBLE);
                        ImageShowUtil.getInstance().loadImg(list_images.get(i),iv);
                    }
                });
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show);
        gridView=(MyImgGridView)findViewById(R.id.grid);
        iv=(ImageView)findViewById(R.id.iv);
        iv.setVisibility(View.GONE);
        iv.setOnClickListener(this);
        btn_number=(Button)findViewById(R.id.btn_number);
        btn_number.setOnClickListener(this);
        list_images=new ArrayList<>();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        width = point.x;
        getImage();
    }
    public void getImage(){
        new Runnable(){
            @Override
            public void run() {
                Uri imageuri= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver imageResolver=ImageShowActivity.this.getContentResolver();
                Cursor imageCursor = imageResolver.query(imageuri, null,
                        MediaStore.MediaColumns.MIME_TYPE + "=? or "
                                + MediaStore.MediaColumns.MIME_TYPE + "=?", new String[]{
                                "image/jpeg", "image/png"},
                        MediaStore.MediaColumns.DATE_MODIFIED);
                while (imageCursor.moveToNext()){
                    String path = imageCursor.getString(imageCursor
                            .getColumnIndex(MediaStore.MediaColumns.DATA));
                    list_images.add(path);
                }
                //扫描完释放
                imageCursor.close();
                ImageHandler.sendEmptyMessage(IMAGE_OK);
            }
        }.run();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv:
                    iv.setVisibility(View.GONE);
                    iv.setImageBitmap(null);
                break;
            case R.id.btn_number:
//                for (int i=0;i<30;i++){
//                    ImageShowUtil.getInstance().deleteCacheWithUrl(list_images.get(i));
//                }
//                ImageShowUtil.getInstance().deleteAllCache();
                adapter.notifyDataSetChanged();
                break;
        }
    }
}
