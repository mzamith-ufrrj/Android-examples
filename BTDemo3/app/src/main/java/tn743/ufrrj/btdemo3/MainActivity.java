package tn743.ufrrj.btdemo3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private BluetoothReceiver mReceiver = null;

    private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
    private static final int  LOCATION_PERMISSION_CODE = 1001;
    private static final int  REQUEST_ENABLE_BT        = 1002;
    private static final int  REQUEST_SCAN             = 1003;
    private static final int  HTTP                     = 1004;

    private final BluetoothAdapter mBTAdapter = BluetoothAdapter.getDefaultAdapter();

    private Button mmBtnBTON = null;
    private Button mmBtnBTScan = null;
    private Button mmBtnHttp = null;

    private ListView mmListDevices;
    //private TextView mmTxtDevice;

    private ArrayAdapter<String> mmAdapter = null;//, mmAdapterAux = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mmBtnBTON = findViewById(R.id.btnbton);
        mmBtnBTScan = findViewById(R.id.btnbtscan);
        mmBtnHttp = findViewById(R.id.btnhttp);
        mmListDevices = findViewById(R.id.lstdevicelist);
        //mmTxtDevice = findViewById(R.id.txtDeviceName);
        mmBtnBTON.setOnClickListener(new onClickButtons());
        mmBtnBTScan.setOnClickListener(new onClickButtons());
        mmBtnHttp.setOnClickListener(new onClickButtons());
        hardwareBTCheck();
        permissions();

        String[] listItem = {"NO DEVICES!"};
        //listItem = getResources().getStringArray(R.array.array_technology);
        mmAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listItem);
        mmListDevices.setAdapter(mmAdapter);
        mmListDevices.setOnItemClickListener(new onClickListView());
        mReceiver = new BluetoothReceiver();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_UUID);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();
    }
    private void hardwareBTCheck(){
        // Then you can selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            Toast.makeText(this, "FEATURE_BLUETOOTH", Toast.LENGTH_SHORT).show();

        }
// Use this check to determine whether BLE is supported on the device. Then
// you can selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "FEATURE_BLUETOOTH_LE", Toast.LENGTH_SHORT).show();

        }
    }

    private void permissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, LOCATION_PERMISSION_CODE);
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.BLUETOOTH)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, REQUEST_ENABLE_BT);
            }
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.BLUETOOTH_ADMIN)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQUEST_SCAN);
            }
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE}, HTTP);
            }
        }
    }//private void permissions(){
    private void btnHttpClick(){
        CommDemo d = new CommDemo();
        d.start();
    }
    @SuppressLint("MissingPermission")
    private void btnOnClick() {
        if (!mBTAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }//if (!mBTAdapter.isEnabled()) {
    }//private void btnOnClick() {

    @SuppressLint("MissingPermission")
    private void btnScanClick() {

        if (mBTAdapter.isDiscovering())
            mBTAdapter.cancelDiscovery();
        int reti = mBTAdapter.getState();
        if (reti == BluetoothAdapter.STATE_OFF )
            Log.d(TAG, "STATE_OFF");

        if (reti == BluetoothAdapter.STATE_ON)
            Log.d(TAG, "STATE_ON");

        mmAdapter  = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mDeviceList.clear();
        boolean ret = mBTAdapter.startDiscovery();
        Log.d(TAG, "startDiscovery: " + Boolean.toString(ret));



    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //                                  #1 --->
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(this, "Sorry!!!, you can't use this app without granting permission [LOCATION_PERMISSION_CODE]", Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(this, "[LOCATION_PERMISSION_CODE] permission granted!", Toast.LENGTH_LONG).show();
            }//if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            return;
        }//if (requestCode == LOCATION_PERMISSION_CODE) {

        //                                  #2 --->
        if (requestCode == REQUEST_ENABLE_BT) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(this, "Sorry!!!, you can't use this app without granting permission [REQUEST_ENABLE_BT]", Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(this, "[REQUEST_ENABLE_BT] permission granted!", Toast.LENGTH_LONG).show();
            }//if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            return;
        }//if (requestCode == LOCATION_PERMISSION_CODE) {

        //                                  #3 --->
        if (requestCode == REQUEST_SCAN) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(this, "Sorry!!!, you can't use this app without granting permission [REQUEST_SCAN]", Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(this, "[REQUEST_SCAN] permission granted!", Toast.LENGTH_LONG).show();
            }//if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            return;
        }//if (requestCode == LOCATION_PERMISSION_CODE) {

        if (requestCode == HTTP) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(this, "Sorry!!!, you can't use this app without granting permission [HTTP]", Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(this, "[HTTP] permission granted!", Toast.LENGTH_LONG).show();
            }//if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            return;
        }//if (requestCode == LOCATION_PERMISSION_CODE) {



    }//public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    private class BluetoothReceiver extends BroadcastReceiver {
        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceHardwareAddress = device.getAddress(); // MAC address
                String devicename = device.getName();
                //String uuid = device.getUuids()[0].toString();
                mmAdapter.add(devicename);
                mDeviceList.add(device);
                Log.d(TAG, deviceHardwareAddress + " ---> " + devicename);
            }else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //do something else
                if (mDeviceList.size() == 0){
                    mmAdapter.add(new String("NO DEVICES!"));
                }else{
                    mmListDevices.setAdapter(mmAdapter);
                    BluetoothDevice device = mDeviceList.get(0);
                    Log.d(TAG, "Selected: " + device.getName() + " ---> " + device.getAddress());
                    boolean ret = device.fetchUuidsWithSdp();
                }//if (mDeviceList.size() == 0){

            }else if (BluetoothDevice.ACTION_UUID.equals(action)) {
                BluetoothDevice deviceExtra = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Parcelable[] uuidExtra = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
                Log.d(TAG, "DeviceExtra address: " + deviceExtra.getAddress());
                if (uuidExtra != null) {
                    for (Parcelable p : uuidExtra)
                        Log.d(TAG, "uuidExtra: " + p);

                } else {
                    Log.d(TAG,"uuidExtra is still null");
                }//if (uuidExtra != null) {
            }//}else if (BluetoothDevice.ACTION_UUID.equals(action)) {
                //IPC:::: https://stackoverflow.com/questions/14812326/android-bluetooth-get-uuids-of-discovered-devices/37070600#37070600

            //}//if (BluetoothDevice.ACTION_FOUND.equals(action)) {

        }
    }

    /**
     * Listview event
     */
    private class onClickListView implements AdapterView.OnItemClickListener {
        @SuppressLint("MissingPermission")
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //String name = mmAdapter.getItem(i);
            BluetoothDevice d =  mDeviceList.get(i);
            Toast.makeText(getApplicationContext(),d.getName() + " --> " + d.getAddress() ,Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Button click event class
     */
    private class onClickButtons implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnbton:btnOnClick(); break;
                case R.id.btnbtscan:btnScanClick(); break;
                case R.id.btnhttp:btnHttpClick();break;

            }

        }//public void onClick(View v) {
    }//private class onClickButtons implements View.OnClickListener{
}//public class MainActivity extends AppCompatActivity {