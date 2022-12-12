//https://stackoverflow.com/questions/2642777/trusting-all-certificates-using-httpclient-over-https/6378872#6378872
package ufrrj.tn743.a05jnetwork;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
public class HttpsClient implements Runnable{

    private static final String TAG = "HttpsClient";
    private String mURL             = null;
    private Handler mHandler        = null;
    private String mMsgKey          = null;

    public HttpsClient (String s_url){

        mURL = new String(s_url);

    }

    public void setMsgKey(String st){ mMsgKey =  new String(st); }
    public void setHandle(Handler h){ mHandler = h ; }

    private void sendMessageToMainThread(String s){
        if (mHandler == null) return;

        Message message = mHandler.obtainMessage();
        Bundle b = new Bundle();
        b.putString(mMsgKey, s);
        message.setData(b);
        mHandler.sendMessage(message);
    }

    @Override
    public void run() {
        //build.gradle (Module:xxx.app)
        OkHttpClient client;
        client = new OkHttpClient();


        MyCallBack myCallBack = new MyCallBack();
        Request request = new Request.Builder().url("https://www.dcc.ufrrj.br/~marcelo/android.php").build();
        //Request request = new Request.Builder().url("https://www.dcc.ufrrj.br/~marcelo").build();
        client.newCall(request).enqueue(myCallBack);

    }//public void run() {


    private class MyCallBack implements Callback {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d(TAG, "From server: " + e.toString());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String s = response.body().string();
            Log.d(TAG, "From server: " + s);
            sendMessageToMainThread(s);

        }

    }
}
