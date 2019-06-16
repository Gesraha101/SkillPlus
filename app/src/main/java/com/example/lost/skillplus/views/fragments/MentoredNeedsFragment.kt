package com.example.lost.skillplus.views.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.enums.Keys

class MentoredNeedsFragment : Fragment() {

    private var formId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            formId = it.getInt(Keys.FORM_ID.key)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mentored_needs, container, false)
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
