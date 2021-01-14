package com.joeSoFine.dormcuments.ui.residents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnAttach
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.joeSoFine.dormcuments.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.joeSoFine.dormcuments.UITools
import com.joeSoFine.dormcuments.databaseService
import java.util.*

class ResidentFragment : Fragment() {
    val ref = "Users"

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_residents, container, false)
        val lottie = root.findViewById<LottieAnimationView>(R.id.animation_view)
        var residentLayout = root.findViewById<LinearLayout>(R.id.residentLayout)

        databaseService.setFoodChildListener(lottie, residentLayout, layoutInflater, requireFragmentManager(), requireContext(), ref)
        //UITools.onHelpedClicked(requireContext(),R.string.helpDialogTitleResident, R.string.helpDialogMsgResident)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        UITools.setUpBasicToolbar(view, requireContext(), R.string.helpDialogTitleResident, R.string.helpDialogMsgResident)
    }

}