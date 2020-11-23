package com.joeSoFine.dormcuments.ui

import android.annotation.SuppressLint
import android.os.Build
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.joeSoFine.dormcuments.R
import com.joeSoFine.dormcuments.ui.cleaning.Cleaning
import kotlinx.android.synthetic.main.fragment_create_cleaning.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object UITools {
    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.O)
    fun setUpDatepicker(root: View): String {
        val datePicker = root.findViewById<DatePicker>(R.id.datePicker)
        val today = Calendar.getInstance()
        var unform = ""

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

        unform = "${datePicker.dayOfMonth}/${datePicker.month}/${datePicker.year}"
        return unform
    }

    fun setUpPreCleaning(clean: Cleaning, spinner1: Spinner, spinner2: Spinner, date: EditText, task: EditText, note: EditText, stat: TextView, unform: TextView){
        spinner1.setSelection((spinner1.adapter as ArrayAdapter<String>).getPosition(clean.c1))
        spinner2.setSelection((spinner2.adapter as ArrayAdapter<String>).getPosition(clean.c2))
        date.setText(clean.date)
        task.setText(clean.task)
        note.setText(clean.note)
        stat.text = clean.checkedBy
        unform.text = clean.unform
    }


}