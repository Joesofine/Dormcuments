package com.example.dormcuments.ui.more

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.dormapp.ui.rules.RulesFragment
import com.example.dormcuments.R
import com.example.dormcuments.ui.agenda.MeetingFragment
import com.example.dormcuments.ui.cleaning.CleaningFragment
import com.example.dormcuments.ui.residents.ResidentFragment

class MoreFragment : Fragment(),View.OnClickListener  {
    private lateinit var rules_button: Button
    private lateinit var residents_button: Button
    private lateinit var cleaning_button: Button
    private lateinit var meeting_button: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_more, container, false)

        rules_button = root.findViewById(R.id.rules_button) as Button;
        residents_button = root.findViewById(R.id.residents_button) as Button;
        cleaning_button = root.findViewById(R.id.cleaning_button) as Button;
        meeting_button = root.findViewById(R.id.meeting_button) as Button;

        rules_button.setOnClickListener(this)
        residents_button.setOnClickListener(this)
        cleaning_button.setOnClickListener(this)
        meeting_button.setOnClickListener(this)

        return  root
    }

    override fun onClick(p0: View?) {

        if (p0 === cleaning_button || p0 === residents_button || p0 === meeting_button || p0 === rules_button ) {
            when {
                p0 === cleaning_button -> {
                    requireFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, CleaningFragment()).addToBackStack(null).commit()
                }
                p0 === rules_button -> {
                    requireFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, RulesFragment()).addToBackStack(null).commit()
                }
                p0 === residents_button -> {
                    requireFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, ResidentFragment()).addToBackStack(null).commit()
                }
                p0 === meeting_button -> {
                    requireFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, MeetingFragment()).addToBackStack(null).commit()
                }
            }
        }
    }
}