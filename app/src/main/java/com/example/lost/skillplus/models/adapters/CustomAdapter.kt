package com.example.lost.skillplus.models.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.podos.raw.Schedule

class CustomAdapter(private val context: Context, private var modelArrayList: java.util.ArrayList<Schedule>) : BaseAdapter() {

    override fun getViewTypeCount(): Int {
        return count
    }

    override fun getItemViewType(position: Int): Int {

        return position
    }

    override fun getCount(): Int {
        return modelArrayList.size
    }

    override fun getItem(position: Int): Any {
        return modelArrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder

        if (convertView == null) {
            holder = ViewHolder()
            val inflater = context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.lv_item, null, true)

            holder.checkBox = convertView!!.findViewById(R.id.cb) as CheckBox
            holder.tvAnimal = convertView.findViewById(R.id.animal) as TextView

            convertView.tag = holder
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = convertView.tag as ViewHolder
        }


        holder.tvAnimal!!.text = modelArrayList[position].getAnimals()

        holder.checkBox!!.isChecked = modelArrayList[position].getSelecteds()

        holder.checkBox!!.setTag(R.integer.btnplusview, convertView)
        holder.checkBox!!.tag = position
        holder.checkBox!!.setOnClickListener {
            val tempView = holder.checkBox!!.getTag(R.integer.btnplusview) as View
            val tv = tempView.findViewById(R.id.animal) as TextView
            val pos = holder.checkBox!!.tag as Int
            Toast.makeText(context, "Checkbox $pos clicked!", Toast.LENGTH_SHORT).show()

            if (modelArrayList[pos].getSelecteds()) {
                modelArrayList[pos].setSelecteds(false)
                public_modelArrayList = modelArrayList
            } else {
                modelArrayList[pos].setSelecteds(true)
                public_modelArrayList = modelArrayList
            }
        }

        return convertView
    }

    private inner class ViewHolder {

        var checkBox: CheckBox? = null
        var tvAnimal: TextView? = null

    }

    companion object {
        lateinit var public_modelArrayList: java.util.ArrayList<Schedule>
    }

}