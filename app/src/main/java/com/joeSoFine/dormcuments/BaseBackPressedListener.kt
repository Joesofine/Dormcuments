package com.joeSoFine.dormcuments

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager


class BaseBackPressedListener(private val activity: FragmentActivity) : OnBackPressedListener {
    override fun doBack() {
        activity.supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}