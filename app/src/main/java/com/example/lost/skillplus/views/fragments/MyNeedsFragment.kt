package com.example.lost.skillplus.views.fragments

import RetrofitManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.adapters.RequestsAdapter
import com.example.lost.skillplus.models.managers.BackendServiceManager
import com.example.lost.skillplus.models.managers.PreferencesManager
import com.example.lost.skillplus.models.podos.raw.MyId
import com.example.lost.skillplus.models.podos.responses.MyNeedResponse
import kotlinx.android.synthetic.main.fragment_my_needs.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class MyNeedsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

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
        return inflater.inflate(R.layout.fragment_my_needs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myReq = MyId(PreferencesManager(this.context!!).getId())

        val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
        val call: Call<MyNeedResponse>? = service?.getMyNeeds(myReq)
        call?.enqueue(object : Callback<MyNeedResponse> {
            override fun onFailure(call: Call<MyNeedResponse>, t: Throwable) {
                Toast.makeText(activity, "Failed  cause is " + t.cause, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<MyNeedResponse>, response: Response<MyNeedResponse>) {
                if (response.body()?.status == true) {
                    rv_my_needs.apply {
                        layoutManager = LinearLayoutManager(activity)
                        if (response.body()?.sqlresponse!!.isNotEmpty()) {
                            adapter = RequestsAdapter(response.body()!!.sqlresponse)
//                            (adapter as RequestsAdapter).onItemClick = { post ->
//                                (activity as CategoryContentActivity).loadFragment( post)
//                            }
                            //TODO implement MyNeedsAdapter or use RequestsAdapter
                        }
                    }
                } else {
                    Toast.makeText(activity, "Error: " + response.body(), Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                MyNeedsFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
