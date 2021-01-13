package com.joeSoFine.dormcuments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import com.airbnb.lottie.LottieAnimationView
import com.joeSoFine.dormcuments.ui.cleaning.Cleaning
import com.joeSoFine.dormcuments.ui.cleaning.CleaningDetailsFragment
import com.joeSoFine.dormcuments.ui.cleaning.CleaningFragment
import java.time.LocalDate

object UICleaning {

    fun setUpPreCleaning(root: View, clean: Cleaning, spinner1: Spinner, spinner2: Spinner, date: EditText, task: EditText, note: EditText, stat: TextView, unform: TextView){
        spinner1.setSelection((spinner1.adapter as ArrayAdapter<String>).getPosition(clean.c1))
        spinner2.setSelection((spinner2.adapter as ArrayAdapter<String>).getPosition(clean.c2))
        date.setText(clean.date)
        task.setText(clean.task)
        note.setText(clean.note)
        stat.text = clean.checkedBy
        unform.text = clean.unform

        switchIni(root, task)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun onTaskClicked(root: View, @SuppressLint("UseSwitchCompatOrMaterialCode") switch: Switch){
        root.findViewById<EditText>(R.id.task).setOnTouchListener { v, event ->
            if (MotionEvent.ACTION_UP == event.action) {
                switch.requestFocus()
            }
            true
        }
    }

    fun onCleaningSavedClick(id: String, ref: String, spinner_c1: Spinner, spinner_c2: Spinner, date2: EditText, cleaning: Cleaning, context: Context, fragmentManager: FragmentManager, succes: LottieAnimationView, fail: LottieAnimationView) {
        if (validateCleaningInput(spinner_c1, spinner_c2, date2, context)) {
            if (id != null) { databaseService.saveCleaningToDatabase(ref, id, cleaning, succes, fail, fragmentManager) }
        }
    }

    fun validateCleaningInput(spinner_c1: Spinner, spinner_c2: Spinner, date2: EditText, context: Context): Boolean {
        return if ((spinner_c1.selectedItem.toString() == spinner_c2.selectedItem.toString()) && spinner_c1.selectedItem.toString() != "None" ) {
            Toast.makeText(context, "Cannot select the same cleaner twice", Toast.LENGTH_SHORT).show()
            false
        } else if (date2.text.toString() == "") {
            date2.error = "Please choose a date"
            false
        } else {
            true
        }
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    fun switchIni(root: View, task: EditText){
        val switchA = root.findViewById<Switch>(R.id.switchA)
        val switchB = root.findViewById<Switch>(R.id.switchB)
        val switchC = root.findViewById<Switch>(R.id.switchC)
        val switchD = root.findViewById<Switch>(R.id.switchD)
        val switchE = root.findViewById<Switch>(R.id.switchE)
        val switchF = root.findViewById<Switch>(R.id.switchF)
        val switchG = root.findViewById<Switch>(R.id.switchG)
        val switchH = root.findViewById<Switch>(R.id.switchH)
        val switchI = root.findViewById<Switch>(R.id.switchI)
        val switchJ = root.findViewById<Switch>(R.id.switchJ)
        val switchK = root.findViewById<Switch>(R.id.switchK)

        setSwitchStatus(switchA, "A", task)
        setSwitchStatus(switchB, "B", task)
        setSwitchStatus(switchC, "C", task)
        setSwitchStatus(switchD, "D", task)
        setSwitchStatus(switchE, "E", task)
        setSwitchStatus(switchF, "F", task)
        setSwitchStatus(switchG, "G", task)
        setSwitchStatus(switchH, "H", task)
        setSwitchStatus(switchI, "I", task)
        setSwitchStatus(switchJ, "J", task)
        setSwitchStatus(switchK, "K", task)

        listenerOnChange(switchA, "A", task)
        listenerOnChange(switchB, "B", task)
        listenerOnChange(switchC, "C", task)
        listenerOnChange(switchD, "D", task)
        listenerOnChange(switchE, "E", task)
        listenerOnChange(switchF, "F", task)
        listenerOnChange(switchG, "G", task)
        listenerOnChange(switchH, "H", task)
        listenerOnChange(switchI, "I", task)
        listenerOnChange(switchJ, "J", task)
        listenerOnChange(switchK, "K", task)
    }

    fun listenerOnChange(switch: Switch, st: String, task: EditText){
        switch.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            if (UITools.str.isEmpty()){
                UITools.str = st
            } else {
                UITools.str = task.text.toString() + " + " + st
            }
            if (isChecked){
                task.setText(UITools.str)
            } else {
                val s = UITools.str.split(" + ")
                if (st == s[0]){
                    UITools.str = task.text.toString().replace("$st + ", "")
                } else {
                    UITools.str = task.text.toString().replace(" + $st", "")
                }
                task.setText(UITools.str)
            }
        }
    }

    fun setSwitchStatus(switch: Switch, st: String, task: EditText){
        UITools.str = task.text.toString()
        if ( UITools.str.contains(st)){ switch.isChecked = true}
    }


    fun setSwitchStatusEvents(switch: Switch, rn: String, parti: TextView){
        if ( parti.text.toString().contains(rn)){ switch.isChecked = true}
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createCleaningItem(c1: String, c2: String, date: String, cleaningid: String, unform: String, myContainer: LinearLayout, layoutInflater: LayoutInflater, fragmentManager: FragmentManager){

        val ExpandableCardview: View = layoutInflater.inflate(R.layout.list_element_cleaning_food, null, false)

        var show: ImageView = ExpandableCardview.findViewById(R.id.show)
        var datefield: TextView = ExpandableCardview.findViewById(R.id.date)
        var w1: TextView = ExpandableCardview.findViewById(R.id.who1)
        var w2: TextView = ExpandableCardview.findViewById(R.id.who2)
        var un: TextView = ExpandableCardview.findViewById(R.id.unformatted)
        var id: TextView = ExpandableCardview.findViewById(R.id.idCon)


        var eventdate = unform.split("/")
        var local = LocalDate.of(eventdate[2].toInt(), eventdate[1].toInt()+1, eventdate[0].toInt())

        UITools.setC(c1, w1)
        UITools.setC(c2, w2)

        datefield.setText(date)
        un.text = unform
        id.text = cleaningid


        show.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", cleaningid)
            val fragment2 = CleaningDetailsFragment()
            fragment2.arguments = bundle
            fragmentManager?.beginTransaction()?.add(R.id.nav_host_fragment, fragment2)?.addToBackStack(null)?.commit()
        }
        if (myContainer.childCount == 0) {
            myContainer.addView(ExpandableCardview)
        } else {
            for (i in 0..myContainer.childCount - 1) {
                val ufd = myContainer.getChildAt(i).findViewById<TextView>(R.id.unformatted).text.toString().split("/")
                val elementDate = LocalDate.of(ufd[2].toInt(),ufd[1].toInt()+1,ufd[0].toInt())

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
                        val elementDateK = LocalDate.of(ufdK[2].toInt(),ufdK[1].toInt()+1,ufdK[0].toInt())

                        if (local.isBefore(elementDateK) || local.isEqual(elementDateK)) {
                            myContainer.addView(ExpandableCardview, k)
                            break

                        } else {

                            for (j in k..myContainer.childCount - 1) {
                                val ufdJ = myContainer.getChildAt(j).findViewById<TextView>(R.id.unformatted).text.toString().split("/")
                                val elementDateJ = LocalDate.of(ufdJ[2].toInt(), ufdJ[1].toInt()+1, ufdJ[0].toInt())

                                if (local.isBefore(elementDateJ)) {
                                    myContainer.addView(ExpandableCardview, j)
                                    UITools.bool = true
                                    break
                                }
                            }
                        }
                    }
                }
                if (UITools.bool == true){
                    break
                }
            }
        }
    }


}