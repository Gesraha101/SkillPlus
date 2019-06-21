package com.example.lost.skillplus.helpers.adapters

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.lost.skillplus.R
import com.example.lost.skillplus.helpers.podos.raw.Learner

class MySkillLearnerAdapter(private val list: List<Learner>) : RecyclerView.Adapter<MySkillLearnerAdapter.MySkillLearnerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySkillLearnerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MySkillLearnerViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MySkillLearnerViewHolder, position: Int) {
        val request: Learner = list[position]
        holder.bind(request)
    }

    override fun getItemCount(): Int = list.size
    inner class MySkillLearnerViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(R.layout.skill_learner, parent, false)) {
        private var userName: TextView? = null
        private var userImage: ImageView? = null
        private var formScheduleList: RecyclerView? = null

        private var context: Context? = null

        init {
            userName = itemView.findViewById(R.id.skill_user_name)
            formScheduleList = itemView.findViewById(R.id.rv_my_skills_learner)
            userImage = itemView.findViewById(R.id.skill_user_image)
            context = parent.context

        }

        fun bind(request: Learner) {
            userName?.text = request.user_name
            Glide.with(context!!)
                    .load(request.user_pic)
                    .into(userImage!!)
            formScheduleList?.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = ScheduleStringAdapter(request.schedule!!)
                Log.d("adapter my skill", request.schedule!!.size.toString())
            }


        }
    }
}