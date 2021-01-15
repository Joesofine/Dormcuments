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
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.textfield.TextInputLayout
import com.joeSoFine.dormcuments.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.joeSoFine.dormcuments.SmartTools
import com.joeSoFine.dormcuments.UITools
import com.joeSoFine.dormcuments.databaseService
import kotlinx.android.synthetic.main.fragment_create_foodclub.*
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
        val root = inflater.inflate(R.layout.fragment_create_foodclub, container, false)
        val unf = root.findViewById<TextView>(R.id.unf)
        val succes = root.findViewById<LottieAnimationView>(R.id.succes)
        val fail = root.findViewById<LottieAnimationView>(R.id.fail)

        val bundle = this.arguments
        var clubid = bundle?.getString("id")

        databaseService.iniSpinGetArr(root,requireContext())
        root.findViewById<ImageView>(R.id.delete).visibility = View.VISIBLE

        getdata = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (clubid != null) {
                    var c1 = p0.child(clubid).child("c1").getValue().toString()
                    var c2 = p0.child(clubid).child("c2").getValue().toString()
                    var date: String = p0.child(clubid).child("date").getValue().toString()
                    var dinner = p0.child(clubid).child("dinner").getValue().toString()
                    var note = p0.child(clubid).child("note").getValue().toString()
                    var par = p0.child(clubid).child("participants").getValue().toString()
                    var diet = p0.child(clubid).child("diets").getValue().toString()
                    unf.text = p0.child(clubid).child("unform").getValue().toString()

                    choosenDate = date
                    root.findViewById<Spinner>(R.id.spinner_c1).setSelection(( root.findViewById<Spinner>(R.id.spinner_c1).adapter as ArrayAdapter<String>).getPosition(c1))
                    root.findViewById<Spinner>(R.id.spinner_c2).setSelection(( root.findViewById<Spinner>(R.id.spinner_c2).adapter as ArrayAdapter<String>).getPosition(c2))
                    root.findViewById<TextInputLayout>(R.id.date2).editText?.setText(choosenDate)
                    root.findViewById<TextInputLayout>(R.id.dinner).editText?.setText(dinner)
                    root.findViewById<TextInputLayout>(R.id.note).editText?.setText(note)
                    root.findViewById<TextView>(R.id.parti).setText(par)
                    root.findViewById<TextView>(R.id.die).setText(diet)
                }
            }
            override fun onCancelled(p0: DatabaseError) { println("err") }
        }

        database.addValueEventListener(getdata)
        database.addListenerForSingleValueEvent(getdata)

        UITools.setUpDatepicker(root, unf)
        UITools.onDeleteClicked(root, requireContext(), clubid!!, ref, requireFragmentManager())


        root.findViewById<Button>(R.id.save).setOnClickListener {
            val din = dinner.editText?.text.toString()
            val not = note.editText?.text.toString()
            val part = parti.text.toString()
            val diet = die.text.toString()

            if ((spinner_c1.selectedItem.toString() == spinner_c2.selectedItem.toString()) && spinner_c1.selectedItem.toString() != "None" ) {
                Toast.makeText(context, "Cannot select the same chef twice", Toast.LENGTH_SHORT).show()
            } else if (unf.text.toString() == "") {
                date2.error = "Please choose a date"
            } else {

                var clubid = bundle?.getString("id")
                val club = Foodclub(spinner_c1.selectedItem.toString(), spinner_c2.selectedItem.toString(), date2.editText?.text.toString(), din, not, part, diet, unf.text.toString())

                if (clubid != null) {
                    database.child(clubid).setValue(club)
                        .addOnSuccessListener {
                            UITools.playLotiieOnce(succes, requireFragmentManager(),"pop")
                        }
                        .addOnFailureListener {
                            UITools.playLotiieOnceNoPop(fail)
                        }

                }
            }
        }
        return root
    }
}