package com.example.dormcuments.ui.signIn

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.appcompat.app.AppCompatActivity
import com.example.dormcuments.MainActivity
import com.example.dormcuments.R
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignIn : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        mail.setOnTouchListener { v, event ->
            if (MotionEvent.ACTION_UP == event.action) {
                mail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.email_icon_tint, 0, 0, 0)
                password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password_icon, 0, 0, 0)
            }
            false
        }

        password.setOnTouchListener { v, event ->
            if (MotionEvent.ACTION_UP == event.action) {
                mail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.email_icon, 0, 0, 0)
                password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password_icon_tint, 0, 0, 0)
            }
            false
        }



        signIpButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        })

        SignUpButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, SignUp::class.java)
            startActivity(intent)
        })

        ForgotPasswordBotton.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, ForgottenPassword::class.java)
            startActivity(intent)
        })
    }

    private fun resetDrawable(){
        mail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.email_icon, 0, 0, 0)
        password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password_icon, 0, 0, 0)
    }
}