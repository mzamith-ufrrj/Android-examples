package ufrrj.tn743.a05jnetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int CHAR_HEIGHT = 42;
    private String mMsgKey = null;
    private static Vector<String> mLog = null;
    private static int mNLines = 0;
    private Button mBtnUDP = null,
                   mBtnTCP = null,
                   mBtnHttps = null,
                   mBtnWiFiAPs = null; //not implemented

    private EditText mEdtSocket = null;
    private static final int BUFFER_SIZE = 1024;


    private LinearLayout mMainLayout = null;
    private MyFrame mFrame = null;
    private onClickButtons mOnClick = null;

    private Handler mHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOnClick = new onClickButtons();
        mBtnUDP = findViewById(R.id.btnUdp);
        mBtnUDP.setOnClickListener(mOnClick);
        mBtnTCP = findViewById(R.id.btnTCP);
        mBtnTCP.setOnClickListener(mOnClick);
        mBtnHttps = findViewById(R.id.btnHttps);
        mBtnHttps.setOnClickListener(mOnClick);

        mEdtSocket = findViewById(R.id.edt_socket);
        mMainLayout = findViewById(R.id.main_layout);

        mFrame = new MyFrame(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        mFrame.setLayoutParams(params);
        mFrame.setBackgroundColor(getColor(R.color.yellow_ufrrj));
        mMainLayout.addView(mFrame);

        mLog = new Vector<String>();

        mMsgKey = new String ("TN743-exemplo");

    }// protected void onCreate(Bundle savedInstanceState) {

    public void receive(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        mLog.add(new String(s));
        mFrame.invalidate();
        /*
        mBtnUDP.setEnabled(true);
        mBtnTCP.setEnabled(true);
        mBtnHttps.setEnabled(true);
        mBtnWiFiAPs.setEnabled(true);

         */
    }
    private void btnHTTPsClick() {
        HttpsClient client = new HttpsClient("https://192.168.1.9/");
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                String string = bundle.getString(mMsgKey);
                if (string != null){
                    receive(string);
                }

            }
        };

        client.setHandle(mHandler);
        client.setMsgKey(mMsgKey);

        Thread t = new Thread(client);
        t.start();
    }

    private void btnTCPClick(){
        TCPClient client =  new TCPClient(mEdtSocket.getText().toString());

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                String string = bundle.getString(mMsgKey);
                if (string != null){
                    receive(string);
                }

            }
        };

        client.setHandle(mHandler);
        client.setMsgKey(mMsgKey);

        Thread t = new Thread(client);
        t.start();
    }
    private void btnUDPClick(){
        /*
        mBtnUDP.setEnabled(false);
        mBtnTCP.setEnabled(false);
        mBtnHttps.setEnabled(false);
        mBtnWiFiAPs.setEnabled(false);

         */
        new AsynUDP(this).execute(mEdtSocket.getText().toString(), "Cellular [All your bases are belong to us!]");

    }//private void btnUDPClick(){

    private class MyFrame extends FrameLayout{

        public MyFrame(@NonNull Context context) {
            super(context);
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int w = canvas.getWidth();
            int h  = canvas.getHeight();
            if (mNLines == 0){
                double r = Math.ceil((double)(h) / (double) (CHAR_HEIGHT));
                mNLines = (int) r;
            }

            //Do some drawing
            Paint paint = new Paint();
            paint.setColor(getColor(R.color.yellow_ufrrj));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawPaint(paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(40);

            boolean flag = true;
            int index = 0;
            while (flag){


                int index2 = mLog.size() - index;
                if (index2 <= 0)
                    flag = false;

                if (flag){
                    String line = Integer.toString(index2) + ":" + mLog.get(index2-1);
                    canvas.drawText(line, 8, (index+1)*CHAR_HEIGHT, paint);
                }

                if (index < mNLines)
                    index++;
                else
                    flag = false;

            }//while (flag){



        }// public void onDraw(Canvas canvas) {
    }

    /**
     * Callbacks classes
     */
    private class onClickButtons implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnUdp: btnUDPClick();break;
                case R.id.btnTCP: btnTCPClick();break;
                case R.id.btnHttps: btnHTTPsClick();break;
            }
        }
    }//private class onClickButtons implements View.OnClickListener{

    /*
    private class UPD_connection implements Runnable {

        @Override
        public void run() {
            sendUDPMsg();
        }//public void run() {
    }//private class UPD_connection implements Runnable {

     */
}