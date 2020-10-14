package com.example.dormcuments.ui.calender

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.View.FOCUS_LEFT
import android.view.ViewGroup
import android.widget.*
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
import kotlinx.android.synthetic.main.list_element_calendar.*
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList

class CalenderFragment : Fragment(),View.OnClickListener {
    var database = FirebaseDatabase.getInstance().getReference("Events")
    lateinit var getdata : ValueEventListener;
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
    var targetHeight = 0
    var targetWidth = 0
    private var current_week: Int = 0
    private var current_month: Int = 0
    private var current_year: Int = 0



    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_calender, container, false)
        auth = Firebase.auth

        myContainer = root.findViewById(R.id.LinScroll)
        sliderLayout = root.findViewById(R.id.sliderLayout);
        scroll = root.findViewById(R.id.scroll)
        week = root.findViewById(R.id.weekID)
        month = root.findViewById(R.id.month)
        year = root.findViewById(R.id.year)

        week.setOnClickListener(this)
        month.setOnClickListener(this)
        year.setOnClickListener(this)

        getTagetSize()
        week.layoutParams = LinearLayout.LayoutParams(targetWidth, targetHeight)
        month.layoutParams = LinearLayout.LayoutParams(targetWidth, targetHeight)
        year.layoutParams = LinearLayout.LayoutParams(targetWidth, targetHeight)


        val calendar = Calendar.getInstance();
        current_week = calendar.get(Calendar.WEEK_OF_YEAR)
        current_month= calendar.get(Calendar.MONTH)
        current_year= calendar.get(Calendar.YEAR)

        makeWeekArr(current_year)
        makeMonthArr(current_month)
        makeYearArr(current_year)

        buttonPressed(week,weeks,targetWidth - 120, current_week - 1)


        getdata = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (i in p0.children) {
                    var title: String = i.child("title").getValue() as String
                    var dateStart: String = i.child("dateStart").getValue() as String
                    var dateEnd: String = i.child("dateEnd").getValue() as String
                    var timeStart: String = i.child("timeStart").getValue() as String
                    var timeEnd: String = i.child("timeEnd").getValue() as String
                    var location: String = i.child("location").getValue() as String
                    var des: String = i.child("des").getValue() as String
                    var allday: String = i.child("allDay").getValue() as String
                    var notis: String = i.child("notification").getValue() as String
                    var created: String = i.child("createdBy").getValue() as String
                    var doesRepeat: String = i.child("doesRepeat").getValue() as String


                    var eventid = i.key.toString()

                    createEventView(title, dateStart, dateEnd, timeStart, timeEnd, des, location, allday, notis, doesRepeat, created, eventid,  myContainer)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                println("err")
            }
        }

        database.addValueEventListener(getdata)


        root.findViewById<FloatingActionButton>(R.id.add).setOnClickListener {
            requireFragmentManager().beginTransaction().add(
                R.id.nav_host_fragment,
                CreateEventFragment()
            ).addToBackStack(null).commit()
        }

        return root
    }

    @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor")
    override fun onClick(p0: View?) {
        if (p0 === week || p0 === month || p0 === year) {
            if (p0 === week) {
                buttonPressed(week,weeks,targetWidth - 120, current_week - 1)

            } else if (p0 === month) {
                buttonPressed(month,months,targetWidth - 40, current_month)

            } else if (p0 === year) {
                buttonPressed(year,years,targetWidth * 3 / years.size, current_year - 2019)
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


    @SuppressLint("ResourceAsColor")
    private fun buttonLoop(arr: ArrayList<String>, buttonWidth: Int) {
        sliderLayout.removeAllViews()
        for (element in arr) {
            val button = Button(context)

            button.setFocusable(true)
            button.setFocusableInTouchMode(true)
            button.layoutParams = LinearLayout.LayoutParams(buttonWidth, targetHeight)
            button.text = element
            button.background = resources.getDrawable(R.color.VeryDarkBlueTopBar)
            context?.let { ContextCompat.getColor(it, R.color.LighterDarkBlue) }?.let { button.setTextColor(it) }

            sliderLayout.addView(button)

            button.setOnClickListener() {
                val childCount = sliderLayout.getChildCount()

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

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun buttonPressed(button: Button, arr: ArrayList<String>, width: Int, current: Int){
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
        }
    }
    private fun createEventView(title: String, dateStart: String, dateEnd: String, timeStart: String, timeEnd: String, des: String, location: String,
                                allDay: String, notification: String, doesRepeat: String, createdBy: String,
                                eventid: String, myContainer: LinearLayout){

        val ExpandableCardview: View =
            layoutInflater.inflate(R.layout.list_element_calendar, null, false)

        var sumLayout : ConstraintLayout = ExpandableCardview.findViewById(R.id.sumLayout)
        var titleLayout : ConstraintLayout = ExpandableCardview.findViewById(R.id.titleLayout)
        var expand : ImageView = ExpandableCardview.findViewById(R.id.expand)
        var eventtitle: TextView = ExpandableCardview.findViewById(R.id.eventTitle)
        var Date: TextView = ExpandableCardview.findViewById(R.id.date)
        var all : TextView = ExpandableCardview.findViewById(R.id.all)
        var startDate: TextView = ExpandableCardview.findViewById(R.id.dateStart2)
        var endDate: TextView = ExpandableCardview.findViewById(R.id.dateEnd2)
        var startTime: TextView = ExpandableCardview.findViewById(R.id.timeStart2)
        var endTime: TextView = ExpandableCardview.findViewById(R.id.timeEnd2)
        var desc: TextView = ExpandableCardview.findViewById(R.id.des)
        var desCon: ImageView = ExpandableCardview.findViewById(R.id.desIcon)
        var loc: TextView = ExpandableCardview.findViewById(R.id.loctext)
        var locCon: ImageView = ExpandableCardview.findViewById(R.id.locIcon)
        var notCon: ImageView = ExpandableCardview.findViewById(R.id.notIcon)
        var notText: TextView = ExpandableCardview.findViewById(R.id.notTekst)
        var reap: TextView = ExpandableCardview.findViewById(R.id.reap)
        var reaCon: ImageView = ExpandableCardview.findViewById(R.id.reaIcon)
        var divdes: View = ExpandableCardview.findViewById(R.id.divdes)
        var divloc: View = ExpandableCardview.findViewById(R.id.divloc)
        var divnot: View = ExpandableCardview.findViewById(R.id.divnot)

        eventtitle.setText(title)
        Date.setText(dateStart)

        if (allDay.equals("true")) {
            startTime.visibility = View.GONE
            endTime.visibility = View.GONE
        }
        else {
            all.visibility = View.GONE
            startDate.setText(dateStart)
            endDate.setText(dateEnd)
            startTime.setText(timeStart)
            endTime.setText(timeEnd)
        }

        if (doesRepeat.equals("Does not repeat")){
            reap.visibility = View.GONE
            reaCon.visibility = View.GONE
        } else {
            reap.setText(doesRepeat)
        }

        if (!location.equals("")){
            loc.setText(location)
        } else {
            divloc.visibility = View.GONE
            loc.visibility = View.GONE
            locCon.visibility = View.GONE
        }

        if (!notification.equals("No notification")){
            notText.setText(notification)
            } else {
            divnot.visibility = View.GONE
            notText.visibility = View.GONE
            notCon.visibility = View.GONE
        }
        if (!des.equals("")){
            desc.setText(des)
        } else {
            divdes.visibility = View.GONE
            desc.visibility = View.GONE
            desCon.visibility = View.GONE
        }

        //Set OnClickListener that handles expansion and collapse of view
        titleLayout.setOnClickListener {
            expandList(sumLayout, expand)}

        myContainer.addView(ExpandableCardview)
    }

    private fun expandList(
        sumLayout: ConstraintLayout,
        expand: ImageView) {
        if (sumLayout.visibility == View.GONE) {
            sumLayout.visibility = View.VISIBLE
            expand.rotation = 90f
        } else if (sumLayout.visibility == View.VISIBLE) {
            sumLayout.visibility = View.GONE
            expand.rotation = 0f
        }
    }

}