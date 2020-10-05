package com.example.dormcuments.ui.cleaning

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.dormcuments.R
import com.example.dormcuments.ui.foodclub.EditFoodFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CleaningDetailsFragment : Fragment() {
    var database = FirebaseDatabase.getInstance().getReference("Cleaning")
    lateinit var getdata : ValueEventListener
    lateinit var editBundle : Bundle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_cleaning_details, container, false)

        val bundle = this.arguments

        getdata = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                if (bundle != null) {
                    var cleaningid = bundle.getString("id")
                    if (cleaningid != null) {

                        var w1 = p0.child(cleaningid).child("c1").getValue().toString().substring(1,3)
                        var w2 = p0.child(cleaningid).child("c2").getValue().toString().substring(1,3)

                        if (w1.equals("on")){ w1 = "NA" }
                        if (w2.equals("on")){ w2 = "NA" }

                        root.findViewById<TextView>(R.id.chefs).text = "$w1 , $w2"
                        root.findViewById<TextView>(R.id.date).text = p0.child(cleaningid).child("date").getValue().toString()
                        root.findViewById<TextView>(R.id.task).text = p0.child(cleaningid).child("task").getValue().toString()
                        root.findViewById<TextView>(R.id.note).text = p0.child(cleaningid).child("note").getValue().toString()

                        setId(cleaningid)
                    }
                }

            }
            override fun onCancelled(p0: DatabaseError) { println("err") }
        }

        database.addValueEventListener(getdata)
        database.addListenerForSingleValueEvent(getdata)

        root.findViewById<ImageView>(R.id.edit).setOnClickListener() {

            val tempFrag = EditCleaningFragment()
            tempFrag.arguments = editBundle
            requireFragmentManager().beginTransaction().add(R.id.nav_host_fragment, tempFrag).addToBackStack(null).commit()
        }

        return root
    }

    private fun setId(cleaningid: String){
        editBundle = Bundle()
        editBundle.putString("id", cleaningid)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
}