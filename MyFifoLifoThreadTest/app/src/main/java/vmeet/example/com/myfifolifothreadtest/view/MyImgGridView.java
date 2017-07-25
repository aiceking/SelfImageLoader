package vmeet.example.com.myfifolifothreadtest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Vmmet on 2017/4/11.
 */
public class MyImgGridView extends GridView{
    public boolean isOnMeasure;

    public boolean isOnMeasure() {
        return isOnMeasure;
    }

    public MyImgGridView(Context context) {
        super(context);
    }

    public MyImgGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImgGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        isOnMeasure=true;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        isOnMeasure = false;
        super.onLayout(changed, l, t, r, b);
    }
}
