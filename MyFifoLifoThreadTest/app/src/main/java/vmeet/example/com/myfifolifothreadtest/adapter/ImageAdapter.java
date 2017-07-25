package vmeet.example.com.myfifolifothreadtest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import vmeet.example.com.myfifolifothreadtest.ImageShowActivity;
import vmeet.example.com.myfifolifothreadtest.ImageShowUtil;
import vmeet.example.com.myfifolifothreadtest.R;
import vmeet.example.com.myfifolifothreadtest.enumeration.LoadType;
import vmeet.example.com.myfifolifothreadtest.view.MyImgGridView;

/**
 * Created by Vmmet on 2017/1/9.
 */
public class ImageAdapter extends BaseAdapter {
private Context context;
    private List<String> list;
    public ImageAdapter(Context context, List<String> list){
        this.context=context;
        this.list=list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(
                    R.layout.image_item, viewGroup, false);
            viewHolder.iv_listview=(ImageView)view.findViewById(R.id.iv_listview);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if(viewGroup instanceof MyImgGridView){
            if(((MyImgGridView)viewGroup).isOnMeasure()){
                return view;
            }
        }
        ViewGroup.LayoutParams contentP_img = viewHolder.iv_listview.getLayoutParams();
        contentP_img.width = ImageShowActivity.width/3;
        contentP_img.height = ImageShowActivity.width/3;
        ImageShowUtil.getInstance().loadImg(list.get(i),viewHolder.iv_listview,R.drawable.b);
        return view;
    }
    class ViewHolder{
        ImageView iv_listview;
    }
}
