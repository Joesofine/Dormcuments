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
import com.joeSoFine.dormcuments.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.joeSoFine.dormcuments.databaseService
import com.joeSoFine.dormcuments.ui.UITools
import kotlinx.android.synthetic.main.fragment_create_cleaning.*

class EditCleaningFragment : Fragment() {
    var database = FirebaseDatabase.getInstance().getReference("Cleaning")
    lateinit var getdata : ValueEventListener
    var str = ""
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

        unform = unf.text.toString()
        unform = UITools.setUpDatepicker(root)
        UITools.iniSpinners(root,requireContext(), resources.getStringArray(R.array.spinner_cooks))
        UITools.onTaskClicked(root, root.findViewById(R.id.switchH))
        UITools.onDeleteClicked(root, requireContext(), cleaningid, ref, requireFragmentManager())

        root.findViewById<Button>(R.id.save).setOnClickListener {
            UITools.onCleaningSavedClick(cleaningid,
                ref,
                root,
                spinner_c1,
                spinner_c2,
                task,
                note,
                stats,
                unf,
                date2,
                requireContext(),
                requireFragmentManager())
        }


        return root
    }



}