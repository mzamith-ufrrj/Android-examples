package ufrrj.tn743.a05jnetwork;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPClient implements Runnable {

    private static final String TAG = "TCPClient";

    private String mServer = null;
    private int mPort = 0;
    private boolean mIsRunning = true;

    private PrintWriter mBufferOut = null;
    private BufferedReader mBufferIn = null;
    private String mServerMessage = null;

    private Handler mHandler = null;
    private String mMsgKey = null;

    public TCPClient (String s){
        String[] parse = s.split(":");
        mServer = new String(parse[0]);
        mPort = Integer.parseInt(parse[1]);

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
        InetAddress serverAddr = null;
        Socket socket = null;
        try {
            serverAddr = InetAddress.getByName(mServer);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        try {
            socket = new Socket(serverAddr, mPort);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //receives the message which the server sends back
        try {
            mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (mIsRunning){
            try {
                mServerMessage = mBufferIn.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }



            Log.d(TAG, "From server: " + mServerMessage);
            if (mServerMessage.compareTo("END THE MSG!")==0)
                mIsRunning = false;

            mBufferOut.println("We get signal!\nAll your bases are belong to us!");
            mBufferOut.flush();

            sendMessageToMainThread(mServerMessage);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }//while (mIsRunning){

        mBufferOut.println("GAME OVER");
        mBufferOut.flush();
        mBufferOut.close();

        mBufferIn = null;
        mBufferOut = null;
        mServerMessage = null;

    }//public void run() {
}//public class TCPClient implements Runnable {
