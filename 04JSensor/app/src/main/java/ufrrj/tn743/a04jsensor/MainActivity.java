package ufrrj.tn743.a04jsensor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{
    private final static String TAG = "MainActivity";
    private Button mOpenCamera = null,
                   mGPS = null;




    private MyBroadcastReceive mMessageReceiver = null;
    private static Bitmap mBitmap = null;
    private ImageView mImage = null;
    private FrameLayout mImgFrame = null;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mImage = findViewById(R.id.id_imgSource);
        mOpenCamera = findViewById(R.id.id_btn_opencamera);
        mImgFrame = findViewById(R.id.id_img_frame);



        mGPS = findViewById(R.id.id_btn_gps);
        MyOnClickListener onClick = new MyOnClickListener();

        mOpenCamera.setOnClickListener(onClick);
        mGPS.setOnClickListener(onClick);

        mMessageReceiver = new MyBroadcastReceive();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("All-your-based-are-belong-to-us"));


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mBitmap != null)
            mImage.setImageBitmap(mBitmap);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void loadCameraInfo() {
        CameraManager manager = (CameraManager) getApplicationContext().getSystemService(getApplicationContext().CAMERA_SERVICE);
        String[] cameraIds = null;
        try {
            cameraIds = manager.getCameraIdList();

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
/*
        String log = "";
        if (cameraIds != null){
            for (int i = 0; i < cameraIds.length; i++){
                //CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraIds[i]);
                log = log + "CAMERA: " + cameraIds[i] + "\n";
            }
        }

        mLog.setText(log);

 */
    }

    private void callCameraActivity() {
        Intent i = new Intent(this, CameraActivity.class);
        i.putExtra("camera", "1");
        startActivity(i);
    }





    private void receivePicture(){
        Toast.makeText(getApplicationContext(), "Hello David!", Toast.LENGTH_SHORT).show();
    }

    private void setImage(byte[] bytes){
        int orientation = ((WindowManager)  getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();

        if (orientation == 0){
            Bitmap aux = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            Matrix matrix = new Matrix();
            matrix.postRotate(-90);

            mBitmap = Bitmap.createBitmap(aux, 0, 0, aux.getWidth(), aux.getHeight(), matrix, true);
            aux = null;
            System.gc();
        }else if (orientation == 3){
            Bitmap aux = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            Matrix matrix = new Matrix();
            matrix.postRotate(180);

            mBitmap = Bitmap.createBitmap(aux, 0, 0, aux.getWidth(), aux.getHeight(), matrix, true);
            aux = null;
            System.gc();
        }else
            mBitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);



        //mBitmap = bitmap;

    }

    private void callGPS(){
        startActivity(new Intent(this, SecondActivity.class));
    }
    /*
    private void processReceive(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    */
    /**
 *  Classes to describe events used by activity such as button click event
 */
   private class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.id_btn_opencamera){
                callCameraActivity();
                return;
            }//if (v.getId() == R.id.BtnOpenCamera){

            if (v.getId() == R.id.id_btn_gps){
                callGPS();

            }

        }
    }//   private class MyOnClickListener implements View.OnClickListener{


    private class MyBroadcastReceive extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            byte[] bytes = intent.getByteArrayExtra("message");
            setImage(bytes);

        }
    }


}