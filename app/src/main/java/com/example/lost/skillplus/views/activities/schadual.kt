package com.example.lost.skillplus.views.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.adapters.CustomAdapter
import com.example.lost.skillplus.models.podos.raw.Schadual
import com.example.lost.skillplus.models.podos.raw.Skill
import java.util.*

class schadual : AppCompatActivity() {
    private var lv: ListView? = null
    private var modelArrayList: ArrayList<Schadual>? = null
    private var customAdapter: CustomAdapter? = null
    private var btnselect: Button? = null
    private var btndeselect: Button? = null
    private var btnnext: Button? = null
    private var skillFromDetailsFragment : Skill? = null
    private var schadualDatail : List<Long>? = null
    private val animallist = arrayOf("Lion", "Tiger", "Leopard", "Cat", "dog", "3asfora")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schadual)
        skillFromDetailsFragment = intent.getSerializableExtra("Skill") as Skill
        schadualDatail = skillFromDetailsFragment!!.schedule

        lv = findViewById(R.id.lv)
        btnnext = findViewById(R.id.next) as Button

        modelArrayList = getModel(false)
        customAdapter = CustomAdapter(this, modelArrayList!!)
        lv!!.adapter = customAdapter

        btnnext!!.setOnClickListener {
            val intent = Intent(this@schadual, PaymentActivity::class.java)
            if(skillFromDetailsFragment?.skill_id != null) {
                Log.d("schadual", skillFromDetailsFragment?.skill_id.toString())
                //       Toast.makeText(this@schadual, skillFromDetailsFragment!!.cat_id , Toast.LENGTH_LONG)
                intent.putExtra("SkillId", skillFromDetailsFragment!!.skill_id)
                startActivity(intent)
            }
        }
    }

    private fun getModel(isSelect: Boolean): ArrayList<Schadual> {
        val list = ArrayList<Schadual>()
        for (i in 0..((schadualDatail?.size ?: 2) - 1)) {

            val model = Schadual()
            model.setSelecteds(isSelect)
            schadualDatail?.get(i)?.let { model.setAnimals(it.toString()) }
            list.add(model)
        }
        return list
    }

}
