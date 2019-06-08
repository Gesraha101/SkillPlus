package com.example.lost.skillplus.views.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Patterns
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.lost.skillplus.R

import kotlinx.android.synthetic.main.activity_add_teacher_skill.*

class AddTeacherSkillActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_teacher_skill)
        setSupportActionBar(toolbar)

        val shake = AnimationUtils.loadAnimation(this, R.anim.animation) as Animation
        var badEntry:Boolean
        btn_proceed.setOnClickListener {
            if (eT_Title.text.toString().isEmpty() || eT_Description.text.toString().isEmpty() || eT_NumberOfSessions.text.toString().isEmpty() || eT_SessionDuration.text.toString().isEmpty() || eT_Price.text.toString().isEmpty() || eT_ExtraFees.text.toString().isEmpty()) {
                Toast.makeText(this@AddTeacherSkillActivity, "Please complete all of the entries !", Toast.LENGTH_LONG).show()
            } else {
                badEntry=false
                if (eT_NumberOfSessions.text.toString().toIntOrNull() == null) {
                    eT_NumberOfSessions.error = "A number is required"
                    eT_NumberOfSessions.startAnimation(shake)
                    eT_NumberOfSessions.requestFocus()
                    badEntry=true
                }
                if (eT_SessionDuration.text.toString().toIntOrNull() == null) {
                    eT_SessionDuration.error = "A number is required"
                    eT_SessionDuration.startAnimation(shake)
                    eT_SessionDuration.requestFocus()
                    badEntry=true
                }
                if (eT_Price.text.toString().toIntOrNull() == null) {
                    eT_Price.error = "A number is required"
                    eT_Price.startAnimation(shake)
                    eT_Price.requestFocus()
                    badEntry=true
                }
                if (eT_ExtraFees.text.toString().toIntOrNull() == null) {
                    eT_ExtraFees.error = "A number is required"
                    eT_ExtraFees.startAnimation(shake)
                    eT_ExtraFees.requestFocus()
                    badEntry=true
                }
                if(!badEntry) {

                    val intent = Intent(this@AddTeacherSkillActivity, ScheduleActivity::class.java)
                    intent.putExtra("skillName", eT_Title?.text.toString())
                    intent.putExtra("skillDesc", eT_Description?.text.toString())
                    intent.putExtra("numberOfSessions", eT_NumberOfSessions.text.toString().toInt())
                    intent.putExtra("sessionDuration", eT_SessionDuration?.text.toString().toFloat())
                    intent.putExtra("skillPrice", eT_Price?.text.toString().toFloat()) //Todo: skill_price instead of price in backend
                    intent.putExtra("extraFees", eT_ExtraFees?.text.toString().toFloat())//Todo: update extra_fees UI

                    startActivity(intent)
                }
            }
        }
    }
}


