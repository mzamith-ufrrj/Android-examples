package ufrrj.tn743.jhello;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button mBtnNext = null;
    private onClickEvent mClickEvent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnNext = findViewById(R.id.btn_main_next);
        mClickEvent = new onClickEvent();
        mBtnNext.setOnClickListener(mClickEvent);

    }

    private class onClickEvent implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.btn_main_next){
                Toast t = Toast.makeText(getApplicationContext(), "Próxima tela!", Toast.LENGTH_SHORT);
                t.show();
                Intent intent = new Intent(getApplicationContext(), LayoutActivityII.class);



                startActivity(intent);

            }//if(view.getId() == R.id.btn_main_next){
        }//public void onClick(View view) {
    }//private class onClickEvent implements View.OnClickListener{
}