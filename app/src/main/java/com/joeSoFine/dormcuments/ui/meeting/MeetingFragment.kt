package com.joeSoFine.dormcuments.ui.meeting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnAttach
import androidx.fragment.app.Fragment
import com.joeSoFine.dormcuments.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.joeSoFine.dormcuments.UITools
import com.joeSoFine.dormcuments.databaseService

class MeetingFragment : Fragment() {
    lateinit var myContainer: LinearLayout
    val ref = "Agenda"

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var root = inflater.inflate(R.layout.fragment_meeting, container, false)
        myContainer = root.findViewById(R.id.LinScroll)
        var progressBar = root.findViewById<ProgressBar>(R.id.progressBar5)
        progressBar.visibility = View.VISIBLE

        databaseService.setFoodChildListener(progressBar,myContainer,layoutInflater, requireFragmentManager(), requireContext(), ref)

        root.findViewById<FloatingActionButton>(R.id.add).setOnClickListener {
            requireFragmentManager().beginTransaction().add(
                R.id.nav_host_fragment,
                AddTopic()
            ).addToBackStack(null).commit()

        }

        root.findViewById<ImageView>(R.id.question).setOnClickListener{
            UITools.onHelpedClicked(requireContext(),R.string.helpDialogTitleMeetings, R.string.helpDialogMsgMeetings)
        }

        root.findViewById<ImageView>(R.id.documents).setOnClickListener{
            requireFragmentManager().beginTransaction().add(
                R.id.nav_host_fragment,
                SummaryMenuFragment()
            ).addToBackStack(null).commit()
        }

        return root
    }


}