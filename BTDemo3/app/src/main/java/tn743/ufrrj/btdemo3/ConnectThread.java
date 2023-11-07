package tn743.ufrrj.btdemo3;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class ConnectThread extends Thread {
    private String TAG = "ConnectThread";


    private UUID MY_UUID = UUID.randomUUID();

    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;

    private BluetoothAdapter mBTAdapter = null;


    private InputStream mmInStream;
    private OutputStream mmOutStream;

    @SuppressLint("MissingPermission")
    public ConnectThread(BluetoothAdapter a) {
        // Use a temporary object that is later assigned to mmSocket
        // because mmSocket is final.
        BluetoothSocket tmp = null;
        mBTAdapter = a;
//        mmDevice = mBTAdapter.getRemoteDevice("A0:4F:85:2E:DF:87");;
        mmDevice = mBTAdapter.getRemoteDevice("5C:C9:D3:B0:70:23");;


        try {
            // Get a BluetoothSocket to connect with the given BluetoothDevice.
            // MY_UUID is the app's UUID string, also used in the server code.
            tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            Log.e(TAG, "Socket's create() method failed", e);
        }
        mmSocket = tmp;
    }
    private void manageMyConnectedSocket(){
        //This lines should be in main activity -
        //besides the adapter and device, we should choice the service
        //mmDevice = mBluetoothAdapter.getRemoteDevice(address);
        //mChatService.connect(device, secure);
        byte[] bytes = "I am a client :)\nAll your bases are belong to us!".getBytes();
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] buffer;
        int msgSize = 0;
        try {

            msgSize = mmInStream.available();
            if (msgSize != 0){
                buffer = new byte[1024];
                msgSize = mmInStream.read(buffer, 0, msgSize);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @SuppressLint("MissingPermission")
    public void run() {
        // Cancel discovery because it otherwise slows down the connection.
        mBTAdapter.cancelDiscovery();
        boolean ret = mmSocket.isConnected();
        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and return.
            Log.e(TAG, connectException.getMessage());
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                Log.e(TAG, "Could not close the client socket", closeException);
            }
            return;
        }

        // The connection attempt succeeded. Perform work associated with
        // the connection in a separate thread.
        manageMyConnectedSocket();
    }

    // Closes the client socket and causes the thread to finish.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the client socket", e);
        }
    }

}


/****
 * private void trustEveryone() {
 *     try {
 *             HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
 *                     public boolean verify(String hostname, SSLSession session) {
 *                             return true;
 *                     }});
 *             SSLContext context = SSLContext.getInstance("TLS");
 *             context.init(null, new X509TrustManager[]{new X509TrustManager(){
 *                     public void checkClientTrusted(X509Certificate[] chain,
 *                                     String authType) throws CertificateException {}
 *                     public void checkServerTrusted(X509Certificate[] chain,
 *                                     String authType) throws CertificateException {}
 *                     public X509Certificate[] getAcceptedIssuers() {
 *                             return new X509Certificate[0];
 *                     }}}, new SecureRandom());
 *             HttpsURLConnection.setDefaultSSLSocketFactory(
 *                             context.getSocketFactory());
 *     } catch (Exception e) { // should never happen
 *             e.printStackTrace();
 *     }
 * }
 */