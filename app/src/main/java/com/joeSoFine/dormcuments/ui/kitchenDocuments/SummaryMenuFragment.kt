package com.joeSoFine.dormcuments.ui.kitchenDocuments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.*
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.joeSoFine.dormcuments.R
import com.joeSoFine.dormcuments.UITools
import com.nambimobile.widgets.efab.ExpandableFabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL

class SummaryMenuFragment : Fragment() {
    val arr = ArrayList<String>()
    val sumArr = ArrayList<SumObject>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_summary_menu, container, false)
        val list = root.findViewById<ListView>(R.id.list)
        val myWebView: WebView = root.findViewById(R.id.webs)
        val lottie = root.findViewById<LottieAnimationView>(R.id.animation_view)

        GlobalScope.launch(Dispatchers.IO) {
            val id = "12bpMAMO1x-nCekFFzxG2ZaIhHsM_rp5hPaMOIF7lzgw"
            val url = "https://docs.google.com/spreadsheets/d/$id/export?format=csv&id=$id"

            val csv = URL(url).readText().split("\n")

            for (i in 1..csv.size - 1){
                val title = csv[i].split(",")[1]
                val sumUrl = csv[i].split(",")[3]
                val lastUpdate = csv[i].split(",")[5]

                val sum = SumObject(title, sumUrl, lastUpdate)
                sumArr.add(sum)
                arr.add(title)
            }

            activity?.runOnUiThread(Runnable {
                val adapter = adapter_list(requireContext(), sumArr)

                list.adapter = adapter

                list.setOnItemClickListener { parent, view, position, id ->
                    Toast.makeText(context, sumArr[position].title, Toast.LENGTH_SHORT).show()

                    myWebView.loadUrl(sumArr[position].url)

                }
                lottie.visibility = View.GONE

            })

        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        UITools.setUpBasicToolbar(view, requireContext(), R.string.helpDialogTitleSum, R.string.helpDialogMsgSum)
    }


}