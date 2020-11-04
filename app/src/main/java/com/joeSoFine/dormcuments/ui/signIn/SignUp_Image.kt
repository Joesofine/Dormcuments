package com.joeSoFine.dormcuments.ui.signIn

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.joeSoFine.dormcuments.R
import kotlinx.android.synthetic.main.activity_sign_up.save
import kotlinx.android.synthetic.main.activity_sign_up2.*
import java.io.File


class SignUp_Image : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up2)

        save.setOnClickListener(View.OnClickListener {

            val intent = Intent(applicationContext, SignIn::class.java)
            startActivity(intent)
        })

    }
}