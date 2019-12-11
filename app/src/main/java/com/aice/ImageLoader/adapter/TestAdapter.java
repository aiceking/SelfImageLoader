package com.aice.ImageLoader.adapter;

import android.os.Build;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.aice.ImageLoader.R;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class TestAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public TestAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ((TextView)helper.getView(R.id.tv_name)).setText(item);
//        ((ImageView)helper.getView(R.id.iv_head)).setImageResource(R.mipmap.ic_launcher);
        ImageView imageView=(ImageView)helper.getView(R.id.iv_head);
        Glide.with(imageView.getContext()).load(R.mipmap.ic_launcher).into(imageView);
        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

            }
        });

        imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onPreDraw() {

                Log.v("heihei=", helper.getLayoutPosition()+"="+imageView.isAttachedToWindow()+"="+imageView.getWidth()+"="+imageView.getHeight());
//                Log.v("xixi=",helper.getAdapterPosition()+"="+imageView.getHeight()+"="+imageView.getWidth());
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
    }
}
