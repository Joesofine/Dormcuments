package com.joeSoFine.dormcuments.ui.shopping

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.Html
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.joeSoFine.dormcuments.R
import com.joeSoFine.dormcuments.databaseService
import com.joeSoFine.dormcuments.UITools
import com.nambimobile.widgets.efab.ExpandableFabLayout

class ShoppingFragment : Fragment(), View.OnClickListener {
    lateinit var myContainer: LinearLayout
    val ref = "Shoppinglist"

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_shopping, container, false)
        myContainer = root.findViewById(R.id.LinScroll)
        val lottie = root.findViewById<LottieAnimationView>(R.id.animation_view)
        databaseService.setShopChildListener(lottie, myContainer, layoutInflater, requireContext(), ref )


        return root
    }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val expandableFabLayout = view.findViewById<ExpandableFabLayout>(R.id.fab_layout)
        expandableFabLayout.portraitConfiguration.fabOptions.forEach { it.setOnClickListener(this) }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.option1 -> { UITools.addItemDialog(requireContext(), layoutInflater, requireFragmentManager(), ref)}
        }
    }

}