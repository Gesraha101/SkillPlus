package com.example.lost.skillplus.views.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.podos.raw.Skill
import com.example.lost.skillplus.views.activities.ChooseSchaduleActivity
import kotlinx.android.synthetic.main.fragment_skill_details.*
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

        btn_apply.setOnClickListener{
            val intent = Intent(activity , ChooseSchaduleActivity::class.java)
                    intent.putExtra("Skill", skill)
            startActivity(intent)
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
