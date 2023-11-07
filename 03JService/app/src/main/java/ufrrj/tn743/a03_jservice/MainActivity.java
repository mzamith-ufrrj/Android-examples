package ufrrj.tn743.a03_jservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private Button mBtnStartService = null,
                   mBtnStopService = null,
                   mBtnDownloadService = null,
                   mBtnBGService = null;

    private MyBroadcastReceive mMessageReceiver = null;

    private RandomBOUND mRandService = null;
    private MyServiceConnection mServiceConn = null;
    private boolean mIsService = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnStartService = findViewById(R.id.btn_start_service);
        mBtnStopService = findViewById(R.id.btn_stop_service);
        mBtnDownloadService = findViewById(R.id.btn_download_service);
        mBtnBGService = findViewById(R.id.btn_bg_service);

        BtnEventClick ec = new BtnEventClick();

        mBtnStartService.setOnClickListener(ec);
        mBtnStopService.setOnClickListener(ec);
        mBtnDownloadService.setOnClickListener(ec);
        mBtnBGService.setOnClickListener(ec);

        mMessageReceiver = new MyBroadcastReceive();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("From-Download-To-MainActivity"));

        Intent intent = new Intent(this, RandomBOUND.class);
        mServiceConn = new MyServiceConnection();
        bindService(intent, mServiceConn, Context.BIND_AUTO_CREATE);

    }

        @Override
        protected void onDestroy() {
            // Unregister since the activity is about to be closed.
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
            unbindService(mServiceConn);
            super.onDestroy();
        }

    private void startServiceEvent(){
        startService(new Intent(this, PlayFG.class));
        /*https://stackoverflow.com/questions/3907713/how-to-send-and-receive-broadcast-message
        Intent intent = new Intent("msg");    //action: "msg"
        intent.setPackage(getPackageName());
        intent.putExtra("message", "hello!");
        getApplicationContext().sendBroadcast(intent);*/


    }

    private void stopServiceEvent(){stopService(new Intent(this, PlayFG.class));}

    private void getRandomNumber(){
        //startService(new Intent(this, Download.class));
        if (mIsService){
            int rand = mRandService.getRandom();
                Toast.makeText(this, "Número sorteado:" + Integer.toString(rand), Toast.LENGTH_SHORT).show();
        }
    }

    private void callSeriveBG(){
        Toast.makeText(this, "Acessando o servidor", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DownloadBG.class);
        File dirname = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        intent.putExtra(DownloadBG.EXTRA_PARAM_DIR, dirname.getPath());
        intent.putExtra(DownloadBG.EXTRA_PARAM_FILE, "aulas.zip");

        startService(intent);
    }

    private void processReceive(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


    //--------------------------------------------------------------------------------------------------
    private class MyBroadcastReceive extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");

            processReceive(message);
        }
    }//private class MyBroadcastReceive extends BroadcastReceiver{

    //--------------------------------------------------------------------------------------------------
    private class BtnEventClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_start_service:startServiceEvent();break;
                case R.id.btn_stop_service:stopServiceEvent();break;
                case R.id.btn_download_service:
                    getRandomNumber();break;
                case R.id.btn_bg_service:callSeriveBG();break;

            }
        }
    }//private class BtnEventClick implements View.OnClickListener{
    //--------------------------------------------------------------------------------------------------
    private class MyServiceConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            RandomBOUND.LocalBinder binder = (RandomBOUND.LocalBinder) service;
            mRandService = binder.getService();
            mIsService = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIsService = false;
        }
    }
}