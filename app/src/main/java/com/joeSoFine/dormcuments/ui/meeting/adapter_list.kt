package com.joeSoFine.dormcuments.ui.meeting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.joeSoFine.dormcuments.R
import java.util.ArrayList

class adapter_list(context: Context, arr: ArrayList<SumObject>) : ArrayAdapter<String?>(
    context,
    R.layout.listeelement_simple
) {
    private var context1 = context
    private val sumArr: ArrayList<SumObject> = arr
    override fun getCount(): Int {
        return sumArr.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var viewHolder = ViewHolder()
        if (convertView == null) {
            val mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = mInflater.inflate(R.layout.listeelement_simple, parent, false)
            viewHolder.title = convertView!!.findViewById<TextView>(R.id.listeelem_overskrift)
            viewHolder.update = convertView.findViewById<TextView>(R.id.last_update)
            convertView.setTag(viewHolder)
        } else {
            viewHolder = convertView.tag as ViewHolder
        }
        viewHolder.title?.text = sumArr[position].title
        viewHolder.update?.text = sumArr[position].updated

        return convertView
    }

    internal class ViewHolder {
        var title: TextView? = null
        var update: TextView? = null
    }

    init {
        context1 = context
    }
}
