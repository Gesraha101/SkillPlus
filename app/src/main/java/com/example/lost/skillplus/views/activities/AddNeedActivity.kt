package com.example.lost.skillplus.views.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import com.example.lost.skillplus.R

import kotlinx.android.synthetic.main.activity_add_need.*
import kotlinx.android.synthetic.main.activity_add_teacher_skill.*

class AddNeedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_need)
        setSupportActionBar(toolbar)


    }
}
