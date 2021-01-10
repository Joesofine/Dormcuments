package com.joeSoFine.dormcuments.ui.signIn

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.joeSoFine.dormcuments.MainActivity
import com.joeSoFine.dormcuments.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.joeSoFine.dormcuments.databaseService
import kotlinx.android.synthetic.main.activity_sign_up.*

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
                    databaseService.delteChildFromDatabase(userid, ref, applicationContext)
                    val intent = Intent(applicationContext, SignIn::class.java)
                    startActivity(intent)
                }
            }

        }
    }

    override fun onStop() {
        super.onStop()
        if (bool == false) {
            val userid = auth.currentUser?.uid
            if (userid != null) {
                auth.currentUser?.delete()?.addOnSuccessListener {
                    databaseService.delteChildFromDatabase(userid, ref, applicationContext)
                    val intent = Intent(applicationContext, SignIn::class.java)
                    startActivity(intent)
                    auth.signOut()
                }
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_with_facebook)
        auth = Firebase.auth

        setIconsTint(city_signup, R.drawable.city_icon_white, R.drawable.city_icon_tint)
        setIconsTint(country_signup, R.drawable.county_icon_white, R.drawable.county_icon_tint)
        setIconsTint(diet, R.drawable.diet_icon_white, R.drawable.diet_icon_tint)
        setIconsTint(funfact, R.drawable.fun_icon_white, R.drawable.fun_icon_tint)

        val myAdapter = ArrayAdapter(applicationContext, R.layout.spinner_layout, resources.getStringArray(R.array.spinner))
        myAdapter.setDropDownViewResource(R.layout.spinner_layout_dropdown)
        room_spinner.adapter = myAdapter

        save.setOnClickListener(View.OnClickListener {
            val number = room_spinner.selectedItem.toString()
            val city = city_signup.text.toString()
            val country = country_signup.text.toString()
            val from = "$city, $country"
            val diet = diet.text.toString()
            val fact = funfact.text.toString()


            if (number == "Roomnumber") {
                Toast.makeText(applicationContext, "Please choose roomnumber", Toast.LENGTH_SHORT)
                    .show()
            } else if (city.isEmpty()) {
                city_signup.error = "Please let us know where you are from"
            } else if (country.isEmpty()) {
                country_signup.error = "Please let us know where you are from"
            } else {
                createFbAccount(number, from, diet, fact)
            }
        })
    }


    private fun setIconsTint(edit: EditText, noTint: Int, tint: Int) {
        edit.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                edit.setCompoundDrawablesWithIntrinsicBounds(tint, 0, 0, 0)
                edit.getBackground().mutate().setColorFilter(
                    getResources().getColor(android.R.color.holo_blue_dark),
                    PorterDuff.Mode.SRC_ATOP
                )
            } else {
                edit.setCompoundDrawablesWithIntrinsicBounds(noTint, 0, 0, 0)
                edit.getBackground().mutate().setColorFilter(
                    getResources().getColor(android.R.color.white),
                    PorterDuff.Mode.SRC_ATOP
                )
            }
        }
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

            Toast.makeText(applicationContext, "Succes", Toast.LENGTH_SHORT).show()
            bool = true
        }
    }

    companion object {
        private const val TAG = "EmailPassword"
        private const val RC_MULTI_FACTOR = 9005
    }



}