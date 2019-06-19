package com.example.lost.skillplus.models.adapters

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.podos.raw.CurrentSkill


class CurrentSkillAdapter(private val list: List<CurrentSkill>) : RecyclerView.Adapter<CurrentSkillAdapter.SkillViewHolder>() {


    var onItemClick: ((CurrentSkill) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SkillViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {

        val skill: CurrentSkill = list[position]
        holder.bind(skill)
    }

    override fun getItemCount(): Int = list.size

    inner class SkillViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(R.layout.current_skill_view, parent, false)) {
        private var title: TextView? = null
        private var image: ImageView? = null
        private var imageName: ImageView? = null
        private var price: TextView? = null
        private var sessionNumber: TextView? = null
        private var extraSession: TextView? = null
        private var sessionDuration: TextView? = null
        private var dates: RecyclerView? = null

        private var context: Context? = null

        init {
            title = itemView.findViewById(R.id.skill_name)
            image = itemView.findViewById(R.id.post_image)
            imageName = itemView.findViewById(R.id.poster_profile_image)
            price = itemView.findViewById(R.id.skill_price)
            sessionNumber = itemView.findViewById(R.id.session_number)
            extraSession = itemView.findViewById(R.id.extra_session)
            sessionDuration = itemView.findViewById(R.id.session_duration)
            dates = itemView.findViewById(R.id.rv_current_skills_schedual)
            context = parent.context
            itemView.setOnClickListener {
                onItemClick?.invoke(list[adapterPosition])
            }
        }
        fun bind(skill: CurrentSkill) {
            title?.text = skill.skill_name
            Glide.with(context!!)
                    .load(skill.photo_path)
                    .into(image!!)
            sessionNumber?.text = skill.session_no.toString()
            extraSession?.text = skill.extra_fees.toString()
            sessionDuration?.text = skill.duration.toString()
            imageName?.visibility = View.GONE
            price?.text = java.lang.StringBuilder().append(" ${skill.skill_price} EGP")
            dates?.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = ScheduleStringAdapter(skill.schedule!!)
            }
        }
    }
}
