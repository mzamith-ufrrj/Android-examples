package tn743.ufrrj.a07_jwebview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private Button mBtnLink = null;
    private Button mBtnFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnLink = findViewById(R.id.idBtnLink);
        mBtnFile = findViewById(R.id.idBtnFile);
        onButtonClick onClick = new onButtonClick();
        mBtnLink.setOnClickListener(onClick);
        mBtnFile.setOnClickListener(onClick);


    }

    private void onLinkClick(){
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("type", "link");
        startActivity(intent);

    }

    private void onFileClick(){
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("type", "file");
        startActivity(intent);
    }

    private class onButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.idBtnLink:onLinkClick();break;
                case R.id.idBtnFile:onFileClick();break;
            }
        }//public void onClick(View view) {
    }//private class onButtonClick implements View.OnClickListener{
}//public class MainActivity extends AppCompatActivity {