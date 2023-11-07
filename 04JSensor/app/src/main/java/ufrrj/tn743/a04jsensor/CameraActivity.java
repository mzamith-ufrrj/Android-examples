package ufrrj.tn743.a04jsensor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Size;

import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import android.hardware.camera2.CameraDevice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CameraActivity extends AppCompatActivity {
    private final static String TAG = "CameraActivity";
    private static final int REQUEST_CAMERA_PERMISSION = 200;

    private Button mBtnTakeAPic = null;
    private String mCameraId;
    private CameraDevice mCameraDevice = null;
    private Size mSize; //imageDimension;
    private MySurfaceTextureListener mTextureListener = null;
    private TextureView mTextureView;
    private MyCameraDeviceStateCallback mMyCameraDeviceStateCallback = null;
    protected CameraCaptureSession mCameraCaptureSessions;
    private CaptureRequest.Builder mCaptureRequestBuilder;
    private int mOrientation = -1;
    private FrameLayout mPicFrame = null;

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {ORIENTATIONS.append(Surface.ROTATION_0, 270);
            ORIENTATIONS.append(Surface.ROTATION_90, 0);
            ORIENTATIONS.append(Surface.ROTATION_180, 90);
            ORIENTATIONS.append(Surface.ROTATION_270, 180);}

    /**
     * Create and draw UI components
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Intent i = getIntent();

        mCameraId = i.getStringExtra("camera");
        mTextureView = findViewById(R.id.id_texture);

        mTextureListener = new MySurfaceTextureListener();
        mTextureView.setSurfaceTextureListener(mTextureListener);

        mBtnTakeAPic = findViewById(R.id.id_btn_takepicture);
        mBtnTakeAPic.setOnClickListener(new MyOnClickListener());
        mMyCameraDeviceStateCallback = new MyCameraDeviceStateCallback();

        mPicFrame = findViewById(R.id.id_pic_frame);
        mOrientation = ((WindowManager)  getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();

        if (mOrientation == 1)
            mPicFrame.setRotation(-90.0f);
        else if (mOrientation == 3)
            mPicFrame.setRotation(90.0f);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");

    }//protected void onResume() {

    @Override
    protected void onPause() {

        super.onPause();
    }//protected void onPause() {
    /**
     * Open camera
     */
    private void openCamera() {

        CameraManager manager = (CameraManager) getSystemService(getApplicationContext().CAMERA_SERVICE);
        CameraCharacteristics characteristics = null;
        try {
            characteristics = manager.getCameraCharacteristics(mCameraId);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        assert map != null;
        mSize = map.getOutputSizes(SurfaceTexture.class)[0];

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
            return;
        }

        try {
            manager.openCamera(mCameraId, mMyCameraDeviceStateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }//private void openCamera() {

    /**
     * Update method that keeps camera working the foreground
     */
    private void updatePreview(){

        if (mCameraDevice == null)
            return;

        /*
        mCaptureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        mCaptureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, 270);
        //mCaptureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, Surface.ROTATION_90);
*/
        try {
            mCameraCaptureSessions.setRepeatingRequest(mCaptureRequestBuilder.build(), null, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    /**
     * Create a camera preview in according to camera ID
     */
    private void createCameraPreview(){

        SurfaceTexture texture = mTextureView.getSurfaceTexture();
        texture.setDefaultBufferSize(mSize.getWidth(), mSize.getHeight());
        Surface surface = new Surface(texture);
        try {


            int opt = ORIENTATIONS.get(mOrientation);


            mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mCaptureRequestBuilder.addTarget(surface);


            mCaptureRequestBuilder.set(CaptureRequest.CONTROL_EFFECT_MODE,CameraMetadata.CONTROL_EFFECT_MODE_MONO);
            mCaptureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            mCaptureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, opt);
            //

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        try {
            mCameraDevice.createCaptureSession(Arrays.asList(surface), new MyCameraCaptureSSCback(mCaptureRequestBuilder), null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }


    }
    /*
    private void sendAMessage(){
        Intent myintent = new Intent("All-your-based-are-belong-to-us");
        // You can also include some extra data.
        myintent.putExtra("message", "Game over!");
        LocalBroadcastManager.getInstance(this).sendBroadcast(myintent);
        this.finish();
    }
    */
    /**
     * Take a picture from camera
     */
    private void takeapicture(){

        CameraManager manager = (CameraManager) getSystemService(getApplicationContext().CAMERA_SERVICE);
        CameraCharacteristics characteristics = null;
        try {
            characteristics = manager.getCameraCharacteristics(mCameraDevice.getId());
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        if (characteristics == null) return;
        Size[] jpegSizes = null;
        jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
        int width = 640; int height = 480;
        if (jpegSizes != null && jpegSizes.length > 0) {
            width = jpegSizes[0].getWidth();
            height = jpegSizes[0].getHeight();
        }
        ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
        List<Surface> outputSurfaces = new ArrayList<Surface>(2);
        outputSurfaces.add(reader.getSurface());
        outputSurfaces.add(new Surface(mTextureView.getSurfaceTexture()));


        CaptureRequest.Builder captureBuilder = null;
        try {
            captureBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        if (captureBuilder == null) return ;
        captureBuilder.addTarget(reader.getSurface());


        int opt = ORIENTATIONS.get(mOrientation);


        captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        captureBuilder.set(CaptureRequest.CONTROL_EFFECT_MODE,CameraMetadata.CONTROL_EFFECT_MODE_MONO); //B/W mode, comment this line in case of color picture
        captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, opt);



        MyImageReaderOnImageAvailableListener readerListener = new MyImageReaderOnImageAvailableListener();
        reader.setOnImageAvailableListener(readerListener, null); //mBackgroundHandler

        MyCaptureCallBack2Save mycapturecallback2save = new MyCaptureCallBack2Save();
        MyCameraCaptureSSCback mycameracapturesessionstatecallback2 = new MyCameraCaptureSSCback(captureBuilder, mycapturecallback2save);
        try {
            mCameraDevice.createCaptureSession(outputSurfaces, mycameracapturesessionstatecallback2, null); //mBackgroundHandler
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }







    }

    private void sendImage(byte[] bytes){

        Intent myintent = new Intent("All-your-based-are-belong-to-us");
        // You can also include some extra data.
        myintent.putExtra("message", bytes);
        LocalBroadcastManager.getInstance(this).sendBroadcast(myintent);
        finish();

    }
/**
 * Classes used by some camera events
 */
    private class MyCameraCaptureSSCback extends CameraCaptureSession.StateCallback{

        private CaptureRequest.Builder mCaptureBuilder = null;
        //private CaptureRequest.Builder mCaptureBuilder;
        private MyCaptureCallBack2Save mMyCaptureCallBack2Save = null;
        private int mState = -1;
        public MyCameraCaptureSSCback(CaptureRequest.Builder c){
            super();
            this.mCaptureBuilder = c;
            this.mState = 1;
        }
        public MyCameraCaptureSSCback(CaptureRequest.Builder c, MyCaptureCallBack2Save m){
            super();
            this.mState = 2;
            this.mCaptureBuilder = c;
            this.mMyCaptureCallBack2Save = m;
        }

        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            if (this.mState == 1){
                if (mCameraDevice == null) return;
                mCameraCaptureSessions = session;
                //Updates the smartphone screen
                updatePreview();
                return;
            }//if (this.mState == 1){
            if (this.mState == 2){
                try {
                    session.capture(this.mCaptureBuilder.build(), mMyCaptureCallBack2Save, null); //mBackgroundHandler
                    /**
                     * mMyCaptureCallBack2Save and mBackgroundHandler are from CameraActivity <------------------------- IPC
                     */
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
                return;
            }//if (this.mState == 2){

        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {

        }
    }
//-------------------------------------------------------------------------------------------------
    private class MyCaptureCallBack2Save extends CameraCaptureSession.CaptureCallback {
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            Toast.makeText(CameraActivity.this, "File saved!", Toast.LENGTH_SHORT).show();
            createCameraPreview();
        }
    }


//-------------------------------------------------------------------------------------------------
    private class MyImageReaderOnImageAvailableListener implements ImageReader.OnImageAvailableListener{

        @Override
        public void onImageAvailable(ImageReader reader) {
            Image image = null;
            image = reader.acquireLatestImage();
 //           int width = reader.getWidth();
//            int height = reader.getHeight();
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();


            byte[] bytes = new byte[buffer.capacity()];
            buffer.get(bytes);
                //--------------save file

                //String filename = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())) + ".jpg";

                //                     save in picture default directory
                //String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/" + filename;

                //                                    save in download directory
                //String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + filename;
            /*
                File file = new File(path);
                OutputStream output = null;
                try {
                    output = new FileOutputStream(file);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                try {
                    output.write(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

             //--------------save file

            if (image != null) image.close();
            */

            sendImage(bytes);


        }



    }//private class ImageReaderEvent implements ImageReader.OnImageAvailableListener{



//-------------------------------------------------------------------------------------------------
    private class MySurfaceTextureListener implements TextureView.SurfaceTextureListener{

        @Override
        public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

        }
    }//private class TexttureListener implements TextureView.SurfaceTextureListener{
//-------------------------------------------------------------------------------------------------
    private class MyCameraDeviceStateCallback extends CameraDevice.StateCallback {


        @Override
        public void onOpened(@NonNull CameraDevice camera) {

            mCameraDevice = camera;
            createCameraPreview();


        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            mCameraDevice.close();
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            mCameraDevice.close();
            mCameraDevice = null;

        }
    }//private class CameraStateCallBack extends CameraDevice.StateCallback {

/**
 *  Classes to describe events used by activity such as button click event
 */
    private class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            takeapicture();
        }
    }
}//public class CameraActivity extends AppCompatActivity {