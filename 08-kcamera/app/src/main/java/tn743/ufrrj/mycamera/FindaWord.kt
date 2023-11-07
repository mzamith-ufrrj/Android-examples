package tn743.ufrrj.mycamera

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FindaWord : AppCompatActivity() {
    private lateinit var mLabelExample : TextView
    private lateinit var mEditExample:EditText
    private lateinit var mLinearLayout:LinearLayout
    private var mMyList:List<EditText> = emptyList()



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finda_word)

        val bundle = intent.extras
        val message = bundle!!.getString("Word")
        var s = 0
        if (message != null)
            s = message.length





        mLabelExample = findViewById(R.id.id_text_label_log)
        mEditExample = findViewById(R.id.id_text_edt_log)
        mLinearLayout = findViewById(R.id.id_linear_layout)


        if (message != null) {

            for (c in message){
                var E:EditText = EditText(this)
                E.setText(c.toString())
                mLinearLayout.addView(E)
            }
        }

        /*
        mMyList = mutableListOf(E)


        E.setText("All your bases are belong to us!")
        mLinearLayout.addView(E)
        */

        mLabelExample.setText(message)
    }
}

