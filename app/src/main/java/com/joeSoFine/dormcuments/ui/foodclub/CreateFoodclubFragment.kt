package com.joeSoFine.dormcuments.ui.foodclub

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.joeSoFine.dormcuments.R
import com.google.firebase.database.FirebaseDatabase
import com.joeSoFine.dormcuments.SmartTools
import kotlinx.android.synthetic.main.fragment_create_foodclub.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class CreateFoodclubFragment : Fragment() , View.OnClickListener{
    var database = FirebaseDatabase.getInstance().getReference("Foodclub")
    var choosenDate = ""
    var unform = ""

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_edit_food, container, false)
        val datePicker = root.findViewById<DatePicker>(R.id.datePicker)
        val today = Calendar.getInstance()

        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))

        { view, year, month, day ->
            val local = LocalDate.of(datePicker.year, datePicker.month + 1, datePicker.dayOfMonth)
            //val month = month + 1
            val Formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM", Locale.ENGLISH)
            val msg = local.format(Formatter)
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

        val myAdapter = ArrayAdapter(requireContext(), R.layout.spinner_layout, resources.getStringArray(R.array.spinner_cooks))
        myAdapter.setDropDownViewResource(R.layout.spinner_layout_dropdown)
        root.findViewById<Spinner>(R.id.spinner_c1).adapter = myAdapter
        root.findViewById<Spinner>(R.id.spinner_c2).adapter = myAdapter

        root.findViewById<Button>(R.id.save).setOnClickListener {
            val din = dinner.text.toString()
            val not = note.text.toString()

            if ((spinner_c1.selectedItem.toString() == spinner_c2.selectedItem.toString()) && spinner_c1.selectedItem.toString() != "None" ) {
                    Toast.makeText(context, "Cannot select the same chef twice", Toast.LENGTH_SHORT).show()
            } else if (choosenDate == "") {
                    date2.error = "Please choose a date"
            } else {

                val clubid = database.push().key
                val club = Foodclub(spinner_c1.selectedItem.toString(), spinner_c2.selectedItem.toString(), choosenDate, din, not, "", "", unform)

                if (clubid != null) {

                    database.child(clubid).setValue(club)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Foodclub been created", Toast.LENGTH_SHORT).show()
                            requireFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, FoodclubFragment()).addToBackStack(null)
                                .commit()
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

    override fun onClick(p0: View?) {
    }

}