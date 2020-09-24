package com.example.dormcuments.ui.meeting

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnAttach
import androidx.fragment.app.Fragment
import com.example.dormcuments.R
import com.example.dormcuments.ui.shopping.AddShopItem
import com.example.dormcuments.ui.shopping.Item
import com.example.dormcuments.ui.shopping.adapter_shop
import com.example.dormcuments.ui.shopping.item_
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_meeting.*
import java.util.ArrayList

class MeetingFragment : Fragment() {
    var meetArr: ArrayList<Topic> = ArrayList()
    var database = FirebaseDatabase.getInstance().getReference("Agenda")
    val meet = topic_
    lateinit var getdata : ValueEventListener;

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_meeting, container, false)

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

    };
}