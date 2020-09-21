package com.example.dormapp.ui.rules

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.dormcuments.R
import java.util.*

class adapter_rules (context: Context, arr: Array<String>) : ArrayAdapter<String?>(context, R.layout.rules_list_layout) {
    private var context1 = context
    private val ruleArr: Array<String> = arr
    override fun getCount(): Int {
        return ruleArr.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var viewHolder = ViewHolder()
        if (convertView == null) {
            val mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = mInflater.inflate(R.layout.rules_list_layout, parent, false)
            viewHolder.rule = convertView!!.findViewById<TextView>(R.id.rules_list_text)
            viewHolder.number = convertView.findViewById<TextView>(R.id.rules_list_number)
            convertView.setTag(viewHolder)
        } else {
            viewHolder = convertView.tag as ViewHolder
        }
        viewHolder.rule?.setText(ruleArr[position])
        viewHolder.number!!.text = (position + 1).toString()
        return convertView
    }

    internal class ViewHolder {
        var rule: TextView? = null
        var number: TextView? = null
    }

    init {
        context1 = context
    }
}
