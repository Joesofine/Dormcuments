package com.example.dormcuments.ui.meeting

import android.os.Bundle
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

class MeetingFragment : Fragment() {
    var database = FirebaseDatabase.getInstance().getReference("Agenda")
    lateinit var getdata : ValueEventListener;
    lateinit var myContainer: LinearLayout

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var root = inflater.inflate(R.layout.fragment_meeting, container, false)

        myContainer = root.findViewById(R.id.LinScroll)

        getdata = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (i in p0.children) {
                    var name1: String = i.child("name").getValue() as String
                    var sum1: String = i.child("summary").getValue() as String
                    var topicId = i.key.toString()

                    createTopic(name1, sum1, topicId, myContainer)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                println("err")
            }
        }

        database.addValueEventListener(getdata)
        database.addListenerForSingleValueEvent(getdata)
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

    private fun createTopic(name: String, des: String, topicId: String, myContainer: LinearLayout){

        val ExpandableCardview: View =
            layoutInflater.inflate(R.layout.list_element_meeting, null, false)

        var sumLayout : ConstraintLayout  = ExpandableCardview.findViewById(R.id.sumLayout)
        var titleLayout : ConstraintLayout = ExpandableCardview.findViewById(R.id.titleLayout)
        var expand : ImageView = ExpandableCardview.findViewById(R.id.expand)
        var divider: View = ExpandableCardview.findViewById(R.id.div)
        var delete: ImageView = ExpandableCardview.findViewById(R.id.delete)
        var meetingItem: TextView = ExpandableCardview.findViewById(R.id.meetingItem)
        var sum: TextView = ExpandableCardview.findViewById(R.id.sum)

        meetingItem.setText(name)
        sum.setText(des)

        //Set OnClickListener that handles expansion and collapse of view
        titleLayout.setOnClickListener {
            expandList(sumLayout, expand, divider) }

        delete.setOnClickListener {
            myContainer.removeView(ExpandableCardview)
            deleteTopic(topicId)
        }

        myContainer.addView(ExpandableCardview)
    }

    private fun deleteTopic(topicId: String){
        var dName = database.child(topicId)

        dName.removeValue()
        Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show()
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
}