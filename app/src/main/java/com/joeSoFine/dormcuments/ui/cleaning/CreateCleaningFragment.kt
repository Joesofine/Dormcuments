package com.joeSoFine.dormcuments.ui.cleaning

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
import com.joeSoFine.dormcuments.databaseService
import com.joeSoFine.dormcuments.ui.UITools
import kotlinx.android.synthetic.main.fragment_create_cleaning.*
import kotlinx.android.synthetic.main.fragment_create_foodclub.date2
import kotlinx.android.synthetic.main.fragment_create_foodclub.note
import kotlinx.android.synthetic.main.fragment_create_foodclub.spinner_c1
import kotlinx.android.synthetic.main.fragment_create_foodclub.spinner_c2
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class CreateCleaningFragment() : Fragment() {
    var str = ""
    var unform = ""
    val ref = "Cleaning"

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_create_cleaning, container, false)

        switchIni(root)
        unform = UITools.setUpDatepicker(root)
        UITools.iniSpinners(root,requireContext(),resources.getStringArray(R.array.spinner_cooks))

        root.findViewById<Button>(R.id.save).setOnClickListener { onSavedClick() }

        UITools.onTaskClicked(root, root.findViewById(R.id.switchH))


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

    private fun validateInput(): Boolean {
        return if ((spinner_c1.selectedItem.toString() == spinner_c2.selectedItem.toString()) && spinner_c1.selectedItem.toString() != "None" ) {
            Toast.makeText(context, "Cannot select the same cleaner twice", Toast.LENGTH_SHORT).show()
            false
        } else if (date2.text.toString() == "") {
            date2.error = "Please choose a date"
            false
        } else {
            true
        }
    }
    private fun onSavedClick(){
        if (validateInput()){
            val cleaningid = databaseService.generateID(ref)
            val cleaning = Cleaning(
                spinner_c1.selectedItem.toString(),
                spinner_c2.selectedItem.toString(),
                date2.text.toString(),
                task.text.toString(),
                note.text.toString(),
                "Unchecked",
                unform
            )

            if (cleaningid != null) {
                databaseService.saveCleaningToDatabase(ref, cleaningid, cleaning, requireContext(), CleaningFragment(), requireFragmentManager())
            }
        }
    }

}