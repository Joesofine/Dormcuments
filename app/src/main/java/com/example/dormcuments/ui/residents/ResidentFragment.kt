package com.example.dormcuments.ui.residents

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.example.dormcuments.R
import com.example.dormcuments.ui.shopping.AddShopItem
import kotlinx.android.synthetic.main.fragment_residents.*
import kotlinx.android.synthetic.main.fragment_residents.view.*

class ResidentFragment : Fragment() {
    private val residentsArr = ArrayList<String>()
    var targetHeight = 0
    var targetWidth = 0
    var width = 0
    var height = 0
    private lateinit var residentLayout: LinearLayout
    val Float.px: Float
        get() = (this * Resources.getSystem().displayMetrics.density).toFloat()
    val Int.dp: Int
        get() = (this / Resources.getSystem().displayMetrics.density).toInt()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_residents, container, false)

        root.findViewById<ImageView>(R.id.profileButton).setOnClickListener(){
            requireFragmentManager().beginTransaction().add(
                R.id.nav_host_fragment,
                profileFragment()
            ).addToBackStack(null).commit()
        }

        residentLayout = root.findViewById(R.id.residentLayout);

        makeResidentArr()
        getTagetSize()
        createResidentButtons()

        return root
    }

    private fun makeResidentArr(){
        for (i in 1..17) {
            val st = "$i      Resident"
            residentsArr.add(st)
        }
    }

    private fun getTagetSize(){
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)

        width = displayMetrics.widthPixels
        height = displayMetrics.heightPixels

        targetWidth = ((width * 9 / 10 ))
        targetHeight = (height / 18)
    }

    fun View.margin(left: Float? = null, top: Float? = null, right: Float? = null, bottom: Float? = null) {
        layoutParams<ViewGroup.MarginLayoutParams> {
            left?.run { leftMargin = dpToPx(this) }
            top?.run { topMargin = dpToPx(this) }
            right?.run { rightMargin = dpToPx(this) }
            bottom?.run { bottomMargin = dpToPx(this) }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun createResidentButtons(){
        for (element in residentsArr) {
            val button = Button(context)
            button.layoutParams = LinearLayout.LayoutParams(targetWidth, targetHeight)
            button.background = resources.getDrawable(R.drawable.more_button)
            button.text = element
            context?.let { ContextCompat.getColor(it, R.color.White) }?.let { button.setTextColor(it) }
            val mar = (width - targetWidth) / 2
            button.margin(left = mar.dp.toFloat())
            button.margin(top = 5F)
            if ( element == residentsArr[residentsArr.size - 1]) {
                button.margin(bottom = 5F)
            }
            residentLayout.addView(button)
        }
    }

    inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) {
        if (layoutParams is T) block(layoutParams as T)
    }

    fun View.dpToPx(dp: Float): Int = context.dpToPx(dp)
    fun Context.dpToPx(dp: Float): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()
}