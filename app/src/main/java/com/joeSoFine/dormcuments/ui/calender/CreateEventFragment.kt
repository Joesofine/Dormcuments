package com.joeSoFine.dormcuments.ui.calender

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
import com.airbnb.lottie.LottieAnimationView
import com.joeSoFine.dormcuments.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.joeSoFine.dormcuments.SmartTools
import com.joeSoFine.dormcuments.UITools
import kotlinx.android.synthetic.main.fragment_create_event.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class CreateEventFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    var all = "false"
    var database = FirebaseDatabase.getInstance().getReference("Events")
    var databaseU = FirebaseDatabase.getInstance().getReference("Users")
    var choosenDateStart = ""
    var choosenDateEnd = ""
    lateinit var getdata : ValueEventListener;
    lateinit var Sdate: LocalDate
    lateinit var Edate: LocalDate
    lateinit var unformattedDate: String

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        SmartTools.setUpOnBackPressed(requireActivity())
        val root = inflater.inflate(R.layout.fragment_create_event, container, false)
        val allday = root.findViewById<Switch>(R.id.allday)
        val datePickerStart = root.findViewById<DatePicker>(R.id.datePickerStart)
        val datePickerEnd = root.findViewById<DatePicker>(R.id.datePickerEnd)
        val succes = root.findViewById<LottieAnimationView>(R.id.succes)
        val fail = root.findViewById<LottieAnimationView>(R.id.fail)
        val eventTitle = root.findViewById<EditText>(R.id.eventTitle)
        val divloc = root.findViewById<View>(R.id.divloc)
        val today = Calendar.getInstance()
        val dayOfWeekFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE. dd. MMM. yyyy", Locale.ENGLISH)
        Sdate = LocalDate.now()
        Edate = LocalDate.now()
        unformattedDate = Sdate.toString()
        var formattet = Sdate.format(dayOfWeekFormatter)
        root.findViewById<TextView>(R.id.dateStart).text = formattet
        root.findViewById<TextView>(R.id.dateEnd).text = formattet
        auth = Firebase.auth
        val bundle = this.arguments

        val eventType = bundle?.getString("type")
        if (!eventType.equals("Social event")){
            eventTitle.visibility = View.GONE
            divloc. visibility = View.GONE
        }



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
            root.findViewById<TextView>(R.id.save).isClickable = false
            var title: String
            if (eventType.equals("Meeting")){
                title = "Kitchen Meeting"
            } else if (eventType.equals("Cleaning")){
                title = "Common Cleaning"
            }else if (eventType.equals("Book kitchen")){
                title = "B"
            } else {
                title = eventTitle.text.toString()
            }
            val locat = location.text.toString()
            val desc = des.text.toString()
            val datStart = dateStart.text.toString()
            val datEnd = dateEnd.text.toString()
            val timStart = timeStart.text.toString()
            val timEnd = timeEnd.text.toString()
            val reapet = spinner_repeat.selectedItem.toString()
            val col = bundle?.getString("type")
            val not = spinner_notis.selectedItem.toString()


            if (title.isEmpty()) {
                eventTitle.error = "Please name your event"
                eventTitle.requestFocus()
            } else {

                getdata = object : ValueEventListener {
                        val userid = auth.currentUser?.uid.toString()

                        override fun onDataChange(p0: DataSnapshot) {
                            var name= p0.child(userid).child("fname").getValue().toString().split(" ")
                            var room: String = p0.child(userid).child("number").getValue() as String
                            var created = "${name[0]}, $room"
                            if (eventType.equals("Book kitchen")) {
                                title = "Booked: ${name[0]} - $room"
                            }

                            createEvent(title, datStart, datEnd, timStart, timEnd, desc, locat, col!!, all, not, reapet, created)
                        }

                        override fun onCancelled(p0: DatabaseError) {
                            println("err")
                        }
                    }


                    databaseU.addListenerForSingleValueEvent(getdata)
                }
        }

        val myAdapterRea = ArrayAdapter(requireContext(), R.layout.spinner_layout, resources.getStringArray(R.array.spinner_reapets))
        myAdapterRea.setDropDownViewResource(R.layout.spinner_layout_dropdown)
        root.findViewById<Spinner>(R.id.spinner_repeat).adapter = myAdapterRea

        val myAdapterNoti = ArrayAdapter(requireContext(), R.layout.spinner_layout, resources.getStringArray(R.array.spinner_notification))
        myAdapterNoti.setDropDownViewResource(R.layout.spinner_layout_dropdown)
        root.findViewById<Spinner>(R.id.spinner_notis).adapter = myAdapterNoti



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
    private fun createEvent(title: String, datStart: String, datEnd: String, timStart: String, timEnd: String,
                            desc: String, locat: String, col: String, day: String, not: String, reapet: String, created: String){

        if (reapet.equals("Every week")){
            var count = -7

            val calendar = Calendar.getInstance()
            val calendarNext = Calendar.getInstance()
            calendarNext.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH))
            val futureWeeksInYear = calendar.getActualMaximum(Calendar.WEEK_OF_YEAR) - calendar.get(Calendar.WEEK_OF_YEAR)
            val WeeksInNextYear = calendarNext.getActualMaximum(Calendar.WEEK_OF_YEAR)

            for (week in futureWeeksInYear..futureWeeksInYear){
                count += 7


                val repeatDateS = LocalDate.of(Sdate.year, Sdate.month, Sdate.dayOfMonth).plusDays(count.toLong())
                val repeatDateE = LocalDate.of(Edate.year, Edate.month, Edate.dayOfMonth).plusDays(count.toLong())

                val eventid = database.push().key

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

                val event = Event(title, msgStart, msgEnd, timStart, timEnd, desc, locat, col, day, not, reapet, created, "", unformattedDate)


                if (eventid != null) {

                    database.child(eventid).setValue(event)
                        .addOnSuccessListener {
                            UITools.playLotiieOnce(succes, requireFragmentManager(), "pop")
                        }
                        .addOnFailureListener {
                            // Write failed
                            UITools.playLotiieOnce(fail, requireFragmentManager(), "noPop")
                        }
                }
            }

            requireFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, CalenderFragment()).addToBackStack(null).commit()

        } else {
            val eventid = database.push().key
            val event = Event(title, datStart, datEnd, timStart, timEnd, desc, locat, col, day, not, reapet, created, "", unformattedDate)

            if (eventid != null) {

                database.child(eventid).setValue(event)
                    .addOnSuccessListener {
                        UITools.playLotiieOnce(succes, requireFragmentManager(), "pop")
                    }
                    .addOnFailureListener {
                        UITools.playLotiieOnce(fail, requireFragmentManager(), "noPop")
                    }
            }
        }
    }
}