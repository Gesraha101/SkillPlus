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
import com.example.lost.skillplus.helpers.adapters.MyNeedFormsAdapter
import com.example.lost.skillplus.helpers.enums.Keys
import com.example.lost.skillplus.helpers.managers.BackendServiceManager
import com.example.lost.skillplus.helpers.podos.raw.MyId
import com.example.lost.skillplus.helpers.podos.responses.MyNeedFormsResponse
import kotlinx.android.synthetic.main.fragment_need_form.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NeedFormFragment : Fragment() {
    var formId: Int = 0
    var needId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            needId = it.getInt(Keys.NEED_ID.key)
            formId = it.getInt(Keys.FORM_ID.key)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_need_form, container, false)
        if (arguments?.getInt(Keys.NEED_ID.key) != 0) {
            needId = arguments!!.getInt(Keys.NEED_ID.key)
        }
        if (arguments?.getInt(Keys.FORM_ID.key) != 0) {
            formId = arguments!!.getInt(Keys.FORM_ID.key)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myReq = MyId(needId)
        Log.d("needId", needId.toString())

        val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
        val call: Call<MyNeedFormsResponse>? = service?.getMyNeedForms(myReq)
        call?.enqueue(object : Callback<MyNeedFormsResponse> {
            override fun onFailure(call: Call<MyNeedFormsResponse>, t: Throwable) {
                Toast.makeText(activity, "Failed  cause is " + t.cause, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<MyNeedFormsResponse>, response: Response<MyNeedFormsResponse>) {
                if (response.body()?.status == true) {
                    rv_my_need_forms.apply {
                        layoutManager = LinearLayoutManager(activity)
                        if (response.body()?.sqlresponse != null) {
                            adapter = MyNeedFormsAdapter(response?.body()!!.sqlresponse)
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
        fun newInstance(needId: Int, formId: Int) =
                NeedFormFragment().apply {
                    arguments = Bundle().apply {
                        putInt(Keys.NEED_ID.key, needId)
                        putInt(Keys.FORM_ID.key, formId)
                    }
                }
    }
}
