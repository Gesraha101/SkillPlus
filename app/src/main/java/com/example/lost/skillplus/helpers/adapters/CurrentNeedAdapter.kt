package com.example.lost.skillplus.helpers.adapters

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.lost.skillplus.R
import com.example.lost.skillplus.helpers.podos.raw.CurrentNeed


class CurrentNeedAdapter(private val list: List<CurrentNeed>) : RecyclerView.Adapter<CurrentNeedAdapter.SkillViewHolder>() {


    var onItemClick: ((CurrentNeed) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SkillViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {

        val skill: CurrentNeed = list[position]
        holder.bind(skill)
    }

    override fun getItemCount(): Int = list.size

    inner class SkillViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(R.layout.current_need_view, parent, false)) {
        private var title: TextView? = null
        private var image: ImageView? = null
        private var status: TextView? = null
        private var dates: RecyclerView? = null

        private var context: Context? = null

        init {
            title = itemView.findViewById(R.id.need_name)
            image = itemView.findViewById(R.id.post_image)
            dates = itemView.findViewById(R.id.rv_current_skills_schedual)
            status = itemView.findViewById(R.id.need_flag)

            context = parent.context
            itemView.setOnClickListener {
                onItemClick?.invoke(list[adapterPosition])
            }
        }

        fun bind(skill: CurrentNeed) {
            title?.text = java.lang.StringBuilder().append(" Skill Name : ${skill.need_name} ")
            Glide.with(context!!)
                    .load(skill.need_photo)
                    .into(image!!)
            if (skill.flag == true) {
                status?.text = "Accepted"
            } else {
                status?.text = "Waiting"
            }
            dates?.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = ScheduleStringAdapter(skill.schedule!!)
            }
        }
    }
}
