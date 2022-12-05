package ufrrj.tn743.a03_jservice;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DownloadBG extends IntentService {


    public static final String EXTRA_PARAM_DIR     = "directory.name";
    public static final String EXTRA_PARAM_FILE    = "file.name";

    public String mDirName = null,
                  mFileName = null;

    public DownloadBG() {
        super("DownloadBG");
    }
    private void sendMessage(String msg){
        Intent myintent = new Intent("From-Download-To-MainActivity");
        // You can also include some extra data.
        myintent.putExtra("message", msg);
        LocalBroadcastManager.getInstance(this).sendBroadcast(myintent);
    }

    private void download(){

        OkHttpClient client;
        client = new OkHttpClient();
        DownloadBG.MyCallBack myCallBack = new DownloadBG.MyCallBack();
        Request request = new Request.Builder().url("https://www.dcc.ufrrj.br/~marcelo/aulas/TN743/aulas.zip").build();
        client.newCall(request).enqueue(myCallBack);
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            mDirName = intent.getStringExtra(EXTRA_PARAM_DIR);
            mFileName = intent.getStringExtra(EXTRA_PARAM_FILE);
            download();
        }else{
            sendMessage("Não foi informado o diretório e/ou nome do arquivo!");
        }
    }

    @Override
    public void onDestroy(){
        Toast.makeText(this, "Help!", Toast.LENGTH_LONG).show();
    }
    private class MyCallBack implements Callback {
        @Override
        public void onFailure(Call call, IOException e) {
            sendMessage("Failure!");
        }
        @Override
        public void onResponse(Call call, Response response) throws IOException {

            File file = new File(mDirName, mFileName);
            if (file.exists())
                file.delete();
            BufferedSink sink = Okio.buffer(Okio.sink(file));
            sink.writeAll(response.body().source());
            sink.close();
            sendMessage("Download concluído: " + mFileName);
            stopSelf();

        }

    }

}