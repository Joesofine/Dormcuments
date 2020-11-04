package com.joeSoFine.dormapp.ui.rules

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.joeSoFine.dormcuments.R
import kotlin.collections.ArrayList

class adapter_rules (context: Context, arr: ArrayList<String>) : ArrayAdapter<String>(context, R.layout.list_element_rules) {
    private var context1 = context
    private val ruleArr: ArrayList<String> = arr
    override fun getCount(): Int {
        return ruleArr.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var viewHolder = ViewHolder()
        if (convertView == null) {
            val mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = mInflater.inflate(R.layout.list_element_rules, parent, false)
            viewHolder.rule = convertView!!.findViewById<TextView>(R.id.rules_list_text)
            viewHolder.number = convertView.findViewById<TextView>(R.id.rules_list_number)
            convertView.setTag(viewHolder)
        } else {
            viewHolder = convertView.tag as ViewHolder
        }
        viewHolder.rule?.setText(ruleArr[position])
        var num = (position + 1)
        if (num < 10) {
            viewHolder.number!!.text = "0$num."
        } else {
            viewHolder.number!!.text = "$num."

        }

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
