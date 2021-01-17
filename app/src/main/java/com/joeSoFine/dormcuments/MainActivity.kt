package com.joeSoFine.dormcuments


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.joeSoFine.dormcuments.ui.foodclub.FoodclubFragment
import com.joeSoFine.dormcuments.ui.more.MoreFragment
import com.joeSoFine.dormcuments.ui.shopping.ShopViewpagerFragment
import com.joeSoFine.dormcuments.ui.shopping.ShoppingFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    protected var onBackPressedListener: OnBackPressedListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        FacebookSdk.sdkInitialize(applicationContext)
//        AppEventsLogger.activateApp(this)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        navView.setupWithNavController(navController)

        firebaseAnalytics = Firebase.analytics
    }

    @JvmName("setOnBackPressedListener1")
    fun setOnBackPressedListener(onBackPressedListener: OnBackPressedListener?) {
        this.onBackPressedListener = onBackPressedListener
    }


    override fun onBackPressed() {
        val bottomNavigationView = findViewById<View>(R.id.nav_view) as BottomNavigationView
        val f = supportFragmentManager.fragments.last()?.childFragmentManager?.fragments?.get(0)

        if (onBackPressedListener != null) {
            if (f is ShopViewpagerFragment || f is FoodclubFragment || f is MoreFragment) {
                onBackPressedListener!!.doBack()
                bottomNavigationView.selectedItemId = (R.id.calender)
            } else
                onBackPressedListener!!.doBack()
        } else
            super.onBackPressed()
    }
}