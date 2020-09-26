package com.example.dormcuments.ui.meeting

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnAttach
import androidx.fragment.app.Fragment
import com.example.dormcuments.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_meeting.*
import java.util.*

class MeetingFragment : Fragment() {
    var meetArr: ArrayList<Topic> = ArrayList()
    var database = FirebaseDatabase.getInstance().getReference("Agenda")
    val meet = topic_
    lateinit var titleLayout: ConstraintLayout
    lateinit var sumLayout: ConstraintLayout
    lateinit var expand: ImageView
    lateinit var getdata : ValueEventListener;

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var root = inflater.inflate(R.layout.fragment_meeting, container, false)

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
                    if (meetArr.size != 0) {
                        val adaptor = context?.let { adapter_meet(it, meetArr) }
                        list.adapter = adaptor
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                println("err")
            }
        }

        database.addValueEventListener(getdata)
        database.addListenerForSingleValueEvent(getdata)

        Handler().postDelayed(Runnable {

        }, 0)



        root.doOnAttach { database.removeEventListener(getdata) }




        makeList()

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

    private fun makeList(){

        //Inflater to XML filer ind, et Cardview og en Spacer som bruges til at skabe afstand fordi det ikke er muligt med Padding eller Layout Margin.
        val ExpandableCardview: View =
            layoutInflater.inflate(R.layout.list_element_meeting, null, false)

        sumLayout = ExpandableCardview.findViewById(R.id.sumLayout)
        titleLayout = ExpandableCardview.findViewById(R.id.titleLayout)
        expand = ExpandableCardview.findViewById(R.id.expand)

        //Set OnClickListener that handles expansion and collapse of view
        titleLayout.setOnClickListener {
            expandList(sumLayout, expand)
        }

        expand.setOnClickListener {
            expandList(sumLayout, expand)
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

}