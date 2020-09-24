package com.example.dormcuments

import android.content.Intent
import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import com.example.dormcuments.ui.cleaning.CleaningFragment
import com.example.dormcuments.ui.shopping.Item
import com.example.dormcuments.ui.shopping.ShoppingFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_add_shop_item.*

class AddShopItem : Fragment() {
    var database = FirebaseDatabase.getInstance().getReference("Shoppinglist")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_add_shop_item, container, false)

        root.findViewById<ImageButton>(R.id.addItem).setOnClickListener {
            val item = inputItem.text.toString()

            if (item.isEmpty()) {
                inputItem.error = "Please input a product"

            } else {

                val itemId = database.push().key
                val product = Item(item)

                if (itemId != null) {

                    database.child(itemId).setValue(product)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Item has been added", Toast.LENGTH_SHORT).show()
                            requireFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, ShoppingFragment()).addToBackStack(null).commit()


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