package com.example.covid_19india

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.covid_19india.databinding.ItemListBinding
import org.w3c.dom.Text

class StateAdapter(val list: List<StatewiseItem>): BaseAdapter() {

    override fun getCount(): Int =list.size

    override fun getItem(position: Int) =list[position]

    override fun getItemId(position: Int): Long =position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view=convertView ?: LayoutInflater.from(parent!!.context).inflate(R.layout.item_list,parent,false)
        val item=list[position]

        view.findViewById<TextView>(R.id.confirm).text=SpannableDelta("${item.confirmed}\n ↑${item.deltaconfirmed?: "0"}",
                "#D50000", item.confirmed?.length?:0)
        view.findViewById<TextView>(R.id.active).text=item.active
        view.findViewById<TextView>(R.id.recover).text=SpannableDelta("${item.recovered}\n ↑${item.deltarecovered?: "0"}",
                "#2E7D32", item.recovered?.length?:0)
        view.findViewById<TextView>(R.id.deceased).text=SpannableDelta("${item.deaths}\n ↑${item.deltadeaths?: "0"}",
                "#FF000000", item.deaths?.length?:0)
        view.findViewById<TextView>(R.id.stateTv).text=item.state
        return view
    }
}