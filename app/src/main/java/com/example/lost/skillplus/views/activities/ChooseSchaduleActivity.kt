package com.example.lost.skillplus.views.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.ListView
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.adapters.CustomAdapter
import com.example.lost.skillplus.models.podos.raw.Schedule
import com.example.lost.skillplus.models.podos.raw.Skill
import java.util.*

class ChooseSchaduleActivity : AppCompatActivity() {
    private var lv: ListView? = null
    private var modelArrayList: ArrayList<Schedule>? = null
    private var customAdapter: CustomAdapter? = null
    private var btnnext: Button? = null
    private var skillFromDetailsFragment : Skill? = null
    private lateinit var schadualDatail : List<Long>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_schadule)
        skillFromDetailsFragment = intent.getSerializableExtra("Skill") as Skill
        schadualDatail = skillFromDetailsFragment!!.schedule!!

        lv = this.findViewById(R.id.lv)
        btnnext = findViewById(R.id.next)

        modelArrayList = getModel(false)
        customAdapter = CustomAdapter(this, modelArrayList!!)
        lv!!.adapter = customAdapter

        btnnext!!.setOnClickListener {
            val intent = Intent(this@ChooseSchaduleActivity, PaymentActivity::class.java)
            if(skillFromDetailsFragment?.skill_id != null) {
                Log.d("SchaduleActivity", skillFromDetailsFragment?.skill_id.toString())
                //       Toast.makeText(this@SchaduleActivity, skillFromDetailsFragment!!.cat_id , Toast.LENGTH_LONG)
                intent.putExtra("SkillId", skillFromDetailsFragment!!.skill_id)
                startActivity(intent)
            }
        }
    }

    private fun getModel(isSelect: Boolean): ArrayList<Schedule> {
        val list = ArrayList<Schedule>()
        for (i in 0 until (schadualDatail?.size ?: 2)) {

            val model = Schedule()
            model.setSelecteds(isSelect)
            schadualDatail?.get(i)?.let { model.setAnimals(it.toString()) }
            list.add(model)
        }
        return list
    }

}
