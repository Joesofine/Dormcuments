package com.example.dormcuments.ui.cleaning

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.dormcuments.R
import com.example.dormcuments.ui.foodclub.EditFoodFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_cleaning_details.*
import kotlinx.android.synthetic.main.fragment_create_cleaning.*
import kotlinx.android.synthetic.main.fragment_food_details.*
import kotlinx.android.synthetic.main.fragment_food_details.checkText

class CleaningDetailsFragment : Fragment() {
    var database = FirebaseDatabase.getInstance().getReference("Cleaning")
    var databaseU = FirebaseDatabase.getInstance().getReference("Users")
    lateinit var getdata : ValueEventListener
    lateinit var getdataU : ValueEventListener
    lateinit var editBundle : Bundle
    private lateinit var auth: FirebaseAuth
    var roomnumber: String = ""
    var name: String = ""
    var str: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_cleaning_details, container, false)
        val checked = root.findViewById<Switch>(R.id.checked)
        val weekTask = root.findViewById<ConstraintLayout>(R.id.weeklyTask)
        val extraTask = root.findViewById<ConstraintLayout>(R.id.extraTask)
        val expandWeekly = root.findViewById<ImageView>(R.id.expandWeekly)
        val expandExtra = root.findViewById<ImageView>(R.id.expandExtra)

        auth = Firebase.auth
        val bundle = this.arguments

        getdata = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                if (bundle != null) {
                    var cleaningid = bundle.getString("id")
                    if (cleaningid != null) {

                        var w1 = p0.child(cleaningid).child("c1").getValue().toString().substring(1,3)
                        var w2 = p0.child(cleaningid).child("c2").getValue().toString().substring(1,3)
                        var status = p0.child(cleaningid).child("checkedBy").getValue().toString()

                        if (w1.equals("on")){ w1 = "NA" }
                        if (w2.equals("on")){ w2 = "NA" }

                        root.findViewById<TextView>(R.id.chefs).text = "$w1 , $w2"
                        root.findViewById<TextView>(R.id.date).text = p0.child(cleaningid).child("date").getValue().toString()
                        root.findViewById<TextView>(R.id.task).text = p0.child(cleaningid).child("task").getValue().toString()
                        root.findViewById<TextView>(R.id.note).text = p0.child(cleaningid).child("note").getValue().toString()
                        root.findViewById<TextView>(R.id.checkText).text = "Checked by: $status"

                        setId(cleaningid)
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) { println("err") }
        }

        database.addValueEventListener(getdata)
        database.addListenerForSingleValueEvent(getdata)

        getdataU = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                var cleaningid = bundle?.getString("id")
                val userid = auth.currentUser?.uid
                if (userid != null) {
                    roomnumber = p0.child(userid).child("number").getValue().toString()
                    name = p0.child(userid).child("fname").getValue().toString()
                }
                setSwitchStatus(checked)
                if (cleaningid != null) { listenerOnChange(checked,roomnumber,name,cleaningid) }
            }
            override fun onCancelled(p0: DatabaseError) { println("err") }
        }

        databaseU.addValueEventListener(getdataU)
        databaseU.addListenerForSingleValueEvent(getdataU)


        root.findViewById<ImageView>(R.id.edit).setOnClickListener() {
            val tempFrag = EditCleaningFragment()
            tempFrag.arguments = editBundle
            requireFragmentManager().beginTransaction().add(R.id.nav_host_fragment, tempFrag).addToBackStack(null).commit()
        }

        expandWeekly.setOnClickListener(){
            expandList(weekTask, expandWeekly)
        }

        expandExtra.setOnClickListener(){
            expandList(extraTask,expandExtra)
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

    private fun listenerOnChange(switch: Switch, rn: String, name: String, cleaningid: String){
        switch.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            if (isChecked){
                val n = name.split(" ")[0]
                val str = "$n, $rn"
                checkText.text = "Checked by: $str"
                database.child(cleaningid).child("checkedBy").setValue(str).addOnSuccessListener {
                    Toast.makeText(context, "Cleaning is checked", Toast.LENGTH_SHORT).show()
                }
                    .addOnFailureListener {
                        // Write failed
                        Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show()
                    }
            } else {
                checkText.text = "Cleaning check"
                database.child(cleaningid).child("checkedBy").setValue("").addOnSuccessListener {
                    Toast.makeText(context, "Cleaning is unchecked", Toast.LENGTH_SHORT).show()
                }
                    .addOnFailureListener {
                        // Write failed
                        Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun setSwitchStatus(switch: Switch){
        if ( checkText.text.toString().contains("9")){ switch.isChecked = true}
    }
    private fun expandList(taskLayout: ConstraintLayout, expand: ImageView) {
        if (taskLayout.visibility == View.GONE) {
            taskLayout.visibility = View.VISIBLE
            expand.rotation = 90f
        } else if (taskLayout.visibility == View.VISIBLE) {
            taskLayout.visibility = View.GONE
            expand.rotation = 0f
        }
    }
}