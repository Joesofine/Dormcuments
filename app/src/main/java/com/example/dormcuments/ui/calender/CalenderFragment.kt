package com.example.dormcuments.ui.calender

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.dormcuments.R
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalenderFragment : Fragment(),View.OnClickListener {
    private val months = ArrayList<String>()
    private val weeks = ArrayList<String>()
    private val years = ArrayList<String>()
    private lateinit var sliderLayout: LinearLayout
    private lateinit var week: Button
    private lateinit var month: Button
    private lateinit var year: Button
    var targetHeight = 0
    var targetWidth = 0

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_calender, container, false)

        var btnLogout = root.findViewById<Button>(R.id.btnLogout)

        btnLogout.setOnClickListener(View.OnClickListener {
            // Logout
            if (AccessToken.getCurrentAccessToken() != null) {
                GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, GraphRequest.Callback {
                    AccessToken.setCurrentAccessToken(null)
                    LoginManager.getInstance().logOut()
                    requireActivity().finish()
                }).executeAsync()
            }
        })

        sliderLayout = root.findViewById(R.id.sliderLayout);
        week = root.findViewById(R.id.weekID) as Button;
        month = root.findViewById(R.id.month) as Button;
        year = root.findViewById(R.id.year) as Button;

        week.setOnClickListener(this)
        month.setOnClickListener(this)
        year.setOnClickListener(this)

        getTagetSize()

        week.layoutParams = LinearLayout.LayoutParams(targetWidth, targetHeight)
        month.layoutParams = LinearLayout.LayoutParams(targetWidth, targetHeight)
        year.layoutParams = LinearLayout.LayoutParams(targetWidth, targetHeight)

        makeWeekArr()
        makeMonthArr()
        makeYearArr()
        topButton()

        return root
    }

    @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor")
    override fun onClick(p0: View?) {

        if (p0 === week || p0 === month || p0 === year) {
            if (p0 === week) {
                week.background = resources.getDrawable(R.color.Slider)
                month.background = resources.getDrawable(R.color.ButtonLight)
                year.background = resources.getDrawable(R.color.ButtonLight)
                buttonLoop(weeks,targetWidth - 120)

            } else if (p0 === month) {
                week.background = resources.getDrawable(R.color.ButtonLight)
                month.background = resources.getDrawable(R.color.Slider)
                year.background = resources.getDrawable(R.color.ButtonLight)
                buttonLoop(months,targetWidth - 40)

            } else if (p0 === year) {
                week.background = resources.getDrawable(R.color.ButtonLight)
                month.background = resources.getDrawable(R.color.ButtonLight)
                year.background = resources.getDrawable(R.color.Slider)
                buttonLoop(years,targetWidth * 3 / years.size)
            }
        }
    }

    private fun makeMonthArr(){
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
    private fun makeWeekArr(){
        for (i in 1..52) {
            val st = "U$i"
            weeks.add(st)
        }
    }
    private fun makeYearArr(){
        val timeStamp = SimpleDateFormat("yyyy").format(Calendar.getInstance().time)
        for (i in 2019..timeStamp.toInt()) {
            years.add(i.toString())
        }
    }


    @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor")
    private fun topButton() {
        week.background = resources.getDrawable(R.color.ButtonLight)
        month.background = resources.getDrawable(R.color.Slider)
        year.background = resources.getDrawable(R.color.ButtonLight)
        buttonLoop(months,targetWidth - 40)
    }

    private fun buttonLoop(arr: ArrayList<String>, buttonWidth: Int){
        sliderLayout.removeAllViews()
        for (element in arr) {
            val button = Button(context)
            button.layoutParams = LinearLayout.LayoutParams(buttonWidth.toInt(), targetHeight.toInt())
            button.background = resources.getDrawable(R.color.ButtonLight)
            button.text = element
            sliderLayout.addView(button)
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
}