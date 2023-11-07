package tn743.ufrrj.j10services;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MainActivity extends AppCompatActivity {
    private static final String NOTIFICATION_MESSAGE_ID = "Mensagem de notificação criada pelo usuário Marcelo Zamith, muito importante para todos os usuários";
    private static final String CHANNEL_ID = "tn743.ufrrj.j10service.MainActivity";
    private NotificationManagerCompat mNotificationManagerCompat;

    private MyBroadcastReceive mMessageReceiver = null;

    private Toolbar mToolbar = null;
    private Button mServiceFG = null;
    private Button mServiceBG = null;
    private Button mServiceUB = null;
    private Button mServiceFG_URL = null;
    private EditText mLog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        mToolbar = findViewById(R.id.id_main_toolbar);
        setSupportActionBar(mToolbar);
        BTN_Event_MA buttonEvent = new BTN_Event_MA();

        mServiceFG = findViewById(R.id.id_btn_primeiro_plano);
        mServiceBG = findViewById(R.id.id_btn_segundo_plano);
        mServiceUB = findViewById(R.id.id_btn_desvinculado);
        mServiceFG_URL = findViewById(R.id.id_btn_fg_url);
        mLog = findViewById(R.id.id_log);


        mServiceFG.setOnClickListener(buttonEvent);
        mServiceBG.setOnClickListener(buttonEvent);
        mServiceUB.setOnClickListener(buttonEvent);
        mServiceFG_URL.setOnClickListener(buttonEvent);

        mNotificationManagerCompat = NotificationManagerCompat.from(MainActivity.this);

        mMessageReceiver = new MyBroadcastReceive();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("From-BackgroundService-To-MainActivity"));


    }

    @Override
    protected void onStart(){
        super.onStart();
        Toast.makeText(getApplicationContext(), "onStart called", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onStop() {

        super.onResume();
        Toast.makeText(getApplicationContext(), "onPause called", Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);


    }

    private void FGURLEvent(){

        Intent i = new Intent(this, FGService.class);
        i.putExtra("parameter", 42);
        startService(i);
    }

    private void FGBTNEvent() {
        Intent intent = new Intent(this, ForegroundService.class);
        //intent.putExtra("MSG", "Michael Myers");
        //startForegroundService(intent);
        startService(intent);

        //Aplicativo morre
        /*
        Context context = getApplicationContext();
        Intent intent = new Intent(this, ForegroundService.class);
        intent.putExtra("MSG", "Michael Myers");
        context.startForegroundService(intent);
        */
    }
    private void call_background_service(){
        createSimpleNotification("Jogo - Desafios", "Executando tarefa em segundo plano", 42);
        Intent intent = new Intent(this, BackgroundService.class);
        startService(intent);

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
            CharSequence name = "tn743.ufrrj.j10service.MainActivity";

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

//--------------------------------------------------------------------------------------------------
    private class BTN_Event_MA implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.id_btn_primeiro_plano){
                FGBTNEvent();
                return;
            }//if (v.getId() == R.id.id_btn_primeiro_plano){
            if (v.getId() == R.id.id_btn_segundo_plano){
                call_background_service();
                return;
            }//if (v.getId() == R.id.id_btn_segundo_plano){
            if (v.getId() == R.id.id_btn_fg_url){
                FGURLEvent();
                return;
                //start service with processing in second thread <----

            }

        }//public void onClick(View v) {
    }//private class BTN_Event_MA implements View.OnClickListener{
//--------------------------------------------------------------------------------------------------

    private class MyBroadcastReceive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            mLog.setText(message);
            createSimpleNotification("Jogo - Desafios", "Tarefa de segundo plano concluída", 42);
           // processReceive(message);
        }
    }//private class MyBroadcastReceive extends BroadcastReceiver{
//--------------------------------------------------------------------------------------------------
}//public class MainActivity extends AppCompatActivity {