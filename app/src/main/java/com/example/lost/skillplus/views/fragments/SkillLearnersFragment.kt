package com.example.lost.skillplus.views.fragments

import RetrofitManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.adapters.MySkillLearnerAdapter
import com.example.lost.skillplus.models.enums.Keys
import com.example.lost.skillplus.models.managers.BackendServiceManager
import com.example.lost.skillplus.models.podos.raw.MyId
import com.example.lost.skillplus.models.podos.responses.MySkillLearnersResponse
import kotlinx.android.synthetic.main.fragment_skill_learners_fragments.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SkillLearnersFragment : Fragment() {

    private var skillId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            skillId = it.getInt(Keys.SKILL_ID.key)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_skill_learners_fragments, container, false)
        if (arguments?.getInt(("skill_id")) != 0) {
            skillId = arguments!!.getInt("skill_id")

        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myReq = MyId(skillId)
        Log.d("skillId", skillId.toString())

        val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
        val call: Call<MySkillLearnersResponse>? = service?.getMySkillsLearners(myReq)
        call?.enqueue(object : Callback<MySkillLearnersResponse> {
            override fun onFailure(call: Call<MySkillLearnersResponse>, t: Throwable) {
                Toast.makeText(activity, "Failed  cause is " + t.cause, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<MySkillLearnersResponse>, response: Response<MySkillLearnersResponse>) {
                if (response.body()?.status == true) {

                    rv_my_skill_learners.apply {
                        layoutManager = LinearLayoutManager(activity)
                        if (response.body()?.skills!!.isNotEmpty()) {
                            adapter = MySkillLearnerAdapter(response.body()!!.skills)


                        }
                    }
                } else {
                    Toast.makeText(activity, "Error: " + response.body(), Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SkillLearnersFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(skillId: Int) =
                SkillLearnersFragment().apply {
                    arguments = Bundle().apply {
                        putInt(Keys.SKILL_ID.key, skillId)
                    }
                }
    }
}
