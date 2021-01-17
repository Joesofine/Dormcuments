package com.joeSoFine.dormcuments.ui.shopping

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.joeSoFine.dormcuments.*
import com.nambimobile.widgets.efab.ExpandableFabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_calender.*

class ShopViewpagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        SmartTools.setUpOnBackPressed(requireActivity())

        val root = inflater.inflate(R.layout.fragment_shop_viewpager, container, false)

        initViewPager2WithFragments(root)
                return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var userid = Firebase.auth.currentUser!!.uid
        databaseService.getUserName(userid, view.findViewById(R.id.toolbar))
        val contextView = view.findViewById<View>(R.id.contextView)
        UITools.setUpBasicToolbar(view, contextView, requireContext(), R.string.helpDialogTitleGrocery, R.string.helpDialogMsgGrocery)
    }


    fun initViewPager2WithFragments(root: View) {
        var viewPager2: ViewPager2 = root.findViewById(R.id.pager)
        var adapter = ExampleStateAdapter(requireActivity().supportFragmentManager,lifecycle)
        viewPager2.adapter = adapter
        viewPager2.setPageTransformer(ZoomOutPageTransformer())

        // If you want to scroll the ViewPager Vertical uncomment the next line:
        // viewPager2.orientation = ViewPager2.ORIENTATION_VERTICAL

        var tablayout: TabLayout = root. findViewById(R.id.tablayout)
        TabLayoutMediator(tablayout,viewPager2){tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Grocery"
                }
                1 -> {
                    tab.text = "Inventory"
                    val badge = tab.getOrCreateBadge()
                    badge.backgroundColor = resources.getColor(R.color.meeting)
                    var getdataU = object : ValueEventListener {
                        override fun onDataChange(p0: DataSnapshot) {
                            badge.number = p0.childrenCount.toInt()
                            if (badge.number == 0){
                                tab.removeBadge()
                            }
                        }
                        override fun onCancelled(p0: DatabaseError) { println("err") }
                    }

                    FirebaseDatabase.getInstance().getReference("Inventory").addValueEventListener(getdataU)
                }
            }
        }.attach()


        tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.removeBadge() }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })






    }
}