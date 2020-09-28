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
import kotlinx.coroutines.*
import java.io.IOException
import java.net.URL
import java.util.ArrayList

class RulesFragment : Fragment() {
    var ruleArr: ArrayList<String> = ArrayList()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_rules, container, false)


        ruleArr.clear()

        ruleArr.add("Ingen is")

        GlobalScope.launch(Dispatchers.IO) {
            val id = "1TM-GdlklF1xUKt0mgrkuQ47eCrFGgQSGkpM2q2u-ZCg"
            val url = "https://docs.google.com/spreadsheets/d/$id/export?format=csv&id=$id"

            val csv = URL(url).readText().split("\n")
            val kode = csv[1].split(",")[1]

            withContext(Dispatchers.Main){
                ruleArr.add("$kode er sej")
                if (ruleArr.size != 0) {
                    val adaptor = context?.let { adapter_rules(it, ruleArr) }
                    root.findViewById<ListView>(R.id.list).adapter = adaptor
                }
            }



        }





        //val context: Context = requireContext()
        //val list = root.findViewById<ListView>(R.id.fodd)

        //val itemsAdapter = ArrayAdapter(context, R.layout.rules_list_layout, R.id.rules_list_text, ruleArr)
        //list.adapter = itemsAdapter

        return root

    }

}

