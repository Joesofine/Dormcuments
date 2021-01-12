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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.joeSoFine.dormcuments.R
import com.joeSoFine.dormcuments.databaseService
import com.joeSoFine.dormcuments.UITools

class ShoppingFragment : Fragment() {
    lateinit var myContainer: LinearLayout
    val ref = "Shoppinglist"

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_shopping, container, false)
        myContainer = root.findViewById(R.id.LinScroll)
        var progressBar = root.findViewById<ProgressBar>(R.id.progressBar7)
        progressBar.visibility = View.VISIBLE

        databaseService.setShopChildListener(progressBar, myContainer, layoutInflater, requireContext(), ref )

        root.findViewById<FloatingActionButton>(R.id.add).setOnClickListener {
            UITools.addItemDialog(requireContext(), layoutInflater, requireFragmentManager(), ref)
        }

        root.findViewById<ImageView>(R.id.question).setOnClickListener{
            UITools.onHelpedClicked(requireContext(), R.string.helpDialogTitleGrocery, R.string.helpDialogMsgGrocery)
        }

        return root
    }

}