package tn743.ufrrj.a12jfragcomm;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class MainFragment extends Fragment {
    private static String TAG = "MainFragment";
    private Button mBtnList = null;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setExitTransition(inflater.inflateTransition(R.transition.fade));
        setEnterTransition(inflater.inflateTransition(R.transition.slide_top));


        String in_param = "";
            if (getArguments() != null) {
            in_param = getArguments().getString("my_parameter");
        }

        /*
        mBBluetooth  = getView().findViewById(R.id.btn_bluetooth);
        mBTCP  = getView().findViewById(R.id.btn_tcp);
        mBUDP  = getVi;ew().findViewById(R.id.btn_udp);
        onButtonClick onbuttonclick = new onButtonClick();
        mBBluetooth.setOnClickListener(onbuttonclick);
        mBTCP.setOnClickListener(onbuttonclick)
        mBUDP.setOnClickListener(onbuttonclick);


        */

        Log.d(TAG, in_param);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_main, container, false);
        mBtnList = view.findViewById(R.id.id_main_frag_btnlist);
        onButtonClick eventClick = new onButtonClick();
        mBtnList.setOnClickListener(eventClick);

        return view;
    }

    private void listConnection(){
        Toast.makeText(getActivity().getApplicationContext(),"TCP, UDP e Bluetooth",Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getActivity(), SecondActivity.class);
        startActivity(i);
    }
    private class onButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.id_main_frag_btnlist){
                listConnection();
                return;
            }//if (getId() == R.id.btn_bluetooth){


        }//public void onClick(View v) {
    }//private class onButtonClick implements View.OnClickListener{


}//public class MainFragment extends Fragment {