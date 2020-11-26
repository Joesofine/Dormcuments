package com.joeSoFine.dormcuments.ui.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.joeSoFine.dormapp.ui.rules.RulesFragment
import com.joeSoFine.dormcuments.BaseBackPressedListener
import com.joeSoFine.dormcuments.MainActivity
import com.joeSoFine.dormcuments.R
import com.joeSoFine.dormcuments.ui.cleaning.CleaningFragment
import com.joeSoFine.dormcuments.ui.meeting.MeetingFragment
import com.joeSoFine.dormcuments.ui.residents.ResidentFragment
import com.joeSoFine.dormcuments.ui.residents.profileFragment
import com.joeSoFine.dormcuments.ui.shopping.InventoryShoppingFragment
import kotlinx.android.synthetic.main.fragment_more.*


class MoreFragment : Fragment(),View.OnClickListener  {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_more, container, false)
        var activity = activity
        (activity as MainActivity).setOnBackPressedListener(BaseBackPressedListener(activity))


        val rules_button = root.findViewById(R.id.rules_button) as Button;
        val residents_button = root.findViewById(R.id.residents_button) as Button;
        val cleaning_button = root.findViewById(R.id.cleaning_button) as Button;
        val meeting_button = root.findViewById(R.id.meeting_button) as Button;
        val profile_button = root.findViewById(R.id.profile_button) as Button;
        val inventory_button = root.findViewById(R.id.inventory_button) as Button;

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
                p0 === profile_button -> {
                    requireFragmentManager().beginTransaction().add(R.id.nav_host_fragment, profileFragment()).addToBackStack(null).commit()
                }
                p0 === inventory_button -> {
                    requireFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, InventoryShoppingFragment()).addToBackStack(null).commit()
                }
            }
        }
    }
}