package com.joeSoFine.dormcuments

import FoodDetailsFragment
import android.animation.Animator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.joeSoFine.dormcuments.ui.calender.EditEventFragment
import com.joeSoFine.dormcuments.ui.meeting.Topic
import com.joeSoFine.dormcuments.ui.shopping.Item
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object UITools {
    var str = ""
    var bool = false

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.O)
    fun setUpDatepicker(root: View, unf: TextView) {
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
            val unform = "${datePicker.dayOfMonth}/${datePicker.month}/${datePicker.year}"
            root.findViewById<EditText>(R.id.date2).setText(msg)
            unf.text = unform
            datePicker.visibility = View.GONE
        }

        root.findViewById<EditText>(R.id.date2).setOnTouchListener { v, event ->
            if (ACTION_UP == event.action) {
                datePicker.visibility = View.VISIBLE
            }
            true
        }
    }

    fun iniSpinners(root: View, context: Context, arr: ArrayList<String>){
        val myAdapter = ArrayAdapter(context, R.layout.spinner_layout, arr)
        myAdapter.setDropDownViewResource(R.layout.spinner_layout_dropdown)
        root.findViewById<Spinner>(R.id.spinner_c1).adapter = myAdapter
        root.findViewById<Spinner>(R.id.spinner_c2).adapter = myAdapter
    }

    fun onDeleteClicked(root: View, context: Context, id: String, ref: String, fragmentManager: FragmentManager){
        root.findViewById<ImageView>(R.id.delete).setOnClickListener(){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.dialogTitle)
        builder.setMessage(R.string.dialogMessage)
        builder.setIcon(R.drawable.ic_baseline_warning_24)

        builder.setPositiveButton("Continue"){ dialogInterface, which ->
            if (id != null) {
                databaseService.delteChildFromDatabase(id, ref)
                Toast.makeText(context, "Deleted", Toast.LENGTH_LONG).show()
                fragmentManager.popBackStack()
                fragmentManager.popBackStack()
            }
        }
        builder.setNeutralButton("Cancel"){ dialogInterface, which ->
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
        }
    }

    fun onHelpedClicked(context: Context, dialogTitle: Int, dialogMsg: Int){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMsg)
        builder.setIcon(R.drawable.help_dialog_icon_foreground)

        builder.setPositiveButton("Continue"){ dialogInterface, which ->
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
            databaseService.delteChildFromDatabase(itemid, ref)
        }

        myContainer.addView(ExpandableCardview)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createClubItem(
        c1: String,
        c2: String,
        date: String,
        clubid: String,
        unform: String,
        myContainer: LinearLayout,
        layoutInflater: LayoutInflater,
        fragmentManager: FragmentManager,
        context: Context,
        ref: String
    ){

        var ExpandableCardview: View = layoutInflater.inflate(R.layout.list_element_cleaning_food, null, false)
        var show: ImageView = ExpandableCardview.findViewById(R.id.show)
        var datefield: TextView = ExpandableCardview.findViewById(R.id.date)
        var w1: TextView = ExpandableCardview.findViewById(R.id.who1)
        var w2: TextView = ExpandableCardview.findViewById(R.id.who2)
        var un: TextView = ExpandableCardview.findViewById(R.id.unformatted)
        var id: TextView = ExpandableCardview.findViewById(R.id.idCon)

        var eventdate = unform.split("/")
        var local = LocalDate.of(eventdate[2].toInt(), eventdate[1].toInt() + 1, eventdate[0].toInt())

        setC(c1,w1)
        setC(c2,w2)


        datefield.setText(date)
        un.text = unform
        id.text = clubid


        show.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", clubid)
            val fragment2 = FoodDetailsFragment()
            fragment2.arguments = bundle
            fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment, fragment2)?.addToBackStack(null)?.commit()        }



        if (myContainer.childCount == 0) {
            myContainer.addView(ExpandableCardview)
        } else {
            for (i in 0..myContainer.childCount - 1) {
                val ufd = myContainer.getChildAt(i).findViewById<TextView>(R.id.unformatted).text.toString().split("/")
                val elementDate = LocalDate.of(ufd[2].toInt(), ufd[1].toInt() + 1, ufd[0].toInt())

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
                        val elementDateK = LocalDate.of(ufdK[2].toInt(), ufdK[1].toInt() + 1, ufdK[0].toInt())

                        if (local.isBefore(elementDateK) || local.isEqual(elementDateK)) {
                            myContainer.addView(ExpandableCardview, k)
                            break

                        } else {

                            for (j in k..myContainer.childCount - 1) {
                                val ufdJ = myContainer.getChildAt(j).findViewById<TextView>(R.id.unformatted).text.toString().split("/")
                                val elementDateJ = LocalDate.of(ufdJ[2].toInt(), ufdJ[1].toInt() + 1, ufdJ[0].toInt())

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

    fun createTopicAdd(layout: LinearLayout, layoutInflater: LayoutInflater, ref: String, context: Context){
        val ExpandableCardview: View =
            layoutInflater.inflate(R.layout.fragment_add_topic, null, false)

        layout.addView(ExpandableCardview)
    }


    fun createShopAdd(layout: LinearLayout, layoutInflater: LayoutInflater, ref: String, context: Context){
        val ExpandableCardview: View =
            layoutInflater.inflate(R.layout.layout_add_item, null, false)

        var add: ImageButton = ExpandableCardview.findViewById(R.id.addItem2)
        var inputItem: EditText = ExpandableCardview.findViewById(R.id.inputItem2)
        var c = false

        inputItem.requestFocus()

        add.setOnClickListener {
            if (c == true) {
                add.isEnabled = false
            } else {
                val item = inputItem.text.toString()

                if (item.isEmpty()) {
                    inputItem.error = "Please input a product"

                } else {
                    val product = Item(item)
                    createShopAdd(layout, layoutInflater, ref, context)
                    databaseService.saveShopItemToDatabase(ref, product, context, layout, layoutInflater)
                    c = true
                }
            }
        }
        layout.addView(ExpandableCardview)
    }


    fun createResident(
        fullname: String,
        rn: String,
        userId: String,
        bdate: String,
        sfrom: String,
        food: String,
        fact: String,
        url: String,
        myContainer: LinearLayout,
        layoutInflater: LayoutInflater,
        context: Context
    ) {
        val ExpandableCardview: View = layoutInflater.inflate(R.layout.list_element_resident, null, false)

        var sumLayout: ConstraintLayout = ExpandableCardview.findViewById(R.id.sumLayout)
        var titleLayout: ConstraintLayout = ExpandableCardview.findViewById(R.id.titleLayout)
        var expand: ImageView = ExpandableCardview.findViewById(R.id.expand)
        var room: TextView = ExpandableCardview.findViewById(R.id.resRn)
        var resName: TextView = ExpandableCardview.findViewById(R.id.resName)
        var resLast: TextView = ExpandableCardview.findViewById(R.id.resLast)
        var age: TextView = ExpandableCardview.findViewById(R.id.age)
        var birth: TextView = ExpandableCardview.findViewById(R.id.birthday)
        var from: TextView = ExpandableCardview.findViewById(R.id.from)
        var diet: TextView = ExpandableCardview.findViewById(R.id.diet)
        var funny: TextView = ExpandableCardview.findViewById(R.id.fact)
        var userImg: ImageView = ExpandableCardview.findViewById(R.id.userImage)
        var id: TextView = ExpandableCardview.findViewById(R.id.idCon)

        if (!rn.isEmpty()) {
            Glide.with(context.applicationContext).load(url).into(userImg)

            if (fullname.contains(" ")) {
                val name = fullname.split(" ")
                resName.text = name[0]
                resLast.text = name[1]
            } else {
                resName.text = fullname
                resLast.text = ""
            }

            val birthday = bdate.split("/")
            room.text = rn
            age.text = getAge(birthday[2].toInt(), birthday[1].toInt(), birthday[0].toInt())
            birth.text = bdate
            from.text = sfrom
            id.text = userId

            if (food.equals("")) {
                diet.text = "None"
            } else {
                diet.text = food
            }
            if (fact.equals("")) {
                funny.text = "I'm not very funny"
            } else {
                funny.text = fact
            }

            titleLayout.setOnClickListener { expandList(sumLayout, expand) }


            sortResidentsLowToHigh(myContainer, ExpandableCardview, rn.toInt())
        }


    }

    fun sortResidentsLowToHigh(myContainer: LinearLayout, ExpandableCardview: View, rn: Int){
        if (myContainer.childCount == 0) {
            myContainer.addView(ExpandableCardview)
        } else {
            for (i in 0..myContainer.childCount - 1) {
                val room = myContainer.getChildAt(i).findViewById<TextView>(R.id.resRn).text.toString().toInt()
                if (room >= rn) {
                    myContainer.addView(ExpandableCardview, i)
                    break

                } else if (room < rn) {
                    if (i == myContainer.childCount - 1) {
                        myContainer.addView(ExpandableCardview)
                        break

                    } else if (rn <= myContainer.getChildAt(i + 1).findViewById<TextView>(R.id.resRn).text.toString().toInt())  {
                        myContainer.addView(ExpandableCardview, i + 1)
                        break

                    } else {
                        for (k in i+1..myContainer.childCount -1){
                            val roomK = myContainer.getChildAt(k).findViewById<TextView>(R.id.resRn).text.toString().toInt()
                            if (rn < roomK){
                                myContainer.addView(ExpandableCardview, k)
                                break

                            }
                        }
                    }
                }
            }
        }
    }

    fun createTopic(
        name: String,
        des: String,
        topicId: String,
        myContainer: LinearLayout,
        layoutInflater: LayoutInflater,
        ref: String,
        context: Context
    ){

        val ExpandableCardview: View =
            layoutInflater.inflate(R.layout.list_element_meeting, null, false)

        var sumLayout : ConstraintLayout  = ExpandableCardview.findViewById(R.id.sumLayout)
        var titleLayout : ConstraintLayout = ExpandableCardview.findViewById(R.id.titleLayout)
        var expand : ImageView = ExpandableCardview.findViewById(R.id.expand)
        var divider: View = ExpandableCardview.findViewById(R.id.div)
        var delete: ImageView = ExpandableCardview.findViewById(R.id.delete)
        var meetingItem: TextView = ExpandableCardview.findViewById(R.id.meetingItem)
        var sum: TextView = ExpandableCardview.findViewById(R.id.sum)
        var id: TextView = ExpandableCardview.findViewById(R.id.idCon)

        meetingItem.setText(name)
        sum.setText(des)
        id.text = topicId

        //Set OnClickListener that handles expansion and collapse of view
        titleLayout.setOnClickListener {
            expandListMeeting(sumLayout, expand, divider) }

        delete.setOnClickListener {
            databaseService.delteChildFromDatabase(topicId, ref)
        }

        myContainer.addView(ExpandableCardview)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("InflateParams", "UseSwitchCompatOrMaterialCode", "SetTextI18n")
    fun createEventView(
        title: String,
        dateStart: String,
        unformattedDate: String,
        dateEnd: String,
        timeStart: String,
        timeEnd: String,
        des: String,
        location: String,
        allDay: String,
        notification: String,
        doesRepeat: String,
        createdBy: String,
        eventid: String,
        par: String,
        color: String,
        myContainer: LinearLayout,
        arrString: String,
        layoutInflater: LayoutInflater,
        fragmentManager: FragmentManager,
        ref: String,
        context: Context
    ){
        val ExpandableCardview: View =
            layoutInflater.inflate(R.layout.list_element_calendar, null, false)

        val sumLayout : ConstraintLayout = ExpandableCardview.findViewById(R.id.sumLayout)
        val titleLayout : ConstraintLayout = ExpandableCardview.findViewById(R.id.titleLayout)
        val expand : ImageView = ExpandableCardview.findViewById(R.id.expand)
        val eventtitle: TextView = ExpandableCardview.findViewById(R.id.eventTitle)
        val Date: TextView = ExpandableCardview.findViewById(R.id.date)
        val all : TextView = ExpandableCardview.findViewById(R.id.all)
        val startDate: TextView = ExpandableCardview.findViewById(R.id.dateStart2)
        val endDate: TextView = ExpandableCardview.findViewById(R.id.dateEnd2)
        val startTime: TextView = ExpandableCardview.findViewById(R.id.timeStart2)
        val endTime: TextView = ExpandableCardview.findViewById(R.id.timeEnd2)
        val desc: TextView = ExpandableCardview.findViewById(R.id.des)
        val desCon: ImageView = ExpandableCardview.findViewById(R.id.desCon)
        val loc: TextView = ExpandableCardview.findViewById(R.id.loctext)
        val locCon: ImageView = ExpandableCardview.findViewById(R.id.locIcon)
        val notCon: ImageView = ExpandableCardview.findViewById(R.id.notIcon)
        val notText: TextView = ExpandableCardview.findViewById(R.id.notTekst)
        val reap: TextView = ExpandableCardview.findViewById(R.id.reap)
        val reaCon: ImageView = ExpandableCardview.findViewById(R.id.reaIcon)
        val divloc: View = ExpandableCardview.findViewById(R.id.divloc)
        val divnot: View = ExpandableCardview.findViewById(R.id.divnot)
        val by: TextView = ExpandableCardview.findViewById(R.id.by)
        val switch: Switch = ExpandableCardview.findViewById(R.id.joinSwitch)
        val parti: TextView = ExpandableCardview.findViewById(R.id.parti)
        val divpar:View = ExpandableCardview.findViewById(R.id.divdes4)
        val uf: TextView = ExpandableCardview.findViewById(R.id.unformatted)
        val editEvent: ImageView = ExpandableCardview.findViewById(R.id.editEvent)
        val colorView:ConstraintLayout = ExpandableCardview.findViewById(R.id.colorShow)
        val colorExpand:ConstraintLayout = ExpandableCardview.findViewById(R.id.colorShowExand)
        val id = ExpandableCardview.findViewById<TextView>(R.id.idCon)



        var eventdate = unformattedDate.split("-")
        var local = LocalDate.of(eventdate[0].toInt(), eventdate[1].toInt(), eventdate[2].toInt())

        id.text = eventid
        eventtitle.text = title
        uf.text = unformattedDate
        by.text = "Created by:\n$createdBy"
        parti.text = par

        if (color.equals("Social event")){
            colorView.setBackgroundResource(R.drawable.blue_round_button)
            colorExpand.setBackgroundResource(R.drawable.blue_expand_button)

        } else if (color.equals("Book kitchen")){
            colorView.setBackgroundResource(R.drawable.red_round_button);
            colorExpand.setBackgroundResource(R.drawable.red_expand_button)

        } else {
            colorView.setBackgroundResource(R.drawable.default_round_button);
            colorExpand.setBackgroundResource(R.drawable.default_expand_button)
        }

        val dayOfWeekFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EEEE", Locale.ENGLISH)
        val dayAndMonthFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d. MMMM", Locale.ENGLISH)
        val yearFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.ENGLISH)

        if (arrString.equals("weeks")){
            Date.text = local.format(dayOfWeekFormatter)
        } else if (arrString.equals("months")){
            Date.text = local.format(dayAndMonthFormatter)
        } else {
            Date.text = local.format(yearFormatter)
        }

        if (allDay.equals("true")) {
            startTime.visibility = View.GONE
            endTime.visibility = View.GONE
        }
        else {
            all.visibility = View.GONE
            startDate.text = dateStart
            endDate.text = dateEnd
            startTime.text = timeStart
            endTime.text = timeEnd
        }

        if (doesRepeat.equals("Does not repeat")){
            reap.visibility = View.GONE
            reaCon.visibility = View.GONE
        } else {
            reap.text = doesRepeat
        }

        setVisiblityEvent(location, "", loc, divloc, locCon)
        setVisiblityEvent(notification, "No notification", notText, divnot, notCon)
        setVisiblityEvent(des, "", desc, divloc, desCon)

        if (des.equals("") && location.equals("")){
            divpar.visibility = View.GONE
        }

        titleLayout.setOnClickListener { expandListEvent(sumLayout, expand, colorView, colorExpand)}
        databaseService.setSwitchAndEditForCurrentUser(switch, parti, editEvent, by, eventid, ref, context)

        editEvent.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("id", eventid)
            val fragment2 = EditEventFragment()
            fragment2.arguments = bundle
            fragmentManager?.beginTransaction()?.add(R.id.nav_host_fragment, fragment2)?.addToBackStack(null)?.commit()
        }


        // Sorts events first date first
        if (myContainer.childCount == 0) { // For empty list, input into index 0
            myContainer.addView(ExpandableCardview)
        } else {
            for (i in 0..myContainer.childCount - 1) {
                val ufd = myContainer.getChildAt(i).findViewById<TextView>(R.id.unformatted).text.toString().split("-")
                val elementDate = LocalDate.of(ufd[0].toInt(), ufd[1].toInt(), ufd[2].toInt())

                if (elementDate.isAfter(local) || elementDate.isEqual(local) ) { // If the date of current and existing element is same or current is before, input before.
                    myContainer.addView(ExpandableCardview, i)
                    break

                } else if (local.isAfter(elementDate)) {
                    if (i == myContainer.childCount - 1) {
                        myContainer.addView(ExpandableCardview)
                        break

                    } else {
                        val ufdK = myContainer.getChildAt(i + 1).findViewById<TextView>(R.id.unformatted).text.toString().split("-")
                        val elementDateK = LocalDate.of(ufdK[0].toInt(), ufdK[1].toInt(), ufdK[2].toInt())

                        if (local.isBefore(elementDateK) || local.isEqual(elementDateK)) {
                            myContainer.addView(ExpandableCardview, i + 1)
                            break

                        } else {

                            for (j in i + 1..myContainer.childCount - 1) {
                                val ufdJ = myContainer.getChildAt(j).findViewById<TextView>(R.id.unformatted).text.toString().split("-")
                                val elementDateJ = LocalDate.of(ufdJ[0].toInt(), ufdJ[1].toInt(), ufdJ[2].toInt())

                                if (local.isBefore(elementDateJ)) {
                                    myContainer.addView(ExpandableCardview, j)
                                    bool = true
                                    break
                                }
                            }
                        }
                    }
                }
                if (bool){
                    bool = false
                    break
                }
            }
        }
    }

    fun expandList(
        sumLayout: ConstraintLayout, expand: ImageView
    ) {
        if (sumLayout.visibility == View.GONE) {
            sumLayout.visibility = View.VISIBLE
            expand.rotation = 90f
        } else if (sumLayout.visibility == View.VISIBLE) {
            sumLayout.visibility = View.GONE
            expand.rotation = 0f
        }
    }

    fun getAge(year: Int, month: Int, day: Int): String? {
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()
        dob[year, month] = day
        var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
        if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
            age--
        }
        val ageInt = age
        return ageInt.toString()
    }

    fun expandListMeeting(
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

    fun expandListEvent(
        sumLayout: ConstraintLayout,
        expand: ImageView,
        colorView: ConstraintLayout,
        colorExand: ConstraintLayout
    ) {
        if (sumLayout.visibility == View.GONE) {
            sumLayout.visibility = View.VISIBLE
            expand.rotation = 90f
            colorView.visibility = View.GONE
            colorExand.visibility = View.VISIBLE

        } else if (sumLayout.visibility == View.VISIBLE) {
            sumLayout.visibility = View.GONE
            expand.rotation = 0f
            colorView.visibility = View.VISIBLE
            colorExand.visibility = View.GONE
        }
    }

    fun visivlityEditButton(room: String, IV: ImageView, TV: TextView) {
        if (TV.text.toString().contains(room)) {
            IV.visibility = View.VISIBLE
        }
    }

    fun setVisiblityEvent(st: String, equals: String, tv: TextView, div: View, con: ImageView){
        if (!st.equals(equals)){
            tv.text = st
        } else {
            div.visibility = View.GONE
            tv.visibility = View.GONE
            con.visibility = View.GONE
        }
    }

    fun setWhoops(myContainer: LinearLayout, whoops: TextView){
        if (myContainer.childCount == 0) {
            whoops.visibility = View.VISIBLE
        } else {
            whoops.visibility = View.GONE
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun eventDateCall(
        i: DataSnapshot,
        arrString: String,
        myContainer: LinearLayout,
        layoutInflater: LayoutInflater,
        fragmentManager: FragmentManager,
        context: Context,
        ref: String
    ){
        var dateUn: String = i.child("unformattedDate").value as String
        var title: String = i.child("title").value as String
        var dateStart: String = i.child("dateStart").value as String
        var dateEnd: String = i.child("dateEnd").value as String
        var timeStart: String = i.child("timeStart").value as String
        var timeEnd: String = i.child("timeEnd").value as String
        var location: String = i.child("location").value as String
        var des: String = i.child("des").value as String
        var allday: String = i.child("allDay").value as String
        var notis: String = i.child("notification").value as String
        var created: String = i.child("createdBy").value as String
        var doesRepeat: String = i.child("doesRepeat").value as String
        var par = i.child("participants").value.toString()
        var color = i.child("color").value.toString()
        var eventid = i.key.toString()

        createEventView(
            title,
            dateStart,
            dateUn,
            dateEnd,
            timeStart,
            timeEnd,
            des,
            location,
            allday,
            notis,
            doesRepeat,
            created,
            eventid,
            par,
            color,
            myContainer,
            arrString, layoutInflater, fragmentManager, ref, context
        )
    }

    fun addItemDialog(context: Context, layoutInflater: LayoutInflater, fragmentManager: FragmentManager, ref: String) {
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL

        val alert = AlertDialog.Builder(context, R.style.MyDialogStyle)
        alert.setTitle(Html.fromHtml("<font color='#FFFFFF'>Add items to list</font>"))

        if (ref.equals("Agenda")) {
            createTopicAdd(layout, layoutInflater, ref, context)
        } else {
            createShopAdd(layout, layoutInflater, ref, context)
        }

        alert.setView(layout)

        alert.setPositiveButton("Done") { dialog, whichButton ->
            if (ref.equals("Agenda")) {
                val topic = layout.getChildAt(0).findViewById<EditText>(R.id.inputItem).text.toString()
                val des = layout.getChildAt(0).findViewById<EditText>(R.id.sum).text.toString()

                if (!topic.isEmpty()) {
                    val top = Topic(topic, des)
                    databaseService.pushTopicToDatabase(ref, top, context, fragmentManager)
                }

            } else {
                val item = layout.getChildAt(layout.childCount - 1).findViewById<EditText>(R.id.inputItem2).text.toString()

                if (!item.isEmpty()) {
                    val product = Item(item)
                    databaseService.saveShopItemToDatabase(ref, product, context, layout, layoutInflater)
                }
            }
        }
        alert.show().withCenteredButtons()
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

    fun playLotiieOnce(lottie: LottieAnimationView, fragmentManager: FragmentManager, pop: String){
        lottie.visibility = View.VISIBLE
        lottie.playAnimation()

        lottie.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                if (pop.equals("pop")){ fragmentManager.popBackStack()}
                lottie.visibility = View.GONE
            }
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
        })
    }

    fun setC(c: String, w: TextView) {
        var cSub = c.substring(c.length - 2, c.length)

        if (c.equals("None")) {
            w.setText("NA")
        } else {
            w.setText(cSub)
        }
    }

}