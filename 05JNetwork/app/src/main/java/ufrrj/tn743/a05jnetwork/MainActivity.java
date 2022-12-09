package ufrrj.tn743.a05jnetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button mBtnUDP = null,
                   mBtnTCP = null,
                   mBtnHttps = null,
                   mBtnWiFiAPs = null;

    private EditText edtSocket = null;

    private onClickButtons mOnClick = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOnClick = new onClickButtons();
        mBtnUDP = findViewById(R.id.btnUdp);
        mBtnUDP.setOnClickListener(mOnClick);



    }

    private void btnUDPClick(){
        Toast.makeText(getApplicationContext(), "Hello!", Toast.LENGTH_SHORT).show();
    }

    private class onClickButtons implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnUdp: btnUDPClick();break;
            }
        }
    }
}