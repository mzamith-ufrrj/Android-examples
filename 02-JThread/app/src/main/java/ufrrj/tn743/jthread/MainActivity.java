package ufrrj.tn743.jthread;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private String mMsgKey = null;
    private Button mBtnThread = null,
                   mBtnMThread = null,
                   mBtnLoadImg = null,
                   mBtnMemInfo = null,
                   mBtnCG = null;

    private TextView mTxtnCores = null,
                     mTxtMaxMem = null,
                     mTxtTotalMem = null,
                     mTxtFreeMem = null,
                     mTxtOutput  = null;

    private onButtonClick mOnBtnClick = null;
    private Handler mHandler = null;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnThread  = findViewById(R.id.btn_thread);
        mBtnMThread = findViewById(R.id.btn_mthread);
        mBtnCG = findViewById(R.id.btn_cg);
        mBtnMemInfo = findViewById(R.id.btn_mem_info);
        mBtnLoadImg = findViewById(R.id.btn_load_img);

        mTxtnCores = findViewById(R.id.txt_n_cores);
        mTxtMaxMem = findViewById(R.id.txt_max_mem);
        mTxtTotalMem = findViewById(R.id.txt_total_mem);
        mTxtFreeMem = findViewById(R.id.txt_free_mem);
        mTxtOutput = findViewById(R.id.txt_output);


        mOnBtnClick = new onButtonClick();
        mBtnThread.setOnClickListener(mOnBtnClick);
        mBtnMThread.setOnClickListener(mOnBtnClick);
        mBtnCG.setOnClickListener(mOnBtnClick);
        mBtnMemInfo.setOnClickListener(mOnBtnClick);
        mBtnLoadImg.setOnClickListener(mOnBtnClick);
        int n = Runtime.getRuntime().availableProcessors();
        mTxtnCores.setText(Integer.toString(n));

        //Defining parallel task

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                String string = bundle.getString(mMsgKey);
                if (string != null)
                    mTxtOutput.setText(string);
            }
        };

        mMsgKey = new String ("Example thread comm!!!");


    }



    private void onBtnThreadClick(){
        MyRunnable r = new MyRunnable();
        r.setMsgKey(mMsgKey);
        r.setHandle(mHandler);
        new Thread(r).start();
    }

    private void onBtnMthreadClick(){
        /*
        Message message = mHandler.obtainMessage();
        Bundle b = new Bundle();
        b.putString(MyRunnable.TAG, "All your bases are belong to us!");
        message.setData(b);
        mHandler.sendMessage(message);
*/

    }

    private void onBtnCGClick(){
        System.gc();
    }

    private void onBtnMemInfoClick(){
        long total = Runtime.getRuntime().totalMemory();
        long max = Runtime.getRuntime().maxMemory();
        long free = Runtime.getRuntime().freeMemory();

        mTxtMaxMem.setText(Long.toString(max));
        mTxtTotalMem.setText(Long.toString(total));
        mTxtFreeMem.setText(Long.toString(free));
    }

    private void onBtnLoadImagelick(){

    }

    /*
     * event class to catch button click event. Note that
     * each click event is associated to different method
     */
    private class onButtonClick implements OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_mthread:onBtnMthreadClick();break;
                case R.id.btn_thread:onBtnThreadClick();break;
                case R.id.btn_cg:onBtnCGClick();break;
                case R.id.btn_mem_info:onBtnMemInfoClick();break;
                case R.id.btn_load_img:onBtnLoadImagelick();break;
            }//switch (view.getId()){
        }//public void onClick(View view) {
    }//private class onButtonClick implements OnClickListener {
}