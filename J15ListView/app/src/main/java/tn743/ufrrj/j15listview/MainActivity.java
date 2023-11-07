//https://guides.codepath.com/android/using-the-recyclerview
package tn743.ufrrj.j15listview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";
    private RecyclerView recyclerView = null;
   // private List<Pessoa> listaPessoas = null;
    private ArrayList<Pessoa> listaPessoas = null;

    private void loadData(){
        int anonymous = R.drawable.anonymous;
        int foto = R.drawable.foto;
        int linux = R.drawable.tux;

        listaPessoas.add(new Pessoa("Anonymous", "-42", anonymous));
        listaPessoas.add(new Pessoa("Tux", "42", linux));
        listaPessoas.add(new Pessoa("Aleatório", "10000", foto));




    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview);
        listaPessoas = new ArrayList<Pessoa>();
        loadData();
        PessoaAdapter adapter = new PessoaAdapter(listaPessoas);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}