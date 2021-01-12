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
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.joeSoFine.dormcuments.*
import com.joeSoFine.dormcuments.ui.residents.profileFragment
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
        SmartTools.setUpOnBackPressed(requireActivity())

        myContainer = root.findViewById(R.id.LinScroll)
        val lottie = root.findViewById<LottieAnimationView>(R.id.animation_view)

       databaseService.setFoodChildListener(lottie,myContainer, layoutInflater, requireFragmentManager(), requireContext(), ref )

        root.findViewById<FloatingActionButton>(R.id.add2).setOnClickListener {
            requireFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, CreateFoodclubFragment()).addToBackStack(null).commit()
        }

        root.findViewById<ImageView>(R.id.question).setOnClickListener{
            UITools.onHelpedClicked(requireContext(),R.string.helpDialogTitleFoodclub, R.string.helpDialogMsgFoodclub)
        }
        return root
    }
}