package com.example.lost.skillplus.models.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.podos.raw.Skill
import com.iarcuschin.simpleratingbar.SimpleRatingBar


class MySkillAdapter(private val list: List<Skill>) : RecyclerView.Adapter<MySkillAdapter.SkillViewHolder>() {


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
        private var imageName: ImageView? = null
        private var price: TextView? = null
        private var posterName: TextView? = null
        private var posterRate: SimpleRatingBar? = null
        private var isFavorite: Button? = null
        private var context: Context? = null

        init {
            title = itemView.findViewById(R.id.skill_name)
            image = itemView.findViewById(R.id.post_image)
            imageName = itemView.findViewById(R.id.poster_profile_image)
            price = itemView.findViewById(R.id.skill_price)
            posterName = itemView.findViewById(R.id.poster_name)
            posterRate = itemView.findViewById(R.id.poster_rate)
            isFavorite = itemView.findViewById(R.id.is_favorite)
            context = parent.context
            itemView.setOnClickListener {
                onItemClick?.invoke(list[adapterPosition])
            }
        }

        fun bind(skill: Skill) {
            title?.text = skill.skill_name
            Glide.with(context!!)
                    .load(skill.photo_path)
                    .into(image!!)
            posterName?.visibility = View.GONE
            imageName?.visibility = View.GONE
            price?.text = java.lang.StringBuilder().append(" ${skill.skill_price} EGP")
            posterRate?.rating = skill.rate!!
            isFavorite?.visibility = View.GONE

        }
    }
}
