package com.example.lost.skillx.views.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.lost.skillx.R
import com.example.lost.skillx.models.adapters.CategoriesAdapter
import com.example.lost.skillx.models.podos.Category
import com.example.lost.skillx.views.activities.CategoryContentActivity
import kotlinx.android.synthetic.main.fragment_categories.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CategoriesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private val BASE_URL = "https://eve3xf4ee4.execute-api.us-east-2.amazonaws.com/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = listOf(
                Category("Raising Arizona", "", "idk"),
                Category("Raising Arizona1", "", "idk1"),
                Category("Raising Arizona2", "", "idk2"),
                Category("Raising Arizona3", "", "idk3"),
                Category("Raising Arizona4", "", "idk4"),
                Category("Raising Arizona5", "", "idk5"),
                Category("Raising Arizona6", "", "idk6"),
                Category("Raising Arizona7", "", "idk7"),
                Category("Raising Arizona8", "", "idk8"),
                Category("Raising Arizona9", "", "idk9")
        )
        rv_categories.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            adapter = CategoriesAdapter(list)

            (adapter as CategoriesAdapter).onItemClick = { category ->

                val intent = Intent(activity, CategoryContentActivity::class.java)
                val bundle = Bundle()
                bundle.putSerializable("CATEGORY", category)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        /*val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val response = retrofit.create(CategoriesResponse::class.java)
        val call = response.categories
        call.enqueue(object : Callback<CategoriesList> {
            override fun onResponse(call: Call<CategoriesList>, response: Response<CategoriesList>) {
                val list = response.body()!!.categoriesList as List<Category>

                rv_categories.apply {
                    // set a LinearLayoutManager to handle Android
                    // RecyclerView behavior
                    layoutManager = LinearLayoutManager(activity)
                    // set the custom adapter to the RecyclerView
                    adapter = CategoriesAdapter(list)

                    (adapter as CategoriesAdapter).onItemClick = { category ->

                        val intent = Intent(activity, CategoryContentActivity::class.java)
                        val bundle = Bundle()
                        bundle.putSerializable("CATEGORY", category)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<CategoriesList>, t: Throwable) {
                Toast.makeText(activity, t.message, Toast.LENGTH_SHORT).show()
            }
        })*/
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
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
        fun newInstance() =
            CategoriesFragment()
    }
}
