package com.example.lost.skillplus.views.activities

import RetrofitManager
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.example.lost.skillplus.R
import com.example.lost.skillplus.helpers.enums.Keys
import com.example.lost.skillplus.helpers.managers.BackendServiceManager
import com.example.lost.skillplus.helpers.managers.PreferencesManager
import com.example.lost.skillplus.helpers.podos.raw.AddNeed
import com.example.lost.skillplus.helpers.podos.raw.Category
import com.example.lost.skillplus.helpers.podos.responses.AddNeedResponse
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
    var category: Category? = null
    var addneed = AddNeed("", "", "", 0, 0)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student_need)
        var progressOverlay: View = findViewById(R.id.progress_overlay)

        category = intent.getSerializableExtra(Keys.CATEGORY.key) as Category

        btn_add_image_need.setOnClickListener {
            pickPhotoFromGallery()
        }

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
                            if (category!!.cat_id != 0) {
                                addneed = AddNeed(need_name = titleEditText.text.toString(),
                                        need_desc = descEditText.text.toString(),
                                        need_photo = downloadUri.toString(),
                                        cat_id = category!!.cat_id,
                                        user_id = PreferencesManager(this@AddStudentNeedActivity).getId())
                            }

                        }
                    }
                }
            } else {
                if (category!!.cat_id != 0) {
                    addneed = AddNeed(need_name = titleEditText.text.toString(),
                            need_desc = descEditText.text.toString(),
                            need_photo = category!!.cat_photo,
                            cat_id = category!!.cat_id,
                            user_id = PreferencesManager(this@AddStudentNeedActivity).getId())
                }
            }
            progressOverlay.visibility = View.VISIBLE
            animateView(progressOverlay, View.VISIBLE, 0.4f, 200)


            val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
            val call: Call<AddNeedResponse>? = service?.addNeed(addneed)
            call?.enqueue(object : Callback<AddNeedResponse> {
                override fun onFailure(call: Call<AddNeedResponse>, t: Throwable) {
                    Toast.makeText(this@AddStudentNeedActivity, "Failed " + t.cause, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<AddNeedResponse>, response: Response<AddNeedResponse>) {

                    val i = Intent(this@AddStudentNeedActivity, HomeActivity::class.java)
                    Handler().postDelayed({
                        animateView(progressOverlay, View.GONE, 0f, 200)
                        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        i.putExtra("isComingAfterSubmition", true)
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

    fun animateView(view: View, toVisibility: Int, toAlpha: Float, duration: Int) {
        val show = toVisibility == View.VISIBLE
        if (show) {
            view.alpha = 0f
        }
        view.visibility = View.VISIBLE
        view.animate()
                .setDuration(duration.toLong())
                .alpha(if (show) toAlpha else 0f)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        view.visibility = toVisibility
                    }
                })
    }
}
