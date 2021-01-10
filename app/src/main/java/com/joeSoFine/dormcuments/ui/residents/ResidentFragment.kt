package com.joeSoFine.dormcuments.ui.residents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnAttach
import com.bumptech.glide.Glide
import com.joeSoFine.dormcuments.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.joeSoFine.dormcuments.UITools
import com.joeSoFine.dormcuments.databaseService
import java.util.*

class ResidentFragment : Fragment() {
    val ref = "Users"

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_residents, container, false)
        var progressBar = root.findViewById<ProgressBar>(R.id.progressBar6)
        var residentLayout = root.findViewById<LinearLayout>(R.id.residentLayout)
        progressBar.visibility = View.VISIBLE

        databaseService.setFoodChildListener(progressBar, residentLayout, layoutInflater, requireFragmentManager(), requireContext(), ref)

        root.findViewById<ImageView>(R.id.helpButton).setOnClickListener{
            UITools.onHelpedClicked(requireContext(),R.string.helpDialogTitleResident, R.string.helpDialogMsgResident)
        }

        return root
    }
}