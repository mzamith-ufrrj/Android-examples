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
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.IOException;

public class ForegroundService extends Service {
    private static final String TAG = "ForegroundService";
    private static final String NOTIFICATION_MESSAGE_ID = "Mensagem de notificação criada pelo usuário Marcelo Zamith";
    private static final int NOTIFICATION_ID = 42;
    private static final String CHANNEL_ID = "tn743.ufrrj.j10service.Service";
//    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    private MediaPlayer mPlayer;
    private boolean mFirstRun;
    NotificationManagerCompat mNotificationManagerCompat;


    public ForegroundService() {
        super();
        mFirstRun = false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        mNotificationManagerCompat = NotificationManagerCompat.from(ForegroundService.this);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Log block
        String msg = "";
        if (intent == null){
            Log.i(TAG, "intent is null <----");
        }else{
            msg = intent.getExtras().getString("MSG");
            Log.i(TAG, "intent parameter: " +  msg);
        }

        if (!mFirstRun){
            mFirstRun = true;
            Log.i(TAG, "onStartCommand FIRST TIME");
            if (msg != "")
                createSimpleNotification(msg, getString(R.string.str_notification_message), 42);
            else
                createSimpleNotification("Executando em segundo plano", getString(R.string.str_notification_message), 42);
        }else{
            Log.i(TAG, "onStartCommand AGAIN <---");
        }



        AssetFileDescriptor afd = null;
        try {
            afd = getAssets().openFd("Michael_Myers.mp3");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            mPlayer.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mPlayer.setLooping( true );
        mPlayer.start();

        return START_NOT_STICKY ; //   START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.stop();
    }

    //@Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createSimpleNotification(String title, String text, int notificationId) {

        //removes all previously shown notifications.
        mNotificationManagerCompat.cancelAll();


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.nature_img);

        //open the url when user taps the notification
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.c1ctech.com/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(bitmap)
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

    }
    private void createNotificationChannel() {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Toast.makeText(this, "Build.VERSION.SDK_INT >= Build.VERSION_CODES.O", Toast.LENGTH_LONG).show();
            //Channel name
            CharSequence name = "tn743.ufrrj.j10service.Service";

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
    }

    /*
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
*/
}