package com.example.dormcuments.ui.cleaning

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.dormcuments.R
import com.example.dormcuments.ui.foodclub.Foodclub
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_edit_cleaning.*
import kotlinx.android.synthetic.main.fragment_edit_food.*
import kotlinx.android.synthetic.main.fragment_edit_food.date2
import kotlinx.android.synthetic.main.fragment_edit_food.dinner
import kotlinx.android.synthetic.main.fragment_edit_food.note
import kotlinx.android.synthetic.main.fragment_edit_food.spinner_c1
import kotlinx.android.synthetic.main.fragment_edit_food.spinner_c2
import java.util.*

class EditCleaningFragment : Fragment() {
    var database = FirebaseDatabase.getInstance().getReference("Cleaning")
    lateinit var getdata : ValueEventListener
    var choosenDate = ""

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_edit_cleaning, container, false)
        val bundle = this.arguments
        val datePicker = root.findViewById<DatePicker>(R.id.datePicker)
        val today = Calendar.getInstance()

        getdata = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                if (bundle != null) {
                    var cleaningid = bundle.getString("id")
                    if (cleaningid != null) {

                        var w1 = p0.child(cleaningid).child("c1").getValue().toString()
                        var w2 = p0.child(cleaningid).child("c2").getValue().toString()
                        var date: String = p0.child(cleaningid).child("date").getValue().toString()
                        var task = p0.child(cleaningid).child("task").getValue().toString()
                        var note = p0.child(cleaningid).child("note").getValue().toString()

                        root.findViewById<Spinner>(R.id.spinner_c1).setSelection((spinner_c1.adapter as ArrayAdapter<String>).getPosition(w1))
                        root.findViewById<Spinner>(R.id.spinner_c2).setSelection((spinner_c2.adapter as ArrayAdapter<String>).getPosition(w2))
                        choosenDate = date
                        root.findViewById<EditText>(R.id.date2).setText(choosenDate)
                        root.findViewById<EditText>(R.id.tasks).setText(task)
                        root.findViewById<EditText>(R.id.note).setText(note)
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
            val tas = tasks.text.toString()
            val not = note.text.toString()

            if (choosenDate == "") {
                date2.error = "Please choose a date"
            } else {

                var cleanningid = bundle?.getString("id")
                val cleaning = Cleaning(spinner_c1.selectedItem.toString(), spinner_c2.selectedItem.toString(), choosenDate, tas, not)


                if (cleanningid != null) {
                    database.child(cleanningid).setValue(cleaning)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Cleaning has been updated", Toast.LENGTH_SHORT).show()
                            getFragmentManager()?.popBackStack()
                        }
                        .addOnFailureListener {
                            // Write failed
                            Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }

        return root
    }
}