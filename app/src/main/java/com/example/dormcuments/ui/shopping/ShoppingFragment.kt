package com.example.dormcuments.ui.shopping

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnAttach
import androidx.fragment.app.Fragment
import com.example.dormcuments.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_shopping.*
import kotlinx.android.synthetic.main.fragment_shopping.*
import kotlinx.android.synthetic.main.list_element_shopping.*
import java.util.*


class ShoppingFragment : Fragment() {

    var shop_arr: ArrayList<Item> = ArrayList()
    var database = FirebaseDatabase.getInstance().getReference("Shoppinglist")
    lateinit var getdata : ValueEventListener
    lateinit var myContainer: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_shopping, container, false)

        myContainer = root.findViewById(R.id.LinScroll)

        getdata = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                for (i in p0.children) {
                    var name1: String = i.child("name").getValue() as String

                    createTopic(name1, myContainer)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                println("err")
            }
        }

        database.addValueEventListener(getdata)
        database.addListenerForSingleValueEvent(getdata)

        root.doOnAttach { database.removeEventListener(getdata) }

        root.findViewById<FloatingActionButton>(R.id.add).setOnClickListener {
            requireFragmentManager().beginTransaction().add(
                R.id.nav_host_fragment,
                AddShopItem()
            ).addToBackStack(null).commit()


        }
        return root
    }

    override fun onDetach() {
        super.onDetach()
        database.removeEventListener(getdata)

    }

    private fun createTopic(name: String, myContainer: LinearLayout){

        val ExpandableCardview: View =
            layoutInflater.inflate(R.layout.list_element_shopping, null, false)


        var delete: ImageView = ExpandableCardview.findViewById(R.id.delete)
        var shoppingItem: TextView = ExpandableCardview.findViewById(R.id.shoppingItem)

        shoppingItem.setText(name)

        delete.setOnClickListener {
            Toast.makeText(context, "Delete!", Toast.LENGTH_SHORT).show()
        }

        myContainer.addView(ExpandableCardview)
    }



}