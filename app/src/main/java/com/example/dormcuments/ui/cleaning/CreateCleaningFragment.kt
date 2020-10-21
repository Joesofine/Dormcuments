package com.example.dormcuments.ui.cleaning

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils.replace
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.dormcuments.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_create_cleaning.*
import kotlinx.android.synthetic.main.fragment_create_foodclub.date2
import kotlinx.android.synthetic.main.fragment_create_foodclub.note
import kotlinx.android.synthetic.main.fragment_create_foodclub.spinner_c1
import kotlinx.android.synthetic.main.fragment_create_foodclub.spinner_c2
import java.util.*


class CreateCleaningFragment() : Fragment() {
    var database = FirebaseDatabase.getInstance().getReference("Cleaning")
    var choosenDate = ""
    var str = ""
    var unform = ""


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_create_cleaning, container, false)

        switchIni(root)

        val datePicker = root.findViewById<DatePicker>(R.id.datePicker)
        val today = Calendar.getInstance()

        datePicker.init(
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        )

        { view, year, month, day ->
            val month = month + 1
            val msg = "$day/$month"
            unform = "$day/$month/$year"
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

        root.findViewById<EditText>(R.id.task).setOnTouchListener { v, event ->
            if (MotionEvent.ACTION_UP == event.action) {
                switchH.requestFocus()
            }
            true
        }

        val myAdapter = ArrayAdapter(
            requireContext(), R.layout.spinner_layout, resources.getStringArray(
                R.array.spinner_cooks
            )
        )
        myAdapter.setDropDownViewResource(R.layout.spinner_layout_dropdown)
        root.findViewById<Spinner>(R.id.spinner_c1).adapter = myAdapter
        root.findViewById<Spinner>(R.id.spinner_c2).adapter = myAdapter

        root.findViewById<Button>(R.id.save).setOnClickListener {
            val tas = task.text.toString()
            val not = note.text.toString()

            if (choosenDate == "") {
                date2.error = "Please choose a date"
            } else {

                val cleaningid = database.push().key
                val cleaning = Cleaning(
                    spinner_c1.selectedItem.toString(),
                    spinner_c2.selectedItem.toString(),
                    choosenDate,
                    tas,
                    not,
                    "Unchecked", unform
                )

                if (cleaningid != null) {

                    database.child(cleaningid).setValue(cleaning)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Cleaning been created", Toast.LENGTH_SHORT).show()
                            requireFragmentManager().beginTransaction().replace(
                                R.id.nav_host_fragment,
                                CleaningFragment()
                            ).addToBackStack(null).commit()
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

    private fun switchIni(root: View){
        val switchA = root.findViewById<Switch>(R.id.switchA)
        val switchB = root.findViewById<Switch>(R.id.switchB)
        val switchC = root.findViewById<Switch>(R.id.switchC)
        val switchD = root.findViewById<Switch>(R.id.switchD)
        val switchE = root.findViewById<Switch>(R.id.switchE)
        val switchF = root.findViewById<Switch>(R.id.switchF)
        val switchG = root.findViewById<Switch>(R.id.switchG)
        val switchH = root.findViewById<Switch>(R.id.switchH)
        val switchI = root.findViewById<Switch>(R.id.switchI)
        val switchJ = root.findViewById<Switch>(R.id.switchJ)
        val switchK = root.findViewById<Switch>(R.id.switchK)

        listenerOnChange(switchA, "A")
        listenerOnChange(switchB, "B")
        listenerOnChange(switchC, "C")
        listenerOnChange(switchD, "D")
        listenerOnChange(switchE, "E")
        listenerOnChange(switchF, "F")
        listenerOnChange(switchG, "G")
        listenerOnChange(switchH, "H")
        listenerOnChange(switchI, "I")
        listenerOnChange(switchJ, "J")
        listenerOnChange(switchK, "K")

    }

    private fun listenerOnChange(switch: Switch, st: String){
        switch.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            if (str.isEmpty()){
                str = st
            } else {
                str = task.text.toString() + " + " + st
            }
            if (isChecked){
                task.setText(str)
            } else {
                val s = str.split(" + ")
                if (st == s[0]){
                    str = task.text.toString().replace("$st + ", "")
                } else {
                    str = task.text.toString().replace(" + $st", "")
                }
                task.setText(str)
            }
        }
    }

}