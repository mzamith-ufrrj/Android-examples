package tn743.ufrrj.btdemo3;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CommDemo  extends Thread {
    private static final String TAG = "CommDemo";
    @Override
    public void run() {
        URL               url = null;
        InputStream       in = null;
        HttpURLConnection urlConnection = null;
        int responseCode  = -1;
        try {
            url = new URL("https://192.168.1.11");
        } catch (MalformedURLException e) {
            Log.d(TAG, "http: " + e.toString());
            throw new RuntimeException(e);
        }


        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            //urlConnection.connect();
            responseCode = urlConnection.getResponseCode();
        } catch (IOException e) {
            Log.d(TAG, "http: " + e.toString());
            throw new RuntimeException(e);
        }

        try {
            in = new BufferedInputStream(urlConnection.getInputStream());
        } catch (IOException e) {
            Log.d(TAG, "http: " + e.toString());
            throw new RuntimeException(e);
        }finally {
            urlConnection.disconnect();

        }

        String st = "EMPITY";

        if (in != null)
            st = readStream(in);

        Log.d(TAG, "http: " + st);
    }


    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}
