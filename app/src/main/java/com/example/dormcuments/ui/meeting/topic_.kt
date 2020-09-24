package com.example.dormcuments.ui.meeting

import android.content.Context
import com.example.dormcuments.ui.meeting.Topic
import com.example.dormcuments.ui.meeting.topic_
import java.util.ArrayList
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

public object topic_ {
    private val ourInstance: topic_ = topic_
    var meetArr: ArrayList<Topic> = ArrayList<Topic>()
    public fun getInstance(): com.example.dormcuments.ui.meeting.topic_? {
        return ourInstance
    }

    private fun topic_() {}

    fun setMeetArr(arr: ArrayList<Topic>?, context: Context?) {
        saveUser(context!!, arr!!)
    }

    fun getShopArr(context: Context?): ArrayList<Topic>? {
        meetArr.clear()
        topic_.loadUser(context!!)
        return meetArr
    }

    private fun saveUser(context: Context, arr: ArrayList<Topic>) {
        val sharedPreferences = context.getSharedPreferences("Topics", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json: String = gson.toJson(arr)
        editor.putString("meetArr", json)
        editor.apply()
    }

    private fun loadUser(context: Context) {
        val sharedPreferences = context.getSharedPreferences("Topics", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("meetArr", null)
        val type = object : TypeToken<ArrayList<Topic?>?>() {}.type
        meetArr = gson.fromJson(json, type)
        if (meetArr == null) {
            meetArr = ArrayList()
        }
    }
}