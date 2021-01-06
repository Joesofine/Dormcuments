package com.joeSoFine.dormcuments.ui.foodclub

import FoodDetailsFragment
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.doOnAttach
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.joeSoFine.dormcuments.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.joeSoFine.dormcuments.UITools
import com.joeSoFine.dormcuments.databaseService
import java.time.LocalDate


class FoodclubFragment : Fragment() {
    lateinit var myContainer: LinearLayout
    lateinit var unform: String
    val ref = "Foodclub"

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_foodclub, container, false)
        myContainer = root.findViewById(R.id.LinScroll)
        var progressBar = root.findViewById<ProgressBar>(R.id.progressBar4)
        progressBar.visibility = View.VISIBLE

       databaseService.setFoodChildListener(progressBar,myContainer, layoutInflater, requireFragmentManager(), requireContext(), ref )

        root.findViewById<FloatingActionButton>(R.id.add2).setOnClickListener {
            requireFragmentManager().beginTransaction().add(R.id.nav_host_fragment, CreateFoodclubFragment()).addToBackStack(null).commit()
        }

        root.findViewById<ImageView>(R.id.question).setOnClickListener{
            UITools.onHelpedClicked(requireContext(),R.string.helpDialogTitleFoodclub, R.string.helpDialogMsgFoodclub)
        }
        return root
    }
}