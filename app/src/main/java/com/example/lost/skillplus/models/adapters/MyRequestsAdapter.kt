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
import com.example.lost.skillplus.models.podos.raw.Request

class MyRequestsAdapter(private val list: List<Request>) : RecyclerView.Adapter<MyRequestsAdapter.RequestViewHolder>() {

    var onItemClick: ((Request) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RequestViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val request: Request = list[position]
        holder.bind(request)
    }

    override fun getItemCount(): Int = list.size

    inner class RequestViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(R.layout.my_need_view, parent, false)) {
        private var title: TextView? = null
        private var image: ImageView? = null
        private var posterDesc: TextView? = null
        private var context: Context? = null
        private var Proposal: Button? = null

        init {
            title = itemView.findViewById(R.id.skill_name)
            image = itemView.findViewById(R.id.post_image)
            posterDesc = itemView.findViewById(R.id.my_need_desc)
            Proposal = itemView.findViewById(R.id.proposal_btn)
            context = parent.context
            itemView.setOnClickListener {
                onItemClick?.invoke(list[adapterPosition])
            }
        }

        fun bind(request: Request) {
            title?.text = request.need_name
            Glide.with(context!!)
                    .load(request.need_photo)
                    .into(image!!)

            Proposal?.visibility = View.GONE
            posterDesc?.text = request.need_desc
        }
    }
}