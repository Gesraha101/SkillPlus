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
        val skillRequest = Skill(
                 skill_name =eT_Title?.text.toString()
                ,skill_desc=eT_Description?.text.toString()
                ,session_no=eT_NumberOfSessions?.text.toString().toInt()
                ,duration=eT_SessionDuration?.text.toString().toFloat()
                ,skill_price=eT_Price?.text.toString().toFloat() //skill_price instead of price in backend
                ,extra_fees=eT_ExtraFees?.text.toString().toFloat()
                ,user_id=1
                ,cat_id=1
                ,schedule = listOf( System.currentTimeMillis() )

        ,user_name = null
        ,adding_date = null
        ,rate = null
        ,skill_id = null
        )

        val service = RetrofitManager.getInstance()?.create(ServiceManager::class.java)
        val call: Call<SkillsResponse>? = service?.addSkill(skillRequest)
        call?.enqueue(object : Callback<SkillsResponse> {
            override fun onResponse(call: Call<SkillsResponse>, response: Response<SkillsResponse>) {
                if (response.isSuccessful) {
                    if(response.body()?.status  == true) { startActivity(Intent(this@AddTeacherSkillActivity, HomeActivity::class.java))}
                            else{
                                Toast.makeText(this@AddTeacherSkillActivity,response.body().toString(), Toast.LENGTH_LONG).show()
                                 }
                    } else {
                        Toast.makeText(this@AddTeacherSkillActivity, "Failed ", Toast.LENGTH_LONG).show()
                    }
            }

            override fun onFailure(call: Call<SkillsResponse>, t: Throwable) {
                Toast.makeText(this@AddTeacherSkillActivity, "Failed ", Toast.LENGTH_LONG).show()
            }

        })
    }

}
