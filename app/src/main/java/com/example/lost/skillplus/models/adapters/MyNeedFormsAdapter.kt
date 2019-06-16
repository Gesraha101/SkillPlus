package com.example.lost.skillplus.models.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.podos.raw.SqlResponseFromMyNeedForms


class MyNeedFormsAdapter(private val list: List<SqlResponseFromMyNeedForms>) : RecyclerView.Adapter<MyNeedFormsAdapter.MyNeedFormViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyNeedFormViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyNeedFormViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MyNeedFormViewHolder, position: Int) {
        val request: SqlResponseFromMyNeedForms = list[position]
        holder.bind(request)
    }

    override fun getItemCount(): Int = list.size

    inner class MyNeedFormViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(R.layout.form, parent, false)) {
        private var userName: TextView? = null
        private var userImage: ImageView? = null
        private var context: Context? = null

        init {
            userName = itemView.findViewById(R.id.form_user_name)
            userImage = itemView.findViewById(R.id.form_user_image)
            context = parent.context
        }

        fun bind(request: SqlResponseFromMyNeedForms) {
            userName?.text = request.user_name
            Glide.with(context!!)
                    .load(request.user_pic)
                    .into(userImage!!)

        }
    }
}