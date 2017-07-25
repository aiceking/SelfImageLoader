package vmeet.example.com.myfifolifothreadtest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import vmeet.example.com.myfifolifothreadtest.ImgThread.ImgThreadPool;
import vmeet.example.com.myfifolifothreadtest.enumeration.LoadType;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ImgThreadPool myThreadPool;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv.setText("任务:" + msg.what);
        }

    };
    private TextView tv;
    private Button btn_FIFO,btn_LIFO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv=(TextView)findViewById(R.id.tv);
        btn_FIFO=(Button)findViewById(R.id.btn_FIFO);
        btn_LIFO=(Button)findViewById(R.id.btn_LIFO);
        btn_FIFO.setOnClickListener(this);
        btn_LIFO.setOnClickListener(this);
        int number=Runtime.getRuntime().availableProcessors()+1;
        myThreadPool = new ImgThreadPool(number);
        for (int i = 0; i < 100; i++) {
            final int index = i;
            myThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(index);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_FIFO:
                myThreadPool.setLoadType(LoadType.FIFO);
                break;
            case R.id.btn_LIFO:
                myThreadPool.setLoadType(LoadType.LIFO);
                break;
        }
    }
}
