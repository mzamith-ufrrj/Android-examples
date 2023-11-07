package ufrrj.tn743.jhello;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LayoutActivityII extends AppCompatActivity {
    private Button mBtnNext = null,
                   mBtnPrev = null;

    private onClickEvent mOnClickEvent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_ii);
        mOnClickEvent = new onClickEvent();
        mBtnNext = findViewById(R.id.btn_ii_next);
        mBtnPrev = findViewById(R.id.btn_ii_prev);

        mBtnNext.setOnClickListener(mOnClickEvent);
        mBtnPrev.setOnClickListener(mOnClickEvent);

    }


    private class onClickEvent implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.btn_ii_next){
                Toast t = Toast.makeText(getApplicationContext(), "Próxima tela!", Toast.LENGTH_SHORT);
                t.show();
                Intent intent = new Intent(getApplicationContext(), LayoutActivityIII.class);
                startActivity(intent);
                return;

            }//if(view.getId() == R.id.btn_main_next){

            if(view.getId() == R.id.btn_ii_prev){
                Toast t = Toast.makeText(getApplicationContext(), "Tela anterior!", Toast.LENGTH_SHORT);
                t.show();
                onBackPressed();

                //Intent intent = new Intent(getApplicationContext(), LayoutActivityII.class);
                //startActivity(intent);
                return;

            }//if(view.getId() == R.id.btn_main_next){

        }//public void onClick(View view) {
    }//private class onClickEvent implements View.OnClickListener{
}