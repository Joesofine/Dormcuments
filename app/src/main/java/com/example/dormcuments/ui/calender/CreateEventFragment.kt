package com.example.dormcuments.ui.calender

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.dormcuments.R
import com.example.dormcuments.ui.meeting.MeetingFragment
import com.example.dormcuments.ui.meeting.Topic
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_add_shop_item.*
import kotlinx.android.synthetic.main.fragment_add_shop_item.inputItem
import kotlinx.android.synthetic.main.fragment_add_topic.*
import kotlinx.android.synthetic.main.fragment_create_event.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class CreateEventFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    var all = "false"
    var database = FirebaseDatabase.getInstance().getReference("Events")
    var choosenDateStart = ""
    var choosenDateEnd = ""


    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_create_event, container, false)
        val spinner_color = root.findViewById<Spinner>(R.id.spinner_color)
        val head = root.findViewById<EditText>(R.id.eventTitle)
        val locat = root.findViewById<EditText>(R.id.location)
        val descrip = root.findViewById<EditText>(R.id.des)
        val allday = root.findViewById<Switch>(R.id.allday)
        val colorIcon = root.findViewById<Button>(R.id.colorIcon)
        val datePickerStart = root.findViewById<DatePicker>(R.id.datePickerStart)
        val datePickerEnd = root.findViewById<DatePicker>(R.id.datePickerEnd)
        val today = Calendar.getInstance()
        auth = Firebase.auth

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
            val created = auth.currentUser?.uid


            if (title.isEmpty()) {
                eventTitle.error = "Please name your event"
                eventTitle.requestFocus()
            } else {
                if (created != null) {
                    createEvent(title, datStart, datEnd, timStart, timEnd, desc, locat, col, all, not, reapet, created)
                }

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

    private fun createEvent(title: String, datStart: String, datEnd: String, timStart: String, timEnd: String,
                            desc: String, locat: String, col: String, day: String, not: String, reapet: String, created: String){
        val eventid = database.push().key
        val event = Event(title, datStart, datEnd, timStart, timEnd, desc, locat, col, day, not, reapet, created)

        if (eventid != null) {

            database.child(eventid).setValue(event)
                .addOnSuccessListener {
                    Toast.makeText(context, "Event has been created", Toast.LENGTH_SHORT).show()
                    requireFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, CalenderFragment()).addToBackStack(null).commit()


                }
                .addOnFailureListener {
                    // Write failed
                    Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show()
                }
        }
    }




}