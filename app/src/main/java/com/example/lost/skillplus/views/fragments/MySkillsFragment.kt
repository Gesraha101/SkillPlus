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
import com.example.lost.skillplus.models.adapters.MySkillAdapter
import com.example.lost.skillplus.models.managers.BackendServiceManager
import com.example.lost.skillplus.models.managers.PreferencesManager
import com.example.lost.skillplus.models.podos.raw.MyId
import com.example.lost.skillplus.models.podos.responses.MySkillResponse
import kotlinx.android.synthetic.main.fragment_my_skills.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MySkillsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_skills, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myReq = MyId(PreferencesManager(this.context!!).getId())

        val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
        val call: Call<MySkillResponse>? = service?.getMySkills(myReq)
        call?.enqueue(object : Callback<MySkillResponse> {
            override fun onFailure(call: Call<MySkillResponse>, t: Throwable) =
                    Toast.makeText(activity, "Failed  cause is " + t.cause, Toast.LENGTH_LONG).show()

            override fun onResponse(call: Call<MySkillResponse>, response: Response<MySkillResponse>) {
                if (response.body()?.status == true) {
                    rv_my_skills.apply {
                        layoutManager = LinearLayoutManager(activity)
                        if (response.body()?.skills!!.isNotEmpty()) {
                            adapter = MySkillAdapter(response.body()!!.skills)
                            (adapter as MySkillAdapter).onItemClick = { post ->
                                val bundle = Bundle()
                                val skillLearnersFragments = SkillLearnersFragments()
                                bundle.putInt("skill_id", post.skill_id!!)
                                skillLearnersFragments.arguments = bundle
                                fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, skillLearnersFragments)?.commit()
                            }
                        }
                    }
                } else {
                    Toast.makeText(activity, "Error: " + response.body(), Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                MySkillsFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
