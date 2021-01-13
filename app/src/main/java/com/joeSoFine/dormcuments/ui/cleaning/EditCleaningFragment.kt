package com.joeSoFine.dormcuments.ui.cleaning

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.joeSoFine.dormcuments.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.joeSoFine.dormcuments.UICleaning
import com.joeSoFine.dormcuments.databaseService
import com.joeSoFine.dormcuments.UITools

class EditCleaningFragment : Fragment() {
    var database = FirebaseDatabase.getInstance().getReference("Cleaning")
    lateinit var getdata : ValueEventListener
    var unform = ""
    val ref = "Cleaning"

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_create_cleaning, container, false)
        val bundle = this.arguments
        var cleaningid = bundle?.getString("id")
        root.findViewById<ImageView>(R.id.delete).visibility = View.VISIBLE
        val spinner_c1 = root.findViewById<Spinner>(R.id.spinner_c1)
        val spinner_c2 = root.findViewById<Spinner>(R.id.spinner_c2)
        val date2 = root.findViewById<EditText>(R.id.date2)
        val task = root.findViewById<EditText>(R.id.task)
        val note = root.findViewById<EditText>(R.id.note)
        val stats = root.findViewById<TextView>(R.id.stats)
        val unf = root.findViewById<TextView>(R.id.unf)
        val succes = root.findViewById<LottieAnimationView>(R.id.succes)
        val fail = root.findViewById<LottieAnimationView>(R.id.fail)

        databaseService.iniSpinGetArr(root,requireContext())
        database.addValueEventListener(databaseService.setEventListener(
            root,
            cleaningid!!,
            spinner_c1,
            spinner_c2,
            date2,
            task,
            note,
            stats,
            unf
        ))

        UITools.setUpDatepicker(root, unf)
        UICleaning.onTaskClicked(root, root.findViewById(R.id.switchH))
        UITools.onDeleteClicked(root, requireContext(), cleaningid, ref, requireFragmentManager())

        root.findViewById<Button>(R.id.save).setOnClickListener {
            val cleaning = Cleaning(
                spinner_c1.selectedItem.toString(),
                spinner_c2.selectedItem.toString(),
                date2.text.toString(),
                task.text.toString(),
                note.text.toString(),
                stats.text.toString(),
                unf.text.toString()
            )

            UICleaning.onCleaningSavedClick(cleaningid,
                ref,
                spinner_c1,
                spinner_c2,
                date2,
                cleaning,
                requireContext(),
                requireFragmentManager(),
                succes, fail)
        }


        return root
    }



}