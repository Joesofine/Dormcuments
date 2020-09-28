package com.example.dormcuments.ui.meeting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.example.dormcuments.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_add_shop_item.inputItem
import kotlinx.android.synthetic.main.fragment_add_topic.*


class AddTopic : Fragment() {

    var database = FirebaseDatabase.getInstance().getReference("Agenda")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_add_topic, container, false)
         root.findViewById<ImageButton>(R.id.addItem).setOnClickListener {

            val topic = inputItem.text.toString()
            val des = sum.text.toString()

            if (topic.isEmpty()) {
                inputItem.error = "Please input a product"

            } else if (des.isEmpty()){
                sum.error = "Please add a description"

            } else {

                val topicId = database.push().key
                val top = Topic(topic, des)

                if (topicId != null) {

                    database.child(topicId).setValue(top)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Topic has been added", Toast.LENGTH_SHORT).show()
                            requireFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, MeetingFragment()).addToBackStack(null).commit()


                        }
                        .addOnFailureListener {
                            // Write failed
                            Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }

        return root
    }
}