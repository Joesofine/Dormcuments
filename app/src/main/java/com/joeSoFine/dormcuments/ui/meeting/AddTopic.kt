package com.joeSoFine.dormcuments.ui.meeting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import com.joeSoFine.dormcuments.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_add_topic.*


class AddTopic : Fragment() {

    var database = FirebaseDatabase.getInstance().getReference("Agenda")



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_topic, container, false)
    }

}