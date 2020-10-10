package com.example.dormcuments.ui.residents

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.dormcuments.R
import com.example.dormcuments.ui.signIn.SignIn
import com.example.dormcuments.ui.signIn.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.time.LocalDate
import java.util.*

class profileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    lateinit var getdata : ValueEventListener
    var database = FirebaseDatabase.getInstance().getReference("Users")
    var cbdate = Calendar.getInstance()

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
        val city_edit:EditText = root.findViewById(R.id.city_edit)
        val country_edit:EditText = root.findViewById(R.id.country_edit)
        val today = Calendar.getInstance()

        val userid = auth.currentUser?.uid

        getdata = object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(p0: DataSnapshot) {

                if (userid != null) {
                    var name = p0.child(userid).child("fname").getValue().toString()
                    var rnumber = p0.child(userid).child("number").getValue().toString()
                    var birthday = p0.child(userid).child("bdate").getValue().toString().split("/")
                    var orgin = p0.child(userid).child("from").getValue().toString()
                    var food = p0.child(userid).child("diet").getValue().toString()
                    var fact = p0.child(userid).child("funfact").getValue().toString()


                    val byear = birthday[2].toInt()
                    val bmonth = birthday[1].toInt()
                    val bday = birthday[0].toInt()


                    val cc: List<String> = orgin.split(", ")

                    name_signup.setText(name)
                    from.setText(orgin)
                    city_edit.setText(cc[0])
                    country_edit.setText(cc[1])
                    diet.setText(food)
                    funfact.setText(fact)
                    date.setText(getAge(byear,bmonth,bday))
                    room_spinner.setSelection(
                        (room_spinner.adapter as ArrayAdapter<String>).getPosition(
                            rnumber
                        )
                    )

                    datePicker.init(birthday[2].toInt(), birthday[1].toInt() - 1, birthday[0].toInt()) { view, year, month, day ->
                        val month = month + 1
                        val msg = "$day/$month/$year"
                        date.setText(msg)
                        date.setTextColor(resources.getColor(R.color.White))
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) { println("err") }
        }

        database.addValueEventListener(getdata)
        database.addListenerForSingleValueEvent(getdata)

        date.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus){
                datePicker.visibility = View.VISIBLE
                close.visibility = View.VISIBLE
                date.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.edit_pen_icon_tint, 0)
                date.getBackground().mutate().setColorFilter(
                    getResources().getColor(android.R.color.holo_blue_dark),
                    PorterDuff.Mode.SRC_ATOP
                )
            } else {
                datePicker.visibility = View.GONE
                close.visibility = View.GONE
                date.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.edit_pen_icon_white,
                    0
                )
                date.getBackground().mutate().setColorFilter(
                    getResources().getColor(android.R.color.white),
                    PorterDuff.Mode.SRC_ATOP
                )
            }
        }
        from.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                city_signup.visibility = View.GONE
                city_edit.visibility = View.VISIBLE
                country_edit.visibility = View.VISIBLE
            } else {
                city_signup.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.edit_pen_icon_white,
                    0
                )
                city_signup.getBackground().mutate().setColorFilter(
                    getResources().getColor(android.R.color.white),
                    PorterDuff.Mode.SRC_ATOP
                )
            }
        }


        val myAdapter = ArrayAdapter(
            requireContext(), R.layout.spinner_layout, resources.getStringArray(
                R.array.spinner
            )
        )
        myAdapter.setDropDownViewResource(R.layout.spinner_layout_dropdown)
        room_spinner.adapter = myAdapter

        setIconsTint(name_signup, R.drawable.edit_pen_icon_white, R.drawable.edit_pen_icon_tint)
        //setIconsTint(from, R.drawable.edit_pen_icon_white, R.drawable.edit_pen_icon_tint)
        setIconsTint(diet, R.drawable.edit_pen_icon_white, R.drawable.edit_pen_icon_tint)
        setIconsTint(funfact, R.drawable.edit_pen_icon_white, R.drawable.edit_pen_icon_tint)
        setIconsTint(city_edit, R.drawable.edit_pen_icon_white, R.drawable.edit_pen_icon_tint)
        setIconsTint(country_edit, R.drawable.edit_pen_icon_white, R.drawable.edit_pen_icon_tint)



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

            val fname = name_signup.text.toString()
            val number = room_spinner.selectedItem.toString()
            val bdate = date.text.toString()
            val city = city_edit.text.toString()
            val country = country_edit.text.toString()
            val from = "$city, $country"
            val diet = diet.text.toString()
            val fact = funfact.text.toString()


            if (fname.isEmpty()) {
                name_signup.error = "Please write a name"
            } else if (number == "Roomnumber") {
                Toast.makeText(requireContext(), "Please choose roomnumber", Toast.LENGTH_SHORT).show()
            } else if (bdate.isEmpty()) {
                date.error = "Please choose birthday"
            } else if (city.isEmpty()) {
                city_signup.error = "Please let us know where you are from"
            } else if (country.isEmpty()) {
                country_signup.error = "Please let us know where you are from"
            } else {

                val user = User(fname, number, bdate, from, diet, fact)

                if (userid != null) {
                    database.child(userid).setValue(user)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Changes are saved", Toast.LENGTH_SHORT).show()
                            getFragmentManager()?.popBackStack()
                        }
                        .addOnFailureListener {
                            // Write failed
                            Toast.makeText(requireContext(), "Try again", Toast.LENGTH_SHORT).show()
                        }
                }

            }
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
                edit.getBackground().mutate().setColorFilter(
                    getResources().getColor(android.R.color.holo_blue_dark),
                    PorterDuff.Mode.SRC_ATOP
                )
            }
            else {
                edit.setCompoundDrawablesWithIntrinsicBounds(0, 0, noTint, 0)
                edit.getBackground().mutate().setColorFilter(
                    getResources().getColor(android.R.color.white),
                    PorterDuff.Mode.SRC_ATOP
                )
            }
        }
    }
    private fun getAge(year: Int, month: Int, day: Int): String? {
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()
        dob[year, month] = day
        var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
        if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
            age--
        }
        val ageInt = age
        return ageInt.toString()
    }
 }