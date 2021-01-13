package com.joeSoFine.dormcuments.ui.calender

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
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.joeSoFine.dormcuments.R
import com.joeSoFine.dormcuments.UITools
import com.joeSoFine.dormcuments.databaseService
import com.nambimobile.widgets.efab.ExpandableFabLayout
import kotlinx.android.synthetic.main.fragment_rules.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalField
import java.time.temporal.WeekFields
import java.util.*
import kotlin.collections.ArrayList

class CalenderFragment : Fragment(),View.OnClickListener {
    var database = FirebaseDatabase.getInstance().getReference("Events")
    lateinit var getdata : ValueEventListener
    lateinit var myContainer: LinearLayout
    private lateinit var auth: FirebaseAuth
    private val months = ArrayList<String>()
    private val weeks = ArrayList<String>()
    private val years = ArrayList<String>()
    private lateinit var sliderLayout: LinearLayout
    private lateinit var week: Button
    private lateinit var month: Button
    private lateinit var year: Button
    private lateinit var scroll: HorizontalScrollView
    private lateinit var whoops: TextView
    private lateinit var lottie: LottieAnimationView
    var targetHeight = 0
    var targetWidth = 0
    private var current_week: Int = 0
    private var current_month: Int = 0
    private var current_year: Int = 0
    val refE = "Events"
    val refU = "Users"


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_calender, container, false)
        lottie = root.findViewById<LottieAnimationView>(R.id.animation_view)
        auth = Firebase.auth

        myContainer = root.findViewById(R.id.LinScroll)
        sliderLayout = root.findViewById(R.id.sliderLayout)
        scroll = root.findViewById(R.id.scroll)
        week = root.findViewById(R.id.weekID)
        month = root.findViewById(R.id.month)
        year = root.findViewById(R.id.year)
        whoops = root.findViewById(R.id.whoops)

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

        buttonPressed(week, weeks, "weeks",targetWidth - 120, current_week - 1)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val expandableFabLayout = view.findViewById<ExpandableFabLayout>(R.id.fab_layout)
        expandableFabLayout.portraitConfiguration.fabOptions.forEach { it.setOnClickListener(this) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor")
    override fun onClick(p0: View?) {

        when(p0?.id){
            R.id.option1 -> { requireFragmentManager().beginTransaction().add(R.id.nav_host_fragment, CreateEventFragment()).addToBackStack(null).commit()}
            R.id.option2 -> { UITools.onHelpedClicked(requireContext(), R.string.helpDialogTitleGrocery, R.string.helpDialogMsgGrocery)}
            R.id.option3 -> { }
            // so on and so forth...
        }

        if (p0 === week || p0 === month || p0 === year) {
            if (p0 === week) {
                buttonPressed(week, weeks, "weeks",targetWidth - 120, current_week - 1)

            } else if (p0 === month) {
                buttonPressed(month, months, "months", targetWidth - 40, current_month)

            } else if (p0 === year) {
                buttonPressed(year, years, "years",targetWidth * 3 / years.size, current_year - (current_year - 1) )
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
        val cal = Calendar.getInstance()
        cal[Calendar.YEAR] = current_year - 1
        cal[Calendar.MONTH] = Calendar.DECEMBER
        cal[Calendar.DAY_OF_MONTH] = 31
        val last_year_weeks = (cal[Calendar.WEEK_OF_YEAR])

        if (last_year_weeks == 53){
            weeks.add("U53")
        }

        for (i in 1..Calendar.getInstance().getActualMaximum(Calendar.WEEK_OF_YEAR)) {
            val st = "U$i"
            weeks.add(st)
        }
    }
    private fun makeYearArr(current_year: Int){
        for (i in current_year - 1..current_year + 1) {
            years.add(i.toString())
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor", "UseCompatLoadingForDrawables")
    private fun buttonLoop(arr: ArrayList<String>, buttonWidth: Int) {
        sliderLayout.removeAllViews()
        for (element in arr) {
            val button = Button(context)

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
                    databaseService.getSortedEvents(0, weekNumber, "weeks", lottie, current_year, myContainer, layoutInflater, requireFragmentManager(), requireContext(), refE, refU,  whoops)

                } else if (arr.equals(months)) {
                    databaseService.getSortedEvents(1, months.indexOf(element) + 1, "months", lottie, current_year, myContainer, layoutInflater, requireFragmentManager(), requireContext(), refE, refU, whoops)

                } else if (arr.equals(years)) {
                    databaseService.getSortedEvents(0, element.toInt(), "years", lottie, current_year, myContainer, layoutInflater, requireFragmentManager(), requireContext(), refE, refU, whoops)
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
    private fun buttonPressed(button: Button, arr: ArrayList<String>, arrString: String, width: Int, current: Int){
        lottie.visibility = View.VISIBLE
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

        if (current == 52) {
            for (i in 0 until sliderLayout.childCount) {
                val v1: View = sliderLayout.getChildAt(i)
                if (v1 is Button) {
                    if (v1.text.contains("53")) {
                        v1.background = resources.getDrawable(R.color.SaturedCrazyDarkBlue)
                        context?.let { ContextCompat.getColor(it, R.color.White) }?.let { v1.setTextColor(it) }
                        v1.isFocusable = true
                        v1.isFocusableInTouchMode = true
                        v1.requestFocus()
                        databaseService.getSortedEvents(0, current + 1, "weeks", lottie, current_year, myContainer, layoutInflater, requireFragmentManager(), requireContext(), refE, refU, whoops)
                    }

                }
            }

        } else {
            var current_longYear = current
            if (arr.size == 53) {
                current_longYear = current + 1
            }

            val v: View = sliderLayout.getChildAt(current_longYear)
            if (v is Button) {
                v.background = resources.getDrawable(R.color.SaturedCrazyDarkBlue)
                context?.let { ContextCompat.getColor(it, R.color.White) }?.let { v.setTextColor(it) }
                v.isFocusable = true
                v.isFocusableInTouchMode = true
                v.requestFocus()

                if (arrString == "weeks") {
                    databaseService.getSortedEvents(0, current + 1, "weeks", lottie, current_year, myContainer, layoutInflater, requireFragmentManager(), requireContext(), refE, refU,  whoops)

                } else if (arrString == "months") {
                    databaseService.getSortedEvents(1, current + 1, "months", lottie, current_year, myContainer, layoutInflater, requireFragmentManager(), requireContext(), refE, refU,  whoops)

                } else if (arrString == "years") {
                    databaseService.getSortedEvents(0, current + current_year - 1, "years", lottie, current_year, myContainer, layoutInflater, requireFragmentManager(), requireContext(), refE, refU, whoops)
                }

            }
        }
    }
}