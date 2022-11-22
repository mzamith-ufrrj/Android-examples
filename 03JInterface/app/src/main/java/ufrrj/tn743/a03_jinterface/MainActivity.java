package ufrrj.tn743.a03_jinterface;





import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainActivity extends AppCompatActivity {
    private static final String MSGKEY = "UFRRJ-tn743";
    private ImageView mImgSource = null;
    private Button mBtnMain = null,
                   mBtnAsync = null,
                   mBtnThread = null,
                   mBtnClean = null,
                   mBtnShowMsg = null;

    private ProgressBar mProgressBarAsync = null,
                        mProgressBarThread = null;
    private Handler mHandler = null;

    private Lock mLock = new ReentrantLock();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onButtonClick buttonClick = new onButtonClick();

        mImgSource = findViewById(R.id.imgSource);
        mBtnMain = findViewById(R.id.btn_main);
        mBtnAsync = findViewById(R.id.btn_async);
        mBtnThread = findViewById(R.id.btn_thread);
        mBtnClean = findViewById(R.id.btn_Clean);
        mBtnShowMsg = findViewById(R.id.btn_ShowMsg);

        mProgressBarAsync = findViewById(R.id.progressBarAsync);
        mProgressBarThread = findViewById(R.id.progressBarThread);

        //mProgressBarAsync.setVisibility(View.VISIBLE);

        mBtnMain.setOnClickListener(buttonClick);
        mBtnAsync.setOnClickListener(buttonClick);
        mBtnThread.setOnClickListener(buttonClick);
        mBtnClean.setOnClickListener(buttonClick);
        mBtnShowMsg.setOnClickListener(buttonClick);


        mHandler = new Handler() {
            /**
             * Method called by Runnable object at the its end
             * @param msg
             */
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                Bitmap bitmap = bundle.getParcelable(MSGKEY);
                setImage(bitmap);

                mProgressBarThread.setVisibility(View.INVISIBLE);
            }
        };


    }//protected void onCreate(Bundle savedInstanceState) {

    private int RGB2BW(int rgb){
        int iRed, iGreen, iBlue, iPixel;
        double dRed, dGreen, dBlue;
        iRed = rgb   & 0x00FF0000; iRed >>= 16;
        iGreen = rgb & 0x0000FF00; iGreen >>= 8;
        iBlue = rgb  & 0x000000FF;

        dRed = (double) (iRed) / 255.0f;
        dGreen = (double) (iGreen) / 255.0f;
        dBlue = (double) (iBlue) / 255.0f;

        iPixel = (int) ((dRed * 0.299f + dGreen * 0.587f + dBlue * 0.114f) * 255.0f);
        return Color.rgb(iPixel, iPixel, iPixel);

    }
    private void btnMainEvent(){
        disableBTN();
        Bitmap bitmap_in = BitmapFactory.decodeResource(getResources(), R.drawable.img_2048_1536);
        int w = bitmap_in.getWidth();
        int h = bitmap_in.getHeight();

        int[] pixels = new int[w * h];
        bitmap_in.getPixels(pixels, 0, w, 0, 0, w, h);
        bitmap_in = null;

        for (int i = 0; i < w * h; i++){
            pixels[i] = RGB2BW(pixels[i]);
        }

        Bitmap bitmap_out = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap_out.setPixels(pixels, 0, w, 0, 0, w, h);
        setImage(bitmap_out);

    }


    private void ShowMessage(){
        Toast t = Toast.makeText(getApplicationContext(), "Exibindo mesagem usando [Toast]", Toast.LENGTH_LONG);
        t.show();
    }

    private void btnAsyncEvent(){
        disableBTN();
        mProgressBarAsync.setVisibility(View.VISIBLE);
        new AsyncTaskRunner().execute(Integer.toString(R.drawable.img_2048_1536));
        //Toast t = Toast.makeText(getApplicationContext(), "btnAsyncEvent(){\n", Toast.LENGTH_SHORT);
        //t.show();
    }

    private void btnThreadEvent(){
        //Toast t = Toast.makeText(getApplicationContext(), "btnThreadEvent(){\n", Toast.LENGTH_SHORT);
        //t.show();
        disableBTN();
        taskInRunnable r = new taskInRunnable();
        r.setHander(mHandler);
        r.setImageFile(R.drawable.img_2048_1536);
        mProgressBarThread.setVisibility(View.VISIBLE);
        new  Thread(r).start();
    }

    private void disableBTN(){
        mBtnMain.setEnabled(false);
        mBtnAsync.setEnabled(false);
        mBtnThread.setEnabled(false);
    }

    private void enableBTN(){
        mBtnMain.setEnabled(true);
        mBtnAsync.setEnabled(true);
        mBtnThread.setEnabled(true);
    }

    private void setImage(Bitmap bitmap){
        //bankLock.lock();
        if (mLock.tryLock()){

            //mBtnMain = findViewById(R.id.btn_main);
            //mBtnAsync = findViewById(R.id.btn_async);
            //mBtnThread = findViewById(R.id.btn_thread);
            try{
                Runtime.getRuntime().gc();
                mImgSource.setImageBitmap(null);;
                mImgSource.setImageBitmap(bitmap);;

            }finally {
                mLock.unlock();
            }
        }

        enableBTN();
    }
    private void clearImage(){
        //bankLock.lock();
        if (mLock.tryLock()){
            try{
                Runtime.getRuntime().gc();
                mImgSource.setImageBitmap(null);;

            }finally {
                mLock.unlock();
            }
        }
    }

    /**
     * Private classes used herein
     */

    /**
     * onButtonClick class to catch button event in this acivity
     *
     */
    private class onButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_main:btnMainEvent(); break;
                case R.id.btn_async:btnAsyncEvent(); break;
                case R.id.btn_thread:btnThreadEvent();break;
                case R.id.btn_Clean:clearImage();break;
                case R.id.btn_ShowMsg:ShowMessage();break;
            }

        }
    }//private class onButtonClick implements View.OnClickListener{

    /**
     * taskInRunnable used to convert RGB to BW, using YIQ matrix
     */
    private class taskInRunnable implements Runnable{
        private int mFileID = -1;
        private Bitmap mBitmap_out = null;
        private Handler m_Handler = null;

        public void setHander(Handler h){ m_Handler = h; }
        public void setImageFile(int v){ mFileID = v; }

        @Override
        public void run() {
            Bitmap bitmap_in = BitmapFactory.decodeResource(getResources(), mFileID);
            int w = bitmap_in.getWidth();
            int h = bitmap_in.getHeight();



            int[] pixels = new int[w * h];
            bitmap_in.getPixels(pixels, 0, w, 0, 0, w, h);
            bitmap_in = null;


            for (int i = 0; i < w * h; i++){
                pixels[i] = RGB2BW(pixels[i]);
            }


            mBitmap_out = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mBitmap_out.setPixels(pixels, 0, w, 0, 0, w, h);

            Message message = m_Handler.obtainMessage();
            Bundle b = new Bundle();
            b.putParcelable(MSGKEY, mBitmap_out);
            //b.putString(MSGKEY, "All your bases are belong to us!");
            message.setData(b);
            m_Handler.sendMessage(message);
        }
    }

    /**
     * Async task, used to transforme RSG to YIQ in background. Not lock the screen
     */
    private class AsyncTaskRunner extends AsyncTask<String, String, String>{
        private Bitmap mBitmap_out = null;
        @Override
        protected String doInBackground(String... strings) {
            Bitmap bitmap_in = BitmapFactory.decodeResource(getResources(), Integer.parseInt(strings[0]));
            int w = bitmap_in.getWidth();
            int h = bitmap_in.getHeight();

            mProgressBarAsync.setMax(w * h);

            int[] pixels = new int[w * h];
            bitmap_in.getPixels(pixels, 0, w, 0, 0, w, h);
            bitmap_in = null;

            int percent = (w * h) / 10;
            for (int i = 0; i < w * h; i++){
                if ((i % percent) == 0)
                    publishProgress(Integer.toString(i+1));
                pixels[i] = RGB2BW(pixels[i]);
            }
            publishProgress(Integer.toString(w * h));

            mBitmap_out = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mBitmap_out.setPixels(pixels, 0, w, 0, 0, w, h);

            return null;
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            mProgressBarAsync.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String result) {

            if (mBitmap_out != null) {
                setImage(mBitmap_out);

                mProgressBarAsync.setVisibility(View.INVISIBLE);
                mProgressBarAsync.setProgress(0);
            }
        }
    }
}//public class MainActivity extends AppCompatActivity {