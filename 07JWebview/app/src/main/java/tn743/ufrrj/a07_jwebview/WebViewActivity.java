package tn743.ufrrj.a07_jwebview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.Toast;

public class WebViewActivity extends AppCompatActivity {
    private WebView mWebView = null;
    private FrameLayout mFrameLayout = null;
    private boolean mIsFile = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        mFrameLayout = findViewById(R.id.idFrame);
        mWebView= new WebView(getBaseContext());
        mWebView.setWebViewClient(new MyWebViewClient());
        mFrameLayout.addView(mWebView);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");

        if (type.compareTo("link") == 0) {

            mWebView.loadUrl("https://www.google.com/");
        }else if (type.compareTo("file") == 0) {
            mWebView.loadUrl("file:///android_asset/hello.html");

            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            webSettings.setBuiltInZoomControls(true);
            webSettings.setSupportZoom(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setLightTouchEnabled(true);
            //webSettings.setLoadWithOverviewMode(true);

            mWebView.addJavascriptInterface(new MyWebAppInterface(this), "android");
            mIsFile = true;
        }else{
            Toast.makeText(this, "ERROR: undefined type", Toast.LENGTH_LONG).show();

        }


    }//protected void onCreate(Bundle savedInstanceState) {

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        boolean p1 =  mWebView.canGoBack();
        int v1 = (KeyEvent.KEYCODE_BACK);
        if ((keyCode == v1) && p1) {
            mWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }


}//public class WebViewActivity extends AppCompatActivity {