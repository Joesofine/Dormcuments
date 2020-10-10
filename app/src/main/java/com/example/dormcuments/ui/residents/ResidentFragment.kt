package com.example.dormcuments.ui.residents

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.text.TextUtils.substring
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.doOnAttach
import com.example.dormcuments.R
import com.example.dormcuments.ui.shopping.AddShopItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_meeting.view.*
import kotlinx.android.synthetic.main.fragment_residents.*
import kotlinx.android.synthetic.main.fragment_residents.view.*
import kotlinx.android.synthetic.main.list_element_resident.view.*
import java.util.*
import kotlin.collections.ArrayList

class ResidentFragment : Fragment() {
    var database = FirebaseDatabase.getInstance().getReference("Users")
    lateinit var getdata : ValueEventListener;
    private lateinit var residentLayout: LinearLayout
    /*val Float.px: Float
        get() = (this * Resources.getSystem().displayMetrics.density).toFloat()
    val Int.dp: Int
        get() = (this / Resources.getSystem().displayMetrics.density).toInt()

     */

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_residents, container, false)
        residentLayout = root.findViewById(R.id.residentLayout);

        getdata = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (i in p0.children) {
                    var name: String = i.child("fname").getValue() as String
                    var date: String = i.child("bdate").getValue() as String
                    var diet: String = i.child("diet").getValue() as String
                    var from: String = i.child("from").getValue() as String
                    var fact: String = i.child("funfact").getValue() as String
                    var roomnumber: String = i.child("number").getValue() as String
                    //var rn = roomnumber.substring(1,3)

                    createResident(name, roomnumber, date, from, diet, fact, residentLayout)
                }
            }
            override fun onCancelled(p0: DatabaseError) {
                println("err")
            }
        }
        database.addValueEventListener(getdata)
        database.addListenerForSingleValueEvent(getdata)
        root.doOnAttach { database.removeEventListener(getdata) }

        root.findViewById<ImageView>(R.id.profileButton).setOnClickListener(){
            requireFragmentManager().beginTransaction().add(
                R.id.nav_host_fragment,
                profileFragment()
            ).addToBackStack(null).commit()
        }
        return root
    }

    override fun onDetach() {
        super.onDetach()
        database.removeEventListener(getdata)

    }

    /*private fun getTagetSize(){
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)

        width = displayMetrics.widthPixels
        height = displayMetrics.heightPixels

        targetWidth = ((width * 9 / 10 ))
        targetHeight = (height / 18)
    }

     */

    /*
    fun View.margin(left: Float? = null, top: Float? = null, right: Float? = null, bottom: Float? = null) {
     layoutParams<ViewGroup.MarginLayoutParams> {
            left?.run { leftMargin = dpToPx(this) }
            top?.run { topMargin = dpToPx(this) }
            right?.run { rightMargin = dpToPx(this) }
            bottom?.run { bottomMargin = dpToPx(this) }
        }
    }
    inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) {
        if (layoutParams is T) block(layoutParams as T)
    }

     */


    private fun createResident(fullname: String, rn: String, bdate: String, sfrom: String, food: String, fact: String, myContainer: LinearLayout){
        val ExpandableCardview: View = layoutInflater.inflate(R.layout.list_element_resident, null, false)

        var sumLayout : ConstraintLayout = ExpandableCardview.findViewById(R.id.sumLayout)
        var titleLayout : ConstraintLayout = ExpandableCardview.findViewById(R.id.titleLayout)
        var expand : ImageView = ExpandableCardview.findViewById(R.id.expand)
        var room: TextView = ExpandableCardview.findViewById(R.id.resRn)
        var resName: TextView = ExpandableCardview.findViewById(R.id.resName)
        var resLast: TextView = ExpandableCardview.findViewById(R.id.resLast)
        var age: TextView = ExpandableCardview.findViewById(R.id.age)
        var birth: TextView = ExpandableCardview.findViewById(R.id.birthday)
        var from: TextView = ExpandableCardview.findViewById(R.id.from)
        var diet: TextView = ExpandableCardview.findViewById(R.id.diet)
        var funny: TextView = ExpandableCardview.findViewById(R.id.fact)
        if (fullname.contains(" ")){
            val name = fullname.split(" ")
            resName.text = name[0]
            resLast.text = name[1]
        } else {
            resName.text = fullname
            resLast.text = ""
        }

        val birthday = bdate.split("/")
        room.text = rn
        age.text = getAge(birthday[2].toInt() ,birthday[1].toInt() ,birthday[0].toInt())
        birth.text = bdate
        from.text = sfrom
        diet.text = food
        funny.text = fact

        titleLayout.setOnClickListener { expandList(sumLayout, expand) }

        if (myContainer.childCount == 0) {
            myContainer.addView(ExpandableCardview)
        } else {
            for (i in 0..myContainer.childCount - 1) {
                val room = myContainer.getChildAt(i).findViewById<TextView>(R.id.resRn).text.toString().toInt()

                if (room >= rn.toInt()) {
                    myContainer.addView(ExpandableCardview, i)
                    break

                } else if (room < rn.toInt()) {
                    if (i == myContainer.childCount - 1) {
                        myContainer.addView(ExpandableCardview)
                        break

                    } else if (rn.toInt() <= i+1)  {
                        myContainer.addView(ExpandableCardview, i + 1)
                        break

                    } else {
                        for (k in i+1..myContainer.childCount -1){
                            val roomK = myContainer.getChildAt(k).findViewById<TextView>(R.id.resRn).text.toString().toInt()
                            if (rn.toInt() < roomK){
                                myContainer.addView(ExpandableCardview, k)
                                break
                            }
                        }
                    }
                }
            }
        }
    }

    private fun expandList(
        sumLayout: ConstraintLayout, expand: ImageView) {
        if (sumLayout.visibility == View.GONE) {
            sumLayout.visibility = View.VISIBLE
            expand.rotation = 90f
        } else if (sumLayout.visibility == View.VISIBLE) {
            sumLayout.visibility = View.GONE
            expand.rotation = 0f
        }
    }

    private fun getAge(year: Int, month: Int, day: Int): String? {
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()
        dob[year, month] = day
        var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
        if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
            age--
        }
        val ageInt = age
        return ageInt.toString()
    }
/*
    fun View.dpToPx(dp: Float): Int = context.dpToPx(dp)
    fun Context.dpToPx(dp: Float): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()

 */
}