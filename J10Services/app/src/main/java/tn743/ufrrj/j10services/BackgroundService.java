package tn743.ufrrj.j10services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class BackgroundService extends IntentService {
    private static final String CHANNEL_ID = "tn743.ufrrj.j10service.BackgroundService";
    private static final String TAG = "BackgroundService";
    private String mHttpMsg = null;

    public BackgroundService() {
        super("BackgroundService");
        //createNotificationChannel();
        //Creating notification channel
        //mNotificationManagerCompat = NotificationManagerCompat.from(BackgroundService.this);
    }

        @Override
    protected void onHandleIntent(Intent intent) {

            Log.i(TAG, "onStartCommand FIRST TIME");


            http();
            sendMessage();


    }

    private void sendMessage(){
        Intent myintent = new Intent("From-BackgroundService-To-MainActivity");
        // You can also include some extra data.
        if (mHttpMsg == null){
            mHttpMsg = new String("SEM MSG");
        }
        myintent.putExtra("message", mHttpMsg);
        LocalBroadcastManager.getInstance(this).sendBroadcast(myintent);
    }

    private void http(){
        OkHttpClient client;
        client = new OkHttpClient();
        String msg = "";

        HttpUrl.Builder queryUrlBuilder = HttpUrl.get("https://www.dcc.ufrrj.br/~marcelo/android/im-gps-html.php").newBuilder();
        //queryUrlBuilder.addQueryParameter("parametro_get", "Marcelo P M Zamith"); //example of get
        RequestBody body = new FormBody.Builder()
                .add("email", "Jurassic@Park.com")
                .add("tel", "555-2347")
                .build();

        Request request = new Request.Builder()
                .url(queryUrlBuilder.build())
                .post(body)
                .build();
        //Request request = new Request.Builder().url("https://www.dcc.ufrrj.br/~marcelo").build();
        //client.newCall(request).enqueue(myCallBack);
        try {
            Response response = client.newCall(request).execute();
            // Log.d(TAG, response.body().string());
            msg = new String(response.body().string());
            mHttpMsg = new String(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //*----------------------------------------------------
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }//private void http(){




}