package com.example.lost.skillplus.models.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.enums.Headers
import com.example.lost.skillplus.models.podos.raw.Notification

class NotificationsAdapter(private val list: ArrayList<Notification>) : RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>() {

    var onItemClick: ((Notification) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return NotificationViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification: Notification = list[position]
        holder.bind(notification)
    }

    override fun getItemCount(): Int = list.size

    inner class NotificationViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(R.layout.notification, parent, false)) {
        private var header: TextView? = null
        private var image: ImageView? = null
        private var body: TextView? = null
        private var context: Context? = null

        init {
            image = itemView.findViewById(R.id.notification_user_pic)
            header = itemView.findViewById(R.id.notification_header)
            body = itemView.findViewById(R.id.notification_body)
            context = parent.context
            itemView.setOnClickListener {
                onItemClick?.invoke(list[adapterPosition])
            }
        }

        fun bind(notification: Notification) {
            when {
                notification.skill_name != null -> {
                    header?.text = Headers.SKILL_APPLICATION.header
                    body?.text = String.format(context!!.resources.getString(R.string.notification_skill_apply), notification.user_name!!, notification.skill_name)
                }
                notification.need_id != null -> {
                    header?.text = Headers.FORM_RECEIVED.header
                    body?.text = String.format(context!!.resources.getString(R.string.notification_form_received), notification.user_name!!, notification.need_name)
                }
                else -> {
                    header?.text = Headers.FORM_APPROVED.header
                    body?.text = String.format(context!!.resources.getString(R.string.notification_form_approved), notification.user_name!!, notification.need_name)
                }
            }
            Glide.with(context!!)
                    .load(notification.user_pic)
                    .into(image!!)
        }
    }
}