package com.example.dormcuments.ui.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dormcuments.AddShopItem
import com.example.dormcuments.R
import com.example.dormcuments.ui.cleaning.CleaningFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ShoppingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_shop_meet, container, false)


        root.findViewById<FloatingActionButton>(R.id.add).setOnClickListener {
            requireFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, AddShopItem()).addToBackStack(null).commit()
        }
        return root
    }
}