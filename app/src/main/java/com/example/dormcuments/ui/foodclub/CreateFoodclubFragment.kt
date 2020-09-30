package com.example.dormcuments.ui.foodclub

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.dormcuments.R
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_create_foodclub.*
import java.util.*

class CreateFoodclubFragment : Fragment() , View.OnClickListener{

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_create_foodclub, container, false)

        val datePicker = root.findViewById<DatePicker>(R.id.datePicker)

        val today = Calendar.getInstance()
        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH))

        { view, year, month, day ->
            val month = month + 1
            val msg = "$day/$month/$year"
            root.findViewById<EditText>(R.id.date2).setText(msg)
            datePicker.visibility = View.GONE
        }



        root.findViewById<EditText>(R.id.date2).setOnTouchListener { v, event ->
            if (MotionEvent.ACTION_UP == event.action) {
                datePicker.visibility = View.VISIBLE
            }
            true
        }

        val myAdapter = ArrayAdapter(requireContext(), R.layout.spinner_layout, resources.getStringArray(R.array.spinner_cooks))
        myAdapter.setDropDownViewResource(R.layout.spinner_layout)
        root.findViewById<Spinner>(R.id.room_spinner2).adapter = myAdapter
        root.findViewById<Spinner>(R.id.room_spinner3).adapter = myAdapter

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

    override fun onClick(p0: View?) {
    }

}