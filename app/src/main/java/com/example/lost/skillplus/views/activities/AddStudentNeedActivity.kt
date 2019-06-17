package com.example.lost.skillplus.views.activities

import RetrofitManager
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.enums.Keys
import com.example.lost.skillplus.models.managers.BackendServiceManager
import com.example.lost.skillplus.models.managers.PreferencesManager
import com.example.lost.skillplus.models.podos.raw.AddNeed
import com.example.lost.skillplus.models.podos.responses.AddNeedResponse
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_add_student_need.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class AddStudentNeedActivity : AppCompatActivity() {
    val PICK_PHOTO_REQUEST: Int = 1
    var downloadUri: Uri? = null
    private var filePath: Uri? = null
    var isPic: Boolean = false
    var category: Int = 0
    var addneed = AddNeed("", "", "", 0, 0)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student_need)
        category = intent.getIntExtra(Keys.CATEGORY.key, 0)
        val personNames = arrayOf("entertainment", "arts", "food")
        val spinner = findViewById<Spinner>(R.id.categorySpinner)
        if (spinner != null) {
            val arrayAdapter = ArrayAdapter(this, R.layout.spiner_layout, personNames)
            spinner.adapter = arrayAdapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    Toast.makeText(this@AddStudentNeedActivity, " position is " + (position + 1) + " " + personNames[position], Toast.LENGTH_SHORT).show()
                    category = position + 1
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }
        }
        btn_add_image_need.setOnClickListener(View.OnClickListener {
            pickPhotoFromGallery()
        })

        btn_add_need.setOnClickListener {
            if (isPic) {
                pic_spinner_add_need.visibility = View.VISIBLE
                val file = filePath
                val mStorageRef: StorageReference = FirebaseStorage.getInstance().reference
                val riversRef = mStorageRef.child("images/" + UUID.randomUUID().toString())
                if (file != null) {
                    val uploadTask = riversRef.putFile(file)

                    uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                        if (!task.isSuccessful) {
                            task.exception?.let {
                                throw it
                            }
                        }
                        return@Continuation riversRef.downloadUrl
                    }).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            downloadUri = task.result
                            if (category != 0) {
                                addneed = AddNeed(need_name = titleEditText.text.toString(),
                                        need_desc = descEditText.text.toString(),
                                        need_photo = downloadUri.toString(),
                                        cat_id = category,
                                        user_id = PreferencesManager(this@AddStudentNeedActivity).getId())
                            }

                        }
                    }
                }
            } else {
                if (category != 0) {
                    addneed = AddNeed(need_name = titleEditText.text.toString(),
                            need_desc = descEditText.text.toString(),
                            need_photo = "mesh batkeshef 3la banat",
                            cat_id = category,
                            user_id = PreferencesManager(this@AddStudentNeedActivity).getId())
                }
            }

            val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
            val call: Call<AddNeedResponse>? = service?.addNeed(addneed)
            call?.enqueue(object : Callback<AddNeedResponse> {
                override fun onFailure(call: Call<AddNeedResponse>, t: Throwable) {
                    Toast.makeText(this@AddStudentNeedActivity, "Failed " + t.cause, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<AddNeedResponse>, response: Response<AddNeedResponse>) {

                    val i = Intent(this@AddStudentNeedActivity, HomeActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    Snackbar.make(it, "Added Successfully !", Snackbar.LENGTH_INDEFINITE).show()
                    Handler().postDelayed({
                        startActivity(i)
                        finish()
                    }, 2500)


                }

            })
        }
    }

    private fun pickPhotoFromGallery() {
        val pickImageIntent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(pickImageIntent, PICK_PHOTO_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  data: Intent?) {
        if (resultCode == Activity.RESULT_OK
                && requestCode == PICK_PHOTO_REQUEST) {

            filePath = data?.data
            isPic = true

            //   Toast.makeText(this@SignUpActivity, "pic selected " + data.toString(), Toast.LENGTH_LONG).show()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
