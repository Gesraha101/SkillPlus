package com.example.lost.skillplus.views.fragments

import RetrofitManager
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.adapters.NotificationsAdapter
import com.example.lost.skillplus.models.enums.Keys
import com.example.lost.skillplus.models.managers.BackendServiceManager
import com.example.lost.skillplus.models.managers.PreferencesManager
import com.example.lost.skillplus.models.podos.raw.Notification
import com.example.lost.skillplus.models.podos.raw.NotificationsRequest
import com.example.lost.skillplus.models.podos.responses.NotificationsResponse
import com.example.lost.skillplus.views.activities.HomeActivity
import kotlinx.android.synthetic.main.fragment_notifications.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NotificationsFragment : Fragment() {

    private var notifications: ArrayList<Notification>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.getSerializable(Keys.NOTIFICATIONS.key) != null)
                notifications = it.getSerializable(Keys.NOTIFICATIONS.key) as ArrayList<Notification>
            else
                notifications = null
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (notifications == null) {
            val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
            val call: Call<NotificationsResponse>? = service?.getNotifications(NotificationsRequest(PreferencesManager(context!!).getId(), 0))
            call?.enqueue(object : Callback<NotificationsResponse> {

                override fun onResponse(call: Call<NotificationsResponse>, response: Response<NotificationsResponse>) {
                    if (response.isSuccessful) {
                        PreferencesManager(context!!).setLastUpdated(System.currentTimeMillis())
                        if (response.body()?.notifications!!.size != 0) {
                            notifications = response.body()?.notifications!!
                            fillRecyclerView()
                        }
                        else
                            place_holder_layout.visibility=View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<NotificationsResponse>, t: Throwable) {
                    Toast.makeText(context, "Failed" + t.localizedMessage, Toast.LENGTH_LONG).show()
                }
            })
        } else {
            fillRecyclerView()
        }
    }

    fun fillRecyclerView() {
        rv_notifications.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            adapter = NotificationsAdapter(notifications!!)

            (adapter as NotificationsAdapter).onItemClick = { notification ->
                startActivity(Intent(activity, HomeActivity::class.java)
                        .putExtra(Keys.NOTIFICATION.key, notification))
                activity!!.finish()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(notifications: ArrayList<Notification>?) =
            NotificationsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(Keys.NOTIFICATIONS.key, notifications)
                }
            }
    }
}