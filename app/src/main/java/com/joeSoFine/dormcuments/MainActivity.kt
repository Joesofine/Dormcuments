package com.joeSoFine.dormcuments


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.joeSoFine.dormcuments.ui.foodclub.FoodclubFragment
import com.joeSoFine.dormcuments.ui.more.MoreFragment
import com.joeSoFine.dormcuments.ui.shopping.ShoppingFragment


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
        val fragments: List<Fragment> = supportFragmentManager.fragments
        //val f: Fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!

        val f = supportFragmentManager.fragments.last()?.getChildFragmentManager()?.getFragments()?.get(0)


        if (onBackPressedListener != null) {
            if (f is ShoppingFragment || f is FoodclubFragment || f is MoreFragment) {
                onBackPressedListener!!.doBack()
                bottomNavigationView.selectedItemId = (R.id.calender)
            } else
                onBackPressedListener!!.doBack()
        } else
            super.onBackPressed()
    }
}