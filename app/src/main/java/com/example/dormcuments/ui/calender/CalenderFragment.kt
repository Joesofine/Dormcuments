package com.example.dormcuments.ui.calender

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.dormcuments.R
import kotlinx.android.synthetic.main.fragment_calender.*
import java.util.*

class CalenderFragment : Fragment(),View.OnClickListener {
    var targetHeight: Float = 0.0f
    var targetWidth: Float = 0.0f
    private val months = ArrayList<String>()
    private val weeks = ArrayList<String>()
    private val years = ArrayList<String>()
    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_calender, container, false)

        val week=root.findViewById(R.id.week) as Button;
        val month=root.findViewById(R.id.month) as Button;
        val year=root.findViewById(R.id.year) as Button;


        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)

        var width = displayMetrics.widthPixels
        var height = displayMetrics.heightPixels

        var targetWidth = (width / 3)
        var targetHeight = week.height

        week.layoutParams = LinearLayout.LayoutParams(targetWidth as Int, targetHeight as Int)
        month.layoutParams = LinearLayout.LayoutParams(targetWidth as Int, targetHeight as Int)
        year.layoutParams = LinearLayout.LayoutParams(targetWidth as Int, targetHeight as Int)


        return root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
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
                    button.text = element
                    button.layoutParams = LinearLayout.LayoutParams(
                        sliderButtonWidth.toInt(),
                        targetHeight.toInt()
                    )
                    button.setBackgroundColor(0xB5E9FF)
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
                    button.text = element
                    button.layoutParams = LinearLayout.LayoutParams(
                        sliderButtonWidth.toInt(),
                        targetHeight.toInt()
                    )
                    button.setBackgroundColor(0xB5E9FF)
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
                    button.text = element
                    button.layoutParams = LinearLayout.LayoutParams(
                        sliderButtonWidth.toInt(),
                        targetHeight.toInt()
                    )
                    button.setBackgroundColor(0xB5E9FF)
                    sliderLayout.addView(button)
                }
            }
        }
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun topButton() {
        sliderLayout.removeAllViews()
        week.background = resources.getDrawable(R.color.ButtonLight)
        month.background = resources.getDrawable(R.color.Slider)
        year.background = resources.getDrawable(R.color.ButtonLight)
        val sliderButtonWidth = targetWidth - 40
        for (element in months) {
            val button = Button(context)
            button.text = element
            button.layoutParams = LinearLayout.LayoutParams(
                sliderButtonWidth.toInt(),
                targetHeight.toInt()
            )
            button.setBackgroundColor(0xB5E9FF)
            sliderLayout.addView(button)
        }
    }
}