package com.example.lost.skillplus.views.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.managers.BackendServiceManager
import com.example.lost.skillplus.models.managers.FragmentsManager
import com.example.lost.skillplus.models.managers.PreferencesManager
import com.example.lost.skillplus.models.podos.raw.FavouriteUpdate
import com.example.lost.skillplus.models.podos.raw.Skill
import com.example.lost.skillplus.models.podos.responses.FavouriteResponse
import com.example.lost.skillplus.views.activities.CategoryContentActivity
import com.example.lost.skillplus.views.activities.ChooseSchaduleActivity
import kotlinx.android.synthetic.main.fragment_skill_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_SENT_KEY = "skill"

class SkillDetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var skill: Skill? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            skill = it.getSerializable(ARG_SENT_KEY) as Skill
        }

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_skill_details, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(this)
                .load(skill!!.photo_path)
                .into(post_image)
        skill_name.text = skill!!.skill_name
        skill_price.append("Total:  ${skill!!.skill_price} EGP")
        poster_name.append("Created by: " + skill!!.user_name)
        description_value.text = skill!!.skill_desc
        sessions_count_value.text = skill!!.session_no.toString()
        session_duration_value.append("${skill!!.duration} hour(s)")
        extra_session_value.append("+" + skill!!.extra_fees.toString() + " per session")
        for (date: Long in skill!!.schedule!!)
            schedule_values.append(Date(date).toString() + "\n")
        poster_rate.rating = skill!!.rate!!
        if (skill!!.schedule!!.size != 0) {
            btn_apply.setOnClickListener {
                val intent = Intent(activity, ChooseSchaduleActivity::class.java).putExtra("Skill", skill)
                startActivity(intent)
            }
        } else {
            btn_apply.setBackgroundColor(resources.getColor(R.color.material_grey_800))
            btn_apply.text = "Full schedule"
        }


        //Check if its not null (i.e not coming from favourites)
        if(skill?.is_favorite!=null) {

            //Check if this skill is favorite , then display its icon
            if (skill?.is_favorite!!)
                is_favorite.setBackgroundResource(R.drawable.is_favourite)
        }
        else
            is_favorite.setBackgroundResource(R.drawable.is_favourite)


        is_favorite.setOnClickListener {
            var favouriteUpdate= FavouriteUpdate(
                    PreferencesManager(this@SkillDetailsFragment.context!!).getId(),
                    skill?.skill_id!!
            )
            val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
            val call: Call<FavouriteResponse>? = service?.updateFavourite(favouriteUpdate)
            call?.enqueue(object : Callback<FavouriteResponse> {
                override fun onResponse(call: Call<FavouriteResponse>, response: Response<FavouriteResponse>) {
                    if (response.isSuccessful) {
                        if (response.body()?.status == true) {
                            if(response.body()?.message==" package added to favorite") {
                                Snackbar.make(view, "Added to your favourites !", Snackbar.LENGTH_SHORT).show()
                                is_favorite.setBackgroundResource(R.drawable.is_favourite)
                                skill!!.is_favorite=true
                            }
                            else {
                                Snackbar.make(view, "Removed from your favourites !", Snackbar.LENGTH_SHORT).show()
                                is_favorite.setBackgroundResource(R.drawable.heart)
                                skill!!.is_favorite=false
                            }
                        } else {
                            Toast.makeText(this@SkillDetailsFragment.context!!, "Failed1", Toast.LENGTH_LONG).show()

                        }
                    } else {
                        Toast.makeText(this@SkillDetailsFragment.context!!, "Failed2", Toast.LENGTH_LONG).show()

                        //Received response but not "OK" response i.e error in the request sent (Server can't handle this request)
                    }
                }

                override fun onFailure(call: Call<FavouriteResponse>, t: Throwable) {
                    Toast.makeText(this@SkillDetailsFragment.context!!, t.message, Toast.LENGTH_LONG).show()
                    //Error receiving response from server i.e error in podo received (Retrofit can't handle this response)
                }

            })
        }
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

        //This block recreates the entire category activity to get recent updates (new posts , favourites updates)
        //Check if its coming from categories)
        if (this.activity!!.localClassName=="views.activities.CategoryContentActivity") {
            this.activity!!.finish()
            this.activity!!.overridePendingTransition(0, 0)
            this.activity!!.startActivity(this.activity!!.intent)
            this.activity!!.overridePendingTransition(0, 0)
        }
        else {
            var frg = this.activity!!.supportFragmentManager.findFragmentByTag("2131296432") //Todo @Gesraha 3amly el Tagname bl menu item id, 7raaaaaam
            var ft: FragmentTransaction = this.activity!!.supportFragmentManager.beginTransaction()
            ft.detach(frg!!)
            ft.attach(frg!!)
            ft.commit()
        }
    }
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(skill: Skill) =
                SkillDetailsFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_SENT_KEY, skill)
                    }
                }
    }

}
