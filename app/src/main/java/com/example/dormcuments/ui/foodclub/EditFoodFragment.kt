package com.example.dormcuments.ui.foodclub

import android.annotation.SuppressLint
import android.app.AlertDialog
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
        var clubid = bundle?.getString("id")

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

                    choosenDate = date
                    root.findViewById<Spinner>(R.id.spinner_c1).setSelection((spinner_c1.adapter as ArrayAdapter<String>).getPosition(w1))
                    root.findViewById<Spinner>(R.id.spinner_c2).setSelection((spinner_c2.adapter as ArrayAdapter<String>).getPosition(w2))
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
        myAdapter.setDropDownViewResource(R.layout.spinner_layout_dropdown)
        root.findViewById<Spinner>(R.id.spinner_c1).adapter = myAdapter
        root.findViewById<Spinner>(R.id.spinner_c2).adapter = myAdapter

        root.findViewById<ImageView>(R.id.delete2).setOnClickListener() {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(R.string.dialogTitle)
            builder.setMessage(R.string.dialogMessage)
            builder.setIcon(R.drawable.ic_baseline_warning_24)

            builder.setPositiveButton("Continue"){dialogInterface, which ->
                if (clubid != null) {
                    deleteClub(clubid)
                    Toast.makeText(context,"Deleted",Toast.LENGTH_LONG).show()
                }
            }
            builder.setNeutralButton("Cancel"){dialogInterface , which ->
            }

            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }

        root.findViewById<Button>(R.id.save).setOnClickListener {
            val din = dinner.text.toString()
            val not = note.text.toString()
            val part = parti.text.toString()
            val diet = die.text.toString()

            if (spinner_c1.selectedItem.toString() != "none" || spinner_c2.selectedItem.toString() != "none"){
                if (spinner_c1.selectedItem.toString() == spinner_c2.selectedItem.toString()) {
                    Toast.makeText(context, "Cannot select the same chef twice", Toast.LENGTH_SHORT).show() }
            } else if (choosenDate == "") {
                date2.error = "Please choose a date"
            } else {

                var clubid = bundle?.getString("id")
                val club = Foodclub(spinner_c1.selectedItem.toString(), spinner_c2.selectedItem.toString(), choosenDate, din, not, part, diet)


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
        private fun deleteClub(clubid: String){
            var dName = database.child(clubid)

            dName.removeValue()
            Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show()
            getFragmentManager()?.popBackStack()
            getFragmentManager()?.popBackStack()
        }
}