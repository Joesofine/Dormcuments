package com.example.dormcuments.ui.foodclub

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.dormcuments.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CreateFoodclubFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_create_foodclub, container, false)

        root.findViewById<Button>(R.id.save).setOnClickListener {
            requireFragmentManager().beginTransaction().add(
                R.id.nav_host_fragment,
                FoodclubFragment()
            ).addToBackStack(null).commit()
        }

        root.findViewById<Button>(R.id.cancel).setOnClickListener {
            requireFragmentManager().beginTransaction().add(
                R.id.nav_host_fragment,
                FoodclubFragment()
            ).addToBackStack(null).commit()
        }

        return root
    }
}