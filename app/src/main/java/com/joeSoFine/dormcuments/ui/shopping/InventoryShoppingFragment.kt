package com.joeSoFine.dormcuments.ui.shopping

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
import com.joeSoFine.dormcuments.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.joeSoFine.dormcuments.databaseService
import com.joeSoFine.dormcuments.UITools
import com.nambimobile.widgets.efab.ExpandableFabLayout

class InventoryShoppingFragment : Fragment(), View.OnClickListener {
    var database = FirebaseDatabase.getInstance().getReference("Inventory")
    lateinit var getdata : ValueEventListener
    lateinit var myContainer: LinearLayout
    val ref = "Inventory"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_shopping, container, false)
        myContainer = root.findViewById(R.id.LinScroll)
        val lottie = root.findViewById<LottieAnimationView>(R.id.animation_view)
        databaseService.setShopChildListener(lottie, myContainer, layoutInflater, requireContext(), ref )

        if (myContainer.childCount == 0){
            lottie.visibility = View.GONE
        }
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