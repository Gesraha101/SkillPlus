package com.example.lost.skillplus.helpers.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.lost.skillplus.R
import com.example.lost.skillplus.helpers.podos.raw.DayTime


class ScheduleAdapter(private val list: ArrayList<DayTime>): RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    var onItemClick: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ScheduleViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val dayTime: DayTime= list[position]
        holder.bind(dayTime)
    }

    override fun getItemCount(): Int = list.size

    inner class ScheduleViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(R.layout.schedule_item, parent, false)) {
        var mDay:TextView? = null
        var mTime:TextView? = null
        var delete: ImageView? = null
        init {
            mDay= itemView.findViewById(R.id.tV_Day)
            mTime= itemView.findViewById(R.id.tV_Time)
            delete= itemView.findViewById(R.id.iv_delete)


        }

        fun bind(dayTime: DayTime) {
            mDay?.text = "Day : "+dayTime.day.toString()
            mTime?.text ="Time : "+dayTime.hour.toString()+":"+dayTime.minute
            delete?.setOnClickListener {
                onItemClick?.invoke(adapterPosition)
            }
        }
    }
}