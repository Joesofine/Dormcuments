package com.example.dormcuments.ui.shopping

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.dormcuments.R
import java.util.ArrayList

class adapter_shop (context: Context, arr: ArrayList<Item>) : ArrayAdapter<String?>(context, R.layout.list_element_shop_meet){
    private var context1 = context
    private val shop_arr: ArrayList<Item> = arr
    override fun getCount(): Int {
        return shop_arr.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var viewHolder = ViewHolder()
        if (convertView == null) {
            val mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = mInflater.inflate(R.layout.list_element_shop_meet, parent, false)
            viewHolder.Name = convertView!!.findViewById(R.id.shoppingItem)
            convertView.setTag(viewHolder)
        } else {
            viewHolder = convertView.tag as ViewHolder
        }
        viewHolder.Name?.text = shop_arr[position].name

        return convertView
    }


    internal class ViewHolder {
        var Name: TextView? = null
    }

    init {
        context1 = context
    }

}