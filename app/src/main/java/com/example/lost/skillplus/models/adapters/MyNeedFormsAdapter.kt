package com.example.lost.skillplus.models.adapters

import RetrofitManager
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.managers.BackendServiceManager
import com.example.lost.skillplus.models.podos.raw.FormApprove
import com.example.lost.skillplus.models.podos.raw.SqlResponseFromMyNeedForms
import com.example.lost.skillplus.models.podos.responses.FormResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
        private var dates: RecyclerView? = null
        private var context: Context? = null
        private var approve: Button? = null

        init {
            userName = itemView.findViewById(R.id.form_user_name)
            userImage = itemView.findViewById(R.id.form_user_image)
            dates = itemView.findViewById(R.id.rv_my_need_schedual)
            approve = itemView.findViewById(R.id.approve)


            context = parent.context
        }

        fun bind(request: SqlResponseFromMyNeedForms) {
            //title?.text = java.lang.StringBuilder().append(" Skill Name : ${skill.need_name} ")

            userName?.text = java.lang.StringBuilder().append("Name : ${request.user_name} ")
            Glide.with(context!!)
                    .load(request.user_pic)
                    .into(userImage!!)
            dates?.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = ScheduleStringAdapter(request.schedule!!)
                approve?.setOnClickListener {

                    val approve = FormApprove(request.form_id, request.need_id, request.schedule)
                    val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
                    val call: Call<FormResponse>? = service?.approveForForm(approve)
                    call?.enqueue(object : Callback<FormResponse> {
                        override fun onFailure(call: Call<FormResponse>, t: Throwable) {
                            Toast.makeText(context, "Error: ", Toast.LENGTH_LONG).show()
                        }

                        override fun onResponse(call: Call<FormResponse>, response: Response<FormResponse>) {
                            Toast.makeText(context, "successfully approved", Toast.LENGTH_LONG).show()

                        }

                    })
                }


            }
        }
    }
}