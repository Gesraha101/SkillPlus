package com.example.lost.skillplus.views.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.managers.BackendServiceManager
import com.example.lost.skillplus.models.managers.PreferencesManager
import com.example.lost.skillplus.models.podos.raw.FavouriteUpdate
import com.example.lost.skillplus.models.podos.raw.Request
import com.example.lost.skillplus.models.podos.responses.FavouriteResponse
import com.example.lost.skillplus.views.activities.AddFormActivity
import kotlinx.android.synthetic.main.fragment_request_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_SENT_KEY = "request"

class RequestDetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var request: Request? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            request = it.getSerializable(ARG_SENT_KEY) as Request
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        request_name.text = request!!.need_name
        poster_name.append("Created by: " + request!!.user_name)
        description_value.text = request!!.need_desc
        btn_apply.setOnClickListener {
            var intent = Intent(this@RequestDetailsFragment.context,AddFormActivity::class.java)
            intent.putExtra("need_id",request!!.need_id)
            startActivity(intent)
        }


    }

    companion object {
        @JvmStatic
        fun newInstance(request: Request) =
                RequestDetailsFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_SENT_KEY, request)
                    }
                }
    }
}
