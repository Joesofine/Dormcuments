package com.example.dormcuments.ui.signIn

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.dormcuments.MainActivity
import com.example.dormcuments.R
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        val myAdapter = ArrayAdapter(applicationContext, R.layout.spinner_layout, resources.getStringArray(R.array.spinner))
        myAdapter.setDropDownViewResource(R.layout.spinner_layout)
        room_spinner.adapter = myAdapter


        save.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, SignUp_Image::class.java)
            startActivity(intent)
        })

        back.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, SignIn::class.java)
            startActivity(intent)
        })

    }
}