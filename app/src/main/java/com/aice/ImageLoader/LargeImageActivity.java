package com.aice.ImageLoader;

import android.os.Bundle;

import com.shizhefei.view.largeimage.LargeImageView;
import com.shizhefei.view.largeimage.factory.InputStreamBitmapDecoderFactory;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LargeImageActivity extends AppCompatActivity {

    @BindView(R.id.lv_large)
    LargeImageView lvLarge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large_image);
        ButterKnife.bind(this);
        try {
            lvLarge.setImage(new InputStreamBitmapDecoderFactory(getAssets().open("111.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
