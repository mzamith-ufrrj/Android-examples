package ufrrj.tn743.jhello;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LayoutActivityIV extends AppCompatActivity {

    private EditText mEdiTextView = null;
    private ProgressBar mProgressBar = null;
    private Button mStart = null;
    private onClickEvent mOnClickEvent = null;
    private int mCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_iv);
        mEdiTextView = findViewById(R.id.edtTextInput);
        Bundle extras = getIntent().getExtras();
        String msg = extras.getString("MSG");
        mOnClickEvent = new onClickEvent();
        mEdiTextView.setText(msg);
        mProgressBar = findViewById(R.id.pgb_example);
        mStart = findViewById(R.id.btn_start);
        mStart.setOnClickListener(mOnClickEvent);

    }




    private void startThread(){
        Toast t = Toast.makeText(getApplicationContext(), "chamando thread!!!", Toast.LENGTH_SHORT);
        t.show();
        new AsyncTaskRunner().execute(Integer.toString(mProgressBar.getMax()));

    }


    /*
     * Class that catch button click event, just to start secondary thread
     */
    private class onClickEvent implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_start: startThread();  break;
            }

        }//public void onClick(View view) {
    }//private class onClickEvent implements View.OnClickListener{

    /*
     * Class that implements Runnable event to execute parallel processing in relation to activity
     */

//    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        //android.os.AsyncTask<Params, Progress, Result>
        @Override
        protected String doInBackground(String... parameter) {

            int max = Integer.parseInt(parameter[0]);
            while(mCounter < max){
                publishProgress(Integer.toString(mCounter));
                mCounter++;
               try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }//while (mCounter < 100){

            return Integer.toString(mCounter);
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            // Synchronized to UI thread.
            // Update progress bar, Notification, or other UI elements
            mProgressBar.setProgress(Integer.parseInt(progress[0]));

           return;
        }

        @Override
        protected void onPostExecute(String result) {
            // Synchronized to UI thread.
            // Report results via UI update, Dialog, or notifications
            Toast t = Toast.makeText(getApplicationContext(), "Total lido: " + result, Toast.LENGTH_SHORT);
            t.show();
            mEdiTextView.setText("Total lido" + result);
            return;
        }
    }
    /*
    private class progressbar_running implements Runnable{

        @Override
        public void run() {

            while (mCounter < mProgressBar.getMax()){
                mProgressBar.setProgress(mCounter);
                mCounter++;

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }//while (mCounter < 100){

            endOfThread();
        }//public void run() {
    }//private class progressbar_running implements Runnable{

     */
}