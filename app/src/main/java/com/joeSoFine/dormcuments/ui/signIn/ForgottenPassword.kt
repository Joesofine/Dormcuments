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
import com.joeSoFine.dormcuments.UITools
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.forgot_password_activity.*

class ForgottenPassword : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_password_activity)
        auth = Firebase.auth


        send.setOnClickListener(View.OnClickListener {
            val emailAddress = email2.editText?.text.toString()
            Firebase.auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        sendEmailVerification(emailAddress)
                        Toast.makeText(applicationContext, "Email Send", Toast.LENGTH_SHORT).show()
                    }
                }


        })
    }

    private fun sendEmailVerification(emailAddress: String) {
        // Disable button
        send.isEnabled = false

        auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener(this) { task ->
                // [START_EXCLUDE]
                // Re-enable button
                send.isEnabled = true

                if (task.isSuccessful) {
                    UITools.playLotiieOnceNoPop(succes2)
                    Toast.makeText(baseContext,
                        "Verification email sent to ${emailAddress} ",
                        Toast.LENGTH_SHORT).show()
                } else {
                    UITools.playLotiieOnceNoPop(fail2)
                    Toast.makeText(baseContext,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
    companion object {
        private const val TAG = "EmailPassword"
        private const val RC_MULTI_FACTOR = 9005
    }
}