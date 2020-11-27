package com.joeSoFine.dormcuments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import com.joeSoFine.dormcuments.ui.cleaning.Cleaning
import com.joeSoFine.dormcuments.ui.cleaning.CleaningFragment
import com.joeSoFine.dormcuments.ui.shopping.Item
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object UITools {
    var str = ""

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.O)
    fun setUpDatepicker(root: View): String {
        val datePicker = root.findViewById<DatePicker>(R.id.datePicker)
        val today = Calendar.getInstance()

        datePicker.init(
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { view, year, month, day ->
            val local = LocalDate.of(datePicker.year, datePicker.month + 1, datePicker.dayOfMonth)
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM", Locale.ENGLISH)
            val msg = local.format(formatter)
            root.findViewById<EditText>(R.id.date2).setText(msg)
            datePicker.visibility = View.GONE
        }

        root.findViewById<EditText>(R.id.date2).setOnTouchListener { v, event ->
            if (ACTION_UP == event.action) {
                datePicker.visibility = View.VISIBLE
            }
            true
        }

        return "${datePicker.dayOfMonth}/${datePicker.month}/${datePicker.year}"
    }

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

    fun iniSpinners(root: View, context: Context, arr: Array<String>){
        val myAdapter = ArrayAdapter(context, R.layout.spinner_layout, arr)
        myAdapter.setDropDownViewResource(R.layout.spinner_layout_dropdown)
        root.findViewById<Spinner>(R.id.spinner_c1).adapter = myAdapter
        root.findViewById<Spinner>(R.id.spinner_c2).adapter = myAdapter
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

    fun onDeleteClicked(root: View, context: Context, id:String, ref: String,  fragmentManager: FragmentManager){
        root.findViewById<ImageView>(R.id.delete).setOnClickListener(){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.dialogTitle)
        builder.setMessage(R.string.dialogMessage)
        builder.setIcon(R.drawable.ic_baseline_warning_24)

        builder.setPositiveButton("Continue"){dialogInterface, which ->
            if (id != null) {
                databaseService.delteChildFromDatabase(id, ref, context)
                Toast.makeText(context,"Deleted",Toast.LENGTH_LONG).show()
                fragmentManager.popBackStack()
                fragmentManager.popBackStack()
            }
        }
        builder.setNeutralButton("Cancel"){dialogInterface , which ->
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
        }
    }

    fun onCleaningSavedClick(id: String, ref: String, spinner_c1: Spinner, spinner_c2: Spinner, date2: EditText, cleaning: Cleaning, context: Context,fragmentManager: FragmentManager) {
        if (validateCleaningInput(spinner_c1, spinner_c2, date2, context)) {
            if (id != null) {
                databaseService.saveCleaningToDatabase(ref, id, cleaning, context, CleaningFragment(), fragmentManager)
            }
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
            if (str.isEmpty()){
                str = st
            } else {
                str = task.text.toString() + " + " + st
            }
            if (isChecked){
                task.setText(str)
            } else {
                val s = str.split(" + ")
                if (st == s[0]){
                    str = task.text.toString().replace("$st + ", "")
                } else {
                    str = task.text.toString().replace(" + $st", "")
                }
                task.setText(str)
            }
        }
    }

    fun setSwitchStatus(switch: Switch, st: String, task: EditText){
        str = task.text.toString()
        if ( str.contains(st)){ switch.isChecked = true}
    }


    fun onHelpedClicked(context: Context, dialogTitle: Int, dialogMsg: Int){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMsg)
        builder.setIcon(R.drawable.help_dialog_icon_foreground)

        builder.setPositiveButton("Continue"){dialogInterface, which ->
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun createShopItem(name: String, itemid: String, myContainer: LinearLayout, layoutInflater: LayoutInflater, context: Context, ref: String){
        var ExpandableCardview = layoutInflater.inflate(R.layout.list_element_shopping, null, false)

        var delete: ImageView = ExpandableCardview.findViewById(R.id.delete)
        var shoppingItem: TextView = ExpandableCardview.findViewById(R.id.shoppingItem)
        var idContainer: TextView = ExpandableCardview.findViewById(R.id.idCon)

        shoppingItem.text = name
        idContainer.text = itemid

        delete.setOnClickListener {
            myContainer.removeView(ExpandableCardview)
            databaseService.delteChildFromDatabase(itemid, ref, context)
        }

        myContainer.addView(ExpandableCardview)
    }

    fun AlertDialog.withCenteredButtons() {
        val positive = getButton(AlertDialog.BUTTON_POSITIVE)

        //Disable the material spacer view in case there is one
        val parent = positive.parent as? LinearLayout
        parent?.gravity = Gravity.CENTER_HORIZONTAL
        val leftSpacer = parent?.getChildAt(1)
        leftSpacer?.visibility = View.GONE

        //Force the default buttons to center
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        layoutParams.weight = 1f
        layoutParams.gravity = Gravity.CENTER

        positive.layoutParams = layoutParams
    }

    fun createShopAdd(layout: LinearLayout, layoutInflater: LayoutInflater, ref: String, context: Context){
        val ExpandableCardview: View =
            layoutInflater.inflate(R.layout.layout_add_item, null, false)

        var add: ImageView = ExpandableCardview.findViewById(R.id.addItem2)
        var inputItem: EditText = ExpandableCardview.findViewById(R.id.inputItem2)
        var c = 0

        inputItem.requestFocus()


        add.setOnClickListener {
            if (c > 0) {
                add.isEnabled = false
            } else {
                val item = inputItem.text.toString()

                if (item.isEmpty()) {
                    inputItem.error = "Please input a product"

                } else {

                    val itemId = databaseService.generateID(ref)
                    val product = Item(item)

                    c = databaseService.saveShopItemToDatabase(ref, itemId!!,product, context, layout, layoutInflater)
                }
            }
        }
        layout.addView(ExpandableCardview)
    }


}