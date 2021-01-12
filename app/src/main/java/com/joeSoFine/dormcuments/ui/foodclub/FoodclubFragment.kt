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
import com.nambimobile.widgets.efab.ExpandableFabLayout
import java.time.LocalDate


class FoodclubFragment : Fragment(), View.OnClickListener {
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
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Although you can set onClickListener functionality for ExpandableFab widget views via
        // XML, Android limits us to defining their methods in the parent Activity. This has a
        // number of downsides when using Fragments, especially from a re-usability standpoint. A
        // better solution would be to implement View.OnClickListener on the Fragment, and define
        // the onClickListeners cleanly like below (see the onClick method for the rest)
        val expandableFabLayout = view.findViewById<ExpandableFabLayout>(R.id.fab_layout)

        expandableFabLayout.portraitConfiguration.fabOptions.forEach { it.setOnClickListener(this) }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.option1 -> { requireFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, CreateFoodclubFragment()).addToBackStack(null).commit()}
            R.id.option2 -> { UITools.onHelpedClicked(requireContext(),R.string.helpDialogTitleFoodclub, R.string.helpDialogMsgFoodclub)}
            // so on and so forth...
        }
    }
}