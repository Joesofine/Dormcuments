package com.joeSoFine.dormcuments.ui.signIn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.joeSoFine.dormcuments.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.forgot_password_activity.*

class ForgottenPassword : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_password_activity)
        auth = Firebase.auth


        send.setOnClickListener(View.OnClickListener {
            //sendEmailVerification()

            val emailAddress = auth.currentUser?.email

            if (emailAddress != null) {
                Firebase.auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            Toast.makeText(applicationContext, "Email Send", Toast.LENGTH_SHORT).show()
                            val intent = Intent(applicationContext, SignIn::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                        }
                    }
            }

        })
    }

    private fun sendEmailVerification() {
        // Disable button
        send.isEnabled = false

        // Send verification email
        // [START send_email_verification]
        val user = auth.currentUser!!
        user.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                // [START_EXCLUDE]
                // Re-enable button
                send.isEnabled = true

                if (task.isSuccessful) {
                    Toast.makeText(baseContext,
                        "Verification email sent to ${user.email} ",
                        Toast.LENGTH_SHORT).show()
                } else {
                    Log.e(TAG, "sendEmailVerification", task.exception)
                    Toast.makeText(baseContext,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT).show()
                }
                // [END_EXCLUDE]
            }
        // [END send_email_verification]
    }
    companion object {
        private const val TAG = "EmailPassword"
        private const val RC_MULTI_FACTOR = 9005
    }
}