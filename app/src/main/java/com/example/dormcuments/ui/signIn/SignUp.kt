package com.example.dormcuments.ui.signIn

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.dormcuments.R
import com.example.dormcuments.ui.shopping.Item
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.spinner_layout.view.*
import java.util.*


class SignUp : AppCompatActivity() {
    var database = FirebaseDatabase.getInstance().getReference("Users")
    private lateinit var auth: FirebaseAuth

    @SuppressLint("ClickableViewAccessibility", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = Firebase.auth

        val datePicker = findViewById<DatePicker>(R.id.datePicker)

        val today = Calendar.getInstance()
        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH))

        { view, year, month, day ->
            val month = month + 1
            val msg = "$day/$month/$year"
            date.setText(msg)
            date.setTextColor(resources.getColor(R.color.White))
        }


        date.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus){
                datePicker.visibility = View.VISIBLE
                close.visibility = View.VISIBLE
                date.setCompoundDrawablesWithIntrinsicBounds(R.drawable.birthday_icon_tint, 0, 0, 0)
                date.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_blue_dark), PorterDuff.Mode.SRC_ATOP)
            } else {
                datePicker.visibility = View.GONE
                close.visibility = View.GONE
                date.setCompoundDrawablesWithIntrinsicBounds(R.drawable.birthday_icon_white, 0, 0, 0)
                date.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP)
            }
        }

        setIconsTint(name_signup, R.drawable.name_icon_white, R.drawable.name_icon_tint)
        setIconsTint(lastname_signup, R.drawable.name_icon_white, R.drawable.name_icon_tint)
        setIconsTint(city_signup, R.drawable.city_icon_white, R.drawable.city_icon_tint)
        setIconsTint(country_signup, R.drawable.county_icon_white, R.drawable.county_icon_tint)
        setIconsTint(diet, R.drawable.diet_icon_white, R.drawable.diet_icon_tint)
        setIconsTint(funfact, R.drawable.fun_icon_white, R.drawable.fun_icon_tint)
        setIconsTint(email, R.drawable.email_icon_white, R.drawable.email_icon_tint)
        setIconsTint(cho_password, R.drawable.password_icon_white, R.drawable.password_icon_tint)
        setIconsTint(reap_password, R.drawable.password_icon_white, R.drawable.password_icon_tint)


        val myAdapter = ArrayAdapter(applicationContext, R.layout.spinner_layout, resources.getStringArray(R.array.spinner))
        myAdapter.setDropDownViewResource(R.layout.spinner_layout_dropdown)
        room_spinner.adapter = myAdapter

        close.setOnClickListener(){
            datePicker.visibility = View.GONE
            close.visibility = View.GONE
        }

        save.setOnClickListener(View.OnClickListener {
            val fname = name_signup.text.toString()
            val lname = lastname_signup.text.toString()
            val number = room_spinner.selectedItem.toString()
            val bdate = date.text.toString()
            val city = city_signup.text.toString()
            val country = country_signup.text.toString()
            val from = "$city, $country"
            val diet = diet.text.toString()
            val fact = funfact.text.toString()
            val Uemail = email.text.toString().toLowerCase().replace(" ", "")
            val chopass = cho_password.text.toString()
            val reapass = reap_password.text.toString()

            if (fname.isEmpty()) {
                name_signup.error = "Please write a name"
            } else if (lname.isEmpty()) {
                lastname_signup.error = "Please add lastname"
            } else if (number == "Roomnumber") {
                Toast.makeText(applicationContext, "Please choose roomnumber", Toast.LENGTH_SHORT).show()
            } else if (bdate.isEmpty()) {
                date.error = "Please choose birthday"
            } else if (city.isEmpty()) {
                city_signup.error = "Please let us know where you are from"
            } else if (country.isEmpty()) {
                country_signup.error = "Please let us know where you are from"
            } else if (fact.isEmpty()) {
                funfact.error = "Please tell us something funny"
            } else if (Uemail.isEmpty()) {
                email.error = "Please write an email adresse"
            } else if (!Uemail.contains("@") || !Uemail.contains(".")) {
                email.error = "Email not valid"
            } else if (chopass.isEmpty()) {
                cho_password.error = "Please create a password"
            } else if (reapass.isEmpty()) {
                reap_password.error = "Please repeat password"
            } else if (reapass != chopass) {
                cho_password.error = "The passwords are not the same"
                reap_password.error = "The passwords are not the same"

            } else {

                val userId = database.push().key
                val user = User(fname, lname, number, bdate, from, diet, fact, Uemail, reapass)

                if (userId != null) {

                    database.child(userId).setValue(user)
                        .addOnSuccessListener {
                            createAccount(Uemail, reapass)
                            Toast.makeText(applicationContext, "User has been created", Toast.LENGTH_SHORT).show()
                            val intent = Intent(applicationContext, SignUp_Image::class.java)
                            startActivity(intent)
                        }
                        .addOnFailureListener {
                            // Write failed
                            Toast.makeText(applicationContext, "Try again", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        })

        back.setOnClickListener {
            val intent = Intent(applicationContext, SignIn::class.java)
            startActivity(intent)
        }
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

    private fun createAccount(email: String, password: String) {
        Log.d(TAG, "createAccount:$email")
        if (!validateForm()) {
            return
        }


        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        // [END create_user_with_email]
    }

    private fun validateForm(): Boolean {
        var valid = true

        val Uemail = email.text.toString()
        if (TextUtils.isEmpty(Uemail)) {
           email.error = "Required."
            valid = false
        } else {
            email.error = null
        }

        val password = cho_password.text.toString()
        if (TextUtils.isEmpty(password)) {
            cho_password.error = "Required."
            valid = false
        } else {
           cho_password.error = null
        }

        return valid
    }



    companion object {
            private const val TAG = "EmailPassword"
            private const val RC_MULTI_FACTOR = 9005
        }
}