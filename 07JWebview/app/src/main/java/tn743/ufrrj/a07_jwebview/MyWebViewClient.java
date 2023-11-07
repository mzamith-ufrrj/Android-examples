package tn743.ufrrj.a07_jwebview;

import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;

public class MyWebViewClient extends WebViewClient {
    private static final String TAG = "MyWebViewClient";
    public MyWebViewClient(){

    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String st = request.getUrl().getHost();
        Log.d(TAG, st);
        return false;
    }


    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        Log.d(TAG, url);
        /*
        if (url.compareTo("https://www.google.com.br") == 0){
            WebResourceRequest newRequest = new W
        }
        */
        return super.shouldInterceptRequest(view, request);
    }
    @Override
    public WebResourceResponse shouldInterceptRequest (final WebView view, String url) {
        // We are currently not intercepting any resources
        return super.shouldInterceptRequest(view, url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        String msg = "All your bases are belong to us!";
        view.loadUrl("javascript:(function(){document.getElementById('id_ex_text').value = '"+msg+"';})()");
        msg = new String("We get signal!");
        view.loadUrl("javascript:(function(){document.getElementById('id_ex_label').innerHTML = '"+msg+"';})()");
    }
}//public class MyWebViewClient extends WebViewClient {
