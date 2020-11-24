package com.joeSoFine.dormcuments

import android.content.Context
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.joeSoFine.dormcuments.ui.UITools
import com.joeSoFine.dormcuments.ui.cleaning.Cleaning

object databaseService {
    var database = FirebaseDatabase.getInstance()

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

    fun setEventListener(id: String, spinner1: Spinner, spinner2: Spinner, date: EditText, task: EditText, note: EditText, stat: TextView, unform: TextView): ValueEventListener {
        var getdata = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var clean = getDataFromDatabase(id, snapshot)
                UITools.setUpPreCleaning(clean, spinner1, spinner2, date, task, note, stat, unform)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        }
        return getdata
    }

    fun delteChildFromDatabase(id: String, ref: String, context: Context, fragmentManager: FragmentManager){
        var dName = database.getReference(ref).child(id)

        dName.removeValue()
        Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show()
        fragmentManager.popBackStack()
        fragmentManager.popBackStack()
    }
}