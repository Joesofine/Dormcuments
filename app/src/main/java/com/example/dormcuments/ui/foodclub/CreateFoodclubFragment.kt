package com.example.dormcuments.ui.foodclub

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.dormcuments.R
import kotlinx.android.synthetic.main.fragment_create_foodclub.*
import java.util.*

class CreateFoodclubFragment : Fragment() , View.OnClickListener{

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_create_foodclub, container, false)






        root.findViewById<EditText>()

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
    private fun showDateDialog() {
        val datePickerDialog = DatePickerDialog(
            requireContext(), DatePickerDialog.OnDateSetListener(),
            Calendar.getInstance()[Calendar.YEAR],
            Calendar.getInstance()[Calendar.MONTH],
            Calendar.getInstance()[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.show()
    }
    fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        val date = Date()
        //Minus because of Java API
        date.year = year - 1900
        date.date = day
        date.month = month
        date.minutes = 0
        date.hours = 0
        date.seconds = 0
        if (date.time <= System.currentTimeMillis()) {
            //date.setText(childInfo.getMonthText(month + 1).toString() + " " + day + " " + year)
            //currentDate = date.time
            date2.setText(null)
        } else {
            Toast.makeText(context, "Not a valid date", Toast.LENGTH_LONG).show()
        }
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

}