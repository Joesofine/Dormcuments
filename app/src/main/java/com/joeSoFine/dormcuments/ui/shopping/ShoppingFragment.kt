package com.joeSoFine.dormcuments.ui.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.doOnAttach
import androidx.fragment.app.Fragment
import com.joeSoFine.dormcuments.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.joeSoFine.dormcuments.ui.UITools

class ShoppingFragment : Fragment() {
    var database = FirebaseDatabase.getInstance().getReference("Shoppinglist")
    lateinit var getdata : ValueEventListener
    lateinit var myContainer: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_shopping, container, false)
        myContainer = root.findViewById(R.id.LinScroll)
        var progressBar = root.findViewById<ProgressBar>(R.id.progressBar7)
        progressBar.visibility = View.VISIBLE

        getdata = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                for (i in p0.children) {
                    var name1: String = i.child("name").getValue() as String
                    var itemId = i.key.toString()

                    createTopic(name1, itemId, myContainer)
                }
                progressBar.visibility = View.GONE
            }
            override fun onCancelled(p0: DatabaseError) { println("err") }
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

        root.findViewById<ImageView>(R.id.question).setOnClickListener{
            UITools.onHelpedClicked(requireContext(),R.string.helpDialogTitleGrocery, R.string.helpDialogMsgGrocery)
        }

        return root
    }

    override fun onDetach() {
        super.onDetach()
        database.removeEventListener(getdata)

    }

    private fun createTopic(name: String, itemid: String ,myContainer: LinearLayout){

        val ExpandableCardview: View =
            layoutInflater.inflate(R.layout.list_element_shopping, null, false)
        var delete: ImageView = ExpandableCardview.findViewById(R.id.delete)
        var shoppingItem: TextView = ExpandableCardview.findViewById(R.id.shoppingItem)

        shoppingItem.setText(name)

        delete.setOnClickListener {
            myContainer.removeView(ExpandableCardview)
            deleteItem(itemid)
        }

        myContainer.addView(ExpandableCardview)
    }


    private fun deleteItem(itemId: String){
        var dName = database.child(itemId)
        dName.removeValue()
        Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show()
    }



}