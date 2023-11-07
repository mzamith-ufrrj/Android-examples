package tn743.ufrrj.j17listadisciplinascc;
//https://guides.codepath.com/android/using-the-recyclerview
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.io.BufferedReader;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";
    private ArrayList<Disciplina> mDisciplinas = null;

    private MyBroadcastReceive mMessageReceiver = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();
        mMessageReceiver = new MyBroadcastReceive();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("disciplina-escolhida"));


        RecyclerView recyclerView = findViewById(R.id.rv_disciplinas);
        DisciplinasAdapter disciplinasAdapter = new DisciplinasAdapter(mDisciplinas, getApplicationContext());
        recyclerView.setAdapter(disciplinasAdapter);
        //recyclerView.addOnItemTouchListener(new RecycleViewClick() );
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);

        super.onDestroy();
    }
    private void loadData(){
        mDisciplinas = new ArrayList<Disciplina>();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open("disciplinas.csv"), "UTF-8"));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                if (mLine.length() > 0){
                    String []campos = mLine.split(";");
                    String codigo = campos[1];
                    String nome = campos[2];
                    String area = campos[3];
                    int periodo = Integer.parseInt(campos[4]);
                    mDisciplinas.add(new Disciplina(codigo, nome, area, periodo));
                }//if (mLine.length() > 0){


            }//while ((mLine = reader.readLine()) != null) {
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Toast.makeText(getApplicationContext(), "Registros carregados: " + Integer.toString(mDisciplinas.size()), Toast.LENGTH_SHORT).show();
    }//private void loadData(){

    private void processReceive(String message){
        int pos = Integer.parseInt(message);
        Disciplina d = mDisciplinas.get(pos);
        String msg = d.getNome() + " Area:" + d.getArea() + " Periodo" + Integer.toString(d.getPeriodo()) ;
        Log.d(TAG, msg);
    }
    private class MyBroadcastReceive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");

            processReceive(message);
        }
    }//private class MyBroadcastReceive extends BroadcastReceiver{
    /*
    private class RecycleViewClick implements RecyclerView.OnItemTouchListener{

        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            Log.d(TAG, "onInterceptTouchEvent" );
            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            Log.d(TAG, "onTouchEvent" );
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            Log.d(TAG, "onRequestDisallowInterceptTouchEvent" );
        }
    }//private class RecycleViewClick implements RecyclerView.OnItemTouchListener{
    */
}//public class MainActivity extends AppCompatActivity {
