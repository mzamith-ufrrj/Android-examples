package ufrrj.tn743.a03_jservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Random;

public class RandomBOUND extends Service {
    private IBinder mIBinder = null;
    private Random mGen = null;
    public RandomBOUND() {
        mGen =  new Random();
        mIBinder = new LocalBinder();
    }


    @Override
    public IBinder onBind(Intent intent) { return mIBinder; }

    public class LocalBinder extends Binder{
        RandomBOUND getService(){return RandomBOUND.this; }
    }

    public int getRandom(){ return mGen.nextInt(100);}
}