package com.joeSoFine.dormcuments.ui.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.joeSoFine.dormcuments.ExampleStateAdapter
import com.joeSoFine.dormcuments.R
import com.joeSoFine.dormcuments.UITools
import com.joeSoFine.dormcuments.databaseService
import com.nambimobile.widgets.efab.ExpandableFabLayout
import kotlinx.android.synthetic.main.activity_main.*

class ShopViewpagerFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val root = inflater.inflate(R.layout.fragment_shop_viewpager, container, false)

        initViewPager2WithFragments(root)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var userid = Firebase.auth.currentUser!!.uid
        databaseService.getUserName(userid, view.findViewById(R.id.toolbar))
        UITools.setUpBasicToolbar(view, requireContext(), R.string.helpDialogTitleGrocery, R.string.helpDialogMsgGrocery)
    }


    fun initViewPager2WithFragments(root: View) {
        var viewPager2: ViewPager2 = root.findViewById(R.id.pager)
        var adapter = ExampleStateAdapter(requireActivity().supportFragmentManager,lifecycle)
        viewPager2.adapter = adapter
        viewPager2.setPageTransformer(ZoomOutPageTransformer())

        // If you want to scroll the ViewPager Vertical uncomment the next line:
        // viewPager2.orientation = ViewPager2.ORIENTATION_VERTICAL

        var tablayout: TabLayout = root. findViewById(R.id.tablayout)
        var names:Array<String> = arrayOf("Grocery","Inventory")
        TabLayoutMediator(tablayout,viewPager2){tab, position ->
            tab.text = names[position]
        }.attach()
    }
}