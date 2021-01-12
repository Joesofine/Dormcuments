package com.joeSoFine.dormcuments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.joeSoFine.dormcuments.ui.shopping.InventoryShoppingFragment
import com.joeSoFine.dormcuments.ui.shopping.ShoppingFragment

class ExampleStateAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    val fragments:ArrayList<Fragment> = arrayListOf(
        ShoppingFragment(),
        InventoryShoppingFragment()
    )


    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}