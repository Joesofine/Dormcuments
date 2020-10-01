package com.example.dormcuments.ui.foodclub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.doOnAttach
import androidx.fragment.app.Fragment
import com.example.dormcuments.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FoodclubFragment : Fragment() {
    var database = FirebaseDatabase.getInstance().getReference("Foodclub")
    lateinit var getdata : ValueEventListener;
    lateinit var myContainer: LinearLayout

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_foodclub, container, false)
        myContainer = root.findViewById(R.id.LinScroll)

        getdata = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                for (i in p0.children) {
                    var chef1: String = i.child("c1").getValue() as String
                    var chef2: String = i.child("c2").getValue() as String
                    var date1: String = i.child("date").getValue() as String
                    var clubid = i.key.toString()

                    createClub(chef1,chef2, date1, clubid, myContainer)
                }
            }
            override fun onCancelled(p0: DatabaseError) { println("err") }
        }

        database.addValueEventListener(getdata)
        database.addListenerForSingleValueEvent(getdata)

        root.doOnAttach { database.removeEventListener(getdata) }

        root.findViewById<FloatingActionButton>(R.id.add2).setOnClickListener {
            requireFragmentManager().beginTransaction().add(R.id.nav_host_fragment, CreateFoodclubFragment()).addToBackStack(null).commit()
        }
        return root
    }

    override fun onDetach() {
        super.onDetach()
        database.removeEventListener(getdata)
    }

    private fun createClub(c1: String, c2: String, date: String, clubid: String ,myContainer: LinearLayout){

        val ExpandableCardview: View =
            layoutInflater.inflate(R.layout.list_element_cleaning_food, null, false)
        var show: ImageView = ExpandableCardview.findViewById(R.id.show)
        var datefield: TextView = ExpandableCardview.findViewById(R.id.date)
        var w1: TextView = ExpandableCardview.findViewById(R.id.who1)
        var w2: TextView = ExpandableCardview.findViewById(R.id.who2)

        if (c1.equals("None")) {
            w1.setText("NA")
        } else if (c2.equals("None")){
            w2.setText("NA")
        } else {
            datefield.setText(date)
            w1.setText(c1.substring(1, 3))
            w2.setText(c2.substring(1, 3))
        }

        show.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", clubid)
            val fragment2 =
                FoodDetailsFragment()
            fragment2.arguments = bundle
            fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment, fragment2)?.commit()        }
        myContainer.addView(ExpandableCardview)
    }
}