package tn743.ufrrj.a13jinterfaces;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolBar = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolBar = findViewById(R.id.toolbar_main);
        mToolBar.setTitle(R.string.main_title);
        mToolBar.setSubtitle(R.string.main_subtitle);
    }
}