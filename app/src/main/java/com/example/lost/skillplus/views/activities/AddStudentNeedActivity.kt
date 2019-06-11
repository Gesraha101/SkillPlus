package com.example.lost.skillplus.views.activities

import RetrofitManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.managers.BackendServiceManager
import com.example.lost.skillplus.models.managers.PreferencesManager
import com.example.lost.skillplus.models.podos.raw.AddNeed
import com.example.lost.skillplus.models.podos.responses.AddNeedResponse
import kotlinx.android.synthetic.main.activity_add_student_need.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStudentNeedActivity : AppCompatActivity() {

    var category: Int = 0
    var addneed = AddNeed("", "", "", 0, 0)


//    val share = PreferencesManager(this@AddStudentNeedActivity)
//    val userId: Int = share.getId()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student_need)
        val personNames = arrayOf("entertainment", "arts", "food")
        val spinner = findViewById<Spinner>(R.id.categorySpinner)
        if (spinner != null) {
            val arrayAdapter = ArrayAdapter(this, R.layout.spiner_layout, personNames)
            spinner.adapter = arrayAdapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    Toast.makeText(this@AddStudentNeedActivity, " position is " + (position + 1) + " " + personNames[position], Toast.LENGTH_SHORT).show()
                    category = position + 1
//                    Log.d("need", "category is " + category.toString())
//                    addneed = AddNeed(need_name = "b3mel molokhia",
//                            need_desc = "basheak sharkah el shief el shepeny",
//                            need_photo = "mesh batkeshef 3la banat",
//                            cat_id = category,
//                            user_id = 5)
//                    Log.d("need", "addneed.cat_id is " + addneed.cat_id.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }
        }
//        val share = PreferencesManager(this@AddStudentNeedActivity)
//        val userId: Int = share.getId().toInt()

        btn_add_need.setOnClickListener {
            if (category != 0) {
                addneed = AddNeed(need_name = titleEditText.text.toString(),
                        need_desc = descEditText.text.toString(),
                        need_photo = "mesh batkeshef 3la banat",
                        cat_id = category,
                        user_id = PreferencesManager(this@AddStudentNeedActivity).getId())
            }
//            else{
//                addneed = AddNeed(need_name = titleEditText.text.toString(),
//                        need_desc = descEditText.text.toString(),
//                        need_photo = "mesh batkeshef 3la banat",
//                        cat_id = 1,
//                        user_id = 10)
//            }

            val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
            val call: Call<AddNeedResponse>? = service?.addNeed(addneed)
            call?.enqueue(object : Callback<AddNeedResponse> {
                override fun onFailure(call: Call<AddNeedResponse>, t: Throwable) {
                    Toast.makeText(this@AddStudentNeedActivity, "Failed " + t.cause, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<AddNeedResponse>, response: Response<AddNeedResponse>) {

                    Toast.makeText(this@AddStudentNeedActivity, "Done ", Toast.LENGTH_LONG).show()

                }

            })
        }
    }
}


//TODO
//pass user id