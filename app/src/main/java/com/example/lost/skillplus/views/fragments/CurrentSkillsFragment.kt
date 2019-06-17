package com.example.lost.skillplus.views.fragments

import RetrofitManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.adapters.CurrentSkillsAdapter
import com.example.lost.skillplus.models.managers.BackendServiceManager
import com.example.lost.skillplus.models.managers.PreferencesManager
import com.example.lost.skillplus.models.podos.raw.MyId
import com.example.lost.skillplus.models.podos.responses.MyCurrentsResponse
import kotlinx.android.synthetic.main.fragment_current_skills.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrentSkillsFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_current_skills, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
        val call: Call<MyCurrentsResponse>? = service?.getMyCurrentSkills(MyId(PreferencesManager(this@CurrentSkillsFragment.context!!).getId()))
        call?.enqueue(object : Callback<MyCurrentsResponse> {

            override fun onResponse(call: Call<MyCurrentsResponse>, response: Response<MyCurrentsResponse>) {
                if (response.isSuccessful) {
                    if (response.body()?.status == true) {
                        rv_current_skils.apply {

                            layoutManager = LinearLayoutManager(activity)

                            if (response.body()?.skills!!.isNotEmpty()) {

                                adapter = CurrentSkillsAdapter(response.body()!!.skills!!)


                            }
                            else
                            {
                                linear_layout.visibility=View.VISIBLE // TODO: CurrentSkills placeholder
                            }
                        }
                    } else {
                        Toast.makeText(activity, "Error: " + response.body(), Toast.LENGTH_LONG).show()
                    }
                }
            }
            override fun onFailure(call: Call<MyCurrentsResponse>, t: Throwable) {
                Toast.makeText(activity, "Failed  cause is " + t.cause + " message is " + t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    companion object {

        @JvmStatic
        fun newInstance() =
                CurrentSkillsFragment()
    }
}
