package tn743.ufrrj.basicdemo1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import tn743.ufrrj.basicdemo1.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private Button button;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        context = getApplicationContext();

        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        //appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
            }
        });

        button = binding.button4;
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(context /* MyActivity */, "All your bases are belong to us!   ",  Toast.LENGTH_SHORT);
                toast.show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.ActivityMainBinding
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, MainActivity2.class);
            startActivity(i);


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        //return NavigationUI.navigateUp(navController, appBarConfiguration)  || super.onSupportNavigateUp();
        return super.onSupportNavigateUp();
    }
}