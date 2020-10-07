package com.example.dormcuments.ui.signIn

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.dormcuments.MainActivity
import com.example.dormcuments.R
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.activity_sign_in.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


class SignIn : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
    private var callbackManager: CallbackManager? = null

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        try {
            val info = packageManager.getPackageInfo(
                "com.example.dormcuments",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                println("--------------------------------------------------------------------------------------------")
                println((Base64.getEncoder().encodeToString(md.digest())))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }


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

        loginButton.setOnClickListener(View.OnClickListener {
            // Login
            callbackManager = CallbackManager.Factory.create()
            LoginManager.getInstance().logInWithReadPermissions(
                this, Arrays.asList(
                    "public_profile",
                    "email"
                )
            )
            println("Facebook onError.=========================================")
            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        println("Facebook token: " + loginResult.accessToken.token)
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                    }
                    override fun onCancel() { println("Facebook onCancel"); }
                    override fun onError(error: FacebookException) { println("Facebook onError") }
                })
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }
}