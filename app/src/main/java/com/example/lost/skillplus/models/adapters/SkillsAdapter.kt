package com.example.lost.skillplus.models.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.podos.Skill


class SkillsAdapter(private val list: List<Skill>): RecyclerView.Adapter<SkillsAdapter.SkillViewHolder>() {

    var onItemClick: ((Skill) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SkillViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {
        val skill: Skill = list[position]
        holder.bind(skill)
    }

    override fun getItemCount(): Int = list.size

    inner class SkillViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(R.layout.post, parent, false)) {
        private var title: TextView? = null
        private var image: ImageView? = null
        private var posterName: TextView? = null
        private var posterRate: TextView? = null
        private var context: Context? = null

        init {
            title = itemView.findViewById(R.id.skill_name)
            image = itemView.findViewById(R.id.post_image)
            posterName = itemView.findViewById(R.id.poster_name)
            posterRate = itemView.findViewById(R.id.poster_rate)
            context = parent.context
            itemView.setOnClickListener {
                onItemClick?.invoke(list[adapterPosition])
            }
        }

        fun bind(skill: Skill) {
            title?.text = skill.title
            /*Glide.with(context!!)
                    .load(cat.imgUrl)
                    .into(image!!)
            */
            posterName?.text = skill.posterName
            posterRate?.text = skill.posterRate
        }
    }
}