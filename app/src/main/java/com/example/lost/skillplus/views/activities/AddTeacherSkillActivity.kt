package com.example.lost.skillplus.views.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.podos.raw.Skill
import com.example.lost.skillplus.models.podos.responses.SkillsResponse
import com.example.lost.skillplus.models.podos.responses.UserResponse
import com.example.lost.skillplus.models.retrofit.ServiceManager

import kotlinx.android.synthetic.main.activity_add_teacher_skill.*
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddTeacherSkillActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_teacher_skill)
        setSupportActionBar(toolbar)


        btn_add_skill.setOnClickListener {
            val intent = Intent(this@AddTeacherSkillActivity,ScheduleActivity::class.java)
            intent.putExtra("skillName",eT_Title?.text.toString())
            intent.putExtra("skillDesc",eT_Description?.text.toString())
            intent.putExtra("numberOfSessions",eT_NumberOfSessions.text.toString().toInt())
            intent.putExtra("sessionDuration",eT_SessionDuration?.text.toString().toFloat())
            intent.putExtra("skillPrice",eT_Price?.text.toString().toFloat()) //Todo: skill_price instead of price in backend
            intent.putExtra("extraFees",eT_ExtraFees?.text.toString().toFloat())//Todo: update extra_fees UI

            startActivity(intent)
        }

    }

}
