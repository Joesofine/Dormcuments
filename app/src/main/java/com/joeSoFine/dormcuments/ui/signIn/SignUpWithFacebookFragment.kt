package com.joeSoFine.dormcuments.ui.signIn

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.joeSoFine.dormcuments.MainActivity
import com.joeSoFine.dormcuments.MainViewModel
import com.joeSoFine.dormcuments.R
import com.joeSoFine.dormcuments.databaseService
import kotlinx.android.synthetic.main.activity_sign_up_with_facebook.*


class SignUpWithFacebookFragment: AppCompatActivity() {
    var database = FirebaseDatabase.getInstance().getReference("Users")
    private lateinit var auth: FirebaseAuth
    var bool = false
    val ref = "Users"

    override fun onBackPressed() {
        super.onBackPressed()
        if (bool == false) {
            val userid = auth.currentUser?.uid
            if (userid != null) {
                auth.currentUser?.delete()?.addOnSuccessListener {
                    auth.signOut()
                    databaseService.delteChildFromDatabase(userid, ref)
                    val intent = Intent(applicationContext, SignIn::class.java)
                    startActivity(intent)
                }
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_with_facebook)
        auth = Firebase.auth

        val myAdapter = ArrayAdapter(applicationContext, R.layout.spinner_layout, resources.getStringArray(R.array.spinner))
        myAdapter.setDropDownViewResource(R.layout.spinner_layout_dropdown)
        room_spinner.adapter = myAdapter
        
        save.setOnClickListener(View.OnClickListener {
            val number = room_spinner.selectedItem.toString()
            val cityString = city.editText?.text.toString()
            val countryString = country.editText?.text.toString()
            val from = "$cityString, $countryString"
            val diets = diet_fb.editText?.text.toString()
            val fact = funfact.editText?.text.toString()


            if (number == "Roomnumber") {
                Toast.makeText(applicationContext, "Please choose roomnumber", Toast.LENGTH_SHORT)
                    .show()
            } else if (cityString.isEmpty()) {
                city.error  = "Please let us know where you are from"
            } else if (countryString.isEmpty()) {
                country.error = "Please let us know where you are from"
            } else {
                createFbAccount(number, from, diets, fact)
            }
        })
    }

    private fun createFbAccount(number: String, from: String, diet: String, fact: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child(userId).child("number").setValue(number)
            database.child(userId).child("from").setValue(from)
            database.child(userId).child("diet").setValue(diet)
            database.child(userId).child("funfact").setValue(fact)

            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            bool = true
        }
    }
}