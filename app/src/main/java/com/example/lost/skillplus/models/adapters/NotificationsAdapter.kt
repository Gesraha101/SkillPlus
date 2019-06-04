package com.example.lost.skillplus.models.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.enums.NotificationTypes
import com.example.lost.skillplus.models.podos.raw.Notification

class NotificationsAdapter(private val list: List<Notification>, private val type: Int): RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>() {

    var onItemClick: ((Notification) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return NotificationViewHolder(inflater, parent, type)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification: Notification = list[position]
        holder.bind(notification)
    }

    override fun getItemCount(): Int = list.size

    inner class NotificationViewHolder(inflater: LayoutInflater, parent: ViewGroup, type: Int) : RecyclerView.ViewHolder(inflater.inflate(if (type == NotificationTypes.APPROVAL.type) R.layout.approval else if(type == NotificationTypes.NEED.type) R.layout.need else if(type == NotificationTypes.SKILL_SCHEDULE.type)R.layout.skill_schedule else 0, parent, false)) {
        private var name: TextView? = null
        private var image: ImageView? = null
        private var brief: TextView? = null
        private var context: Context? = null

        init {
            name = itemView.findViewById(R.id.category_name)
            image = itemView.findViewById(R.id.category_img)
            brief = itemView.findViewById(R.id.category_brief)
            context = parent.context
            itemView.setOnClickListener {
                onItemClick?.invoke(list[adapterPosition])
            }
        }

        fun bind(notification: Notification) {
//            name?.text = notification.cat_name
//            /*Glide.with(context!!)
//                    .load(cat.cat_photo)
//                    .into(image!!)
//            */
//            brief?.text = notification.cat_description
        }
    }
}