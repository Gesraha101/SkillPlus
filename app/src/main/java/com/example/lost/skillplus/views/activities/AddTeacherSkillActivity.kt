package com.example.lost.skillplus.views.activities


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.lost.skillplus.R
import com.example.lost.skillplus.helpers.enums.Keys
import com.example.lost.skillplus.helpers.managers.PreferencesManager
import com.example.lost.skillplus.helpers.podos.raw.Category
import com.example.lost.skillplus.helpers.podos.raw.Skill
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_add_teacher_skill.*
import java.util.*

class AddTeacherSkillActivity : AppCompatActivity() {

    lateinit var activatedCategory: Category
    val PICK_PHOTO_REQUEST: Int = 1
    var downloadUri: Uri? = null
    private var filePath: Uri? = null
    var isPic: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_teacher_skill)
        setSupportActionBar(toolbar)

        activatedCategory = intent.getSerializableExtra(Keys.CATEGORY.key) as Category

        val shake = AnimationUtils.loadAnimation(this, R.anim.animation) as Animation
        var badEntry: Boolean

        btn_add_image.setOnClickListener {
            pickPhotoFromGallery()
        }
        btn_proceed.setOnClickListener {
            badEntry = false
            if (eT_NumberOfSessions.text.toString().toIntOrNull() == null || eT_NumberOfSessions.text.toString().toIntOrNull() == 0) {
                eT_NumberOfSessions.error = "A postive number is required"
                eT_NumberOfSessions.startAnimation(shake)
                eT_NumberOfSessions.requestFocus()
                badEntry = true
            }
            if (eT_SessionDuration.text.toString().toIntOrNull() == null || eT_SessionDuration.text.toString().toIntOrNull() == 0) {
                eT_SessionDuration.error = "A postive number is required"
                eT_SessionDuration.startAnimation(shake)
                eT_SessionDuration.requestFocus()
                badEntry = true
            }
            if (eT_Price.text.toString().toFloatOrNull() == null || eT_Price.text.toString().toIntOrNull() == 0) {
                eT_Price.error = "A postive number is required"
                eT_Price.startAnimation(shake)
                eT_Price.requestFocus()
                badEntry = true
            }
            if (eT_ExtraFees.text.toString().toFloatOrNull() == null) {
                eT_ExtraFees.error = "A number is required"
                eT_ExtraFees.startAnimation(shake)
                eT_ExtraFees.requestFocus()
                badEntry = true
            }
            if (!badEntry) {
                if (isPic) {
                    pic_spinner_add_skill.visibility = View.VISIBLE
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

                                var skill = Skill(
                                        null,
                                        eT_Title?.text.toString(),
                                        eT_Description?.text.toString(),
                                        eT_NumberOfSessions.text.toString().toInt(),
                                        eT_Price?.text.toString().toFloat(),
                                        eT_SessionDuration?.text.toString().toFloat(),
                                        eT_ExtraFees?.text.toString().toFloat(),
                                        downloadUri.toString(),
                                        PreferencesManager(this@AddTeacherSkillActivity).getId(),
                                        activatedCategory.cat_id,
                                        null,
                                        null,
                                        null,
                                        arrayListOf(), false)
                                val intent = Intent(this@AddTeacherSkillActivity, ScheduleActivity::class.java).putExtra(Keys.SKILL.key, skill)
                                startActivity(intent)
                            }
                        }
                    }
                } else {
                    var skill = Skill(
                            null,
                            eT_Title?.text.toString(),
                            eT_Description?.text.toString(),
                            eT_NumberOfSessions.text.toString().toInt(),
                            eT_Price?.text.toString().toFloat(),
                            eT_SessionDuration?.text.toString().toFloat(),
                            eT_ExtraFees?.text.toString().toFloat(),
                            activatedCategory.cat_photo,
                            PreferencesManager(this@AddTeacherSkillActivity).getId(),
                            activatedCategory.cat_id,
                            null,
                            null,
                            null,
                            arrayListOf(), false)
                    val intent = Intent(this@AddTeacherSkillActivity, ScheduleActivity::class.java).putExtra(Keys.SKILL.key, skill)
                    startActivity(intent)
                }

            }
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



