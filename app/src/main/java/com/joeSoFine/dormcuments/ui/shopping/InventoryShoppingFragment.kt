package com.joeSoFine.dormcuments.ui.shopping

import android.app.AlertDialog
import android.os.Bundle
import android.text.Html
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.doOnAttach
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import com.joeSoFine.dormcuments.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.joeSoFine.dormcuments.databaseService
import com.joeSoFine.dormcuments.ui.UITools

class InventoryShoppingFragment : Fragment() {
    var database = FirebaseDatabase.getInstance().getReference("Inventory")
    lateinit var getdata : ValueEventListener
    lateinit var myContainer: LinearLayout
    val ref = "Inventory"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_shopping, container, false)
        myContainer = root.findViewById(R.id.LinScroll)
        var progressBar = root.findViewById<ProgressBar>(R.id.progressBar7)
        progressBar.visibility = View.VISIBLE
        root.findViewById<TextView>(R.id.Topics).text = "Inventory Shoppinglist"

        databaseService.setShopChildListener(progressBar, myContainer, layoutInflater, requireContext(), ref )

        if (myContainer.childCount == 0){
            progressBar.visibility = View.GONE
        }

        root.findViewById<FloatingActionButton>(R.id.add).setOnClickListener {
            val layout = LinearLayout(context)
            layout.orientation = LinearLayout.VERTICAL

            val alert = AlertDialog.Builder(context, R.style.MyDialogStyle)
            alert.setTitle(Html.fromHtml("<font color='#FFFFFF'>Add items to list</font>"))
            UITools.createShopAdd(layout,layoutInflater, ref, requireContext())
            alert.setView(layout)

            alert.setPositiveButton("Done") { dialog, whichButton ->
            }
            alert.show().withCenteredButtons()
        }

        root.findViewById<ImageView>(R.id.question).setOnClickListener{
            UITools.onHelpedClicked(requireContext(),R.string.helpDialogTitleInventory, R.string.helpDialogMsgInventory)
        }
        return root
    }

    fun AlertDialog.withCenteredButtons() {
        val positive = getButton(AlertDialog.BUTTON_POSITIVE)

        //Disable the material spacer view in case there is one
        val parent = positive.parent as? LinearLayout
        parent?.gravity = Gravity.CENTER_HORIZONTAL
        val leftSpacer = parent?.getChildAt(1)
        leftSpacer?.visibility = View.GONE

        //Force the default buttons to center
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        layoutParams.weight = 1f
        layoutParams.gravity = Gravity.CENTER

        positive.layoutParams = layoutParams
    }
}