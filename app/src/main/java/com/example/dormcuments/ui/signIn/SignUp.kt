package com.example.dormcuments.ui.signIn

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dormcuments.R
import com.example.dormcuments.ui.shopping.Item
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.*


class SignUp : AppCompatActivity() {
    var database = FirebaseDatabase.getInstance().getReference("Users")

    @SuppressLint("ClickableViewAccessibility", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val datePicker = findViewById<DatePicker>(R.id.datePicker)

        val today = Calendar.getInstance()
        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH))

        { view, year, month, day ->
            val month = month + 1
            val msg = "$day/$month/$year"
            date.setText(msg)
        }

        date.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus){
                datePicker.visibility = View.VISIBLE
                close.visibility = View.VISIBLE
                date.setCompoundDrawablesWithIntrinsicBounds(R.drawable.birthday_icon_tint, 0, 0, 0)
            } else {
                datePicker.visibility = View.GONE
                date.setCompoundDrawablesWithIntrinsicBounds(R.drawable.birthday_icon, 0, 0, 0)
            }
        }

        setIconsTint(name_signup, R.drawable.name_icon, R.drawable.name_icon_tint)
        setIconsTint(lastname_signup, R.drawable.name_icon, R.drawable.name_icon_tint)
        setIconsTint(city_signup, R.drawable.city_icon, R.drawable.city_icon_tint)
        setIconsTint(country_signup, R.drawable.county_icon, R.drawable.county_icon_tint)
        setIconsTint(diet, R.drawable.diet_icon, R.drawable.diet_icon_tint)
        setIconsTint(funfact, R.drawable.fun_icon, R.drawable.fun_icon_tint)
        setIconsTint(email, R.drawable.email_icon, R.drawable.email_icon_tint)
        setIconsTint(cho_password, R.drawable.password_icon, R.drawable.password_icon_tint)
        setIconsTint(reap_password, R.drawable.password_icon, R.drawable.password_icon_tint)


        val myAdapter = ArrayAdapter(applicationContext, R.layout.spinner_layout, resources.getStringArray(R.array.spinner))
        myAdapter.setDropDownViewResource(R.layout.spinner_layout)
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
            val Uemail = email.text.toString()
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
            if (hasFocus) edit.setCompoundDrawablesWithIntrinsicBounds(tint, 0, 0, 0)
            else edit.setCompoundDrawablesWithIntrinsicBounds(noTint, 0, 0, 0) }
    }
}