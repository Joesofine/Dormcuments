package com.example.dormcuments.ui.residents

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.dormcuments.R
import com.example.dormcuments.ui.signIn.SignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*

class profileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        auth = Firebase.auth

        val date: EditText = root.findViewById(R.id.date)
        val name_signup: EditText = root.findViewById(R.id.name_signup)
        val room_spinner: Spinner = root.findViewById(R.id.room_spinner)
        val from: EditText = root.findViewById(R.id.city_signup)
        val diet: EditText = root.findViewById(R.id.diet)
        val funfact: EditText = root.findViewById(R.id.funfact)
        val datePicker: DatePicker = root.findViewById<DatePicker>(R.id.datePicker)
        val close: Button = root.findViewById(R.id.close)

        val today = Calendar.getInstance()

        datePicker.init(2000, today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)) {
                view, year, month, day ->
            val month = month + 1
            val msg = "$day/$month/$year"
            date.setText(msg)
            date.setTextColor(resources.getColor(R.color.White))
        }

        date.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus){
                datePicker.visibility = View.VISIBLE
                close.visibility = View.VISIBLE
                date.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.edit_pen_icon_tint, 0)
                date.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_blue_dark), PorterDuff.Mode.SRC_ATOP)
            } else {
                datePicker.visibility = View.GONE
                close.visibility = View.GONE
                date.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.edit_pen_icon_white, 0)
                date.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP)
            }
        }

        val myAdapter = ArrayAdapter(requireContext(), R.layout.spinner_layout, resources.getStringArray(R.array.spinner))
        myAdapter.setDropDownViewResource(R.layout.spinner_layout_dropdown)
        room_spinner.adapter = myAdapter

        setIconsTint(name_signup, R.drawable.edit_pen_icon_white, R.drawable.edit_pen_icon_tint)
        setIconsTint(from, R.drawable.edit_pen_icon_white, R.drawable.edit_pen_icon_tint)
        setIconsTint(diet, R.drawable.edit_pen_icon_white, R.drawable.edit_pen_icon_tint)
        setIconsTint(funfact, R.drawable.edit_pen_icon_white, R.drawable.edit_pen_icon_tint)


        close.setOnClickListener(){
            datePicker.visibility = View.GONE
            close.visibility = View.GONE
        }

        root.findViewById<Button>(R.id.signout).setOnClickListener(){
            signOut()
            Toast.makeText(context, "You are now signed out", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, SignIn::class.java)
            startActivity(intent)
        }

        root.findViewById<Button>(R.id.save).setOnClickListener(){
            Toast.makeText(context, "Changes are saved", Toast.LENGTH_SHORT).show()
            getFragmentManager()?.popBackStack()
        }

        return root
    }

    private fun signOut() {
        auth.signOut()
    }

    private fun setIconsTint(edit: EditText, noTint: Int, tint: Int){
        edit.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                edit.setCompoundDrawablesWithIntrinsicBounds(0, 0, tint, 0)
                edit.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_blue_dark), PorterDuff.Mode.SRC_ATOP)
            }
            else {
                edit.setCompoundDrawablesWithIntrinsicBounds(0, 0, noTint, 0)
                edit.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP)
            }
        }
    }

 }