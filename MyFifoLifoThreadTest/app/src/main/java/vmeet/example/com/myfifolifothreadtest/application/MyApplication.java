package vmeet.example.com.myfifolifothreadtest.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by Vmmet on 2017/4/7.
 */
public class MyApplication extends Application{
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }
}
