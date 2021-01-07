package com.joeSoFine.dormcuments.ui.meeting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.joeSoFine.dormcuments.R

//
class summaryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_summary, container, false)


        val myWebView: WebView = root.findViewById(R.id.webs)
        myWebView.loadUrl("https://docs.google.com/document/d/1lTWkgQlWZKZ4tTXg0x-kkE6xva4SVG4mMm4T2WzKkZE/edit")

        return root
    }


}