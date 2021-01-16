package com.joeSoFine.dormcuments.ui.meeting

import android.R.attr.actionBarSize
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.joeSoFine.dormcuments.R
import com.joeSoFine.dormcuments.UITools
import com.joeSoFine.dormcuments.databaseService
import com.nambimobile.widgets.efab.ExpandableFabLayout
import androidx.appcompat.widget.Toolbar


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

        databaseService.setFoodChildListener(lottie, myContainer, layoutInflater, requireFragmentManager(), requireContext(), ref)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val expandableFabLayout = view.findViewById<ExpandableFabLayout>(R.id.fab_layout)
        expandableFabLayout.portraitConfiguration.fabOptions.forEach { it.setOnClickListener(this) }

        val contextView = view.findViewById<View>(R.id.contextView)


        UITools.setUpBasicToolbar(view, contextView, requireContext(), R.string.helpDialogTitleMeetings, R.string.helpDialogMsgMeetings)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.option1 -> {
                UITools.addItemDialog(requireContext(), layoutInflater, requireFragmentManager(), ref)
            }
        }
    }


}