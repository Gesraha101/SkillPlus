package com.example.lost.skillplus.views.fragments

import RetrofitManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.lost.skillplus.R
import com.example.lost.skillplus.helpers.adapters.SkillsAdapter
import com.example.lost.skillplus.helpers.managers.BackendServiceManager
import com.example.lost.skillplus.helpers.managers.FragmentsManager
import com.example.lost.skillplus.helpers.managers.PreferencesManager
import com.example.lost.skillplus.helpers.podos.raw.FavouriteUpdate
import com.example.lost.skillplus.helpers.podos.raw.Request
import com.example.lost.skillplus.helpers.podos.raw.Skill
import com.example.lost.skillplus.helpers.podos.responses.FavouriteResponse
import kotlinx.android.synthetic.main.fragment_favorites.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class FavoritesFragment : Fragment() {

    fun loadFragment(isSkill: Boolean?, paramPassed: Serializable) {
        val fragment: Fragment = if (isSkill!!) {
            SkillDetailsFragment.newInstance(paramPassed as Skill)
        } else {
            RequestDetailsFragment.newInstance(paramPassed as Request)
        }
        FragmentsManager.replaceFragment(fragmentManager!!, fragment, R.id.favorites_fragment, "details_frag_from_favorites", true)
        rv_favorites.visibility = View.GONE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var favouriteUpdate = FavouriteUpdate(
                PreferencesManager(this@FavoritesFragment.context!!).getId(), null
        )
        val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
        val call: Call<FavouriteResponse>? = service?.getFavourites(favouriteUpdate)
        call?.enqueue(object : Callback<FavouriteResponse> {

            override fun onResponse(call: Call<FavouriteResponse>, response: Response<FavouriteResponse>) {
                if (response.isSuccessful) {
                    if (response.body()?.status == true) {
                        rv_favorites.apply {

                            layoutManager = LinearLayoutManager(activity)

                            if (response.body()?.data!!.isNotEmpty()) {

                                adapter = SkillsAdapter(response.body()!!.data!!)
                                (adapter as SkillsAdapter).onItemClick = { post ->
                                    loadFragment(true, post)
                                }
                                (adapter as SkillsAdapter).onFavouriteClick = { post ->
                                    var favouriteUpdate = FavouriteUpdate(
                                            PreferencesManager(this@FavoritesFragment.context!!).getId(),
                                            post.skill_id!!
                                    )

                                    val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
                                    val call: Call<FavouriteResponse>? = service?.updateFavourite(favouriteUpdate)
                                    call?.enqueue(object : Callback<FavouriteResponse> {
                                        override fun onResponse(call: Call<FavouriteResponse>, response: Response<FavouriteResponse>) {
                                            if (response.isSuccessful) {
                                                if (response.body()?.status == true) {
                                                    if (response.body()?.message == " package added to favorite") {
                                                        Snackbar.make(view, "Added to your favourites !", Snackbar.LENGTH_SHORT).show()
                                                        post.is_favorite = true
                                                    } else {
                                                        Snackbar.make(view, "Removed from your favourites !", Snackbar.LENGTH_SHORT).show()
                                                        post.is_favorite = false


                                                    }
                                                } else {
                                                    Toast.makeText(this@FavoritesFragment.context!!, "Failed1", Toast.LENGTH_LONG).show()

                                                }
                                            } else {
                                                Toast.makeText(this@FavoritesFragment.context!!, "Failed2", Toast.LENGTH_LONG).show()

                                                //Received response but not "OK" response i.e error in the request sent (Server can't handle this request)
                                            }
                                        }

                                        override fun onFailure(call: Call<FavouriteResponse>, t: Throwable) {
                                            Toast.makeText(this@FavoritesFragment.context!!, t.message, Toast.LENGTH_LONG).show()
                                            //Error receiving response from server i.e error in podo received (Retrofit can't handle this response)
                                        }

                                    })
                                }
                            } else {
                                linear_layout.visibility = View.VISIBLE
                            }
                        }
                    } else {
                        Toast.makeText(activity, "Error: " + response.body(), Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<FavouriteResponse>, t: Throwable) {
                Toast.makeText(activity, "Failed  cause is " + t.cause + " message is " + t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    companion object {

        @JvmStatic
        fun newInstance() =
                FavoritesFragment()
    }
}
