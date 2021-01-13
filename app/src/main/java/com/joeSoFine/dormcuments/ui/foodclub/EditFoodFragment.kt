package com.joeSoFine.dormcuments.ui.foodclub

import android.annotation.SuppressLint
import android.app.AlertDialog
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.joeSoFine.dormcuments.SmartTools
import com.joeSoFine.dormcuments.UITools
import com.joeSoFine.dormcuments.databaseService
import kotlinx.android.synthetic.main.fragment_edit_food.*
import kotlinx.android.synthetic.main.fragment_edit_food.date2
import kotlinx.android.synthetic.main.fragment_edit_food.dinner
import kotlinx.android.synthetic.main.fragment_edit_food.note
import kotlinx.android.synthetic.main.fragment_edit_food.spinner_c1
import kotlinx.android.synthetic.main.fragment_edit_food.spinner_c2
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class EditFoodFragment : Fragment() {
    var database = FirebaseDatabase.getInstance().getReference("Foodclub")
    val ref = "Foodclub"
    lateinit var getdata : ValueEventListener
    var choosenDate = ""
    lateinit var unform: String

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_edit_food, container, false)
        val bundle = this.arguments
        var clubid = bundle?.getString("id")

        root.findViewById<ImageView>(R.id.delete).visibility = View.VISIBLE

        getdata = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (clubid != null) {
                    var w1 = p0.child(clubid).child("c1").getValue().toString()
                    var w2 = p0.child(clubid).child("c2").getValue().toString()
                    var date: String = p0.child(clubid).child("date").getValue().toString()
                    var dinner = p0.child(clubid).child("dinner").getValue().toString()
                    var note = p0.child(clubid).child("note").getValue().toString()
                    var par = p0.child(clubid).child("participants").getValue().toString()
                    var diet = p0.child(clubid).child("diets").getValue().toString()
                    unform = p0.child(clubid).child("unform").getValue().toString()

                    choosenDate = date
                    root.findViewById<Spinner>(R.id.spinner_c1).setSelection(( root.findViewById<Spinner>(R.id.spinner_c1).adapter as ArrayAdapter<String>).getPosition(w1))
                    root.findViewById<Spinner>(R.id.spinner_c2).setSelection(( root.findViewById<Spinner>(R.id.spinner_c2).adapter as ArrayAdapter<String>).getPosition(w2))
                    root.findViewById<EditText>(R.id.date2).setText(choosenDate)
                    root.findViewById<EditText>(R.id.dinner).setText(dinner)
                    root.findViewById<EditText>(R.id.note).setText(note)
                    root.findViewById<TextView>(R.id.parti).setText(par)
                    root.findViewById<TextView>(R.id.die).setText(diet)
                }
            }
            override fun onCancelled(p0: DatabaseError) { println("err") }
        }

        database.addValueEventListener(getdata)
        database.addListenerForSingleValueEvent(getdata)

        unform = UITools.setUpDatepicker(root)

        UITools.iniSpinners(root,requireContext(), resources.getStringArray(R.array.spinner_cooks))
        UITools.onDeleteClicked(root, requireContext(), clubid!!, ref, requireFragmentManager())


        root.findViewById<Button>(R.id.save).setOnClickListener {
            val din = dinner.text.toString()
            val not = note.text.toString()
            val part = parti.text.toString()
            val diet = die.text.toString()

            if ((spinner_c1.selectedItem.toString() == spinner_c2.selectedItem.toString()) && spinner_c1.selectedItem.toString() != "None" ) {
                Toast.makeText(context, "Cannot select the same chef twice", Toast.LENGTH_SHORT).show()
            } else if (choosenDate == "") {
                date2.error = "Please choose a date"
            } else {

                var clubid = bundle?.getString("id")
                val club = Foodclub(spinner_c1.selectedItem.toString(), spinner_c2.selectedItem.toString(), choosenDate, din, not, part, diet, unform)


                if (clubid != null) {

                    database.child(clubid).setValue(club)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Foodclub has been updated", Toast.LENGTH_SHORT).show()
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