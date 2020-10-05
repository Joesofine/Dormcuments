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
import com.example.dormcuments.ui.foodclub.FoodclubFragment
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_create_cleaning.*
import kotlinx.android.synthetic.main.fragment_create_foodclub.*
import kotlinx.android.synthetic.main.fragment_create_foodclub.date2
import kotlinx.android.synthetic.main.fragment_create_foodclub.dinner
import kotlinx.android.synthetic.main.fragment_create_foodclub.note
import kotlinx.android.synthetic.main.fragment_create_foodclub.spinner_c1
import kotlinx.android.synthetic.main.fragment_create_foodclub.spinner_c2
import java.util.*

class CreateCleaningFragment : Fragment() {
    var database = FirebaseDatabase.getInstance().getReference("Cleaning")
    var choosenDate = ""

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
    val root = inflater.inflate(R.layout.fragment_create_cleaning, container, false)

        val datePicker = root.findViewById<DatePicker>(R.id.datePicker)
        val today = Calendar.getInstance()

        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))

        { view, year, month, day ->
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
            val tas = task.text.toString()
            val not = note.text.toString()

            if (choosenDate == "") {
                date2.error = "Please choose a date"
            } else {

                val cleaningid = database.push().key
                val cleaning = Cleaning(spinner_c1.selectedItem.toString(), spinner_c2.selectedItem.toString(), choosenDate, tas, not)

                if (cleaningid != null) {

                    database.child(cleaningid).setValue(cleaning)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Cleaning been created", Toast.LENGTH_SHORT).show()
                            requireFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, CleaningFragment()).addToBackStack(null).commit()
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
                CleaningFragment()
            ).addToBackStack(null).commit()
        }


        return root
    }
}