package ufrrj.tn743.a03_jservice;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Scanner;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;


public class Download extends Service {
    private static final String TAG = "Download";
    private Response mResponse = null;
    public Download() {
    }
    private String convertInputStream(InputStream is, String encoding) {
            Scanner scanner = new Scanner(is, encoding).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        OkHttpClient client;
        client = new OkHttpClient();
        MyCallBack myCallBack = new MyCallBack();

        /*
        myCallBack.setState(MyCallBack.GETMD5);
        Request request = new Request.Builder().url("https://www.dcc.ufrrj.br/~marcelo/tn743.php").build();
        client.newCall(request).enqueue(myCallBack);
        */


        Request request = new Request.Builder().url("https://www.dcc.ufrrj.br/~marcelo/aulas/TN743/aulas.zip").build();
        client.newCall(request).enqueue(myCallBack);


        //https://www.dcc.ufrrj.br/~marcelo/aulas/TN743/aulas.zip

        return START_NOT_STICKY;
    }

    private void sendMessage(String msg){
        Intent myintent = new Intent("From-Download-To-MainActivity");
        // You can also include some extra data.
        myintent.putExtra("message", msg);
        LocalBroadcastManager.getInstance(this).sendBroadcast(myintent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    private class MyCallBack implements Callback {

  /*
        private void saveFile(Response response){
                InputStream is = response.body().byteStream();
                BufferedInputStream input = new BufferedInputStream(is);
                OutputStream output = null;

                String filename = "example.zip";
                File dir = new File(getApplicationContext().getFilesDir(), "demo");
                if(!dir.exists()){
                    dir.mkdir();
                }

                try {
                    File localfile = new File(dir, filename);
                    output = new FileOutputStream(localfile);
                    byte[] data = new byte[1024];

                    int total = 0, count;
                    do{
                        count = input.read(data);
                        total += count;
                        output.write(data, 0, count);
                        output.flush();
                        output.close();
                        input.close();

                    }while(count != -1);
                    sendMessage("Bytes read: "+Integer.toString(total));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sendMessage("End the game!");
        }
*/


        @Override
        public void onFailure(Call call, IOException e) {
            sendMessage("Failure!");

        }



        @Override
        public void onResponse(Call call, Response response) throws IOException {
            /*
            switch (mState){
                case ERROR: sendMessage(new String("mState is not defined - ERROR!"));break;
                case GETMD5: mMD5 = new String(response.body().string()); sendMessage("MD5: " + mMD5);break;
                case GETFILE:saveFile(response);break;
            }
*/
            File downloadedFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "teste1.zip");
            File localFile = new File(getApplicationContext().getCacheDir(), "teste.zip");
            BufferedSink sink = Okio.buffer(Okio.sink(localFile));
            sink.writeAll(response.body().source());
            sink.close();

            /*
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Files.copy(localFile.toPath(), downloadedFile.toPath());
                sendMessage("File transferred");
            }

            if (localFile.exists())
                localFile.delete();
            */
            stopSelf();
        }
    }

}