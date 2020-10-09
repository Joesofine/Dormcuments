package com.example.dormcuments.ui.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.dormcuments.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_add_shop_item.*

class AddShopItem : Fragment() {
    var database = FirebaseDatabase.getInstance().getReference("Shoppinglist")
    lateinit var myContainer: LinearLayout
    var count: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_add_shop_item, container, false)

        myContainer = root.findViewById(R.id.LinScroll)

        root.findViewById<ImageButton>(R.id.addItem).setOnClickListener {
            if (count > 0) {
                root.findViewById<ImageButton>(R.id.addItem).isEnabled = false
            } else {
                root.findViewById<ImageButton>(R.id.addItem).isEnabled = true
                val item = inputItem.text.toString()

                if (item.isEmpty()) {
                    inputItem.error = "Please input a product"

                } else {

                    val itemId = database.push().key
                    val product = Item(item)

                    if (itemId != null) {

                        database.child(itemId).setValue(product)
                            .addOnSuccessListener {
                                count = 1
                                Toast.makeText(context, "Item has been added", Toast.LENGTH_SHORT)
                                    .show()
                                createCon(myContainer)
                            }
                            .addOnFailureListener {
                                // Write failed
                                Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show()
                            }

                    }
                }
            }
        }
        return root
    }
    private fun createCon(my_Container: LinearLayout){
        val ExpandableCardview: View =
            layoutInflater.inflate(R.layout.layout_add_item, null, false)

        var add: ImageView = ExpandableCardview.findViewById(R.id.addItem2)
        var inputItem: EditText = ExpandableCardview.findViewById(R.id.inputItem2)
        var c = 0

        inputItem.requestFocus()


        add.setOnClickListener {
            if (c > 0) {
                add.isEnabled = false
            } else {
                val item = inputItem.text.toString()

                if (item.isEmpty()) {
                    inputItem.error = "Please input a product"

                } else {

                    val itemId = database.push().key
                    val product = Item(item)

                    if (itemId != null) {

                        database.child(itemId).setValue(product)
                            .addOnSuccessListener {
                                c = 1
                                Toast.makeText(context, "Item has been added", Toast.LENGTH_SHORT)
                                    .show()
                                createCon(myContainer)
                                //requireFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, ShoppingFragment()).addToBackStack(null).commit()


                            }
                            .addOnFailureListener {
                                // Write failed
                                Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
        }
        my_Container.addView(ExpandableCardview)
    }
}