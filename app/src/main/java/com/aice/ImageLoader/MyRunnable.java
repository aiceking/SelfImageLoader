package com.aice.ImageLoader;

import android.util.Log;

import java.util.concurrent.locks.ReentrantLock;

public class MyRunnable implements Runnable{
    private int position;
    private ReentrantLock reentrantLock;
    public MyRunnable(int position,ReentrantLock reentrantLock) {
        this.position=position;
        this.reentrantLock=reentrantLock;

    }
    @Override
    public void run() {
        if (!MainActivity.isRun)return;
            Log.v("xixi=",position+"");

//        try {
//            if (reentrantLock!=null){
//            reentrantLock.lock();
//            Thread.sleep(2000);
//            }
//            Log.v("xixi=",position+"");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }finally {
//            if (reentrantLock!=null){
//            reentrantLock.unlock();
//            }
//        }
    }
}
