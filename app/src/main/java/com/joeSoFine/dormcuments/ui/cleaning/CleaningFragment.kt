package com.joeSoFine.dormcuments.ui.cleaning

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.doOnAttach
import com.joeSoFine.dormcuments.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.joeSoFine.dormcuments.UITools
import com.joeSoFine.dormcuments.databaseService
import java.time.LocalDate

class CleaningFragment : Fragment() {
    val ref = "Cleaning"
    lateinit var myContainer: LinearLayout

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_cleaning, container, false)
        myContainer = root.findViewById(R.id.LinScroll)
        var progressBar = root.findViewById<ProgressBar>(R.id.progressBar3)
        progressBar.visibility = View.VISIBLE

        databaseService.setFoodChildListener(progressBar, myContainer, layoutInflater, requireFragmentManager(), requireContext(), ref )


        root.findViewById<FloatingActionButton>(R.id.add3).setOnClickListener {
            requireFragmentManager().beginTransaction().add(R.id.nav_host_fragment, CreateCleaningFragment()).addToBackStack(null).commit()
        }

        root.findViewById<ImageView>(R.id.question).setOnClickListener{
            UITools.onHelpedClicked(requireContext(),R.string.helpDialogTitleCleaning, R.string.helpDialogMsgCleaning)
        }
        return root
    }



}