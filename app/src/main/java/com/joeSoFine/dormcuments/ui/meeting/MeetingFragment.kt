package com.joeSoFine.dormcuments.ui.meeting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnAttach
import androidx.fragment.app.Fragment
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

class MeetingFragment : Fragment(), View.OnClickListener {
    lateinit var myContainer: LinearLayout
    val ref = "Agenda"

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var root = inflater.inflate(R.layout.fragment_meeting, container, false)
        myContainer = root.findViewById(R.id.LinScroll)
        val lottie = root.findViewById<LottieAnimationView>(R.id.animation_view)

        databaseService.setFoodChildListener(lottie,myContainer,layoutInflater, requireFragmentManager(), requireContext(), ref)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val expandableFabLayout = view.findViewById<ExpandableFabLayout>(R.id.fab_layout)
        expandableFabLayout.portraitConfiguration.fabOptions.forEach { it.setOnClickListener(this) }

        UITools.setUpBasicToolbar(view, requireContext(), R.string.helpDialogTitleFoodclub, R.string.helpDialogMsgFoodclub)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.option1 -> { UITools.addItemDialog(requireContext(), layoutInflater, requireFragmentManager(), ref)}
            R.id.option2 -> { UITools.onHelpedClicked(requireContext(),R.string.helpDialogTitleMeetings, R.string.helpDialogMsgMeetings)}
        }
    }


}