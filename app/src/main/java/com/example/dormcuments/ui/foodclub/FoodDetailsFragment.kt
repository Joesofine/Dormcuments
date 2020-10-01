package com.example.dormcuments.ui.foodclub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.dormcuments.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FoodDetailsFragment : Fragment() {
    var database = FirebaseDatabase.getInstance().getReference("Foodclub")
    lateinit var getdata : ValueEventListener;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_food_details, container, false)
        val bundle = this.arguments

        getdata = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                if (bundle != null) {
                    val clubid = bundle.getString("id")
                    if (clubid != null) {
                        for (i in p0.children) {
                            if (i.key.equals(clubid)){

                                var w1 = i.child("c1").getValue().toString().substring(1,3)
                                var w2 = i.child("c2").getValue().toString().substring(1,3)

                                if (w1.equals("on")) {
                                    w1 = "NA"
                                } else if (w2.equals("on")){
                                    w2 = "NA"
                                } else {
                                    root.findViewById<TextView>(R.id.chefs).text = "$w1, $w2"
                                }
                                root.findViewById<TextView>(R.id.date).text = i.child("date").getValue().toString()
                                root.findViewById<TextView>(R.id.dinner).text = i.child("dinner").getValue().toString()
                                root.findViewById<TextView>(R.id.note).text = i.child("note").getValue().toString()


                            }
                        }
                    }
                }

            }
            override fun onCancelled(p0: DatabaseError) { println("err") }
        }

        database.addValueEventListener(getdata)
        database.addListenerForSingleValueEvent(getdata)

        root.findViewById<ImageView>(R.id.edit).setOnClickListener(){
            requireFragmentManager().beginTransaction().add(R.id.nav_host_fragment, EditFoodFragment()).addToBackStack(null).commit()
        }



        return root
    }
}