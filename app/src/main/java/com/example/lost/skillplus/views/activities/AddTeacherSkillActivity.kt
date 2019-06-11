package com.example.lost.skillplus.views.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.enums.Keys
import com.example.lost.skillplus.models.managers.PreferencesManager
import com.example.lost.skillplus.models.podos.raw.Category
import com.example.lost.skillplus.models.podos.raw.Skill

import kotlinx.android.synthetic.main.activity_add_teacher_skill.*

class AddTeacherSkillActivity : AppCompatActivity() {

    lateinit var activatedCategory: Category

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_teacher_skill)
        setSupportActionBar(toolbar)

        activatedCategory = intent.getSerializableExtra(Keys.CATEGORY.key) as Category
        lL_AddImage.setOnClickListener {
            //Todo: Implement getting a photo
        }
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
                    var skill =Skill(
                            null,
                            eT_Title?.text.toString(),
                            eT_Description?.text.toString(),
                            eT_NumberOfSessions.text.toString().toInt(),
                            eT_Price?.text.toString().toFloat(),
                            eT_SessionDuration?.text.toString().toFloat(),
                            eT_ExtraFees?.text.toString().toFloat(),//Todo: update extra_fees UI
                            "dummy",//Todo: get photo
                            PreferencesManager(this).getId(),
                            activatedCategory.cat_id,
                            null,
                             null,
                            null,
                            arrayListOf())
                    val intent = Intent(this@AddTeacherSkillActivity, ScheduleActivity::class.java).putExtra(Keys.SKILL.key, skill)
                    startActivity(intent)
                }
            }
        }
    }
}


