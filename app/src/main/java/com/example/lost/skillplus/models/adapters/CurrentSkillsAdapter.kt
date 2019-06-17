package com.example.lost.skillplus.models.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.podos.raw.Skill
import com.iarcuschin.simpleratingbar.SimpleRatingBar
import kotlinx.android.synthetic.main.post.view.*


class CurrentSkillsAdapter(private val list: List<Skill>): RecyclerView.Adapter<CurrentSkillsAdapter.CurrentSkillViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentSkillViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CurrentSkillViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: CurrentSkillViewHolder, position: Int) {
        val skill: Skill = list[position]
        holder.bind(skill)
    }

    override fun getItemCount(): Int = list.size

    inner class CurrentSkillViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(R.layout.current_skill, parent, false)) {
        private var title: TextView? = null
        private var image: ImageView? = null
        private var posterName: TextView? = null
        private var context: Context? = null
        private var progress: ProgressBar? = null

        init {
            title = itemView.findViewById(R.id.skill_name)
            image = itemView.findViewById(R.id.post_image)
            posterName = itemView.findViewById(R.id.poster_name)
            context = parent.context
            progress = itemView.findViewById(R.id.schedule_progress)
        }

        fun bind(skill: Skill) {
            title?.text = skill.skill_name
            Glide.with(context!!)
                    .load(skill.photo_path)
                    .into(image!!)

            posterName?.text = StringBuilder().append("Created by: " + skill.user_name) //TODO: user_name returns null form backend
            progress?.max=skill.schedule!!.size
            progress?.progress=skill.session_no-skill.schedule!!.size

        }
    }
}