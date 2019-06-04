package com.example.lost.skillplus.views.activities

import RetrofitManager
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Patterns
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.lost.skillplus.models.podos.raw.User
import com.example.lost.skillplus.models.podos.responses.UserResponse
import com.example.lost.skillplus.models.retrofit.ServiceManager
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class SignUpActivity : AppCompatActivity() {
    val PICK_PHOTO_REQUEST: Int = 1
    var downloadUri: Uri? = null
    private var filePath: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.lost.skillplus.R.layout.activity_sign_up)
        var mStorageRef: StorageReference
        mStorageRef = FirebaseStorage.getInstance().getReference()
        val shake = AnimationUtils.loadAnimation(this, com.example.lost.skillplus.R.anim.animation) as Animation
        buttonPick.setOnClickListener {
            pickPhotoFromGallery()
        }
        btn_register.setOnClickListener {
            if (NameEditText?.text.toString() == "" || mailEditText.text.toString()== "" || passwordEditText.text.toString() == "" || pass2EditText.text.toString() == "") {

//                passwordEditText.validator().nonEmpty()
//                        .atleastOneNumber()
//                        .atleastOneSpecialCharacters()
//                        .atleastOneUpperCase()
//                        .addErrorCallback{ passwordEditText.error = it }.check()

                if (NameEditText?.text.toString() == "") {
                    NameEditText.setError("Required field")
                    NameEditText.startAnimation(shake)
                    NameEditText.requestFocus()
                }
                if (mailEditText?.text.toString() == "") {
                    mailEditText.setError("Required field")
                    mailEditText.startAnimation(shake)
                    mailEditText.requestFocus()
                }
                if (passwordEditText?.text.toString() == "") {
                    passwordEditText.setError("Required field")
                    passwordEditText.startAnimation(shake)
                    passwordEditText.requestFocus()
                }
                if (pass2EditText?.text.toString() == "") {
                    pass2EditText.setError("Required field")
                    pass2EditText.startAnimation(shake)
                    pass2EditText.requestFocus()
                }
            } else {
                if (passwordEditText.text.toString() != pass2EditText.text.toString()) {
                    mailEditText.setError("wrong pattern")
                    mailEditText.startAnimation(shake)
                    pass2EditText.setError("password incorrect")
                    pass2EditText.startAnimation(shake)

                } else {
                    val file = filePath
                    val riversRef = mStorageRef.child("images/" + UUID.randomUUID().toString())
                    if (file != null) {
                        var uploadTask = riversRef.putFile(file)

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
                                Toast.makeText(this@SignUpActivity, "Uri is   ...   " + downloadUri.toString(), Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(this@SignUpActivity, "Uri is  Faild ...   ", Toast.LENGTH_LONG).show()
                            }
                        }

                        val user = User(name = NameEditText?.text.toString(),
                                email = mailEditText?.text.toString(),
                                password = passwordEditText?.text.toString()
                                , pic = downloadUri.toString())
                        val service = RetrofitManager.getInstance()?.create(ServiceManager::class.java)
                        val call: Call<UserResponse>? = service?.addUser(user)
                        call?.enqueue(object : Callback<UserResponse> {

                            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(this@SignUpActivity, "Successfully Added ", Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(this@SignUpActivity, "Failed to add item one", Toast.LENGTH_SHORT).show()
                                }
                            }
                            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
//                                Toast.makeText(this@SignUpActivity, "Failed to add item two", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
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

            Toast.makeText(this@SignUpActivity, "pic selected " + data.toString(), Toast.LENGTH_LONG).show()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}