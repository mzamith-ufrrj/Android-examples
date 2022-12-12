package ufrrj.tn743.a05jnetwork;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class AsynUDP extends AsyncTask<String, String, String> {

    MainActivity mMainActivity = null;
    public AsynUDP(MainActivity m){
        super();
        mMainActivity = m;
    }
    @Override
    protected String doInBackground(String... strings) {
        String[] parse = strings[0].split(":");

        String server = parse[0];
        String output_msg = strings[1];
        int port = Integer.parseInt(parse[1]);



        int msg_lenght = output_msg.length();
        byte []message = output_msg.getBytes();



        DatagramSocket socket = null;
        InetAddress server_ip = null;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        try {
            server_ip  = InetAddress.getByName(server);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        DatagramPacket dp = new DatagramPacket(message, msg_lenght, server_ip, port);

        try {
            socket.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }


        message = new byte[1024];
        dp = new DatagramPacket(message, message.length);
        try {
            socket.receive(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }



        return new String(message, 0, dp.getLength());
    }

    @Override
    protected void onProgressUpdate(String... progress) {

    }

    @Override
    protected void onPostExecute(String result) {
            mMainActivity.receive(result);
    }
}
