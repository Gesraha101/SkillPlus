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
import com.example.lost.skillplus.helpers.adapters.CurrentSkillAdapter
import com.example.lost.skillplus.helpers.managers.BackendServiceManager
import com.example.lost.skillplus.helpers.managers.PreferencesManager
import com.example.lost.skillplus.helpers.podos.raw.MyId
import com.example.lost.skillplus.helpers.podos.responses.CurrentSkillResponse
import kotlinx.android.synthetic.main.fragment_current_skill.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CurrentSkillFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CurrentSkillFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CurrentSkillFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var currentSkillView: View? = null

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
        currentSkillView = inflater.inflate(R.layout.fragment_current_skill, container, false)
        return currentSkillView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myReq = MyId(PreferencesManager(this.context!!).getId())

        val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
        val call: Call<CurrentSkillResponse>? = service?.getCurrentSkills(myReq)
        call?.enqueue(object : Callback<CurrentSkillResponse> {
            override fun onFailure(call: Call<CurrentSkillResponse>, t: Throwable) =
                    Toast.makeText(activity, "Failed  cause is " + t.cause, Toast.LENGTH_LONG).show()

            override fun onResponse(call: Call<CurrentSkillResponse>, response: Response<CurrentSkillResponse>) {
                if (response.body()?.status == true) {
                    rv_current_skills.apply {
                        layoutManager = LinearLayoutManager(activity)
                        if (response.body()?.skills!!.isNotEmpty()) {
                            adapter = CurrentSkillAdapter(response.body()!!.skills)
//                            (adapter as CurrentSkillAdapter).onItemClick = { post ->
//                                val bundle = Bundle()
//                                val skillLearnersFragments = SkillLearnersFragments()
//                                bundle.putInt("skill_id", post.skill_id!!)
//                                skillLearnersFragments.arguments = bundle
//
//                                FragmentsManager.replaceFragment(this@CurrentSkillFragment.fragmentManager!!, skillLearnersFragments, R.id.fragment_container, "skill_learner_fragment", true)
//                                //   fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, skillLearnersFragments)?.commit()
//                            }
                        } else
                            current_skill_placeholder.visibility = View.VISIBLE
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
         * @return A new instance of fragment CurrentSkillFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                CurrentSkillFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
