package com.example.dormcuments.ui.foodclub

import FoodDetailsFragment
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.doOnAttach
import androidx.fragment.app.Fragment
import com.example.dormcuments.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_food_details.*
import java.time.LocalDate


class FoodclubFragment : Fragment() {
    var database = FirebaseDatabase.getInstance().getReference("Foodclub")
    lateinit var getdata : ValueEventListener;
    lateinit var myContainer: LinearLayout
    var bool = false

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_foodclub, container, false)
        myContainer = root.findViewById(R.id.LinScroll)

        getdata = object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(p0: DataSnapshot) {

                for (i in p0.children) {
                    var chef1: String = i.child("c1").getValue() as String
                    var chef2: String = i.child("c2").getValue() as String
                    var date1: String = i.child("date").getValue() as String
                    var unform: String = i.child("unform").getValue() as String
                    var clubid = i.key.toString()

                    createClub(chef1,chef2, date1, clubid, unform, myContainer)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createClub(c1: String, c2: String, date: String, clubid: String, unform: String, myContainer: LinearLayout){

        val ExpandableCardview: View =
            layoutInflater.inflate(R.layout.list_element_cleaning_food, null, false)
        var show: ImageView = ExpandableCardview.findViewById(R.id.show)
        var datefield: TextView = ExpandableCardview.findViewById(R.id.date)
        var w1: TextView = ExpandableCardview.findViewById(R.id.who1)
        var w2: TextView = ExpandableCardview.findViewById(R.id.who2)
        var un: TextView = ExpandableCardview.findViewById(R.id.unformatted)

        var eventdate = unform.split("/")
        var local = LocalDate.of(eventdate[2].toInt(), eventdate[1].toInt(), eventdate[0].toInt())

        if (c1.equals("None") || c2.equals("None")) {
            if (c1.equals("None") && c2.equals("None")) {
                w1.setText("NA")
                w2.setText("NA")
            } else if (c1.equals("None")) {
                w1.setText("NA")
                w2.setText(c2.substring(1, 3))
            } else if (c2.equals("None")){
                w1.setText(c1.substring(1, 3))
                w2.setText("NA")
            }
        } else {
            w1.setText(c1.substring(1, 3))
            w2.setText(c2.substring(1, 3))
        }
        datefield.setText(date)
        un.text = unform


        show.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", clubid)
            val fragment2 = FoodDetailsFragment()
            fragment2.arguments = bundle
            fragmentManager?.beginTransaction()?.add(R.id.nav_host_fragment, fragment2)?.addToBackStack(null)?.commit()        }



        if (myContainer.childCount == 0) {
            myContainer.addView(ExpandableCardview)
        } else {
            for (i in 0..myContainer.childCount - 1) {
                val ufd = myContainer.getChildAt(i).findViewById<TextView>(R.id.unformatted).text.toString().split("/")
                val elementDate = LocalDate.of(ufd[2].toInt(),ufd[1].toInt(),ufd[0].toInt())

                if (elementDate.isAfter(local) || elementDate.isEqual(local) ) {
                    myContainer.addView(ExpandableCardview, i)
                    break

                } else if (elementDate.isBefore(local)) {
                    if (i == myContainer.childCount - 1) {
                        myContainer.addView(ExpandableCardview)
                        break

                    } else {
                        val k = i + 1
                        val ufdK = myContainer.getChildAt(k).findViewById<TextView>(R.id.unformatted).text.toString().split("/")
                        val elementDateK = LocalDate.of(ufdK[2].toInt(),ufdK[1].toInt(),ufdK[0].toInt())

                        if (local.isBefore(elementDateK) || local.isEqual(elementDateK)) {
                            myContainer.addView(ExpandableCardview, k)
                            break

                        } else {

                            for (j in k..myContainer.childCount - 1) {
                                val ufdJ = myContainer.getChildAt(j).findViewById<TextView>(R.id.unformatted).text.toString().split("/")
                                val elementDateJ = LocalDate.of(ufdJ[2].toInt(), ufdJ[1].toInt(), ufdJ[0].toInt())

                                if (local.isBefore(elementDateJ)) {
                                    myContainer.addView(ExpandableCardview, j)
                                    bool = true
                                    break
                                }
                            }
                        }
                    }
                }
                if (bool == true){
                    break
                }
            }
        }
    }
}