package com.example.dormcuments.ui.signIn

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.dormcuments.MainActivity
import com.example.dormcuments.R
import com.example.dormcuments.ui.calender.CalenderFragment
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)


        signIpButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        })

        SignUpButton.setOnClickListener(View.OnClickListener {
            //val intent = Intent(applicationContext, SignUp_activity::class.java)
            //startActivity(intent)
        })

        ForgotPasswordBotton.setOnClickListener(View.OnClickListener {
            //val intent = Intent(applicationContext, Forgotten_activity::class.java)
            //startActivity(intent)
        })
    }
}