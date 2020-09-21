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
import java.text.SimpleDateFormat
import java.util.*

class CalenderFragment : Fragment(),View.OnClickListener {
    var targetHeight = 0
    var targetWidth = 0
    private val months = ArrayList<String>()
    private val weeks = ArrayList<String>()
    private val years = ArrayList<String>()
    private lateinit var sliderLayout: LinearLayout
    private lateinit var week: Button
    private lateinit var month: Button
    private lateinit var year: Button
    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_calender, container, false)
        sliderLayout = root.findViewById(R.id.sliderLayout);

        week = root.findViewById(R.id.weekID) as Button;
        month = root.findViewById(R.id.month) as Button;
        year = root.findViewById(R.id.year) as Button;

        week.setOnClickListener(this)
        month.setOnClickListener(this)
        year.setOnClickListener(this)


        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)

        var width = displayMetrics.widthPixels
        var height = displayMetrics.heightPixels

         targetWidth = ((width / 3)).toInt()
         targetHeight = week.layoutParams.height.toInt()

        week.layoutParams = LinearLayout.LayoutParams(targetWidth as Int, targetHeight as Int)
        month.layoutParams = LinearLayout.LayoutParams(targetWidth as Int, targetHeight as Int)
        year.layoutParams = LinearLayout.LayoutParams(targetWidth as Int, targetHeight as Int)

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

        for (i in 1..52) {
            val st = "U$i"
            weeks.add(st)
        }

        val timeStamp = SimpleDateFormat("yyyy").format(Calendar.getInstance().time)
        for (i in 2019..timeStamp.toInt()) {
            years.add(i.toString())
        }

        topButton()


        val activity: Context? = activity


        return root
    }

    @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor")
    override fun onClick(p0: View?) {

        if (p0 === week || p0 === month || p0 === year) {
            if (p0 === week) {
                sliderLayout.removeAllViews()
                week.background = resources.getDrawable(R.color.Slider)
                month.background = resources.getDrawable(R.color.ButtonLight)
                year.background = resources.getDrawable(R.color.ButtonLight)
                val sliderButtonWidth = targetWidth - 100
                for (element in weeks) {
                    val button = Button(context)
                    button.layoutParams = LinearLayout.LayoutParams(
                        sliderButtonWidth.toInt(),
                        targetHeight.toInt()
                    )
                    button.background = resources.getDrawable(R.color.ButtonLight)
                    button.text = element
                    sliderLayout.addView(button)
                }
            } else if (p0 === month) {
                sliderLayout.removeAllViews()
                week.background = resources.getDrawable(R.color.ButtonLight)
                month.background = resources.getDrawable(R.color.Slider)
                year.background = resources.getDrawable(R.color.ButtonLight)
                val sliderButtonWidth = targetWidth - 40
                for (element in months) {
                    val button = Button(context)
                    button.layoutParams = LinearLayout.LayoutParams(
                        sliderButtonWidth.toInt(),
                        targetHeight.toInt()
                    )
                    button.background = resources.getDrawable(R.color.ButtonLight)
                    button.text = element
                    sliderLayout.addView(button)
                }
            } else if (p0 === year) {
                sliderLayout.removeAllViews()
                week.background = resources.getDrawable(R.color.ButtonLight)
                month.background = resources.getDrawable(R.color.ButtonLight)
                year.background = resources.getDrawable(R.color.Slider)
                val sliderButtonWidth = targetWidth * 3 / years.size
                for (element in years) {
                    val button = Button(context)
                    button.layoutParams = LinearLayout.LayoutParams(sliderButtonWidth.toInt(), targetHeight.toInt())
                    button.background = resources.getDrawable(R.color.ButtonLight)
                    button.text = element
                    sliderLayout.addView(button)
                }
            }
        }
    }
    @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor")
    private fun topButton() {
        sliderLayout.removeAllViews()
        week.background = resources.getDrawable(R.color.ButtonLight)
        month.background = resources.getDrawable(R.color.Slider)
        year.background = resources.getDrawable(R.color.ButtonLight)
        val sliderButtonWidth = targetWidth - 40
        for (element in months) {
            val button = Button(context)
            button.layoutParams = LinearLayout.LayoutParams(sliderButtonWidth.toInt(), targetHeight.toInt())
            button.background = resources.getDrawable(R.color.ButtonLight)
            button.text = element
            sliderLayout.addView(button)
        }
    }
}