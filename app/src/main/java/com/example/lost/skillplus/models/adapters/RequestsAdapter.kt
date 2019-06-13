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
import com.example.lost.skillplus.models.managers.PreferencesManager
import com.example.lost.skillplus.models.podos.raw.Request
import com.iarcuschin.simpleratingbar.SimpleRatingBar


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
        private var price: TextView? = null
        private var posterName: TextView? = null
        private var posterRate: SimpleRatingBar? = null
        private var context: Context? = null
        private var isFav: Button? = null
        init {
            title = itemView.findViewById(R.id.skill_name)
            image = itemView.findViewById(R.id.post_image)
            posterName = itemView.findViewById(R.id.poster_name)
            posterRate = itemView.findViewById(R.id.poster_rate)
            price = itemView.findViewById(R.id.skill_price)
            isFav = itemView.findViewById(R.id.is_favorite)
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

            isFav?.visibility=View.GONE
            posterRate?.visibility = View.GONE
            price?.visibility = View.GONE
            if (request.need_id.equals(PreferencesManager(itemView.context).getId())) {
                posterName?.visibility = View.GONE
            } else {
                posterName?.text = StringBuilder().append("Created by: " + request.user_name)
            }

            posterName?.text = StringBuilder().append("Created by: " + request.user_name)
        }
    }
}