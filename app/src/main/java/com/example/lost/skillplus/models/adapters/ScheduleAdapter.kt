package com.example.lost.skillplus.models.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.podos.raw.DayTime
import com.example.lost.skillplus.models.podos.raw.Skill
import com.iarcuschin.simpleratingbar.SimpleRatingBar


class ScheduleAdapter(private val list: List<DayTime>): RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    var onItemClick: ((Skill) -> Unit)? = null

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
        init {
            mDay= itemView.findViewById(R.id.tV_Day)
            mTime= itemView.findViewById(R.id.tV_Time)

        }

        fun bind(dayTime: DayTime) {
            mDay?.text = "Day : "+dayTime.day.toString()
            mTime?.text ="Time : "+dayTime.hour.toString()+":"+dayTime.minute
        }
    }
}