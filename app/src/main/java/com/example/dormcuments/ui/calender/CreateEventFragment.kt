package com.example.dormcuments.ui.calender

import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.dormcuments.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_create_event.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class CreateEventFragment : Fragment() {
    var all = "false"
    var database = FirebaseDatabase.getInstance().getReference("Events")
    var choosenDateStart = ""
    var choosenDateEnd = ""


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_create_event, container, false)
        val head = root.findViewById<EditText>(R.id.eventTitle)
        val locat = root.findViewById<EditText>(R.id.location)
        val descrip = root.findViewById<EditText>(R.id.des)
        val allday = root.findViewById<Switch>(R.id.allday)
        val datePickerStart = root.findViewById<DatePicker>(R.id.datePickerStart)
        val datePickerEnd = root.findViewById<DatePicker>(R.id.datePickerEnd)
        val today = Calendar.getInstance()
        val eventTitle = head.text.toString()
        val loca = locat.text.toString()
        val desciprtion = descrip.text.toString()

        listenerOnChange(allday)

        root.findViewById<TextView>(R.id.dateStart).setOnClickListener {
            datePickerStart.visibility = View.VISIBLE
        }
        root.findViewById<TextView>(R.id.dateEnd).setOnClickListener {
            datePickerEnd.visibility = View.VISIBLE
        }

        root.findViewById<TextView>(R.id.timeStart).setOnClickListener{
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                timeStart.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }
        root.findViewById<TextView>(R.id.timeEnd).setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                timeEnd.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }
        root.findViewById<TextView>(R.id.save).setOnClickListener {

        }

        val myAdapterRea = ArrayAdapter(requireContext(), R.layout.spinner_layout, resources.getStringArray(R.array.spinner_reapets))
        myAdapterRea.setDropDownViewResource(R.layout.spinner_layout_dropdown)
        root.findViewById<Spinner>(R.id.spinner_repeat).adapter = myAdapterRea

        val myAdapterNoti = ArrayAdapter(requireContext(), R.layout.spinner_layout, resources.getStringArray(R.array.spinner_notification))
        myAdapterNoti.setDropDownViewResource(R.layout.spinner_layout_dropdown)
        root.findViewById<Spinner>(R.id.spinner_notis).adapter = myAdapterNoti

        val myAdapterCol = ArrayAdapter(requireContext(), R.layout.spinner_layout, resources.getStringArray(R.array.spinner_colors))
        myAdapterCol.setDropDownViewResource(R.layout.spinner_layout_dropdown)
        root.findViewById<Spinner>(R.id.spinner_color).adapter = myAdapterCol

        datePickerStart.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))
        { view, year, month, day ->
            val dayOfWeekFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE", Locale.ENGLISH)
            val monthformatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH)

            val date = LocalDate.of(datePickerStart.year, datePickerStart.month + 1, datePickerStart.dayOfMonth)
            val weekday = (date.format(dayOfWeekFormatter))
            val dayofmonth = datePickerStart.dayOfMonth
            val monthform = date.format(monthformatter)
            val yearform = datePickerStart.year
            val msgStart = "$weekday. $dayofmonth. $monthform. $yearform"
            root.findViewById<TextView>(R.id.dateStart).setText(msgStart)
            choosenDateStart = msgStart
            datePickerStart.visibility = View.GONE
        }

        datePickerEnd.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))
        { view, year, month, day ->
            val dayOfWeekFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE", Locale.ENGLISH)
            val monthformatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH)

            val date = LocalDate.of(datePickerStart.year, datePickerStart.month + 1, datePickerStart.dayOfMonth)
            val weekday = (date.format(dayOfWeekFormatter))
            val dayofmonth = datePickerStart.dayOfMonth
            val monthform = date.format(monthformatter)
            val yearform = datePickerStart.year
            val msgEnd = "$weekday. $dayofmonth. $monthform. $yearform"
            root.findViewById<TextView>(R.id.dateEnd).setText(msgEnd)
            choosenDateEnd = msgEnd
            datePickerEnd.visibility = View.GONE
        }










        return root
    }
    private fun listenerOnChange(switch: Switch){
        switch.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            if (isChecked){
                all = "true"
            } else {
                all = "false"
            }
        }
    }
}