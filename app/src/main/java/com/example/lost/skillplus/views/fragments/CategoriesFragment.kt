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
import com.example.lost.skillplus.models.adapters.CategoriesAdapter
import com.example.lost.skillplus.models.enums.Keys
import com.example.lost.skillplus.models.managers.BackendServiceManager
import com.example.lost.skillplus.models.podos.responses.CategoriesResponse
import com.example.lost.skillplus.views.activities.CategoryContentActivity
import kotlinx.android.synthetic.main.fragment_categories.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoriesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
        val call: Call<CategoriesResponse>? = service?.getCategories()
        call?.enqueue(object : Callback<CategoriesResponse> {

            override fun onResponse(call: Call<CategoriesResponse>, response: Response<CategoriesResponse>) {
                if (response.isSuccessful) {
                    if (response.body()?.status == true) {
                        rv_categories.apply {
                            // set a LinearLayoutManager to handle Android
                            // RecyclerView behavior
                            layoutManager = LinearLayoutManager(activity)
                            // set the custom adapter to the RecyclerView
                            adapter = CategoriesAdapter(response.body()!!.categories)

                            (adapter as CategoriesAdapter).onItemClick = { category ->

                                val intent = Intent(activity, CategoryContentActivity::class.java)
                                intent.putExtra(Keys.CATEGORY.key, category)
                                startActivity(intent)
                            }
                        }
                    } else {
                        Toast.makeText(activity, "Error: " + response.body(), Toast.LENGTH_LONG).show()
                    }
                }
            }
            override fun onFailure(call: Call<CategoriesResponse>, t: Throwable) {
                Toast.makeText(activity, "Failed  cause is " + t.cause + " message is " + t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    companion object {

        @JvmStatic
        fun newInstance() =
                CategoriesFragment()
    }
}
