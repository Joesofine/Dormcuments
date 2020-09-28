package com.example.dormcuments.ui.foodclub

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dormcuments.R
import com.example.dormcuments.ui.shopping.AddShopItem
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FoodclubFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_foodclub, container, false)




        root.findViewById<FloatingActionButton>(R.id.add2).setOnClickListener {
            requireFragmentManager().beginTransaction().add(
                R.id.nav_host_fragment,
                CreateFoodclubFragment()
            ).addToBackStack(null).commit()
        }

        return root
    }
}