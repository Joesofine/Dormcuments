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
import com.joeSoFine.dormcuments.UICleaning
import com.joeSoFine.dormcuments.databaseService
import com.joeSoFine.dormcuments.UITools


class CreateCleaningFragment() : Fragment() {
    var unform = ""
    val ref = "Cleaning"

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_create_cleaning, container, false)
        val spinner_c1 = root.findViewById<Spinner>(R.id.spinner_c1)
        val spinner_c2 = root.findViewById<Spinner>(R.id.spinner_c2)
        val date2 = root.findViewById<EditText>(R.id.date2)
        val task = root.findViewById<EditText>(R.id.task)
        val note = root.findViewById<EditText>(R.id.note)

        UICleaning.switchIni(root, task)
        unform = UITools.setUpDatepicker(root)
        UITools.iniSpinners(root,requireContext(),resources.getStringArray(R.array.spinner_cooks))
        UICleaning.onTaskClicked(root, root.findViewById(R.id.switchH))

        root.findViewById<Button>(R.id.save).setOnClickListener {
            val cleaning = Cleaning(
            spinner_c1.selectedItem.toString(),
            spinner_c2.selectedItem.toString(),
            date2.text.toString(),
            task.text.toString(),
            note.text.toString(),
            "Unchecked",
            unform
        )
            UICleaning.onCleaningSavedClick(
                databaseService.generateID(ref)!!,
                ref,
                spinner_c1,
                spinner_c2,
                date2,
                cleaning,
                requireContext(),
                requireFragmentManager())
        }



        return root
    }
}