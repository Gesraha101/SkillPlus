package com.example.lost.skillplus.views.fragments

import RetrofitManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.adapters.CurrentNeedAdapter
import com.example.lost.skillplus.models.managers.BackendServiceManager
import com.example.lost.skillplus.models.managers.PreferencesManager
import com.example.lost.skillplus.models.podos.raw.MyId
import com.example.lost.skillplus.models.podos.responses.CurrentNeedResponse
import kotlinx.android.synthetic.main.fragment_current_need.*
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
 * [currentNeedFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [currentNeedFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CurrentNeedFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    var currentNeedView: View? = null

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
        currentNeedView = inflater.inflate(R.layout.fragment_current_need, container, false)
        return currentNeedView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myReq = MyId(PreferencesManager(this.context!!).getId())

        val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
        val call: Call<CurrentNeedResponse>? = service?.getCurrentneeds(myReq)
        call?.enqueue(object : Callback<CurrentNeedResponse> {
            override fun onFailure(call: Call<CurrentNeedResponse>, t: Throwable) =
                    Toast.makeText(activity, "Failed  cause is " + t.cause, Toast.LENGTH_LONG).show()

            override fun onResponse(call: Call<CurrentNeedResponse>, response: Response<CurrentNeedResponse>) {
                if (response.body()?.status == true) {
                    rv_current_needs.apply {
                        layoutManager = LinearLayoutManager(activity)
                        if (response.body()?.skills!!.isNotEmpty()) {
                            adapter = CurrentNeedAdapter(response.body()!!.skills)
//                            (adapter as CurrentSkillAdapter).onItemClick = { post ->
//                                val bundle = Bundle()
//                                val skillLearnersFragments = SkillLearnersFragments()
//                                bundle.putInt("skill_id", post.skill_id!!)
//                                skillLearnersFragments.arguments = bundle
//
//                                FragmentsManager.replaceFragment(this@CurrentSkillFragment.fragmentManager!!, skillLearnersFragments, R.id.fragment_container, "skill_learner_fragment", true)
//                                //   fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, skillLearnersFragments)?.commit()
//                            }
                        }
                    }
                } else {
                    Toast.makeText(activity, "Error: " + response.body(), Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                CurrentNeedFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
