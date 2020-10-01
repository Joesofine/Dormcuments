package com.example.dormcuments.ui.signIn

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.dormcuments.R
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.*


class SignUp : AppCompatActivity() {
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

    private fun setIconsTint(edit: EditText, noTint: Int, tint: Int){
        edit.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) edit.setCompoundDrawablesWithIntrinsicBounds(tint, 0, 0, 0)
            else edit.setCompoundDrawablesWithIntrinsicBounds(noTint, 0, 0, 0) }
    }


    fun disableSoftInputFromAppearing(editText: EditText) {
        if (Build.VERSION.SDK_INT >= 11) {
            editText.setRawInputType(InputType.TYPE_CLASS_TEXT)
            editText.setTextIsSelectable(true)
        } else {
            editText.setRawInputType(InputType.TYPE_NULL)
            editText.isFocusable = true
        }
    }


}