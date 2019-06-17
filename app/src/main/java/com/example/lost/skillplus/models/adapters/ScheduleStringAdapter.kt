package com.example.lost.skillplus.models.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.lost.skillplus.R
import java.text.SimpleDateFormat
import java.util.*


class ScheduleStringAdapter(private val list: List<Long>) : RecyclerView.Adapter<ScheduleStringAdapter.ScheduleViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ScheduleViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val dayTime: Long = list[position]
        holder.bind(dayTime)
    }

    override fun getItemCount(): Int = list.size

    inner class ScheduleViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(R.layout.schedule_string, parent, false)) {
        var schaduleString: TextView? = null

        init {
            schaduleString = itemView.findViewById(R.id.schedule_string)
        }

        fun bind(SchaduleString: Long) {
            val date = Date(SchaduleString)
            val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
            schaduleString?.text = format.format(date)
        }
    }
}