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
import com.airbnb.lottie.LottieAnimationView
import com.joeSoFine.dormcuments.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.joeSoFine.dormcuments.UITools
import com.joeSoFine.dormcuments.databaseService
import com.nambimobile.widgets.efab.ExpandableFabLayout
import java.time.LocalDate

class CleaningFragment : Fragment(), View.OnClickListener {
    val ref = "Cleaning"
    lateinit var myContainer: LinearLayout

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_cleaning, container, false)
        myContainer = root.findViewById(R.id.LinScroll)
        val lottie = root.findViewById<LottieAnimationView>(R.id.animation_view)

        databaseService.setFoodChildListener(lottie, myContainer, layoutInflater, requireFragmentManager(), requireContext(), ref )
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val expandableFabLayout = view.findViewById<ExpandableFabLayout>(R.id.fab_layout)
        expandableFabLayout.portraitConfiguration.fabOptions.forEach { it.setOnClickListener(this) }

        UITools.setUpBasicToolbar(view, requireContext(), R.string.helpDialogTitleCleaning, R.string.helpDialogMsgCleaning)
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.option1 -> { requireFragmentManager().beginTransaction().add(R.id.nav_host_fragment, CreateCleaningFragment()).addToBackStack(null).commit() }
            R.id.option2 -> { UITools.onHelpedClicked(requireContext(),R.string.helpDialogTitleCleaning, R.string.helpDialogMsgCleaning)}
        }
    }



}