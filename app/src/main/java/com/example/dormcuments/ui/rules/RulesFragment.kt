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

    private val ruleArr = ArrayList<String>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val context: Context = requireContext()

        val root = inflater.inflate(R.layout.fragment_rules, container, false)
        val list = root.findViewById<ListView>(R.id.rules_list)

        hentOrdFraRegneark(ruleArr)

        val itemsAdapter = ArrayAdapter(context, R.layout.rules_list_layout, R.id.rules_list_text, ruleArr)


        list.adapter = itemsAdapter

        return root
    }



    @Throws(IOException::class)
    fun hentOrdFraRegneark(arr: ArrayList<String>) {
        val logic = Logic.create()

        val id = "1aqOWQW5Jw5qdq9zBefOwwOLLX5Oa45-9aux_ORUUOq0"
        println("Henter data som kommasepareret CSV fra regnearket https://docs.google.com/spreadsheets/d/$id/edit?usp=sharing")
        val data: String? = logic.hentUrl("https://docs.google.com/spreadsheets/d/$id/export?format=csv&id=0")

        arr.clear()
        for (line in data?.split("\n".toRegex())?.toTypedArray()!!) {
            val felter = line.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray() // -1 er for at beholde tomme indgange, f.eks. bliver ",,," splittet i et array med 4 tomme strenge
            val rule = felter[0].trim { it <= ' ' }.toLowerCase()
            if (rule.isEmpty()) continue  // spring over linjer med tomme ord
            arr.add(rule)
        }
        println("Rules = $arr")
    }
}
