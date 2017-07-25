package vmeet.example.com.myfifolifothreadtest.ImgThread;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import vmeet.example.com.myfifolifothreadtest.enumeration.LoadType;

/**
 * Created by Vmmet on 2017/4/6.
 */
public class ImgThreadPool extends ThreadPoolExecutor{
    private volatile Semaphore semaphore;
    private  LinkedList<Runnable> runnableList;
    private LoopThread loopThread;
    private boolean flag;
    private LoadType loadType;
    public ImgThreadPool(int corePoolSize) {
        super(corePoolSize, corePoolSize, 1l, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        semaphore = new Semaphore(corePoolSize);
        runnableList = new LinkedList<>();
        flag = true;
        loadType = LoadType.FIFO;//默认是先进先出
        loopThread = new LoopThread();
        loopThread.start();
    }
    public synchronized void addImgRunable(Runnable Runable){
        //提交到任务队列中
        runnableList.add(Runable);
        if (runnableList.size() == 1) {
            //如果这是队列中的第一个任务,那么就去唤醒轮询线程
            synchronized (loopThread) {
                loopThread.notify();
            }
        }
    }
    @Override
    public void execute(Runnable command) {
        super.execute(command);
    }
    //设置是FIFO/LIFO
    public void setLoadType(LoadType loadType) {
        this.loadType = loadType;
    }
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        //任务完成释放信号量
        semaphore.release();
    }

    @Override
    protected void terminated() {
        super.terminated();
        flag = false;//轮询线程关闭
    }

    class LoopThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (flag) {
                synchronized (runnableList){
                if (runnableList.size() == 0) {
                    try {
                        //如果没有任务,轮询线程就等待
                        synchronized (this) {
                            wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                 else {
                    try {
                        //请求信号量
                        semaphore.acquire();
                        int index = runnableList.size();
                        switch (loadType) {
                            case FIFO:
                                //先进先出
                                index = 0;
                                break;
                            case LIFO:
                                //先进后出
                                index = runnableList.size() - 1;
                                break;
                        }
                        //调用父类的添加方法,将任务添加到线程池中
//                        if (runnableList.get(index)!=null){
                            execute(runnableList.get(index));
                            runnableList.remove(index);
//                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }}
            }
        }
    }
}
