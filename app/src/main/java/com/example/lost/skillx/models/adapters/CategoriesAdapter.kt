package com.example.lost.skillx.models.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.lost.skillx.R
import com.example.lost.skillx.models.podos.Category



class CategoriesAdapter(private val list: List<Category>): RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    var onItemClick: ((Category) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CategoryViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val movie: Category = list[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int = list.size

    inner class CategoryViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(R.layout.category, parent, false)) {
        private var name: TextView? = null
        private var image: ImageView? = null
        private var brief: TextView? = null
        private var context: Context? = null

        init {
            name = itemView.findViewById(R.id.category_txt_name)
            image = itemView.findViewById(R.id.category_img)
            brief = itemView.findViewById(R.id.category_txt_brief)
            context = parent.context
            itemView.setOnClickListener {
                onItemClick?.invoke(list[adapterPosition])
            }
        }

        fun bind(cat: Category) {
            name?.text = cat.name
            /*Glide.with(context!!)
                    .load(cat.imgUrl)
                    .into(image!!)
            */
            brief?.text = cat.brief
        }
    }
}