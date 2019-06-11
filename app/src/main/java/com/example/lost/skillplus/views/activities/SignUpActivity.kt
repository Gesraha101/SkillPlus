package com.example.lost.skillplus.views.activities

import RetrofitManager
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.lost.skillplus.models.managers.BackendServiceManager
import com.example.lost.skillplus.models.podos.raw.User
import com.example.lost.skillplus.models.podos.responses.UserResponse
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class SignUpActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"

        fun isEmailValid(email: String): Boolean {
            return EMAIL_REGEX.toRegex().matches(email)
        }
        //isEmailValid
    }

    val PICK_PHOTO_REQUEST: Int = 1
    var downloadUri: Uri? = null
    private var filePath: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.lost.skillplus.R.layout.activity_sign_up)
        val shake = AnimationUtils.loadAnimation(this, com.example.lost.skillplus.R.anim.animation) as Animation
        buttonPick.setOnClickListener {
            pickPhotoFromGallery()
        }
        btn_register.setOnClickListener {
            if (NameEditText?.text.toString() == "" || mailEditText.text.toString() == ""  || passwordEditText.text.toString() == "" || pass2EditText.text.toString() == "") {

                if (NameEditText?.text.toString() == "") {
                    NameEditText.error = "Required field"
                    NameEditText.startAnimation(shake)
                    NameEditText.requestFocus()
                }
                if (mailEditText?.text.toString() == "") {
                    mailEditText.error = "Required field"
                    mailEditText.startAnimation(shake)
                    mailEditText.requestFocus()
                }
                if (passwordEditText?.text.toString() == "") {
                    passwordEditText.error = "Required field"
                    passwordEditText.startAnimation(shake)
                    passwordEditText.requestFocus()
                }
                if (pass2EditText?.text.toString() == "") {
                    pass2EditText.error = "Required field"
                    pass2EditText.startAnimation(shake)
                    pass2EditText.requestFocus()
                }
            }else if(!isEmailValid(mailEditText.text.toString())){

                mailEditText.error = "Wrong Email"
                mailEditText.startAnimation(shake)
                mailEditText.requestFocus()


            }else {
                if (passwordEditText.text.toString() != pass2EditText.text.toString()) {
                    mailEditText.error = "wrong pattern"
                    mailEditText.startAnimation(shake)
                    pass2EditText.error = "password incorrect"
                    pass2EditText.startAnimation(shake)

                } else {
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
                                Toast.makeText(this@SignUpActivity, "Uri is   ...   " + downloadUri.toString(), Toast.LENGTH_LONG).show()

                                val user = User(name = NameEditText?.text.toString(),
                                        email = mailEditText?.text.toString(),
                                        password = passwordEditText?.text.toString()
                                        , pic = if (downloadUri != null) downloadUri.toString() else "https://firebasestorage.googleapis.com/v0/b/skillplus-6d8b3.appspot.com/o/images%2Fuser-5.png?alt=media&token=b2d4da3e-d672-4f44-94f9-bb469158317c")

                                val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
                                val call: Call<UserResponse>? = user.let { it1 -> service?.addUser(it1) }
                                call?.enqueue(object : Callback<UserResponse> {

                                    override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                                        if (response.isSuccessful) {
                                            Toast.makeText(this@SignUpActivity, "Successfully Added ", Toast.LENGTH_LONG).show()
                                            val i = Intent(this@SignUpActivity, LoginActivity::class.java)
                                            startActivity(i)
                                            finish()
                                        } else {
                                            Toast.makeText(this@SignUpActivity, "Failed to add item one", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
//                                Toast.makeText(this@SignUpActivity, "Failed to add item two", Toast.LENGTH_SHORT).show()
                                    }
                                })


                            } else {
                                Toast.makeText(this@SignUpActivity, "Uri is  Faild ...   ", Toast.LENGTH_LONG).show()
                            }
                        }

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