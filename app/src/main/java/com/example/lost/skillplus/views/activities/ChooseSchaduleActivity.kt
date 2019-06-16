package com.example.lost.skillplus.views.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.ListView
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.adapters.CustomAdapter
import com.example.lost.skillplus.models.enums.Keys
import com.example.lost.skillplus.models.podos.raw.Schadule
import com.example.lost.skillplus.models.podos.raw.Skill
import java.util.*

class ChooseSchaduleActivity : AppCompatActivity() {
    private var lv: ListView? = null
    private var modelArrayList: ArrayList<Schadule>? = null
    private var customAdapter: CustomAdapter? = null
    private var btnnext: Button? = null
    private var skillFromDetailsFragment : Skill? = null
    private lateinit var schadualDatail : List<Long>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_schadule)
        skillFromDetailsFragment = intent.getSerializableExtra("Skill") as Skill
        schadualDatail = skillFromDetailsFragment!!.schedule!!

        lv = findViewById(R.id.lv)
        btnnext = findViewById(R.id.next)

        modelArrayList = getModel(false)
        customAdapter = CustomAdapter(this, modelArrayList!!)
        lv!!.adapter = customAdapter

//
//        if(modelArrayList!!.isEmpty()) {
//           btnnext.visibility = View.GONE
//        }
        btnnext!!.setOnClickListener {

            val intent = Intent(this@ChooseSchaduleActivity, PaymentActivity::class.java)
            if(skillFromDetailsFragment?.skill_id != null) {
                intent.putExtra(Keys.SKILL.key, skillFromDetailsFragment)
                startActivity(intent)
            }
        }
    }

    private fun getModel(isSelect: Boolean): ArrayList<Schadule> {
        val list = ArrayList<Schadule>()
        for (i in 0 until schadualDatail.size) {
            val model = Schadule()
            model.setSelecteds(isSelect)
            schadualDatail.get(i).let { model.setSchedule(it.toString()) }
            list.add(model)
        }
        return list
    }

}
