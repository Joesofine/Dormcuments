package com.example.dormcuments.ui.calender

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.dormcuments.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalField
import java.time.temporal.WeekFields
import java.util.*
import kotlin.collections.ArrayList

class CalenderFragment : Fragment(),View.OnClickListener {
    var database = FirebaseDatabase.getInstance().getReference("Events")
    var databaseU = FirebaseDatabase.getInstance().getReference("Users")
    lateinit var getdata : ValueEventListener
    lateinit var Ugetdata : ValueEventListener
    lateinit var myContainer: LinearLayout
    private lateinit var auth: FirebaseAuth
    private val months = ArrayList<String>()
    private val weeks = ArrayList<String>()
    private val years = ArrayList<String>()
    private val currentMonthArr = ArrayList<String>()
    private lateinit var sliderLayout: LinearLayout
    private lateinit var week: Button
    private lateinit var month: Button
    private lateinit var year: Button
    private lateinit var scroll: HorizontalScrollView
    private lateinit var whoops: TextView
    private lateinit var progressBar: ProgressBar
    var targetHeight = 0
    var targetWidth = 0
    private var current_week: Int = 0
    private var current_month: Int = 0
    private var current_year: Int = 0



    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_calender, container, false)
        auth = Firebase.auth

        myContainer = root.findViewById(R.id.LinScroll)
        sliderLayout = root.findViewById(R.id.sliderLayout)
        scroll = root.findViewById(R.id.scroll)
        week = root.findViewById(R.id.weekID)
        month = root.findViewById(R.id.month)
        year = root.findViewById(R.id.year)
        whoops = root.findViewById(R.id.whoops)
        progressBar = root.findViewById<ProgressBar>(R.id.progressBar2)



        week.setOnClickListener(this)
        month.setOnClickListener(this)
        year.setOnClickListener(this)

        getTagetSize()
        week.layoutParams = LinearLayout.LayoutParams(targetWidth, targetHeight)
        month.layoutParams = LinearLayout.LayoutParams(targetWidth, targetHeight)
        year.layoutParams = LinearLayout.LayoutParams(targetWidth, targetHeight)


        val calendar = Calendar.getInstance()
        current_week = calendar.get(Calendar.WEEK_OF_YEAR)
        current_month = calendar.get(Calendar.MONTH)
        current_year = calendar.get(Calendar.YEAR)

        makeWeekArr(current_year)
        makeMonthArr(current_month)
        makeYearArr(current_year)

        buttonPressed(week, weeks, targetWidth - 120, current_week - 1)

        root.findViewById<FloatingActionButton>(R.id.add).setOnClickListener {
            requireFragmentManager().beginTransaction().add(
                R.id.nav_host_fragment,
                CreateEventFragment()
            ).addToBackStack(null).commit()
        }

        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor")
    override fun onClick(p0: View?) {

        if (p0 === week || p0 === month || p0 === year) {
            if (p0 === week) {
                buttonPressed(week, weeks, targetWidth - 120, current_week - 1)

            } else if (p0 === month) {
                buttonPressed(month, months, targetWidth - 40, current_month)

            } else if (p0 === year) {
                buttonPressed(year, years, targetWidth * 3 / years.size, current_year - 2019)
            }
        }
    }

    private fun makeMonthArr(current_month: Int){
        months.add("januar")
        months.add("Februar")
        months.add("March")
        months.add("April")
        months.add("May")
        months.add("June")
        months.add("July")
        months.add("August")
        months.add("September")
        months.add("October")
        months.add("November")
        months.add("December")
    }
    private fun makeWeekArr(current_year: Int){
        for (i in 1..Calendar.getInstance().getActualMaximum(Calendar.WEEK_OF_YEAR)) {
            val st = "U$i"
            weeks.add(st)
        }
    }
    private fun makeYearArr(current_year: Int){
        for (i in 2019..current_year + 1) {
            years.add(i.toString())
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor", "UseCompatLoadingForDrawables")
    private fun buttonLoop(arr: ArrayList<String>, buttonWidth: Int) {
        sliderLayout.removeAllViews()
        for (element in arr) {
            val button = Button(context)

            button.isFocusable = true
            button.isFocusableInTouchMode = true
            button.layoutParams = LinearLayout.LayoutParams(buttonWidth, targetHeight)
            button.text = element
            button.background = resources.getDrawable(R.color.VeryDarkBlueTopBar)
            context?.let { ContextCompat.getColor(it, R.color.LighterDarkBlue) }?.let { button.setTextColor(it) }

            sliderLayout.addView(button)

            button.setOnClickListener {
                myContainer.removeAllViews()
                whoops.visibility = View.GONE

                val childCount = sliderLayout.childCount

                for (i in 0..childCount - 1) {
                    val v: View = sliderLayout.getChildAt(i)

                    if (v is Button) {
                        val but: Button = v
                        but.background = resources.getDrawable(R.color.VeryDarkBlueTopBar)
                        context?.let { ContextCompat.getColor(it, R.color.LighterDarkBlue) }
                            ?.let { but.setTextColor(it) }
                    }
                }
                button.background = resources.getDrawable(R.color.SaturedCrazyDarkBlue)
                context?.let { ContextCompat.getColor(it, R.color.White) }?.let { button.setTextColor(it) }

                if (arr.equals(weeks)) {
                    var weekNumber = element.replace("U", "").toInt()
                    getSortedEvents(0, weekNumber, weeks)

                } else if (arr.equals(months)) {
                    getSortedEvents(1, months.indexOf(element) + 1, months)

                } else if (arr.equals(years)) {
                    getSortedEvents(0, element.toInt(), years)
                }
            }
        }
    }

    private fun getTagetSize(){
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)

        var width = displayMetrics.widthPixels
        var height = displayMetrics.heightPixels

        targetWidth = ((width / 3))
        targetHeight = week.layoutParams.height
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun buttonPressed(button: Button, arr: ArrayList<String>, width: Int, current: Int){
        progressBar.visibility = View.VISIBLE
        myContainer.removeAllViews()
        week.background = resources.getDrawable(R.color.VeryDarkBlueTopBar)
        month.background = resources.getDrawable(R.color.VeryDarkBlueTopBar)
        year.background = resources.getDrawable(R.color.VeryDarkBlueTopBar)

        context?.let {ContextCompat.getColor(it, R.color.LighterDarkBlue) }?.let { week.setTextColor(it) }
        context?.let {ContextCompat.getColor(it, R.color.LighterDarkBlue) }?.let { month.setTextColor(it) }
        context?.let {ContextCompat.getColor(it, R.color.LighterDarkBlue) }?.let { year.setTextColor(it) }

        button.background = resources.getDrawable(R.color.SaturedCrazyDarkBlue)
        context?.let {ContextCompat.getColor(it, R.color.White) }?.let { button.setTextColor(it) }

        buttonLoop(arr, width)
        val v: View = sliderLayout.getChildAt(current)
        if (v is Button) {
            v.background = resources.getDrawable(R.color.SaturedCrazyDarkBlue)
            context?.let { ContextCompat.getColor(it, R.color.White) }?.let { v.setTextColor(it) }
            v.requestFocus()

            if(arr == weeks){
                getSortedEvents(0, current + 1, weeks)

            } else if (arr == months) {
                getSortedEvents(1, current +1, months)

            } else if (arr == years) {
                getSortedEvents(0, current + 2019, years)
            }

        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("InflateParams", "UseSwitchCompatOrMaterialCode", "SetTextI18n")
    private fun createEventView(
        title: String, dateStart: String, unformattedDate: String, dateEnd: String, timeStart: String, timeEnd: String, des: String, location: String,
        allDay: String, notification: String, doesRepeat: String, createdBy: String,
        eventid: String, par: String, myContainer: LinearLayout, arr: ArrayList<String>){

        val ExpandableCardview: View =
            layoutInflater.inflate(R.layout.list_element_calendar, null, false)

        val sumLayout : ConstraintLayout = ExpandableCardview.findViewById(R.id.sumLayout)
        val titleLayout : ConstraintLayout = ExpandableCardview.findViewById(R.id.titleLayout)
        val expand : ImageView = ExpandableCardview.findViewById(R.id.expand)
        val eventtitle: TextView = ExpandableCardview.findViewById(R.id.eventTitle)
        val Date: TextView = ExpandableCardview.findViewById(R.id.date)
        val all : TextView = ExpandableCardview.findViewById(R.id.all)
        val startDate: TextView = ExpandableCardview.findViewById(R.id.dateStart2)
        val endDate: TextView = ExpandableCardview.findViewById(R.id.dateEnd2)
        val startTime: TextView = ExpandableCardview.findViewById(R.id.timeStart2)
        val endTime: TextView = ExpandableCardview.findViewById(R.id.timeEnd2)
        val desc: TextView = ExpandableCardview.findViewById(R.id.des)
        val desCon: ImageView = ExpandableCardview.findViewById(R.id.desCon)
        val loc: TextView = ExpandableCardview.findViewById(R.id.loctext)
        val locCon: ImageView = ExpandableCardview.findViewById(R.id.locIcon)
        val notCon: ImageView = ExpandableCardview.findViewById(R.id.notIcon)
        val notText: TextView = ExpandableCardview.findViewById(R.id.notTekst)
        val reap: TextView = ExpandableCardview.findViewById(R.id.reap)
        val reaCon: ImageView = ExpandableCardview.findViewById(R.id.reaIcon)
        val divloc: View = ExpandableCardview.findViewById(R.id.divloc)
        val divnot: View = ExpandableCardview.findViewById(R.id.divnot)
        val by: TextView = ExpandableCardview.findViewById(R.id.by)
        val switch: Switch = ExpandableCardview.findViewById(R.id.joinSwitch)
        val parti: TextView = ExpandableCardview.findViewById(R.id.parti)
        val divpar:View = ExpandableCardview.findViewById(R.id.divdes4)
        val uf: TextView = ExpandableCardview.findViewById(R.id.unformatted)

        var eventdate = unformattedDate.split("-")
        var local = LocalDate.of(eventdate[0].toInt(), eventdate[1].toInt(), eventdate[2].toInt())

        eventtitle.text = title
        uf.text = unformattedDate
        by.text = "Created by:\n$createdBy"
        parti.text = par

        val dayOfWeekFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EEEE", Locale.ENGLISH)
        val dayAndMonthFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d. MMMM", Locale.ENGLISH)
        val yearFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.ENGLISH)

        if (arr.equals(weeks)){
            Date.text = local.format(dayOfWeekFormatter)
        } else if (arr.equals(months)){
            Date.text = local.format(dayAndMonthFormatter)
        } else {
            Date.text = local.format(yearFormatter)
        }

        if (allDay.equals("true")) {
            startTime.visibility = View.GONE
            endTime.visibility = View.GONE
        }
        else {
            all.visibility = View.GONE
            startDate.text = dateStart
            endDate.text = dateEnd
            startTime.text = timeStart
            endTime.text = timeEnd
        }

        if (doesRepeat.equals("Does not repeat")){
            reap.visibility = View.GONE
            reaCon.visibility = View.GONE
        } else {
            reap.text = doesRepeat
        }

        setVisiblityEvent(location, "", loc, divloc, locCon)
        setVisiblityEvent(notification, "No notification", notText, divnot, notCon)
        setVisiblityEvent(des, "", desc, divloc, desCon)

        if (des.equals("") && location.equals("")){
            divpar.visibility = View.GONE
        }

        titleLayout.setOnClickListener { expandList(sumLayout, expand)}
        setSwitchForCurrentUser(switch, parti, eventid)


        if (myContainer.childCount == 0) {
            myContainer.addView(ExpandableCardview)
        } else {
            for (i in 0..myContainer.childCount - 1) {
                val ufd = myContainer.getChildAt(i).findViewById<TextView>(R.id.unformatted).text.toString().split("-")
                val elementDate = LocalDate.of(ufd[0].toInt(),ufd[1].toInt(),ufd[2].toInt())

                if (elementDate.isAfter(local) || elementDate.isEqual(local) ) {
                    myContainer.addView(ExpandableCardview, i)
                    break

                } else if (local.isAfter(elementDate)) {
                    if (i == myContainer.childCount - 1) {
                        myContainer.addView(ExpandableCardview)
                        break

                    } else {
                        val ufdK = myContainer.getChildAt(i+1).findViewById<TextView>(R.id.unformatted).text.toString().split("-")
                        val elementDateK = LocalDate.of(ufdK[0].toInt(),ufdK[1].toInt(),ufdK[2].toInt())

                        if (local.isBefore(elementDateK) || local.isEqual(elementDateK)) {
                            myContainer.addView(ExpandableCardview, i + 1)
                            break

                        } else {

                            for (j in i + 1..myContainer.childCount - 1) {
                                val ufdJ = myContainer.getChildAt(j).findViewById<TextView>(R.id.unformatted).text.toString().split("-")
                                val elementDateJ = LocalDate.of(ufdJ[0].toInt(), ufdJ[1].toInt(), ufdJ[2].toInt())

                                if (local.isBefore(elementDateJ)) {
                                    myContainer.addView(ExpandableCardview, j)
                                    break

                                }
                            }
                        }
                        break
                    }
                }
            }
        }
    }

    private fun expandList(
        sumLayout: ConstraintLayout,
        expand: ImageView
    ) {
        if (sumLayout.visibility == View.GONE) {
            sumLayout.visibility = View.VISIBLE
            expand.rotation = 90f
        } else if (sumLayout.visibility == View.VISIBLE) {
            sumLayout.visibility = View.GONE
            expand.rotation = 0f
        }
    }

    private fun listenerOnChange(switch: Switch, rn: String, eventid: String, parti: TextView){
        switch.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            var st = ""
            if (isChecked) {

                if (parti.text.toString().isEmpty()) {
                    parti.text = rn
                } else {
                    st = parti.text.toString() + ", " + rn
                    parti.text = st
                }
                st = parti.text.toString()

                database.child(eventid).child("participants").setValue(st).addOnSuccessListener {
                    Toast.makeText(context, "succesfully joined event", Toast.LENGTH_SHORT).show()
                }
                    .addOnFailureListener {}

            } else {
                if (parti.text.toString().contains(", $rn")) {
                    st = parti.text.toString().replace(", $rn", "")
                } else {
                    st = parti.text.toString().replace(rn, "")
                }
                parti.text = st

                database.child(eventid).child("participants").setValue(st).addOnSuccessListener {
                    Toast.makeText(context, "Sign up deleted", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener { }

            }
        }
    }

    private fun setSwitchStatus(switch: Switch, rn: String, parti: TextView){
        if ( parti.text.toString().contains(rn)){ switch.isChecked = true}
    }

    private fun setSwitchForCurrentUser(switch: Switch, parti: TextView, eventid: String){
        Ugetdata = object : ValueEventListener {
            val userid = auth.currentUser?.uid.toString()

            override fun onDataChange(p0: DataSnapshot) {
                var room: String = p0.child(userid).child("number").getValue() as String

                setSwitchStatus(switch, room, parti)
                listenerOnChange(switch, room, eventid, parti)
            }

            override fun onCancelled(p0: DatabaseError) {
                println("err")
            }
        }

        databaseU.addListenerForSingleValueEvent(Ugetdata)
    }

    private fun getSortedEvents(DateIndex: Int, relevantDatePart: Int, arr: ArrayList<String>){

        getdata = object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(p0: DataSnapshot) {
                for (i in p0.children) {

                    var dateUn: String = i.child("unformattedDate").value as String
                    var eventdate = dateUn.split("-")

                    if (arr.equals(weeks)){
                        var local = LocalDate.of(eventdate[0].toInt(), eventdate[1].toInt(), eventdate[2].toInt())
                        val woy: TemporalField = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()

                        if (local.get(woy) == relevantDatePart){
                            eventDateCall(i, arr)
                        }
                    } else {
                        if (eventdate[DateIndex].toInt() == relevantDatePart) {
                            eventDateCall(i, arr)
                        }
                    }
                }
                setWhoops()
                progressBar.visibility = View.GONE
            }

            override fun onCancelled(p0: DatabaseError) {
                println("err")
            }
        }

        database.addListenerForSingleValueEvent(getdata)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun eventDateCall(i: DataSnapshot, arr: ArrayList<String>){
        var dateUn: String = i.child("unformattedDate").value as String
        var title: String = i.child("title").value as String
        var dateStart: String = i.child("dateStart").value as String
        var dateEnd: String = i.child("dateEnd").value as String
        var timeStart: String = i.child("timeStart").value as String
        var timeEnd: String = i.child("timeEnd").value as String
        var location: String = i.child("location").value as String
        var des: String = i.child("des").value as String
        var allday: String = i.child("allDay").value as String
        var notis: String = i.child("notification").value as String
        var created: String = i.child("createdBy").value as String
        var doesRepeat: String = i.child("doesRepeat").value as String
        var par = i.child("participants").value.toString()
        var eventid = i.key.toString()

        createEventView(title, dateStart, dateUn, dateEnd, timeStart, timeEnd, des, location, allday, notis, doesRepeat, created, eventid, par, myContainer, arr)
    }

    private fun setWhoops(){
        if (myContainer.childCount == 0) {
            whoops.visibility = View.VISIBLE
        } else {
            whoops.visibility = View.GONE
        }
    }

    private fun setVisiblityEvent(st: String, equals: String, tv: TextView, div: View, con: ImageView){
        if (!st.equals(equals)){
            tv.text = st
        } else {
            div.visibility = View.GONE
            tv.visibility = View.GONE
            con.visibility = View.GONE
        }
    }

}