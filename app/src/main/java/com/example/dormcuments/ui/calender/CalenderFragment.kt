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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class CalenderFragment : Fragment(),View.OnClickListener {
    var database = FirebaseDatabase.getInstance().getReference("Events")
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
    private fun createEventView(name: String, des: String, topicId: String, myContainer: LinearLayout){

        val ExpandableCardview: View =
            layoutInflater.inflate(R.layout.list_element_meeting, null, false)

        var sumLayout : ConstraintLayout = ExpandableCardview.findViewById(R.id.sumLayout)
        var titleLayout : ConstraintLayout = ExpandableCardview.findViewById(R.id.titleLayout)
        var expand : ImageView = ExpandableCardview.findViewById(R.id.expand)
        var divider: View = ExpandableCardview.findViewById(R.id.div)
        var delete: ImageView = ExpandableCardview.findViewById(R.id.delete)
        var meetingItem: TextView = ExpandableCardview.findViewById(R.id.resName)
        var sum: TextView = ExpandableCardview.findViewById(R.id.sum)

        meetingItem.setText(name)
        sum.setText(des)

        //Set OnClickListener that handles expansion and collapse of view
        titleLayout.setOnClickListener {
            expandList(sumLayout, expand, divider) }

        delete.setOnClickListener {
            myContainer.removeView(ExpandableCardview)
            //deleteTopic(topicId)
        }

        myContainer.addView(ExpandableCardview)
    }

    private fun expandList(
        sumLayout: ConstraintLayout,
        expand: ImageView, divider: View
    ) {
        if (sumLayout.visibility == View.GONE) {
            sumLayout.visibility = View.VISIBLE
            divider.visibility = View.GONE
            expand.rotation = 90f
        } else if (sumLayout.visibility == View.VISIBLE) {
            sumLayout.visibility = View.GONE
            divider.visibility = View.VISIBLE
            expand.rotation = 0f
        }
    }

    private fun deleteEvent(eventid: String){
        var dName = database.child(eventid)

        dName.removeValue()
        Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show()
    }
}