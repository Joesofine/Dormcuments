package com.example.dormcuments.ui.signIn

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.dormcuments.MainActivity
import com.example.dormcuments.R
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.*

class SignUp : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
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

        name_signup.setOnTouchListener { v, event ->
            if (MotionEvent.ACTION_UP == event.action) {
                makeIconsGrey()
                name_signup.setCompoundDrawablesWithIntrinsicBounds(R.drawable.name_icon_tint, 0, 0, 0)
            }
            false
        }
        lastname_signup.setOnTouchListener { v, event ->
            if (MotionEvent.ACTION_UP == event.action) {
                makeIconsGrey()
                lastname_signup.setCompoundDrawablesWithIntrinsicBounds(R.drawable.name_icon_tint, 0, 0, 0)
            }
            false
        }
        date.setOnTouchListener { v, event ->
            if (MotionEvent.ACTION_UP == event.action) {
                makeIconsGrey()
                date.setCompoundDrawablesWithIntrinsicBounds(R.drawable.birthday_icon_tint, 0, 0, 0)
                datePicker.visibility = View.VISIBLE
                close.visibility = View.VISIBLE
            }
            false
        }
        city_signup.setOnTouchListener { v, event ->
            if (MotionEvent.ACTION_UP == event.action) {
                makeIconsGrey()
                city_signup.setCompoundDrawablesWithIntrinsicBounds(R.drawable.city_icon_tint, 0, 0, 0)
            }
            false
        }
        country_signup.setOnTouchListener { v, event ->
            if (MotionEvent.ACTION_UP == event.action) {
                makeIconsGrey()
                country_signup.setCompoundDrawablesWithIntrinsicBounds(R.drawable.county_icon_tint, 0, 0, 0)
            }
            false
        }
        diet.setOnTouchListener { v, event ->
            if (MotionEvent.ACTION_UP == event.action) {
                makeIconsGrey()
                diet.setCompoundDrawablesWithIntrinsicBounds(R.drawable.diet_icon_tint, 0, 0, 0)
            }
            false
        }
        funfact.setOnTouchListener { v, event ->
            if (MotionEvent.ACTION_UP == event.action) {
                makeIconsGrey()
                funfact.setCompoundDrawablesWithIntrinsicBounds(R.drawable.fun_icon_tint, 0, 0, 0)
            }
            false
        }
        email.setOnTouchListener { v, event ->
            if (MotionEvent.ACTION_UP == event.action) {
                makeIconsGrey()
                email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.email_icon_tint, 0, 0, 0)
            }
            false
        }
        cho_password.setOnTouchListener { v, event ->
            if (MotionEvent.ACTION_UP == event.action) {
                makeIconsGrey()
                cho_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password_icon_tint, 0, 0, 0)
            }
            false
        }
        reap_password.setOnTouchListener { v, event ->
            if (MotionEvent.ACTION_UP == event.action) {
                makeIconsGrey()
                reap_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password_icon_tint, 0, 0, 0)
            }
            false
        }




        val myAdapter = ArrayAdapter(applicationContext, R.layout.spinner_layout, resources.getStringArray(R.array.spinner))
        myAdapter.setDropDownViewResource(R.layout.spinner_layout)
        room_spinner.adapter = myAdapter

        close.setOnClickListener(){
            datePicker.visibility = View.GONE
            close.visibility = View.GONE
        }

        save.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, SignUp_Image::class.java)
            startActivity(intent)
        })

        back.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, SignIn::class.java)
            startActivity(intent)
        })

    }

    private fun makeIconsGrey(){
        name_signup.setCompoundDrawablesWithIntrinsicBounds(R.drawable.name_icon, 0, 0, 0)
        lastname_signup.setCompoundDrawablesWithIntrinsicBounds(R.drawable.name_icon, 0, 0, 0)
        date.setCompoundDrawablesWithIntrinsicBounds(R.drawable.birthday_icon, 0, 0, 0)
        city_signup.setCompoundDrawablesWithIntrinsicBounds(R.drawable.city_icon, 0, 0, 0)
        country_signup.setCompoundDrawablesWithIntrinsicBounds(R.drawable.county_icon, 0, 0, 0)
        diet.setCompoundDrawablesWithIntrinsicBounds(R.drawable.diet_icon, 0, 0, 0)
        funfact.setCompoundDrawablesWithIntrinsicBounds(R.drawable.fun_icon, 0, 0, 0)
        email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.email_icon, 0, 0, 0)
        cho_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password_icon, 0, 0, 0)
        reap_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password_icon, 0, 0, 0)
    }

}