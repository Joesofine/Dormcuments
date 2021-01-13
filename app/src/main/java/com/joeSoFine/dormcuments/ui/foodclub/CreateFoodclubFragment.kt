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
import com.joeSoFine.dormcuments.UITools
import com.joeSoFine.dormcuments.databaseService
import kotlinx.android.synthetic.main.fragment_create_foodclub.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class CreateFoodclubFragment : Fragment() {
    var database = FirebaseDatabase.getInstance().getReference("Foodclub")
    var choosenDate = ""
    var unform = ""
    val ref = "Foodclub"

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_create_foodclub, container, false)
        val unf = root.findViewById<TextView>(R.id.unf)

        UITools.setUpDatepicker(root, unf)
        databaseService.iniSpinGetArr(root,requireContext())

        root.findViewById<Button>(R.id.save).setOnClickListener {
            val din = dinner.text.toString()
            val not = note.text.toString()

            if ((spinner_c1.selectedItem.toString() == spinner_c2.selectedItem.toString()) && spinner_c1.selectedItem.toString() != "None" ) {
                    Toast.makeText(context, "Cannot select the same chef twice", Toast.LENGTH_SHORT).show()
            } else if (unf.text.toString() == "") {
                    date2.error = "Please choose a date"
            } else {

                val clubid = database.push().key
                val club = Foodclub(spinner_c1.selectedItem.toString(), spinner_c2.selectedItem.toString(), date2.text.toString(), din, not, "", "", unf.text.toString())
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
}