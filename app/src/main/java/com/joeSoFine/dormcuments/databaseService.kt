package com.joeSoFine.dormcuments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.joeSoFine.dormcuments.ui.cleaning.Cleaning
import com.joeSoFine.dormcuments.ui.meeting.MeetingFragment
import com.joeSoFine.dormcuments.ui.meeting.Topic
import com.joeSoFine.dormcuments.ui.shopping.Item
import com.joeSoFine.dormcuments.ui.signIn.SignUpWithFacebookFragment
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalField
import java.time.temporal.WeekFields
import java.util.*


object databaseService {
    var database = FirebaseDatabase.getInstance()
    var c = 0
    private var auth = Firebase.auth
    var bool = false


    fun generateID(ref: String): String? {
        val id = database.getReference(ref).push().key
        return id
    }

    fun saveCleaningToDatabase(
        ref: String,
        id: String,
        clean: Cleaning,
        succes: LottieAnimationView,
        fail: LottieAnimationView,
        fragmentManager: FragmentManager
    ) {
        database.getReference(ref).child(id).setValue(clean)
            .addOnSuccessListener {
                UITools.playLotiieOnce(succes, fragmentManager, "pop")
            }
            .addOnFailureListener {
                UITools.playLotiieOnce(fail, fragmentManager, "noPop")
            }
    }

    fun saveShopItemToDatabase(ref: String, product: Item, context: Context, layout: LinearLayout, layoutInflater: LayoutInflater) {
        val id = generateID(ref)
        database.getReference(ref).child(id!!).setValue(product)
            .addOnSuccessListener {
                Toast.makeText(context, "Item has been added", Toast.LENGTH_SHORT)
                    .show()
            }
            .addOnFailureListener {
                // Write failed
                Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show()
            }
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

    fun delteChildFromDatabase(id: String, ref: String){
        var dName = database.getReference(ref).child(id)
        dName.removeValue()
    }

    fun setShopChildListener(
        progressBar: LottieAnimationView,
        myContainer: LinearLayout,
        layoutInflater: LayoutInflater,
        context: Context,
        ref: String
    ){
        var childListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                progressBar.visibility = View.VISIBLE
                UITools.createShopItem(snapshot.child("name").value.toString(), snapshot.key.toString(), myContainer, layoutInflater, context, ref)
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
    fun getSortedEvents(
        DateIndex: Int,
        relevantDatePart: Int,
        arrString: String,
        progressBar: LottieAnimationView,
        current_year: Int,
        myContainer: LinearLayout,
        layoutInflater: LayoutInflater,
        fragmentManager: FragmentManager,
        context: Context,
        refE: String,
        refU: String,
        whoops: TextView
    ) {
        var childListener = object : ChildEventListener {
            override fun onChildAdded(i: DataSnapshot, previousChildName: String?) {
                    var dateUn: String = i.child("unformattedDate").value as String
                    var eventdate = dateUn.split("-")

                    if (arrString.equals("years")) {
                        if (eventdate[DateIndex].toInt() == relevantDatePart) {
                            UITools.eventDateCall(i, arrString, myContainer, layoutInflater, fragmentManager, context, refU, refE)
                        }
                    } else {
                        if (eventdate[0].toInt() == current_year) {
                            if (arrString.equals("weeks")) {
                                var local = LocalDate.of(eventdate[0].toInt(), eventdate[1].toInt(), eventdate[2].toInt())
                                val woy: TemporalField = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()

                                if (local.get(woy) == relevantDatePart) {
                                    UITools.eventDateCall(i, arrString, myContainer, layoutInflater, fragmentManager, context, refU, refE)
                                }
                            } else {
                                if (eventdate[DateIndex].toInt() == relevantDatePart) {
                                    UITools.eventDateCall(i, arrString, myContainer, layoutInflater, fragmentManager, context, refU, refE)
                                }
                            }
                        }
                    }
                UITools.setWhoops(myContainer, whoops)
                progressBar.visibility = View.GONE
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                for (i in 0..myContainer.childCount - 1) {
                    if (myContainer.getChildAt(i).findViewById<TextView>(R.id.idCon).text.toString() == snapshot.key.toString()) {
                        var dateUn: String = snapshot.child("unformattedDate").value as String
                        var eventdate = dateUn.split("-")

                        if (arrString.equals("years")) {
                            if (eventdate[DateIndex].toInt() == relevantDatePart) {
                                onEventViewChangedUpdateView(myContainer,i, snapshot, arrString, layoutInflater, fragmentManager, context, refU, refE)
                            }
                        } else {
                            if (eventdate[0].toInt() == current_year) {
                                if (arrString.equals("weeks")) {
                                    var local = LocalDate.of(eventdate[0].toInt(), eventdate[1].toInt(), eventdate[2].toInt())
                                    val woy: TemporalField = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()
                                    if (local.get(woy) == relevantDatePart) {
                                       onEventViewChangedUpdateView(myContainer,i, snapshot, arrString, layoutInflater, fragmentManager, context, refU, refE)
                                    } else {
                                        myContainer.removeView(myContainer.getChildAt(i))
                                    }
                                } else {
                                    if (eventdate[DateIndex].toInt() == relevantDatePart) {
                                        onEventViewChangedUpdateView(myContainer,i, snapshot, arrString, layoutInflater, fragmentManager, context, refU, refE)
                                    } else {
                                        myContainer.removeView(myContainer.getChildAt(i))
                                    }
                                }
                            }
                        }
                        UITools.setWhoops(myContainer, whoops)
                        break
                    }
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                progressBar.visibility = View.VISIBLE
                for (i in 0..myContainer.childCount - 1) {
                    if (myContainer.getChildAt(i).findViewById<TextView>(R.id.idCon).text.toString() == p0.key.toString()) {
                        myContainer.removeView(myContainer.getChildAt(i))
                        Toast.makeText(context, p0.child("title").value.toString() + " was removed", Toast.LENGTH_SHORT).show()
                        break
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

        fun setFoodChildListener(
            progressBar: LottieAnimationView,
            myContainer: LinearLayout,
            layoutInflater: LayoutInflater,
            fragmentManager: FragmentManager,
            context: Context,
            ref: String
        ) {
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
                        fragmentManager
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
                        context
                    )
                }
                else if (ref.equals("Agenda")){
                    UITools.createTopic(
                        snapshot.child("name").value.toString(),
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
                            myContainer.getChildAt(i).findViewById<TextView>(R.id.age).text = UITools.getAge(
                                birthday[2].toInt(),
                                birthday[1].toInt(),
                                birthday[0].toInt()
                            )
                            myContainer.getChildAt(i).findViewById<TextView>(R.id.birthday).text = bdate
                            context?.let { Glide.with(it).load(snapshot.child("url").value.toString()).into(
                                myContainer.getChildAt(i).findViewById<ImageView>(
                                    R.id.userImage
                                )
                            ) }

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

    fun setSwitchAndEditForCurrentUser(
        switch: Switch,
        parti: TextView,
        editIV: ImageView,
        createdTV: TextView,
        eventid: String,
        ref: String,
        context: Context,
        refE: String
    ){
         val getdata = object : ValueEventListener {
            val userid = auth.currentUser?.uid.toString()

            override fun onDataChange(p0: DataSnapshot) {
                var room: String = p0.child(userid).child("number").getValue() as String

                UITools.visivlityEditButton(room, editIV, createdTV)
                UICleaning.setSwitchStatusEvents(switch, room, parti)
                listenerOnChangeEvents(switch, room, eventid, parti, refE, context)
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
                }.addOnFailureListener { }

            }
        }
    }

    fun pushTopicToDatabase(ref: String, topic: Topic, context: Context, fragmentManager: FragmentManager){
        val topicId = generateID(ref)
        database.getReference(ref).child(topicId!!).setValue(topic)
            .addOnSuccessListener {
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, MeetingFragment()).addToBackStack(null).commit()
            }
            .addOnFailureListener {
                // Write failed
                Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show()
            }
    }

    fun checkIfCurrentUserExsist(applicationContext: Context, ref: String) {
        var getdata = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val userid = auth.currentUser?.uid
                if (userid != null) {
                    var roomnumber = p0.child(userid).child("number").getValue().toString()
                    if (roomnumber.isEmpty()) {
                        val intent = Intent(applicationContext, SignUpWithFacebookFragment::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        applicationContext.startActivity(intent)

                    } else {
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        applicationContext.startActivity(intent)
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {
                println("err")
            }
        }
        database.getReference(ref).addValueEventListener(getdata)
    }

    fun iniSpinGetArr(root: View, context: Context) {
        var getdata = object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(p0: DataSnapshot) {
                var spinArr = arrayListOf<String>()
                spinArr.add("None")

                for (i in p0.children){
                    var name = i.child("fname").getValue().toString().split(" ")
                    var room = i.child("number").getValue().toString()
                    var firtsName = name[0]

                    if (!firtsName.equals("null")) {
                        spinArr.add("$firtsName - $room")
                    }
                }
                UITools.iniSpinners(root, context, spinArr)

            }

            override fun onCancelled(p0: DatabaseError) {
            }
        }

        database.getReference("Users").addValueEventListener(getdata)
    }

    fun getUserName(id: String, toolbar: Toolbar){
        var getdata = object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(p0: DataSnapshot) {

                var name = p0.child(id).child("fname").getValue().toString().split(" ")
                var firstName = name[0]
                toolbar.title = "Hello $firstName"
                }
            override fun onCancelled(error: DatabaseError) {}

        }
        database.getReference("Users").addValueEventListener(getdata)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onEventViewChangedUpdateView(myContainer: LinearLayout, i: Int, snapshot: DataSnapshot, arrString: String, layoutInflater: LayoutInflater, fragmentManager: FragmentManager, context: Context, refU:String, refE: String){
        myContainer.removeView(myContainer.getChildAt(i))
        UITools.eventDateCall(snapshot, arrString, myContainer, layoutInflater, fragmentManager, context, refU, refE)
        for (k in 0..myContainer.childCount - 1) {
            if (myContainer.getChildAt(k).findViewById<TextView>(R.id.idCon).text.toString() == snapshot.key.toString()) {
                UITools.expandListEvent((myContainer.getChildAt(k).findViewById<ConstraintLayout>(R.id.sumLayout)), (myContainer.getChildAt(k).findViewById<ImageButton>(R.id.expand)), (myContainer.getChildAt(k).findViewById<ConstraintLayout>(R.id.colorShow)), (myContainer.getChildAt(k).findViewById<ConstraintLayout>(R.id.colorShowExand)))
                break
            }
        }
    }

}