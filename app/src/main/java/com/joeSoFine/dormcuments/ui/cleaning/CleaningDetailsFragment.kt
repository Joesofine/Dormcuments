package com.joeSoFine.dormcuments.ui.cleaning

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.lottie.LottieAnimationView
import com.joeSoFine.dormcuments.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_cleaning_details.*
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
        val lottie = root.findViewById<LottieAnimationView>(R.id.animation_view)

        auth = Firebase.auth
        val bundle = this.arguments


        getdata = object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(p0: DataSnapshot) {

                if (bundle != null) {
                    var cleaningid = bundle.getString("id")
                    if (cleaningid != null) {

                        var chef1 = p0.child(cleaningid).child("c1").getValue().toString()
                        var chef2 = p0.child(cleaningid).child("c2").getValue().toString()
                        var status = p0.child(cleaningid).child("checkedBy").getValue().toString()
                        var extras = p0.child(cleaningid).child("task").getValue().toString()

                        if (chef1.equals("None")){
                            chef1 = "NA"
                        }
                        if (chef2.equals("None")) {
                            chef2 = "NA"
                        }

                        root.findViewById<TextView>(R.id.chefs).text = chef1
                        root.findViewById<TextView>(R.id.chefs2).text = chef2
                        root.findViewById<TextView>(R.id.date).text = p0.child(cleaningid).child("date").getValue().toString()
                        root.findViewById<TextView>(R.id.task).text = p0.child(cleaningid).child("task").getValue().toString()
                        root.findViewById<TextView>(R.id.note).text = p0.child(cleaningid).child("note").getValue().toString()

                        if (status.contains("9")){
                            root.findViewById<TextView>(R.id.checkText).text = "Checked by: $status"
                        } else {
                            root.findViewById<TextView>(R.id.checkText).text = status
                        }

                        setId(cleaningid)
                        setVisiblityOnExtra(extras, root)
                    }
                }
                lottie.visibility = View.GONE
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

    private fun listenerOnChange(switch: Switch, rn: String, name: String, cleaningid: String){
        switch.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            if (isChecked){
                val n = name.split(" ")[0]
                val str = "$n, $rn"
                checkText.text = "Checked by: $str"
                database.child(cleaningid).child("checkedBy").setValue(str).addOnSuccessListener {
                }
                    .addOnFailureListener {
                        // Write failed
                        Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show()
                    }
            } else {
                checkText.text = "Unchecked"
                database.child(cleaningid).child("checkedBy").setValue("Unchecked").addOnSuccessListener {
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
        else {checkText.text = "Unchecked"}
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

    private fun visibleTask(extras: String, string: String, char: TextView, details: TextView){
        if (!extras.contains(string)) {
            char.visibility = View.GONE
            details.visibility = View.GONE
        }
    }

    private fun setVisiblityOnExtra(extras: String, root: View){
        val textA = root.findViewById<TextView>(R.id.textA)
        val textB = root.findViewById<TextView>(R.id.textB)
        val textC = root.findViewById<TextView>(R.id.textC)
        val textD = root.findViewById<TextView>(R.id.textD)
        val textE = root.findViewById<TextView>(R.id.textE)
        val textF = root.findViewById<TextView>(R.id.textF)
        val textG = root.findViewById<TextView>(R.id.textG)
        val textH = root.findViewById<TextView>(R.id.textH)
        val textI = root.findViewById<TextView>(R.id.textI)
        val textJ = root.findViewById<TextView>(R.id.textJ)
        val textK = root.findViewById<TextView>(R.id.textK)

        val detailsA = root.findViewById<TextView>(R.id.detailsA)
        val detailsB = root.findViewById<TextView>(R.id.detailsB)
        val detailsC = root.findViewById<TextView>(R.id.detailsC)
        val detailsD = root.findViewById<TextView>(R.id.detailsD)
        val detailsE = root.findViewById<TextView>(R.id.detailsE)
        val detailsF = root.findViewById<TextView>(R.id.detailsF)
        val detailsG = root.findViewById<TextView>(R.id.detailsG)
        val detailsH = root.findViewById<TextView>(R.id.detailsH)
        val detailsI = root.findViewById<TextView>(R.id.detailsI)
        val detailsJ = root.findViewById<TextView>(R.id.detailsJ)
        val detailsK = root.findViewById<TextView>(R.id.detailsK)

        visibleTask(extras, "A", textA, detailsA)
        visibleTask(extras, "B", textB, detailsB)
        visibleTask(extras, "C", textC, detailsC)
        visibleTask(extras, "D", textD, detailsD)
        visibleTask(extras, "E", textE, detailsE)
        visibleTask(extras, "F", textF, detailsF)
        visibleTask(extras, "G", textG, detailsG)
        visibleTask(extras, "H", textH, detailsH)
        visibleTask(extras, "I", textI, detailsI)
        visibleTask(extras, "J", textJ, detailsJ)
        visibleTask(extras, "K", textK, detailsK)
    }

}