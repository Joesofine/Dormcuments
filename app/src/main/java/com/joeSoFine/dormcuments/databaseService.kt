package com.joeSoFine.dormcuments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.joeSoFine.dormcuments.ui.cleaning.Cleaning
import com.joeSoFine.dormcuments.ui.foodclub.Foodclub
import com.joeSoFine.dormcuments.ui.shopping.Item
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalField
import java.time.temporal.WeekFields
import java.util.*

object databaseService {
    var database = FirebaseDatabase.getInstance()
    var c = 0
    private var auth = Firebase.auth

    fun generateID(ref: String): String? {
        val id = database.getReference(ref).push().key
        return id
    }

    fun saveCleaningToDatabase(ref: String, id: String, clean: Cleaning, context: Context, frag: Fragment, fragmentManager: FragmentManager){
        database.getReference(ref).child(id).setValue(clean)
            .addOnSuccessListener {
                Toast.makeText(context, "Created", Toast.LENGTH_SHORT).show()
                fragmentManager.beginTransaction().replace(
                    R.id.nav_host_fragment,
                    frag
                ).addToBackStack(null).commit()
            }
            .addOnFailureListener {
                // Write failed
                Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show()
            }
    }

    fun saveShopItemToDatabase(ref: String, id: String, product: Item, context: Context, layout: LinearLayout, layoutInflater: LayoutInflater): Int {
        if (id != null) {
            database.getReference(ref).child(id).setValue(product)
                .addOnSuccessListener {
                    c = 1
                    Toast.makeText(context, "Item has been added", Toast.LENGTH_SHORT)
                        .show()
                    UITools.createShopAdd(layout,layoutInflater,ref, context)
                }
                .addOnFailureListener {
                    // Write failed
                    Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show()
                }
        }
        return c
    }

     fun getDataFromDatabase(id: String, p0: DataSnapshot): Cleaning {
         var cleaning = Cleaning(
             p0.child(id).child("c1").value.toString(),
             p0.child(id).child("c2").value.toString(),
             p0.child(id).child("date").value.toString(),
             p0.child(id).child("task").value.toString(),
             p0.child(id).child("note").value.toString(),
             p0.child(id).child("checkedBy").value.toString(),
             p0.child(id).child("unform").value.toString()
         )

         return cleaning
    }

    fun setEventListener(
        root: View,
        id: String,
        spinner1: Spinner,
        spinner2: Spinner,
        date: EditText,
        task: EditText,
        note: EditText,
        stat: TextView,
        unform: TextView
    ): ValueEventListener {
        var getdata = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var clean = getDataFromDatabase(id, snapshot)
                UICleaning.setUpPreCleaning(root, clean, spinner1, spinner2, date, task, note, stat, unform)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        }
        return getdata
    }

    fun delteChildFromDatabase(id: String, ref: String, context: Context){
        var dName = database.getReference(ref).child(id)
        dName.removeValue()
    }

    fun setShopChildListener(progressBar: ProgressBar, myContainer: LinearLayout, layoutInflater: LayoutInflater, context: Context, ref: String){
        var childListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                progressBar.visibility = View.VISIBLE
                UITools.createShopItem(snapshot.child("name").value.toString(),snapshot.key.toString(),myContainer,layoutInflater, context, ref)
                progressBar.visibility = View.GONE
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                progressBar.visibility = View.VISIBLE
                for (i in 0..myContainer.childCount - 1) {
                    if (myContainer.getChildAt(i).findViewById<TextView>(R.id.idCon).text.toString() == snapshot.key.toString()) {
                        myContainer.getChildAt(i).findViewById<TextView>(R.id.shoppingItem).text = snapshot.child("name").value.toString()
                        progressBar.visibility = View.GONE
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                progressBar.visibility = View.VISIBLE
                for (i in 0..myContainer.childCount - 1) {
                    if (myContainer.getChildAt(i).findViewById<TextView>(R.id.idCon).text.toString() == snapshot.key.toString()) {
                        myContainer.removeView(myContainer.getChildAt(i))
                        Toast.makeText(context, snapshot.child("name").value.toString() + " was removed", Toast.LENGTH_SHORT).show()
                    }
                }
                progressBar.visibility = View.GONE
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                progressBar.visibility = View.GONE
            }
        }

        database.getReference(ref).addChildEventListener(childListener)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getSortedEvents(DateIndex: Int, relevantDatePart: Int, arrString: String, progressBar: ProgressBar, current_year: Int, myContainer: LinearLayout, layoutInflater: LayoutInflater, fragmentManager: FragmentManager, context: Context, refE: String, refU: String, whoops: TextView) {
        var childListener = object : ChildEventListener {
            override fun onChildAdded(i: DataSnapshot, previousChildName: String?) {
                    var dateUn: String = i.child("unformattedDate").value as String
                    var eventdate = dateUn.split("-")

                    if (arrString.equals("years")) {
                        if (eventdate[DateIndex].toInt() == relevantDatePart) {
                            UITools.eventDateCall(i, arrString, myContainer, layoutInflater, fragmentManager, context, refU)
                        }
                    } else {
                        if (eventdate[0].toInt() == current_year) {
                            if (arrString.equals("weeks")) {
                                var local = LocalDate.of(eventdate[0].toInt(), eventdate[1].toInt(), eventdate[2].toInt())
                                val woy: TemporalField = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()

                                if (local.get(woy) == relevantDatePart) {
                                    UITools.eventDateCall(i, arrString, myContainer, layoutInflater, fragmentManager, context, refU)
                                }
                            } else {
                                if (eventdate[DateIndex].toInt() == relevantDatePart) {
                                    UITools.eventDateCall(i, arrString, myContainer, layoutInflater, fragmentManager, context, refU)
                                }
                            }
                        }
                    }
                UITools.setWhoops(myContainer, whoops)
                progressBar.visibility = View.GONE
            }


            @SuppressLint("CutPasteId")
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                for (i in 0..myContainer.childCount - 1) {
                    if (myContainer.getChildAt(i).findViewById<TextView>(R.id.idCon).text.toString() == snapshot.key.toString()) {

                        var eventdate = snapshot.child("umformattedDate").value.toString().split("-")
                        var local = LocalDate.of(eventdate[0].toInt(), eventdate[1].toInt(), eventdate[2].toInt())
                        var created = snapshot.child("createdBy").value.toString()
                        var color = snapshot.child("color").value.toString()
                        var allDay = snapshot.child("allDay").value.toString()
                        var doesRepeat = snapshot.child("doesRepeat").value.toString()

                        myContainer.getChildAt(i).findViewById<TextView>(R.id.eventTitle).text = snapshot.child("title").value.toString()
                        myContainer.getChildAt(i).findViewById<TextView>(R.id.unformatted).text = snapshot.child("unformattedDate").value.toString()
                        myContainer.getChildAt(i).findViewById<TextView>(R.id.by).text = "Created by:\n$created"
                        myContainer.getChildAt(i).findViewById<TextView>(R.id.parti).text = snapshot.child("participants").value.toString()

                        if (color.equals("Social event")){
                            myContainer.getChildAt(i).findViewById<ImageView>(R.id.colorShow).setBackgroundResource(R.drawable.blue_round_button)
                            myContainer.getChildAt(i).findViewById<ImageView>(R.id.colorShowExand).setBackgroundResource(R.drawable.blue_expand_button)
                        } else if (color.equals("Book kitchen")){
                            myContainer.getChildAt(i).findViewById<ImageView>(R.id.colorShow).setBackgroundResource(R.drawable.red_round_button)
                            myContainer.getChildAt(i).findViewById<ImageView>(R.id.colorShowExand).setBackgroundResource(R.drawable.red_expand_button)
                        } else {
                            myContainer.getChildAt(i).findViewById<ImageView>(R.id.colorShow).setBackgroundResource(R.drawable.default_round_button)
                            myContainer.getChildAt(i).findViewById<ImageView>(R.id.colorShowExand).setBackgroundResource(R.drawable.default_expand_button)
                        }

                        val dayOfWeekFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EEEE", Locale.ENGLISH)
                        val dayAndMonthFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d. MMMM", Locale.ENGLISH)
                        val yearFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.ENGLISH)

                        if (arrString.equals("weeks")){
                            myContainer.getChildAt(i).findViewById<TextView>(R.id.date).text = local.format(dayOfWeekFormatter)
                        } else if (arrString.equals("months")){
                            myContainer.getChildAt(i).findViewById<TextView>(R.id.date).text = local.format(dayAndMonthFormatter)
                        } else {
                            myContainer.getChildAt(i).findViewById<TextView>(R.id.date).text = local.format(yearFormatter)
                        }

                        if (allDay.equals("true")) {
                            myContainer.getChildAt(i).findViewById<TextView>(R.id.timeStart2).visibility = View.GONE
                            myContainer.getChildAt(i).findViewById<TextView>(R.id.timeEnd2).visibility = View.GONE
                        }
                        else {
                            myContainer.getChildAt(i).findViewById<TextView>(R.id.all).visibility = View.GONE
                            myContainer.getChildAt(i).findViewById<TextView>(R.id.dateStart2).text = snapshot.child("dateStart").value.toString()
                            myContainer.getChildAt(i).findViewById<TextView>(R.id.dateEnd2).text = snapshot.child("dateEnd").value.toString()
                            myContainer.getChildAt(i).findViewById<TextView>(R.id.timeStart2).text = snapshot.child("timeStart").value.toString()
                            myContainer.getChildAt(i).findViewById<TextView>(R.id.timeEnd2).text = snapshot.child("timeEnd").value.toString()
                        }

                        if (doesRepeat.equals("Does not repeat")){
                            myContainer.getChildAt(i).findViewById<TextView>(R.id.reap).visibility = View.GONE
                            myContainer.getChildAt(i).findViewById<ImageView>(R.id.reaIcon).visibility = View.GONE
                        } else {
                            myContainer.getChildAt(i).findViewById<TextView>(R.id.reap).text = doesRepeat
                        }

                        UITools.setVisiblityEvent(snapshot.child("location").value.toString(), "",  myContainer.getChildAt(i).findViewById<TextView>(R.id.loctext),  myContainer.getChildAt(i).findViewById<View>(R.id.divloc), myContainer.getChildAt(i).findViewById<ImageView>(R.id.locIcon))
                        UITools.setVisiblityEvent(snapshot.child("notification").value.toString(), "No notification",  myContainer.getChildAt(i).findViewById<TextView>(R.id.notTekst),  myContainer.getChildAt(i).findViewById<View>(R.id.divnot),  myContainer.getChildAt(i).findViewById<ImageView>(R.id.notIcon))
                        UITools.setVisiblityEvent(snapshot.child("des").value.toString(), "",  myContainer.getChildAt(i).findViewById<TextView>(R.id.des),  myContainer.getChildAt(i).findViewById<View>(R.id.divdes),  myContainer.getChildAt(i).findViewById<ImageView>(R.id.desCon))

                        if (snapshot.child("des").value.toString().equals("") && snapshot.child("location").value.toString().equals("")){
                            myContainer.getChildAt(i).findViewById<View>(R.id.divdes4).visibility = View.GONE
                        }
                    }
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                progressBar.visibility = View.VISIBLE
                for (i in 0..myContainer.childCount - 1) {
                    if (myContainer.getChildAt(i).findViewById<TextView>(R.id.idCon).text.toString() == p0.key.toString()) {
                        myContainer.removeView(myContainer.getChildAt(i))
                        Toast.makeText(context, p0.child("title").value.toString() + " was removed", Toast.LENGTH_SHORT).show()
                    }
                }
                progressBar.visibility = View.GONE
            }

            override fun onChildMoved(p0: DataSnapshot, previousChildName: String?) {
                progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                progressBar.visibility = View.GONE
            }
        }
        database.getReference(refE).addChildEventListener(childListener)
    }

        fun setFoodChildListener(progressBar: ProgressBar, myContainer: LinearLayout, layoutInflater: LayoutInflater, fragmentManager: FragmentManager, context: Context, ref: String) {
        var childListener = object : ChildEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                progressBar.visibility = View.VISIBLE
                if (ref.equals("Foodclub")){
                UITools.createClubItem(
                    snapshot.child("c1").value.toString(),
                    snapshot.child("c2").value.toString(),
                    snapshot.child("date").value.toString(),
                    snapshot.key.toString(),
                    snapshot.child("unform").value.toString(),
                    myContainer,
                    layoutInflater,
                    fragmentManager,
                    context,
                    ref
                )}
                else if (ref.equals("Cleaning")){
                    UICleaning.createCleaningItem(
                        snapshot.child("c1").value.toString(),
                        snapshot.child("c2").value.toString(),
                        snapshot.child("date").value.toString(),
                        snapshot.key.toString(),
                        snapshot.child("unform").value.toString(),
                        myContainer,
                        layoutInflater,
                        fragmentManager,
                        context,
                        ref
                    )
                }
                else if (ref.equals("Users")){
                    UITools.createResident(
                        snapshot.child("fname").value.toString(),
                        snapshot.child("number").value.toString(),
                        snapshot.key.toString(),
                        snapshot.child("bdate").value.toString(),
                        snapshot.child("from").value.toString(),
                        snapshot.child("diet").value.toString(),
                        snapshot.child("funfact").value.toString(),
                        snapshot.child("url").value.toString(),
                        myContainer,
                        layoutInflater,
                        context)
                }
                else if (ref.equals("Agenda")){
                    UITools.createTopic(snapshot.child("name").value.toString(),
                        snapshot.child("summary").value.toString(),
                        snapshot.key.toString(),
                        myContainer,
                        layoutInflater,
                        ref,
                        context
                    )
                }
                progressBar.visibility = View.GONE
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                progressBar.visibility = View.VISIBLE
                for (i in 0..myContainer.childCount - 1) {
                    if (myContainer.getChildAt(i).findViewById<TextView>(R.id.idCon).text.toString() == snapshot.key.toString()) {
                        if (ref.equals("User")){
                            var fname = snapshot.child("fname").value.toString()

                            if (fname.contains(" ")){
                                val name = fname.split(" ")
                                myContainer.getChildAt(i).findViewById<TextView>(R.id.resName).text = name[0]
                                myContainer.getChildAt(i).findViewById<TextView>(R.id.resLast).text = name[1]
                            } else {
                                myContainer.getChildAt(i).findViewById<TextView>(R.id.resName).text = fname
                                myContainer.getChildAt(i).findViewById<TextView>(R.id.resLast).text = " "
                            }

                            var bdate = snapshot.child("bdate").value.toString()
                            val birthday = bdate.split("/")
                            myContainer.getChildAt(i).findViewById<TextView>(R.id.age).text = UITools.getAge(birthday[2].toInt() ,birthday[1].toInt() ,birthday[0].toInt())
                            myContainer.getChildAt(i).findViewById<TextView>(R.id.birthday).text = bdate
                            context?.let { Glide.with(it).load(snapshot.child("url").value.toString()).into(myContainer.getChildAt(i).findViewById<ImageView>(R.id.userImage)) }

                            myContainer.getChildAt(i).findViewById<TextView>(R.id.resRn).text = snapshot.child("number").value.toString()
                            myContainer.getChildAt(i).findViewById<TextView>(R.id.from).text = snapshot.child("from").value.toString()
                            myContainer.getChildAt(i).findViewById<TextView>(R.id.diet).text = snapshot.child("diet").value.toString()
                            myContainer.getChildAt(i).findViewById<TextView>(R.id.fact).text = snapshot.child("funfact").value.toString()

                        } else if (ref.equals("Agenda")){
                            myContainer.getChildAt(i).findViewById<TextView>(R.id.meetingItem).text = snapshot.child("name").value.toString()
                            myContainer.getChildAt(i).findViewById<TextView>(R.id.sum).text = snapshot.child("summary").value.toString()
                        }

                        else {
                            myContainer.getChildAt(i).findViewById<TextView>(R.id.who1).text = snapshot.child("c1").value.toString()
                            myContainer.getChildAt(i).findViewById<TextView>(R.id.who2).text = snapshot.child("c2").value.toString()
                            myContainer.getChildAt(i).findViewById<TextView>(R.id.date).text = snapshot.child("date").value.toString()
                            myContainer.getChildAt(i).findViewById<TextView>(R.id.unformatted).text = snapshot.child("unform").value.toString()
                            progressBar.visibility = View.GONE
                        }
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                progressBar.visibility = View.VISIBLE
                for (i in 0..myContainer.childCount - 1) {
                    if (myContainer.getChildAt(i).findViewById<TextView>(R.id.idCon).text.toString() == snapshot.key.toString()) {
                        myContainer.removeView(myContainer.getChildAt(i))
                        if (ref.equals("Cleaning") || ref.equals("Foodclub"))
                            Toast.makeText(context, snapshot.child("date").value.toString() + " was removed", Toast.LENGTH_SHORT).show()
                        else {
                            Toast.makeText(context, snapshot.child("name").value.toString() + " was removed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                progressBar.visibility = View.GONE
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                progressBar.visibility = View.GONE
            }
        }
            progressBar.visibility = View.GONE
            database.getReference(ref).addChildEventListener(childListener)
    }

    fun setSwitchAndEditForCurrentUser(switch: Switch, parti: TextView, editIV: ImageView, createdTV: TextView, eventid: String, ref: String, context: Context){
         val getdata = object : ValueEventListener {
            val userid = auth.currentUser?.uid.toString()

            override fun onDataChange(p0: DataSnapshot) {
                var room: String = p0.child(userid).child("number").getValue() as String

                UITools.visivlityEditButton(room, editIV, createdTV)
                UICleaning.setSwitchStatusEvents(switch, room, parti)
                listenerOnChangeEvents(switch, room, eventid, parti, ref, context)
            }

            override fun onCancelled(p0: DatabaseError) {
                println("err")
            }
        }

        database.getReference(ref).addListenerForSingleValueEvent(getdata)
    }

    fun listenerOnChangeEvents(switch: Switch, rn: String, eventid: String, parti: TextView, ref: String, context: Context){
        switch.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            var st = ""
            if (isChecked) {

                if (parti.text.toString().isEmpty()) {
                    parti.text = rn
                } else {
                    st = parti.text.toString() + ", " + rn
                    parti.text = st
                }
                st = parti.text.toString()

                database.getReference(ref).child(eventid).child("participants").setValue(st).addOnSuccessListener {
                    Toast.makeText(context, "succesfully joined event", Toast.LENGTH_SHORT).show()
                }
                    .addOnFailureListener {}

            } else {
                if (parti.text.toString().contains(", $rn")) {
                    st = parti.text.toString().replace(", $rn", "")
                } else {
                    st = parti.text.toString().replace(rn, "")
                }
                parti.text = st

                database.getReference(ref).child(eventid).child("participants").setValue(st).addOnSuccessListener {
                    Toast.makeText(context, "Sign up deleted", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener { }

            }
        }
    }

}