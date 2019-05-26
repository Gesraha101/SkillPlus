package com.example.lost.skillplus.models.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.podos.Request


class RequestsAdapter(private val list: List<Request>): RecyclerView.Adapter<RequestsAdapter.RequestViewHolder>() {

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

    inner class RequestViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(R.layout.post, parent, false)) {
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

        fun bind(request: Request) {
            title?.text = request.title
            /*Glide.with(context!!)
                    .load(cat.imgUrl)
                    .into(image!!)
            */
            posterName?.text = request.posterName
            posterRate?.text = request.posterRate
        }
    }
}