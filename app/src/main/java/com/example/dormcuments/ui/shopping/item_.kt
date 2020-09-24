package com.example.dormcuments.ui.shopping

import android.content.Context
import com.example.dormcuments.ui.shopping.item_.ourInstance
import com.example.dormcuments.ui.shopping.item_.item_
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

public object item_ {

    private val ourInstance: item_ = item_
    var shop_arr: ArrayList<Item> = ArrayList<Item>()
    public fun getInstance(): com.example.dormcuments.ui.shopping.item_? {
        return ourInstance
    }

    private fun item_() {}

    fun setShopArr(arr: ArrayList<Item>?, context: Context?) {
        saveUser(context!!, arr!!)
    }

    fun getShopArr(context: Context?): ArrayList<Item>? {
        shop_arr.clear()
        loadUser(context!!)
        return shop_arr
    }

    private fun saveUser(context: Context, arr: ArrayList<Item>) {
        val sharedPreferences = context.getSharedPreferences("Items", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json: String = gson.toJson(arr)
        editor.putString("shop_arr", json)
        editor.apply()
    }

    private fun loadUser(context: Context) {
        val sharedPreferences = context.getSharedPreferences("Items", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("shop_arr", null)
        val type = object : TypeToken<ArrayList<Item?>?>() {}.type
        shop_arr = gson.fromJson(json, type)
        if (shop_arr == null) {
            shop_arr = ArrayList()
        }
    }







}