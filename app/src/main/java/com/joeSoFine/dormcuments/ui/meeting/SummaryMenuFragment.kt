package com.joeSoFine.dormcuments.ui.meeting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.joeSoFine.dormcuments.R

class SummaryMenuFragment : Fragment() {
    val arr = ArrayList<String>()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_summary_menu, container, false)
        val list = root.findViewById<ListView>(R.id.list)
        arr.add("0-1/01/01")
        arr.add("0-2/02/02")
        arr.add("0-3/03/03")
        arr.add("0-4/04/04")





        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(), R.layout.listeelement_simple,
            R.id.listeelem_overskrift, arr
        )

        list.adapter = adapter

        return root
    }


}