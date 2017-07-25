package vmeet.example.com.myfifolifothreadtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import vmeet.example.com.myfifolifothreadtest.bean.ImgSize;
import vmeet.example.com.myfifolifothreadtest.helper.ImageHelper;

public class ImgActivity extends AppCompatActivity {
private ImageView imageView;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);
        imageView=(ImageView)findViewById(R.id.iv);
        textView=(TextView)findViewById(R.id.tv);
        ImgSize size=new ImageHelper().getImageViewSize(imageView);
        textView.setText("高="+size.getHeight()+"  "+"宽="+size.getWidth());
    }

}
