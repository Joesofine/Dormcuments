package com.example.dormcuments.ui.signIn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.dormcuments.MainActivity
import com.example.dormcuments.R
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        save.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        })

        back.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, SignUp::class.java)
            startActivity(intent)
        })

    }
}