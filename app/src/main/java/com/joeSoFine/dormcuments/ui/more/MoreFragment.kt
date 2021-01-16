package com.joeSoFine.dormcuments.ui.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.joeSoFine.dormapp.ui.rules.RulesFragment
import com.joeSoFine.dormcuments.R
import com.joeSoFine.dormcuments.SmartTools
import com.joeSoFine.dormcuments.ui.cleaning.CleaningFragment
import com.joeSoFine.dormcuments.ui.meeting.MeetingFragment
import com.joeSoFine.dormcuments.ui.kitchenDocuments.SummaryMenuFragment
import com.joeSoFine.dormcuments.ui.residents.ResidentFragment
import com.joeSoFine.dormcuments.ui.residents.profileFragment
import kotlinx.android.synthetic.main.fragment_more.*


class MoreFragment : Fragment(),View.OnClickListener  {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_more, container, false)
        SmartTools.setUpOnBackPressed(requireActivity())

        val rules_button = root.findViewById<ConstraintLayout>(R.id.rules_button)
        val residents_button = root.findViewById<ConstraintLayout>(R.id.residents_button)
        val cleaning_button = root.findViewById<ConstraintLayout>(R.id.cleaning_button)
        val meeting_button = root.findViewById<ConstraintLayout>(R.id.meeting_button)
        val profile_button = root.findViewById<ConstraintLayout>(R.id.profile_button)
        val inventory_button = root.findViewById<ConstraintLayout>(R.id.inventory_button)

        rules_button.setOnClickListener(this)
        residents_button.setOnClickListener(this)
        cleaning_button.setOnClickListener(this)
        meeting_button.setOnClickListener(this)
        profile_button.setOnClickListener(this)
        inventory_button.setOnClickListener(this)


        return  root
    }

    override fun onClick(p0: View?) {

        if (p0 === cleaning_button || p0 === residents_button || p0 === meeting_button || p0 === rules_button || p0 === profile_button || p0 === inventory_button ) {
            when {
                p0 === cleaning_button -> {
                    requireFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out).replace(R.id.nav_host_fragment, CleaningFragment()).addToBackStack(null).commit()
                }
                p0 === rules_button -> {
                    requireFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out).replace(R.id.nav_host_fragment, SummaryMenuFragment()).addToBackStack(null).commit()
                }
                p0 === residents_button -> {
                    requireFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out).replace(R.id.nav_host_fragment, ResidentFragment()).addToBackStack(null).commit()
                }
                p0 === meeting_button -> {
                    requireFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out).replace(R.id.nav_host_fragment, MeetingFragment()).addToBackStack(null).commit()
                }
                p0 === profile_button -> {
                    requireFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out).replace(R.id.nav_host_fragment, profileFragment()).addToBackStack(null).commit()
                }
                p0 === inventory_button -> {
                    requireFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out).replace(R.id.nav_host_fragment, RulesFragment()).addToBackStack(null).commit()
                }
            }
        }
    }
}