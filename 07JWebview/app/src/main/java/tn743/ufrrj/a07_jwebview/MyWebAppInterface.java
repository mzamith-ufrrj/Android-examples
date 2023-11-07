package tn743.ufrrj.a07_jwebview;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class MyWebAppInterface {
    private Context mContext;


    MyWebAppInterface(Context c) {
        mContext = c;
    }

    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

}
