package tn743.ufrrj.a12jfragcomm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";
    private Button mBBluetooth = null;
    private Button mBTCP = null;
    private Button mBUDP = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBBluetooth  = findViewById(R.id.btn_bluetooth);
        mBTCP  = findViewById(R.id.btn_tcp);
        mBUDP  = findViewById(R.id.btn_udp);

        onButtonClick onbuttonclick = new onButtonClick();

        mBBluetooth.setOnClickListener(onbuttonclick);
        mBTCP.setOnClickListener(onbuttonclick);
        mBUDP.setOnClickListener(onbuttonclick);

        Bundle bundle = new Bundle();
        bundle.putString("my_parameter", "All your bases are belog to us");

        MainFragment mainfrag = new MainFragment();
        mainfrag.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.main_fragmentView, mainfrag)
                .commit();

    }//protected void onCreate(Bundle savedInstanceState) {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
        return;
    }
    private void bluetoothEvent(){
        /*
        FragmentManager fm = getParentFragmentManager();
        BTFragment bt = new BTFragment();
        FragmentTransaction ft = fm.beginTransaction();

        ft.commit();*/

        BTFragment bt = new BTFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragmentView, bt)
                .addToBackStack(null)
                .setReorderingAllowed(true)
                .commit();
    }// private void bluetoothEvent(){



    private void tcpEvent(){
        TCPFragment tcp = new TCPFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragmentView, tcp)
                .addToBackStack(null)
                .setReorderingAllowed(true)
                .commit();
    }//private void tcpEvent(){

    private void udpEvent(){
        UDPFragment udp = new UDPFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragmentView, udp)
                .addToBackStack(null)
                .setReorderingAllowed(true)
                .commit();
    }//private void udpEvent(){
    private class onButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_bluetooth){
                bluetoothEvent();
                return;
            }//if (getId() == R.id.btn_bluetooth){

            if (v.getId() == R.id.btn_tcp){
                tcpEvent();
                return;
            }

            if (v.getId() == R.id.btn_udp){
                udpEvent();
                return;
            }

        }//public void onClick(View v) {
    }//private class onButtonClick implements View.OnClickListener{
}