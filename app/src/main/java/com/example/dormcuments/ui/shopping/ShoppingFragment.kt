package com.example.dormcuments.ui.shopping

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnAttach
import androidx.fragment.app.Fragment
import com.example.dormcuments.AddShopItem
import com.example.dormcuments.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_shop_meet.*
import java.util.*


class ShoppingFragment : Fragment() {

    var shop_arr: ArrayList<Item> = ArrayList()
    var database = FirebaseDatabase.getInstance().getReference("Shoppinglist")
    val product = item_
    lateinit var getdata : ValueEventListener;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_shop_meet, container, false)

        shop_arr.clear()

        getdata = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                shop_arr.clear()

                for (i in p0.children) {
                    var name1: String = i.child("name").getValue() as String

                    shop_arr.add(Item(name1))
                    product.setShopArr(shop_arr, context)
                    shop_arr = product.getShopArr(context)!!
                    if (shop_arr.size != 0) {
                        val adaptor = context?.let { adapter_shop(it, shop_arr) }
                        list.adapter = adaptor
                    }

                }
            }



            override fun onCancelled(p0: DatabaseError) {
                println("err")
            }



        }

        database.addValueEventListener(getdata)
        database.addListenerForSingleValueEvent(getdata)

        Handler().postDelayed(Runnable {

        }, 0)



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

    };



}