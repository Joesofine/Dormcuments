package com.joeSoFine.dormcuments.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.joeSoFine.dormcuments.MainActivity
import com.joeSoFine.dormcuments.R
import com.joeSoFine.dormcuments.databaseService
import com.joeSoFine.dormcuments.ui.signIn.SignIn

class SpalshActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var database = FirebaseDatabase.getInstance().getReference("Users")
    val ref = "Users"

    // This is the loading time of the splash screen
    private val SPLASH_TIME_OUT:Long = 3000 // 1 sec

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalsh)
        auth = Firebase.auth
        val user = Firebase.auth.currentUser


        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity

            if (user != null) {
                // User is signed in
                databaseService.checkIfCurrentUserExsist(applicationContext, ref)
            } else {
                val intent = Intent(applicationContext, SignIn::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                overridePendingTransition(R.anim.fadein, R.anim.fadeout)
            }
        }, SPLASH_TIME_OUT)
    }
}