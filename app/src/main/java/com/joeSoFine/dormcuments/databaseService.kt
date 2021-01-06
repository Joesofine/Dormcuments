package com.joeSoFine.dormcuments

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.joeSoFine.dormcuments.ui.cleaning.Cleaning
import com.joeSoFine.dormcuments.ui.foodclub.Foodclub
import com.joeSoFine.dormcuments.ui.shopping.Item

object databaseService {
    var database = FirebaseDatabase.getInstance()
    var c = 0


    public fun generateID(ref: String): String? {
        val id = database.getReference(ref).push().key
        return id
    }

    public fun saveCleaningToDatabase(ref: String, id: String, clean: Cleaning, context: Context, frag: Fragment, fragmentManager: FragmentManager){
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
                UITools.setUpPreCleaning(root, clean, spinner1, spinner2, date, task, note, stat, unform)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        }
        return getdata
    }

    fun delteChildFromDatabase(id: String, ref: String, context: Context){
        var dName = database.getReference(ref).child(id)

        dName.removeValue()
        Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show()
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
                    UITools.createCleaningItem(
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
}