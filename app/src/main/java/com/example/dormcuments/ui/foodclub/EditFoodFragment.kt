package com.example.dormcuments.ui.foodclub

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.dormcuments.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_create_foodclub.*
import kotlinx.android.synthetic.main.fragment_edit_food.*
import kotlinx.android.synthetic.main.fragment_edit_food.date2
import kotlinx.android.synthetic.main.fragment_edit_food.dinner
import kotlinx.android.synthetic.main.fragment_edit_food.note
import kotlinx.android.synthetic.main.fragment_edit_food.spinner_c1
import kotlinx.android.synthetic.main.fragment_edit_food.spinner_c2
import kotlinx.android.synthetic.main.fragment_food_details.*
import java.util.*


class EditFoodFragment : Fragment() {
    var database = FirebaseDatabase.getInstance().getReference("Foodclub")
    lateinit var getdata : ValueEventListener
    var choosenDate = ""

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_edit_food, container, false)
        val bundle = this.arguments
        val datePicker = root.findViewById<DatePicker>(R.id.datePicker)
        val today = Calendar.getInstance()

        getdata = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                if (bundle != null) {
                    var clubid = bundle.getString("id")
                    if (clubid != null) {
                        for (i in p0.children) {
                            if (i.key.equals(clubid)){

                                var w1 = i.child("c1").getValue().toString()
                                var w2 = i.child("c2").getValue().toString()
                                var date: String = i.child("date").getValue().toString()
                                var dinner = i.child("dinner").getValue().toString()
                                var note = i.child("note").getValue().toString()

                                root.findViewById<Spinner>(R.id.spinner_c1).setSelection((spinner_c1.adapter as ArrayAdapter<String>).getPosition(w1))
                                root.findViewById<Spinner>(R.id.spinner_c2).setSelection((spinner_c2.adapter as ArrayAdapter<String>).getPosition(w2))
                                choosenDate = date
                                root.findViewById<EditText>(R.id.date2).setText(choosenDate)
                                root.findViewById<EditText>(R.id.dinner).setText(dinner)
                                root.findViewById<EditText>(R.id.note).setText(note)
                            }
                        }
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) { println("err") }
        }

        database.addValueEventListener(getdata)
        database.addListenerForSingleValueEvent(getdata)

        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)) {
                view, year, month, day ->
                val month = month + 1
                val msg = "$day/$month"
                root.findViewById<EditText>(R.id.date2).setText(msg)
                choosenDate = msg
                datePicker.visibility = View.GONE
        }

        root.findViewById<EditText>(R.id.date2).setOnTouchListener { v, event ->
            if (MotionEvent.ACTION_UP == event.action) {
                datePicker.visibility = View.VISIBLE
            }
            true
        }

        val myAdapter = ArrayAdapter(requireContext(), R.layout.spinner_layout, resources.getStringArray(R.array.spinner_cooks))
        myAdapter.setDropDownViewResource(R.layout.spinner_layout)
        root.findViewById<Spinner>(R.id.spinner_c1).adapter = myAdapter
        root.findViewById<Spinner>(R.id.spinner_c2).adapter = myAdapter

        root.findViewById<Button>(R.id.save).setOnClickListener {
            val din = dinner.text.toString()
            val not = note.text.toString()

            if (choosenDate == "") {
                date2.error = "Please choose a date"
            } else {

                val clubid = database.push().key
                val club = Foodclub(spinner_c1.selectedItem.toString(), spinner_c2.selectedItem.toString(), choosenDate, din, not)

                if (clubid != null) {

                    database.child(clubid).setValue(club)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Created been created", Toast.LENGTH_SHORT).show()
                            requireFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, FoodDetailsFragment()).addToBackStack(null).commit()
                        }
                        .addOnFailureListener {
                            // Write failed
                            Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }

        root.findViewById<Button>(R.id.cancel).setOnClickListener {
            requireFragmentManager().beginTransaction().add(
                R.id.nav_host_fragment,
                FoodDetailsFragment()
            ).addToBackStack(null).commit()
        }
        return root
    }
}