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
import com.example.lost.skillplus.helpers.adapters.ScheduleStringAdapter
import com.example.lost.skillplus.helpers.enums.Keys
import com.example.lost.skillplus.helpers.managers.BackendServiceManager
import com.example.lost.skillplus.helpers.podos.raw.MyId
import com.example.lost.skillplus.helpers.podos.responses.MyCurrentNeedFormsResponse
import kotlinx.android.synthetic.main.fragment_mentored_needs.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MentoredNeedsFragment : Fragment() {

    private var formId: Int? = null
    private var mentoredVew: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            formId = it.getInt(Keys.FORM_ID.key)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mentoredVew = inflater.inflate(R.layout.fragment_mentored_needs, container, false)
        return mentoredVew
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myReq = MyId(formId!!)
        Log.d("needId", formId.toString())

        val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
        val call: Call<MyCurrentNeedFormsResponse>? = service?.getMyCurrentNeedsDetails(myReq)
        call?.enqueue(object : Callback<MyCurrentNeedFormsResponse> {
            override fun onFailure(call: Call<MyCurrentNeedFormsResponse>, t: Throwable) {
                Toast.makeText(activity, "Failed  cause is " + t.cause, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<MyCurrentNeedFormsResponse>, response: Response<MyCurrentNeedFormsResponse>) {
                if (response.body()?.status == true) {
                    need_name.text = response.body()?.data?.need
                    session_no.text = response.body()?.data?.session_no.toString()
                    duration.text = response.body()?.data?.duration.toString()
                    need_price.text = response.body()?.data?.need_price.toString()
                    extra_fees.text = response.body()?.data?.extra_fees.toString()

                    rv_current_need_schedual_details.apply {
                        layoutManager = LinearLayoutManager(activity)
                        if (response.body()?.data?.schedule != null) {
                            adapter = ScheduleStringAdapter(response.body()!!.data.schedule)

                            //TODO implement MyNeedsAdapter or use RequestsAdapter
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
        fun newInstance(formId: Int) =
                MentoredNeedsFragment().apply {
                    arguments = Bundle().apply {
                        putInt(Keys.FORM_ID.key, formId)
                    }
                }
    }
}
