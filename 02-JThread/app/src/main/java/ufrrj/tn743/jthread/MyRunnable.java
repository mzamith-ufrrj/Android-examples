package ufrrj.tn743.jthread;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.util.Log;

public class MyRunnable implements Runnable{
    public static final String TAG = "MyRunnable";
    private Handler mHandler = null;
    private String mMsgKey = null;
    private Bundle mBundle = null;



    public void setMsgKey(String st){ mMsgKey =  new String(st); }

    public void setHandle(Handler h){ mHandler = h ; }


    @Override
    public void run() {
        int max = 10;
        int count = 0;
        //Execute computation to the end, afther that, this sends msg informing that it ends up
        //computation
        while(count < max){
            String msg = "***Running***" + Integer.toString(count);
            Log.v(TAG, msg);

            count++;
            try {
                Thread.sleep(200);R
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }//while (mCounter < 100){

        Message message = mHandler.obtainMessage();
        Bundle b = new Bundle();
        b.putString(mMsgKey, "All your bases are belong to us!");
        message.setData(b);
        mHandler.sendMessage(message);

    }// public void run() {


}
