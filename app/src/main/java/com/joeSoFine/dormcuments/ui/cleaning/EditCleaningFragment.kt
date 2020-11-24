package com.joeSoFine.dormcuments.ui.cleaning

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
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.joeSoFine.dormcuments.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.joeSoFine.dormcuments.databaseService
import com.joeSoFine.dormcuments.ui.UITools
import kotlinx.android.synthetic.main.fragment_create_cleaning.*
import kotlinx.android.synthetic.main.fragment_create_cleaning.spinner_c1
import kotlinx.android.synthetic.main.fragment_create_foodclub.*
import kotlinx.android.synthetic.main.fragment_edit_food.date2
import kotlinx.android.synthetic.main.fragment_edit_food.note
import kotlinx.android.synthetic.main.fragment_edit_food.spinner_c2
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class EditCleaningFragment : Fragment() {
    var database = FirebaseDatabase.getInstance().getReference("Cleaning")
    lateinit var getdata : ValueEventListener
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
        val bundle = this.arguments
        var cleaningid = bundle?.getString("id")
        root.findViewById<ImageView>(R.id.delete).visibility = View.VISIBLE

        database.addValueEventListener(databaseService.setEventListener(
            cleaningid!!,
            root.findViewById<Spinner>(R.id.spinner_c1),
            root.findViewById<Spinner>(R.id.spinner_c2),
            root.findViewById<EditText>(R.id.date2),
            root.findViewById<EditText>(R.id.task),
            root.findViewById<EditText>(R.id.note),
            root.findViewById<TextView>(R.id.stats),
            root.findViewById<TextView>(R.id.unf)
        ))

        unform = root.findViewById<TextView>(R.id.unf).text.toString()
        unform = UITools.setUpDatepicker(root)
        UITools.iniSpinners(root,requireContext(), resources.getStringArray(R.array.spinner_cooks))
        UITools.onTaskClicked(root, root.findViewById<Switch>(R.id.switchH))
        UITools.onDeleteClicked(root, requireContext(), cleaningid, ref, requireFragmentManager())
        UITools.onCleaningSavedClick(cleaningid, ref, root, spinner_c1, spinner_c2, task.text.toString(), note.text.toString(),stats.text.toString(), unform, date2,
            requireContext(), requireFragmentManager())


        return root
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
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

        setSwitchStatus(switchA, "A")
        setSwitchStatus(switchB, "B")
        setSwitchStatus(switchC, "C")
        setSwitchStatus(switchD, "D")
        setSwitchStatus(switchE, "E")
        setSwitchStatus(switchF, "F")
        setSwitchStatus(switchG, "G")
        setSwitchStatus(switchH, "H")
        setSwitchStatus(switchI, "I")
        setSwitchStatus(switchJ, "J")
        setSwitchStatus(switchK, "K")

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

    private fun setSwitchStatus(switch: Switch, st: String){
        str = task.text.toString()
        if ( str.contains(st)){ switch.isChecked = true}
    }

}