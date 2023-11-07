package tn743.ufrrj.j10services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FGService extends Service {
    private static final String TAG = "FGService";
    private static final String NOTIFICATION_MESSAGE_ID = "Mensagem de notificação criada pelo usuário Marcelo Zamith";
    private static final int NOTIFICATION_ID = 42;
    private static final String CHANNEL_ID = "tn743.ufrrj.j10service.FGService";
    private static final String THREAD_MSG = "SERVER CONNECTION";
    private boolean mFirstRun;
    private int mMax = -1;
    private Handler mHandler = null;

    private Thread mThread = null;
    NotificationManagerCompat mNotificationManagerCompat;
    public FGService() {
        super();
        mFirstRun = false;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        mNotificationManagerCompat = NotificationManagerCompat.from(FGService.this);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                String string = bundle.getString(THREAD_MSG);
                if (string != null){
                    Log.d(TAG, "End of thread -> " + string);
                    createSimpleNotification("Jogo - Desafios", string, 42);
                    createThread();
                    try {
                        mThread.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    stopSelf();
                    Log.d(TAG, "mThread.interrupt();");
                    /*
                                stopSelf()- which is called by service itself , when task has been completed.
                                stopService(Intent) - which is explicitly called from an activity to stop the service
                     */
                }

            }
        };



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Log block

        if (intent == null){
            Log.i(TAG, "intent is null <----");
        }else{
            mMax = intent.getExtras().getInt("parameter");
            Log.i(TAG, "intent parameter: " +  Integer.toString(mMax));
        }

        if (!mFirstRun){
            mFirstRun = true;
            Log.i(TAG, "onStartCommand FIRST TIME");
            createSimpleNotification("Jogo - Desafios", "Executando em paralelo!!!", 42);
        }else{
            Log.i(TAG, "onStartCommand AGAIN <---");
        }


        createThread();

        return START_NOT_STICKY ; //   START_NOT_STICKY;
    }

    private void createThread(){
        if (mThread != null) return;
        MyRunnable r = new MyRunnable();
        r.setHandle(mHandler);
        r.setMsgKey(THREAD_MSG);
        mThread = new Thread(r);
        mThread.start();
    }
    private void executin_in_bg(){


        /*
        for (int i = 0; i < mMax; i++){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Log.i(TAG, "I -> " +  Integer.toString(i));
        }

         */
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler = null;


        if (mThread != null){

            mThread = null;
            System.gc();
        }



    }



    //@Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createSimpleNotification(String title, String text, int notificationId) {

        //removes all previously shown notifications.
        mNotificationManagerCompat.cancelAll();


        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
        //        R.drawable.nature_img);

        //open the url when user taps the notification
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.c1ctech.com/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setColor(getColor(R.color.yellow_ufrrj))
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                //Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        // notificationId is a unique int for each notification that you must define
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mNotificationManagerCompat.notify(notificationId, notification);

    }//private void createSimpleNotification(String title, String text, int notificationId) {
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Toast.makeText(this, "Build.VERSION.SDK_INT >= Build.VERSION_CODES.O", Toast.LENGTH_LONG).show();
            //Channel name
            CharSequence name = "tn743.ufrrj.j10service.FGService";

            //Channel description
            String description = "This channel will show notification only to important people";

            //The importance level you assign to a channel applies to all notifications that you post to it.
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            //Create the NotificationChannel
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);

            //Set channel description
            channel.setDescription(description);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }//private void createNotificationChannel() {
    private class MyRunnable implements Runnable{
        public static final String TAG = "FGService.MyRunnable";
        private Handler mHandler = null;
        private String mMsgKey = null;
        public void setMsgKey(String st){ this.mMsgKey =  new String(st); }

        public void setHandle(Handler h){ this.mHandler = h ; }
        @Override
        public void run() {
            //*----------------------------------------------------
            OkHttpClient client;
            client = new OkHttpClient();
            String msg = "";

           HttpUrl.Builder queryUrlBuilder = HttpUrl.get("https://www.dcc.ufrrj.br/~marcelo/android/im-gps-html.php").newBuilder();
           queryUrlBuilder.addQueryParameter("parametro_get", "Marcelo P M Zamith"); //example of get

            Request request = new Request.Builder().url(queryUrlBuilder.build()).build();
            //Request request = new Request.Builder().url("https://www.dcc.ufrrj.br/~marcelo").build();
            //client.newCall(request).enqueue(myCallBack);
            try {
                Response response = client.newCall(request).execute();
               // Log.d(TAG, response.body().string());
                msg = new String(response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
            //*----------------------------------------------------
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Message message = this.mHandler.obtainMessage();
            Bundle b = new Bundle();
            b.putString(this.mMsgKey, msg);
            message.setData(b);
            this.mHandler.sendMessage(message);



        }
    }//private class MyRunnable implements Runnable{

}