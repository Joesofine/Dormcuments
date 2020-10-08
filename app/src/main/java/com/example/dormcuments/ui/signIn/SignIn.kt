package com.example.dormcuments.ui.signIn

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.dormcuments.MainActivity
import com.example.dormcuments.R
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


@Suppress("DEPRECATION")
class SignIn : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
    private var callbackManager: CallbackManager? = null
    private lateinit var auth: FirebaseAuth


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = Firebase.auth

        // Code for generating hash key
        /* try {
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

         */


        setIconsTint(mail, R.drawable.email_icon_white, R.drawable.email_icon_tint)
        setIconsTint(password, R.drawable.password_icon_white, R.drawable.password_icon_tint)

        signIpButton.setOnClickListener(View.OnClickListener {
            signIn(mail.text.toString().toLowerCase().replace(" ", ""), password.text.toString())
        })

        SignUpButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, SignUp::class.java)
            startActivity(intent)
        })

        ForgotPasswordBotton.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, ForgottenPassword::class.java)
            startActivity(intent)
        })


        var login: LoginButton = findViewById(R.id.loginButton)
            callbackManager = CallbackManager.Factory.create()
        login.setReadPermissions("public_profile",
            "email")

        LoginManager.getInstance().logOut()
        login.registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    println("Facebook token: " + loginResult.accessToken.token)
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                }

                override fun onCancel() {
                    println("Facebook onCancel"); }

                override fun onError(error: FacebookException) {
                    println("Facebook onError")
                    }
                })
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    private fun setIconsTint(edit: EditText, noTint: Int, tint: Int){
        edit.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                edit.setCompoundDrawablesWithIntrinsicBounds(tint, 0, 0, 0)
                edit.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_blue_dark), PorterDuff.Mode.SRC_ATOP)
            }
            else {
                edit.setCompoundDrawablesWithIntrinsicBounds(noTint, 0, 0, 0)
                edit.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP)
            }
        }
    }

    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn:$email")
        if (!validateForm()) {
            return
        }

        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    val user = auth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }

                // [START_EXCLUDE]
                if (!task.isSuccessful) {
                    status.setText(R.string.auth_failed)
                    status.visibility = View.VISIBLE
                }
                // [END_EXCLUDE]
            }
        // [END sign_in_with_email]
    }
    private fun validateForm(): Boolean {
        var valid = true

        val email = mail.text.toString()
        if (TextUtils.isEmpty(email)) {
            mail.error = "Required."
            valid = false
        } else {
            mail.error = null
        }

        val Upassword = password.text.toString()
        if (TextUtils.isEmpty(Upassword)) {
            password.error = "Required."
            valid = false
        } else {
            password.error = null
        }

        return valid
    }


    companion object {
        private const val TAG = "EmailPassword"
        private const val RC_MULTI_FACTOR = 9005
    }
}