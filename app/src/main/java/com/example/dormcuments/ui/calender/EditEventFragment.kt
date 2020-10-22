package com.example.dormcuments.ui.calender

import android.annotation.SuppressLint
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_create_event.*
import kotlinx.android.synthetic.main.fragment_edit_food.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class EditEventFragment: Fragment() {
    private lateinit var auth: FirebaseAuth
    var all = "false"
    var database = FirebaseDatabase.getInstance().getReference("Events")
    var choosenDateStart = ""
    var choosenDateEnd = ""
    lateinit var getdata : ValueEventListener
    lateinit var Sdate: LocalDate
    lateinit var Edate: LocalDate
    lateinit var unformattedDate: String
    var par = ""
    lateinit var eventid: String

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_create_event, container, false)
        val spinner_color = root.findViewById<Spinner>(R.id.spinner_color)
        val spinner_repeat = root.findViewById<Spinner>(R.id.spinner_repeat)
        val spinner_notis = root.findViewById<Spinner>(R.id.spinner_notis)
        val allday = root.findViewById<Switch>(R.id.allday)
        val colorIcon = root.findViewById<Button>(R.id.colorIcon)
        val datePickerStart = root.findViewById<DatePicker>(R.id.datePickerStart)
        val datePickerEnd = root.findViewById<DatePicker>(R.id.datePickerEnd)
        val today = Calendar.getInstance()
        val bundle = this.arguments
        eventid = bundle?.getString("id").toString()

        auth = Firebase.auth

        getdata = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                if (bundle != null) {
                    if (eventid != null) {

                        all = p0.child(eventid).child("allDay").getValue().toString()
                        var created = p0.child(eventid).child("createdBy").getValue().toString()
                        var color = p0.child(eventid).child("color").getValue().toString()
                        var dateEnd: String = p0.child(eventid).child("dateEnd").getValue().toString()
                        var dateStart = p0.child(eventid).child("dateStart").getValue().toString()
                        var des = p0.child(eventid).child("des").getValue().toString()
                        var doesRepeat = p0.child(eventid).child("doesRepeat").getValue().toString()
                        var location = p0.child(eventid).child("location").getValue().toString()
                        var notification = p0.child(eventid).child("notification").getValue().toString()
                        var timeEnd: String = p0.child(eventid).child("timeEnd").getValue().toString()
                        var timeStart: String = p0.child(eventid).child("timeStart").getValue().toString()
                        var title: String = p0.child(eventid).child("title").getValue().toString()
                        unformattedDate = p0.child(eventid).child("unformattedDate").getValue().toString()
                        par = p0.child(eventid).child("participants").getValue().toString()

                        choosenDateEnd = dateEnd
                        choosenDateStart = dateStart

                        spinner_color.setSelection((spinner_color.adapter as ArrayAdapter<String>).getPosition(color))
                        spinner_repeat.setSelection((spinner_repeat.adapter as ArrayAdapter<String>).getPosition(doesRepeat))
                        spinner_notis.setSelection((spinner_notis.adapter as ArrayAdapter<String>).getPosition(notification))
                        root.findViewById<TextView>(R.id.dateEnd).setText(choosenDateEnd)
                        root.findViewById<TextView>(R.id.dateStart).setText(choosenDateStart)
                        root.findViewById<TextView>(R.id.timeEnd).setText(timeEnd)
                        root.findViewById<TextView>(R.id.timeStart).setText(timeStart)
                        root.findViewById<EditText>(R.id.des).setText(des)
                        root.findViewById<TextView>(R.id.createdText).text = created
                        root.findViewById<EditText>(R.id.location).setText(location)
                        root.findViewById<EditText>(R.id.eventTitle).setText(title)

                        setSwitchStatus(all, allday)

                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) { println("err") }
        }

        database.addValueEventListener(getdata)
        database.addListenerForSingleValueEvent(getdata)

        listenerOnChange(allday,root)

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
            val title = eventTitle.text.toString()
            val locat = location.text.toString()
            val desc = des.text.toString()
            val datStart = dateStart.text.toString()
            val datEnd = dateEnd.text.toString()
            val timStart = timeStart.text.toString()
            val timEnd = timeEnd.text.toString()
            val reapet = spinner_repeat.selectedItem.toString()
            val col = spinner_color.selectedItem.toString()
            val not = spinner_notis.selectedItem.toString()
            val created = createdText.text.toString()


            if (title.isEmpty()) {
                eventTitle.error = "Please name your event"
                eventTitle.requestFocus()
            } else {
                updateEvent(title, datStart, datEnd, timStart, timEnd, desc, locat, col, all, not, reapet, created)
            }
        }

        val myAdapterRea = ArrayAdapter(requireContext(), R.layout.spinner_layout, resources.getStringArray(R.array.spinner_reapets))
        myAdapterRea.setDropDownViewResource(R.layout.spinner_layout_dropdown)
        root.findViewById<Spinner>(R.id.spinner_repeat).adapter = myAdapterRea

        val myAdapterNoti = ArrayAdapter(requireContext(), R.layout.spinner_layout, resources.getStringArray(R.array.spinner_notification))
        myAdapterNoti.setDropDownViewResource(R.layout.spinner_layout_dropdown)
        root.findViewById<Spinner>(R.id.spinner_notis).adapter = myAdapterNoti

        val myAdapterCol = ArrayAdapter(requireContext(), R.layout.spinner_layout, resources.getStringArray(R.array.spinner_colors))
        myAdapterCol.setDropDownViewResource(R.layout.spinner_layout_dropdown)
        spinner_color.adapter = myAdapterCol

        spinner_color.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (spinner_color.selectedItem.toString() == "Default Color"){
                    colorIcon.background = resources.getDrawable(R.drawable.color_button)
                }
                if (spinner_color.selectedItem.toString() == "Red") {
                    colorIcon.background = resources.getDrawable(R.drawable.color_button_red)
                }
                if (spinner_color.selectedItem.toString() == "Blue"){
                    colorIcon.background = resources.getDrawable(R.drawable.color_button_blue)
                }
            }
        }


        datePickerStart.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))
        { view, year, month, day ->
            val dayOfWeekFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE", Locale.ENGLISH)
            val monthformatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH)

            Sdate = LocalDate.of(datePickerStart.year, datePickerStart.month + 1, datePickerStart.dayOfMonth)
            Edate = Sdate
            val weekday = (Sdate.format(dayOfWeekFormatter))
            val dayofmonth = datePickerStart.dayOfMonth
            val monthform = Sdate.format(monthformatter)
            val yearform = datePickerStart.year
            val msgStart = "$weekday. $dayofmonth. $monthform. $yearform"
            unformattedDate = Sdate.toString()
            root.findViewById<TextView>(R.id.dateStart).setText(msgStart)
            root.findViewById<TextView>(R.id.dateEnd).setText(msgStart)
            choosenDateStart = msgStart
            datePickerStart.visibility = View.GONE
        }

        datePickerEnd.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))
        { view, year, month, day ->
            val dayOfWeekFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE", Locale.ENGLISH)
            val monthformatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH)
            Edate = LocalDate.of(datePickerEnd.year, datePickerEnd.month + 1, datePickerEnd.dayOfMonth)

            if (Sdate.isAfter(Edate))
            {
                Toast.makeText(context, "End date cannot be before start date", Toast.LENGTH_SHORT).show()
            } else {
                val weekday = (Edate.format(dayOfWeekFormatter))
                val dayofmonth = datePickerEnd.dayOfMonth
                val monthform = Edate.format(monthformatter)
                val yearform = datePickerEnd.year
                val msgEnd = "$weekday. $dayofmonth. $monthform. $yearform"
                root.findViewById<TextView>(R.id.dateEnd).setText(msgEnd)
                choosenDateEnd = msgEnd
                datePickerEnd.visibility = View.GONE
            }
        }

        return root
    }

    private fun setSwitchStatus(all: String, switch: Switch){
        if (all == "true") {
            switch.isChecked = true
        }
    }

    private fun listenerOnChange(switch: Switch, root: View){
        switch.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            if (isChecked){
                all = "true"
                root.findViewById<TextView>(R.id.timeStart).visibility = View.GONE
                root.findViewById<TextView>(R.id.timeEnd).visibility = View.GONE
            } else {
                all = "false"
                root.findViewById<TextView>(R.id.timeStart).visibility = View.VISIBLE
                root.findViewById<TextView>(R.id.timeEnd).visibility = View.VISIBLE
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateEvent(title: String, datStart: String, datEnd: String, timStart: String, timEnd: String,
                            desc: String, locat: String, col: String, day: String, not: String, reapet: String, created: String) {

        if (reapet.equals("Every week")) {
            var count = -7

            val calendar = Calendar.getInstance()
            val calendarNext = Calendar.getInstance()
            calendarNext.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            val futureWeeksInYear = calendar.getActualMaximum(Calendar.WEEK_OF_YEAR) - calendar.get(Calendar.WEEK_OF_YEAR)
            val WeeksInNextYear = calendarNext.getActualMaximum(Calendar.WEEK_OF_YEAR)

            for (week in futureWeeksInYear..futureWeeksInYear + WeeksInNextYear) {
                count += 7


                val repeatDateS = LocalDate.of(Sdate.year, Sdate.month, Sdate.dayOfMonth).plusDays(count.toLong())
                val repeatDateE = LocalDate.of(Edate.year, Edate.month, Edate.dayOfMonth).plusDays(count.toLong())

                val dayOfWeekFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE", Locale.ENGLISH)
                val monthformatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH)
                val weekdayE = (repeatDateE.format(dayOfWeekFormatter))
                val dayofmonthE = repeatDateE.dayOfMonth
                val monthformE = repeatDateE.format(monthformatter)
                val yearformE = repeatDateE.year
                val msgEnd = "$weekdayE. $dayofmonthE. $monthformE. $yearformE"

                val weekdayS = (repeatDateS.format(dayOfWeekFormatter))
                val dayofmonthS = repeatDateS.dayOfMonth
                val monthformS = repeatDateS.format(monthformatter)
                val yearformS = repeatDateS.year
                val msgStart = "$weekdayS. $dayofmonthS. $monthformS. $yearformS"

                val event = Event(title, msgStart, msgEnd, timStart, timEnd, desc, locat, col, day, not, reapet, created, par, unformattedDate)


                if (eventid != null) {

                    database.child(eventid).setValue(event)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Event has been created", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            // Write failed
                            Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show()
                        }
                }
            }

            requireFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, CalenderFragment()).addToBackStack(null).commit()

        } else {
            val event = Event(title, datStart, datEnd, timStart, timEnd, desc, locat, col, day, not, reapet, created, par, unformattedDate)

            if (eventid != null) {

                database.child(eventid).setValue(event)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Event has been updated", Toast.LENGTH_SHORT).show()
                        requireFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, CalenderFragment()).addToBackStack(null).commit()


                    }
                    .addOnFailureListener {
                        // Write failed
                        Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}