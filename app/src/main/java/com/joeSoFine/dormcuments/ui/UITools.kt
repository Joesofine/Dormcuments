package com.joeSoFine.dormcuments.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentManager
import com.joeSoFine.dormcuments.R
import com.joeSoFine.dormcuments.databaseService
import com.joeSoFine.dormcuments.ui.cleaning.Cleaning
import com.joeSoFine.dormcuments.ui.cleaning.CleaningFragment
import kotlinx.android.synthetic.main.fragment_create_cleaning.*
import kotlinx.android.synthetic.main.fragment_edit_food.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object UITools {
    var str = ""

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.O)
    fun setUpDatepicker(root: View): String {
        val datePicker = root.findViewById<DatePicker>(R.id.datePicker)
        val today = Calendar.getInstance()

        datePicker.init(
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { view, year, month, day ->
            val local = LocalDate.of(datePicker.year, datePicker.month + 1, datePicker.dayOfMonth)
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM", Locale.ENGLISH)
            val msg = local.format(formatter)
            root.findViewById<EditText>(R.id.date2).setText(msg)
            datePicker.visibility = View.GONE
        }

        root.findViewById<EditText>(R.id.date2).setOnTouchListener { v, event ->
            if (ACTION_UP == event.action) {
                datePicker.visibility = View.VISIBLE
            }
            true
        }

        return "${datePicker.dayOfMonth}/${datePicker.month}/${datePicker.year}"
    }

    fun setUpPreCleaning(root: View, clean: Cleaning, spinner1: Spinner, spinner2: Spinner, date: EditText, task: EditText, note: EditText, stat: TextView, unform: TextView){
        spinner1.setSelection((spinner1.adapter as ArrayAdapter<String>).getPosition(clean.c1))
        spinner2.setSelection((spinner2.adapter as ArrayAdapter<String>).getPosition(clean.c2))
        date.setText(clean.date)
        task.setText(clean.task)
        note.setText(clean.note)
        stat.text = clean.checkedBy
        unform.text = clean.unform

        switchIni(root, task)
    }

    fun iniSpinners(root: View, context: Context, arr: Array<String>){
        val myAdapter = ArrayAdapter(context, R.layout.spinner_layout, arr)
        myAdapter.setDropDownViewResource(R.layout.spinner_layout_dropdown)
        root.findViewById<Spinner>(R.id.spinner_c1).adapter = myAdapter
        root.findViewById<Spinner>(R.id.spinner_c2).adapter = myAdapter
    }

    @SuppressLint("ClickableViewAccessibility")
    fun onTaskClicked(root: View, @SuppressLint("UseSwitchCompatOrMaterialCode") switch: Switch){
        root.findViewById<EditText>(R.id.task).setOnTouchListener { v, event ->
            if (MotionEvent.ACTION_UP == event.action) {
                switch.requestFocus()
            }
            true
        }
    }

    fun onDeleteClicked(root: View, context: Context, id:String, ref: String,  fragmentManager: FragmentManager){
        root.findViewById<ImageView>(R.id.delete).setOnClickListener(){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.dialogTitle)
        builder.setMessage(R.string.dialogMessage)
        builder.setIcon(R.drawable.ic_baseline_warning_24)

        builder.setPositiveButton("Continue"){dialogInterface, which ->
            if (id != null) {
                databaseService.delteChildFromDatabase(id, ref, context, fragmentManager)
                Toast.makeText(context,"Deleted",Toast.LENGTH_LONG).show()
            }
        }
        builder.setNeutralButton("Cancel"){dialogInterface , which ->
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
        }
    }

    fun onCleaningSavedClick(id: String, ref: String, root: View, spinner_c1: Spinner, spinner_c2: Spinner, task: EditText, note: EditText, stats: TextView, unform: TextView, date2: EditText, context: Context,fragmentManager: FragmentManager) {
        if (validateCleaningInput(spinner_c1, spinner_c2, date2, context)) {
            val cleaning = Cleaning(
                spinner_c1.selectedItem.toString(),
                spinner_c2.selectedItem.toString(),
                root.findViewById<EditText>(R.id.date2).text.toString(),
                task.text.toString(),
                note.text.toString(),
                stats.text.toString(),
                unform.text.toString()
            )

            if (id != null) {
                databaseService.saveCleaningToDatabase(ref, id, cleaning, context, CleaningFragment(), fragmentManager)
            }
        }

    }

    fun validateCleaningInput(spinner_c1: Spinner, spinner_c2: Spinner, date2: EditText, context: Context): Boolean {
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

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    fun switchIni(root: View, task: EditText){
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

        setSwitchStatus(switchA, "A", task)
        setSwitchStatus(switchB, "B", task)
        setSwitchStatus(switchC, "C", task)
        setSwitchStatus(switchD, "D", task)
        setSwitchStatus(switchE, "E", task)
        setSwitchStatus(switchF, "F", task)
        setSwitchStatus(switchG, "G", task)
        setSwitchStatus(switchH, "H", task)
        setSwitchStatus(switchI, "I", task)
        setSwitchStatus(switchJ, "J", task)
        setSwitchStatus(switchK, "K", task)

        listenerOnChange(switchA, "A", task)
        listenerOnChange(switchB, "B", task)
        listenerOnChange(switchC, "C", task)
        listenerOnChange(switchD, "D", task)
        listenerOnChange(switchE, "E", task)
        listenerOnChange(switchF, "F", task)
        listenerOnChange(switchG, "G", task)
        listenerOnChange(switchH, "H", task)
        listenerOnChange(switchI, "I", task)
        listenerOnChange(switchJ, "J", task)
        listenerOnChange(switchK, "K", task)

    }

    fun listenerOnChange(switch: Switch, st: String, task: EditText){
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

    fun setSwitchStatus(switch: Switch, st: String, task: EditText){
        str = task.text.toString()
        if ( str.contains(st)){ switch.isChecked = true}
    }



}