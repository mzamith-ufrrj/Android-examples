package tn743.ufrrj.mycamera
//https://blog.logrocket.com/initializing-lazy-lateinit-variables-kotlin/
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var mBtnOpenCamera: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mBtnOpenCamera = findViewById(R.id.id_btn_open_camera)
        mBtnOpenCamera.setOnClickListener{BtnOpenCameraEvent();}
    }//override fun onCreate(savedInstanceState: Bundle?) {

    fun BtnOpenCameraEvent(){
        Toast.makeText(this@MainActivity, "You clicked me.", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, FindaWord::class.java)
        var word = "Marcelo"
        intent.putExtra("Word", word)

        startActivity(intent)
    }//fun BtnOpenCameraEvent(){
}//class MainActivity : AppCompatActivity() {