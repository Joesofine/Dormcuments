package com.joeSoFine.dormcuments.ui.signIn

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.joeSoFine.dormcuments.R
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.*


class SignUp : AppCompatActivity() {
    var database = FirebaseDatabase.getInstance().getReference("Users")
    private lateinit var auth: FirebaseAuth

    @SuppressLint("ClickableViewAccessibility", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = Firebase.auth

        val myAdapter = ArrayAdapter(applicationContext, R.layout.spinner_layout, resources.getStringArray(R.array.spinner))
        myAdapter.setDropDownViewResource(R.layout.spinner_layout_dropdown)
        room_spinner.adapter = myAdapter
        if (Build.VERSION.SDK_INT>25){
            room_spinner.isFocusable = true;
            room_spinner.isFocusableInTouchMode = true;
            room_spinner.requestFocus();
        }

        val datePicker = findViewById<DatePicker>(R.id.datePicker)
        val today = Calendar.getInstance()

        datePicker.init(
            2000, today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        )

        { view, year, month, day ->
            val month = month + 1
            val msg = "$day/$month/$year"
            date.editText?.setText(msg)
            date.editText?.setTextColor(resources.getColor(R.color.White))
        }


        date.editText?.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus){
                dateLayout.visibility = View.VISIBLE
            } else {
                dateLayout.visibility = View.GONE
            }
        }

        name_signup.editText?.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus){
                val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
            }
        }
        city_signup.editText?.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus){
                val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
            }
        }
        country_signup.editText?.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus){
                val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
            }
        }

        close.setOnClickListener(){
            dateLayout.visibility = View.GONE
        }

        save.setOnClickListener(View.OnClickListener {
            val fname = name_signup.editText?.text.toString()
            val number = room_spinner.selectedItem.toString()
            val bdate = date.editText?.text.toString()
            val city = city_signup.editText?.text.toString()
            val country = country_signup.editText?.text.toString()
            val from = "$city, $country"
            val diet = diet.editText?.text.toString()
            val fact = funfact.editText?.text.toString()
            val Uemail = email.text.toString().toLowerCase().replace(" ", "")
            val chopass = cho_password.editText?.text.toString()
            val reapass = reap_password.editText?.text.toString()

            if (fname.isEmpty()) {
                name_signup.error = "Please write a name"
                name_signup.requestFocus()
            } else if (number == "Roomnumber") {
                Toast.makeText(applicationContext, "Please choose roomnumber", Toast.LENGTH_SHORT).show()
            } else if (bdate.isEmpty()) {
                date.error = "Please choose birthday"
            } else if (city.isEmpty()) {
                city_signup.error = "Please let us know where you are from"
            } else if (country.isEmpty()) {
                country_signup.error = "Please let us know where you are from"
            } else if (Uemail.isEmpty()) {
                email.error = "Please write an email adresse"
            } else if (!Uemail.contains("@") || !Uemail.contains(".")) {
                email.error = "Email not valid"
            } else if (chopass.isEmpty()) {
                cho_password.error = "Please choose a password"
            } else if (reapass.isEmpty()) {
                reap_password.error = "Please repeat password"
            } else if (reapass != chopass) {
                cho_password.error = "The passwords are not the same"
                reap_password.error = "The passwords are not the same"
            } else if (chopass.length < 8) {
                cho_password.error = "Password has to be at least 8 long"
            } else {
                createAccount(Uemail, reapass, fname, number, bdate, from, diet, fact)
            }
        })
    }

    private fun createAccount(email: String, password: String, fname: String, number: String, bdate: String, from: String, diet: String, fact: String) {
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
                    Toast.makeText(applicationContext, "Part 1 done", Toast.LENGTH_SHORT).show()
                    val userId = auth.currentUser?.uid

                    val user = User(fname, number, bdate, from, diet, fact, "")

                    if (userId != null) {

                        val intent = Intent(applicationContext, SignUp_Image::class.java)
                        intent.putExtra(("userid"), userId)
                        intent.putExtra("user", user)
                        startActivity(intent)
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    }
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

        val password = cho_password.editText?.text.toString()
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
        }
}