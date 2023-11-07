package ufrrj.tn743.a04jsensor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class SecondActivity extends AppCompatActivity {
    private final static String TAG = "SecondActivity";
    private LocationManager mLocationManager = null;

    //private static final double[] SOURCE  = {-22.74234542558444, -43.458787698966354};
    private static final double[] SOURCE  = {-22.812979431992456, -43.207355554130416};

    private static final int  LOCATION_PERMISSION_CODE = 2;
    private static final int  MIN_TIME_MS = 100;
    private static final int  MIN_DISTANCE_M = 10;
    private TextView mGPSCoord = null,
                     mGPSSpeed = null,
                     mCompass = null,
                     mAccele = null,
                     mGPSDistance = null,
                     mGPSAltitude = null,
                     mGPSGyroscope = null,
                     mGPSGravity = null,
                     mGPSSteps = null;

    private MyLocationListener mMyLocationListener = null;
    private boolean mHasCompass = true;

    private float[] mFloatGravity        = new float[3],
                    mFloatGeoMagnetic    = new float[3],
                    mFloatOrientation    = new float[3],
                    mFloatRotationMatrix = new float[9];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        mGPSCoord = findViewById(R.id.edt_coord);
        mGPSSpeed = findViewById(R.id.edt_speed);
        mCompass = findViewById(R.id.edt_compass);
        mAccele = findViewById(R.id.edt_acelle);
        mGPSDistance = findViewById(R.id.edt_distance);
        mGPSAltitude = findViewById(R.id.edt_altitude);
        mGPSGyroscope = findViewById(R.id.edt_gyroscope);
        mGPSGravity = findViewById(R.id.edt_gravity);
        mGPSSteps = findViewById(R.id.edt_steps);

        mLocationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(SecondActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE);
        }

        mMyLocationListener =  new MyLocationListener();
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_MS, MIN_DISTANCE_M, mMyLocationListener);
        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            mMyLocationListener.onLocationChanged(location);
        }


        PackageManager m = getPackageManager();
        if(!m.hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS)) {
            mHasCompass = false;
            mCompass.setText("Device has no compass");
        }

        // First, get an instance of the SensorManager
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Second, get the sensor you're interested in
        //Sensor magnetField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        //Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //Sensor gyroscope =  sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            Sensor gravity =  sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        Sensor steps =  sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

       // MagnetSensorListener sensorEventListenerMagnet = new MagnetSensorListener() ;
       // AccelerometerSensorListener sensorEventListenerAccelerometer = new AccelerometerSensorListener();
       // GyroscopeSensorListener sensorEventListenerGyroscope = new GyroscopeSensorListener();
        GravitySensorListener sensorEventListenerGravity = new GravitySensorListener();
        StepsSensorListener sensorEventListenerStep = new StepsSensorListener();

        // Finally, register your listener
        //sensorManager.registerListener(sensorEventListenerAccelerometer, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        //sensorManager.registerListener(sensorEventListenerMagnet, magnetField, SensorManager.SENSOR_DELAY_NORMAL);
        //sensorManager.registerListener(sensorEventListenerGyroscope, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);

        sensorManager.registerListener(sensorEventListenerGravity, gravity, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListenerStep, steps, SensorManager.SENSOR_DELAY_UI);


    }//protected void onCreate(Bundle savedInstanceState) {

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(SecondActivity.this, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(SecondActivity.this, "Sorry!!!, GPS permission granted!", Toast.LENGTH_LONG).show();
            }//if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
        }//if (requestCode == LOCATION_PERMISSION_CODE) {
    }


/**
 * Events class
 */
    private class StepsSensorListener implements SensorEventListener{

        @Override
        public void onSensorChanged(SensorEvent event) {
            String aux = "(" + Float.toString(event.values[0]) + ")";
            mGPSSteps.setText(aux);

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }
    private class GravitySensorListener implements SensorEventListener{

        @Override
        public void onSensorChanged(SensorEvent event) {
            String aux = "(" + Float.toString(event.values[0]) + "," + Float.toString(event.values[1]) + "," + Float.toString(event.values[2]) + ")";
            mGPSGravity.setText(aux);

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }
    private class GyroscopeSensorListener implements SensorEventListener{

        @Override
        public void onSensorChanged(SensorEvent event) {
            String aux = "(" + Float.toString(event.values[0]) + "," + Float.toString(event.values[1]) + "," + Float.toString(event.values[2]) + ")";
            mGPSGyroscope.setText(aux);

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }
    private class AccelerometerSensorListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            mFloatGravity = event.values;

            if (mHasCompass){
                SensorManager.getRotationMatrix(mFloatRotationMatrix, null, mFloatGravity, mFloatGeoMagnetic);
                SensorManager.getOrientation(mFloatRotationMatrix, mFloatOrientation);
                float angle = (float) (-mFloatOrientation[0]*180/3.14159);

                mCompass.setText(Float.toString(angle));
            }

            String aux = "(" + Float.toString(mFloatGravity[0]) + "," + Float.toString(mFloatGravity[1]) + "," + Float.toString(mFloatGravity[2]) + ")";
            mAccele.setText(aux);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    /**
     * A class for each sensor. One to accelerometer and other do magnetic field
     */
    private class MagnetSensorListener implements SensorEventListener{

        @Override
        public void onSensorChanged(SensorEvent event) {
            mFloatGeoMagnetic = event.values;

            if (mHasCompass){
                SensorManager.getRotationMatrix(mFloatRotationMatrix, null, mFloatGravity, mFloatGeoMagnetic);
                SensorManager.getOrientation(mFloatRotationMatrix, mFloatOrientation);
                float angle = (float) (-mFloatOrientation[0]*180/3.14159);

                mCompass.setText(Float.toString(angle));
            }


        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }//private class MySensorEventListener implements SensorEventListener{
    /**
     * GPS sensor
     */
    private class MyLocationListener implements LocationListener{


        @Override
        public void onLocationChanged(@NonNull Location location) {
            String gps_info = "(" + Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude()) + ")";
            mGPSCoord.setText(gps_info);

            double aux = location.getSpeed();
            gps_info = "(" + Double.toString(aux) + ")";
            mGPSSpeed.setText(gps_info);

            aux = location.getAltitude();
            gps_info = "(" + Double.toString(aux) + ")";
            mGPSAltitude.setText(gps_info);

            Location source = new Location("Source");
            source.setLongitude(SOURCE[1]);
            source.setLatitude(SOURCE[0]);

            float distance = source.distanceTo(location);
            mGPSDistance.setText(Float.toString(distance));
            source = null;

        }

        @Override
        public void onLocationChanged(@NonNull List<Location> locations) {
            LocationListener.super.onLocationChanged(locations);
        }

        @Override
        public void onFlushComplete(int requestCode) {
            LocationListener.super.onFlushComplete(requestCode);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            LocationListener.super.onStatusChanged(provider, status, extras);
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            LocationListener.super.onProviderEnabled(provider);
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            LocationListener.super.onProviderDisabled(provider);
        }
    }//private class MyLocationListener implements LocationListener{

}