package com.joeSoFine.dormcuments.ui.calender

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.joeSoFine.dormcuments.R
import com.joeSoFine.dormcuments.SmartTools
import com.joeSoFine.dormcuments.UITools
import com.joeSoFine.dormcuments.databaseService
import com.joeSoFine.dormcuments.ui.cleaning.CleaningDetailsFragment
import com.nambimobile.widgets.efab.ExpandableFabLayout
import kotlinx.android.synthetic.main.fragment_calender.*
import java.util.*
import kotlin.collections.ArrayList


class CalenderFragment : Fragment(),View.OnClickListener {
    var database = FirebaseDatabase.getInstance().getReference("Events")
    lateinit var getdata : ValueEventListener
    lateinit var myContainer: LinearLayout
    private lateinit var auth: FirebaseAuth
    private val months = ArrayList<String>()
    private val weeks = ArrayList<String>()
    private val years = ArrayList<String>()
    private lateinit var sliderLayout: LinearLayout
    private lateinit var scroll: HorizontalScrollView
    private lateinit var whoops: TextView
    private lateinit var lottie: LottieAnimationView
    lateinit var tabLayout: TabLayout
    lateinit var scroller: TabLayout
    var targetHeight = 0
    var targetWidth = 0
    private var current_week: Int = 0
    private var current_month: Int = 0
    private var current_year: Int = 0
    var weeksWidth = 0
    var monthWidth = 0
    var yearWidth = 0
    val refE = "Events"
    val refU = "Users"
    var tabbed = ""

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_calender, container, false)
        lottie = root.findViewById<LottieAnimationView>(R.id.animation_view)

        auth = Firebase.auth

        myContainer = root.findViewById(R.id.LinScroll)
        sliderLayout = root.findViewById(R.id.sliderLayout)
        scroll = root.findViewById(R.id.scroll)
        whoops = root.findViewById(R.id.whoops)
        tabLayout = root.findViewById(R.id.tabLayout)
        scroller= root. findViewById(R.id.tabLayout_scroll)


        getTagetSize()
        weeksWidth = targetWidth - 120
        monthWidth = targetWidth - 40
        yearWidth = targetWidth * 3

        val calendar = Calendar.getInstance()
        current_week = calendar.get(Calendar.WEEK_OF_YEAR)
        current_month = calendar.get(Calendar.MONTH)
        current_year = calendar.get(Calendar.YEAR)

        makeWeekArr(current_year)
        makeMonthArr()
        makeYearArr(current_year)

        iniAlphaTabLayout()
        iniBetaTablayout()

        buttonPressed(weeks, "weeks", current_week - 1)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var userid = auth.currentUser!!.uid
        val expandableFabLayout = view.findViewById<ExpandableFabLayout>(R.id.fab_layout)
        expandableFabLayout.portraitConfiguration.fabOptions.forEach { it.setOnClickListener(this) }

        var toolbar = view.findViewById(R.id.toolbar) as Toolbar
        toolbar.inflateMenu(R.menu.actionbar_calendar)
        databaseService.getUserName(userid, toolbar)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.info -> {
                    UITools.onHelpedClicked(requireContext(), R.string.helpDialogTitleGrocery, R.string.helpDialogMsgGrocery)
                    true
                }
                R.id.filter -> {
                    // do something
                    true
                }
                R.id.out -> {
                    SmartTools.signOut(requireContext())
                    true
                }
                else -> {
                    super.onOptionsItemSelected(it)
                }
            }
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor")
    override fun onClick(p0: View?) {
        val bundle = Bundle()
        val fragment2 = CreateEventFragment()
        fragment2.arguments = bundle

        when(p0?.id){
            R.id.option1 -> {
                bundle.putString("type", "Social event")
                fragmentManager?.beginTransaction()?.add(R.id.nav_host_fragment, fragment2)?.addToBackStack(null)?.commit()
            }
            R.id.option2 -> {
                bundle.putString("type", "Book kitchen")
                fragmentManager?.beginTransaction()?.add(R.id.nav_host_fragment, fragment2)?.addToBackStack(null)?.commit()
            }
            R.id.option3 -> {
                bundle.putString("type", "Meeting")
                fragmentManager?.beginTransaction()?.add(R.id.nav_host_fragment, fragment2)?.addToBackStack(null)?.commit()
            }
            R.id.option4 -> {
                bundle.putString("type", "Cleaning")
                fragmentManager?.beginTransaction()?.add(R.id.nav_host_fragment, fragment2)?.addToBackStack(null)?.commit()
            }
        }
    }

    private fun makeMonthArr(){
        months.add("januar")
        months.add("Februar")
        months.add("March")
        months.add("April")
        months.add("May")
        months.add("June")
        months.add("July")
        months.add("August")
        months.add("September")
        months.add("October")
        months.add("November")
        months.add("December")
    }
    private fun makeWeekArr(current_year: Int){
        val cal = Calendar.getInstance()
        cal[Calendar.YEAR] = current_year - 1
        cal[Calendar.MONTH] = Calendar.DECEMBER
        cal[Calendar.DAY_OF_MONTH] = 31
        val last_year_weeks = (cal[Calendar.WEEK_OF_YEAR])

        if (last_year_weeks == 53){
            weeks.add("U53")
        }

        for (i in 1..Calendar.getInstance().getActualMaximum(Calendar.WEEK_OF_YEAR)) {
            val st = "U$i"
            weeks.add(st)
        }
    }
    private fun makeYearArr(current_year: Int){
        for (i in current_year - 1..current_year + 1) {
            years.add(i.toString())
        }
    }

    private fun buttonLoop(arr: ArrayList<String>) {
        for (element in arr) {
            var tab: TabLayout.Tab = scroller.newTab()
            tab.text = element
            scroller.addTab(tab)
        }
    }

    private fun getTagetSize(){
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)

        var width = displayMetrics.widthPixels
        var height = displayMetrics.heightPixels

        targetWidth = ((width / 3))
        targetHeight = tabLayout.layoutParams.height
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun buttonPressed(arr: ArrayList<String>, arrString: String, current: Int){
        lottie.visibility = View.VISIBLE
        buttonLoop(arr)


        if (current == 52) {
            var tab = scroller.getTabAt(0)
            tab?.select()

            databaseService.getSortedEvents(
                0,
                current + 1,
                "weeks",
                lottie,
                current_year,
                myContainer,
                layoutInflater,
                requireFragmentManager(),
                requireContext(),
                refE,
                refU,
                whoops
            )
        } else {
            var current_longYear = current
            if (arr.size == 53) {
                current_longYear = current + 1
            } else if (arr.size == 3) {
                current_longYear = current_year - (current)
            }

            var tab = scroller.getTabAt(current_longYear)
            tab?.select()

            if (arrString == "weeks") {
                databaseService.getSortedEvents(
                    0,
                    current + 1,
                    "weeks",
                    lottie,
                    current_year,
                    myContainer,
                    layoutInflater,
                    requireFragmentManager(),
                    requireContext(),
                    refE,
                    refU,
                    whoops
                )

            } else if (arrString == "months") {
                databaseService.getSortedEvents(
                    1,
                    current + 1,
                    "months",
                    lottie,
                    current_year,
                    myContainer,
                    layoutInflater,
                    requireFragmentManager(),
                    requireContext(),
                    refE,
                    refU,
                    whoops
                )

            } else if (arrString == "years") {
                databaseService.getSortedEvents(
                    0,
                    current + current_year - 1,
                    "years",
                    lottie,
                    current_year,
                    myContainer,
                    layoutInflater,
                    requireFragmentManager(),
                    requireContext(),
                    refE,
                    refU,
                    whoops
                )
            }
        }
    }

    fun iniAlphaTabLayout(){
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onTabSelected(tab: TabLayout.Tab?) {
                alphaTabSelected(tab!!)
            }


            @RequiresApi(Build.VERSION_CODES.O)
            override fun onTabReselected(tab: TabLayout.Tab?) {
                alphaTabSelected(tab!!)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                scroller.removeAllTabs()
                myContainer.removeAllViews()
            }
        })

    }

    fun iniBetaTablayout(){

        scroller.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onTabSelected(tab: TabLayout.Tab?) {
                onBetaTabSelected(tab!!)
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onTabReselected(tab: TabLayout.Tab?) {
                onBetaTabSelected(tab!!)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                myContainer.removeAllViews()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun alphaTabSelected(tab: TabLayout.Tab){
        if (tab?.equals(tabLayout.getTabAt(0))!!) {
            tabbed = "week"
            buttonPressed(weeks, "weeks", current_week - 1)
        } else if (tab?.equals(tabLayout.getTabAt(1))!!) {
            tabbed = "month"
            buttonPressed(months, "months", current_month)

        } else if (tab?.equals(tabLayout.getTabAt(2))!!) {
            tabbed = "year"
            buttonPressed(years, "years", current_year - 1)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onBetaTabSelected(tab: TabLayout.Tab){
        if (tabbed.equals("week")) {
            var weekNumber = tab?.text.toString().replace("U", "").toInt()
            databaseService.getSortedEvents(
                0,
                weekNumber,
                "weeks",
                lottie,
                current_year,
                myContainer,
                layoutInflater,
                requireFragmentManager(),
                requireContext(),
                refE,
                refU,
                whoops
            )
        } else if (tabbed.equals("month")) {
            databaseService.getSortedEvents(
                1,
                months.indexOf(tab?.text.toString()) + 1,
                "months",
                lottie,
                current_year,
                myContainer,
                layoutInflater,
                requireFragmentManager(),
                requireContext(),
                refE,
                refU,
                whoops
            )

        } else if (tabbed.equals("year")) {
            databaseService.getSortedEvents(
                0,
                tab?.text.toString().toInt(),
                "years",
                lottie,
                current_year,
                myContainer,
                layoutInflater,
                requireFragmentManager(),
                requireContext(),
                refE,
                refU,
                whoops
            )

        }
    }
}