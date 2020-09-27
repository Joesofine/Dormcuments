package com.example.dormcuments.ui.meeting

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnAttach
import androidx.fragment.app.Fragment
import com.example.dormcuments.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.JsonParser
import java.util.*

class MeetingFragment : Fragment() , AdapterView.OnItemClickListener {
    var existingViews = ArrayList<String>()
    var meetArr: ArrayList<Topic> = ArrayList()
    var database = FirebaseDatabase.getInstance().getReference("Agenda")
    val meet = topic_
    lateinit var expand: ImageView
    lateinit var getdata : ValueEventListener;
    //lateinit var list: ListView
    lateinit var sum: TextView
    lateinit var meetingItem: TextView
    lateinit var myContainer: LinearLayout

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var root = inflater.inflate(R.layout.fragment_meeting, container, false)

        myContainer = root.findViewById(R.id.LinScroll)

        //list.onItemClickListener = this


        meetArr.clear()

        getdata = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                meetArr.clear()

                for (i in p0.children) {
                    var name1: String = i.child("name").getValue() as String
                    var sum1: String = i.child("summary").getValue() as String

                    meetArr.add(Topic(name1,sum1))
                    meet.setMeetArr(meetArr, context)
                    meetArr = meet.getShopArr(context)!!
                    //if (meetArr.size != 0) {
                    //    val adaptor = context?.let { adapter_meet(it, meetArr) }
                    //    list.adapter = adaptor
                    //}
                    createTopic(name1, sum1, myContainer)

                }
            }

            override fun onCancelled(p0: DatabaseError) {
                println("err")
            }
        }

        database.addValueEventListener(getdata)
        database.addListenerForSingleValueEvent(getdata)

        //Handler().postDelayed(Runnable { }, 0)

        root.doOnAttach { database.removeEventListener(getdata) }

        root.findViewById<FloatingActionButton>(R.id.add).setOnClickListener {
            requireFragmentManager().beginTransaction().add(
                R.id.nav_host_fragment,
                AddTopic()
            ).addToBackStack(null).commit()

        }
        return root
    }
    override fun onDetach() {
        super.onDetach()
        database.removeEventListener(getdata)

    }

    private fun createTopic(name: String, des: String, myContainer: LinearLayout){

        val ExpandableCardview: View =
            layoutInflater.inflate(R.layout.list_element_meeting, null, false)

        var sumLayout : ConstraintLayout  = ExpandableCardview.findViewById(R.id.sumLayout)
        var titleLayout : ConstraintLayout = ExpandableCardview.findViewById(R.id.titleLayout)
        var expand : ImageView = ExpandableCardview.findViewById(R.id.expand)
        meetingItem = ExpandableCardview.findViewById(R.id.meetingItem)
        sum = ExpandableCardview.findViewById(R.id.sum)

        //Convert to JSON-object
        //val parser = JsonParser()
        //val element = parser.parse(entry.value.toString())
        //val topic = element.asJsonObject


        meetingItem.setText(name)
        sum.setText(des)

        //Set OnClickListener that handles expansion and collapse of view
        titleLayout.setOnClickListener {
            expandList(sumLayout, expand) }
        expand.setOnClickListener { expandList(sumLayout, expand) }

        //Add cardview to myContainer
        myContainer.addView(ExpandableCardview)
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

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        //expandList(sumLayout, expand)
        //Toast.makeText(context, "haluuuuueeee", Toast.LENGTH_SHORT).show()
        //sumLayout.visibility = View.VISIBLE
    }

}