package com.aice.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aice.ImageLoader.adapter.TestAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    //线程池线程数
    private final int CORE_POOL_SIZE = CPU_COUNT - 1;
    @BindView(R.id.recycler_name)
    RecyclerView recyclerName;
    @BindView(R.id.btn_stop)
    Button btnStop;
    @BindView(R.id.btn_start)
    Button btnStart;
    private TestAdapter testAdapter;
    private List<String> stringList;
    private ThreadPoolExecutor threadPoolExecutor;
    private ReentrantLock reentrantLock;
    private HashMap<String, ReentrantLock> reentrantLockHashMap;
    public static volatile boolean isRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initRecycleView();

        getSupportFragmentManager().beginTransaction().add(new MyFragment(), "heihei").commit();

    }

    private void initRecycleView() {
        stringList = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            stringList.add("第 " + (i + 1) + " 名");
        }
        testAdapter = new TestAdapter(R.layout.recycler_name, stringList);
        recyclerName.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerName.setAdapter(testAdapter);
    }

    public static Activity getActivityFromView(View view) {
        if (null != view) {
            Context context = view.getContext();
            while (context instanceof ContextWrapper) {
                if (context instanceof Activity) {
                    return (Activity) context;
                }
                context = ((ContextWrapper) context).getBaseContext();
            }
        }
        return null;
    }

    @OnClick({R.id.btn_start, R.id.btn_stop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                reentrantLockHashMap = new HashMap<>();
                reentrantLockHashMap.put("haha", new ReentrantLock());
                reentrantLock = new ReentrantLock();
                threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, CORE_POOL_SIZE,
                        60L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>());
                threadPoolExecutor.allowCoreThreadTimeOut(true);
                isRun = true;
                for (int i = 0; i < 200000; i++) {
//            if (i==1||i==3){
//                threadPoolExecutor.submit(new MyRunnable(i + 1,reentrantLockHashMap.get("haha")));
//            }else {
                    threadPoolExecutor.submit(new MyRunnable(i + 1, null));
//            }
                }
                break;
            case R.id.btn_stop:
//                isRun = false;
                threadPoolExecutor.shutdownNow();
                break;
        }
    }


//    @OnClick(R.id.btn_stop)
//    public void onViewClicked() {
//        isRun = false;
////        threadPoolExecutor.shutdownNow();
//    }
//
//    @OnClick(R.id.btn_start)
//    public void onViewClicked() {
//    }
}
