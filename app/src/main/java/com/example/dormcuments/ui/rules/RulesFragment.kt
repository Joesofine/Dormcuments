package com.example.dormapp.ui.rules

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.dormcuments.Logic
import com.example.dormcuments.R
import java.io.IOException

class RulesFragment : Fragment() {

    //private val ruleArr = ArrayList<String>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        //val context: Context = requireContext()
        val root = inflater.inflate(R.layout.fragment_rules, container, false)
        //val list = root.findViewById<ListView>(R.id.fodd)

        //val itemsAdapter = ArrayAdapter(context, R.layout.rules_list_layout, R.id.rules_list_text, ruleArr)
        //list.adapter = itemsAdapter

        return root
    }
}
