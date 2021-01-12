package com.joeSoFine.dormapp.ui.rules

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.ProgressBar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.joeSoFine.dormcuments.R
import com.joeSoFine.dormcuments.UITools
import com.nambimobile.widgets.efab.ExpandableFab
import kotlinx.coroutines.*
import java.net.URL
import java.util.ArrayList

class  RulesFragment : Fragment() {
    var ruleArr: ArrayList<String> = ArrayList()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_rules, container, false)

        val lottie = root.findViewById<LottieAnimationView>(R.id.animation_view)

        ruleArr.clear()


        GlobalScope.launch(Dispatchers.IO) {
            val id = "1TM-GdlklF1xUKt0mgrkuQ47eCrFGgQSGkpM2q2u-ZCg"
            val url = "https://docs.google.com/spreadsheets/d/$id/export?format=csv&id=$id"

            val csv = URL(url).readText().split('\n')

            withContext(Dispatchers.Main){

                for (i: Int in 0..csv.size-1) {
                    val rule = csv[i].replace("\"", "")//.split(",")[0]
                    ruleArr.add(rule)
                    lottie.visibility = View.GONE

                }

                if (ruleArr.size != 0) {
                    val adaptor = context?.let { adapter_rules(it, ruleArr) }
                    root.findViewById<ListView>(R.id.list).adapter = adaptor
                }
            }
        }

        root.findViewById<ImageView>(R.id.question).setOnClickListener{
            UITools.onHelpedClicked(requireContext(),R.string.helpDialogTitleRules, R.string.helpDialogMsgRules)
        }



        return root
    }


}

