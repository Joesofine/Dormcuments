package com.example.dormcuments.ui.signIn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.dormcuments.R
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.forgot_password_activity.*

class ForgottenPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_password_activity)

        send.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, SignIn::class.java)
            startActivity(intent)
        })
    }
}