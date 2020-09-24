package com.example.dormcuments.ui.meeting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.dormcuments.R
import java.util.ArrayList

class adapter_meet (context: Context, arr: ArrayList<Topic>) : ArrayAdapter<String?>(context, R.layout.list_element_meeting){
    private var context1 = context
    private val meetArr: ArrayList<Topic> = arr
    override fun getCount(): Int {
        return meetArr.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var viewHolder = ViewHolder()
        if (convertView == null) {
            val mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = mInflater.inflate(R.layout.list_element_meeting, parent, false)
            viewHolder.Name = convertView!!.findViewById(R.id.meetingItem)
            convertView.setTag(viewHolder)
        } else {
            viewHolder = convertView.tag as ViewHolder
        }
        viewHolder.Name?.text = meetArr[position].name

        return convertView
    }


    internal class ViewHolder {
        var Name: TextView? = null
    }

    init {
        context1 = context
    }
}