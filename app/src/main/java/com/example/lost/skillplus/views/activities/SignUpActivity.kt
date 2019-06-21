package com.example.lost.skillplus.views.activities

import RetrofitManager
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.lost.skillplus.R
import com.example.lost.skillplus.helpers.managers.BackendServiceManager
import com.example.lost.skillplus.helpers.managers.UtilityManager
import com.example.lost.skillplus.helpers.podos.raw.User
import com.example.lost.skillplus.helpers.podos.responses.UserResponse
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
                /*
                     email pattern ss@gg.kk
                 */
        val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        /*
           Only alphanumeric characters (no special characters)
            At least 8 characters long
        */
        val PASS_REGEX = "^[a-zA-Z\\d]{8,}"
        /*
            Only alphanumeric characters
        */
        val NAME_REGEX = "^([A-Za-z](.*))"


        fun isEmailValid(email: String): Boolean {
            return EMAIL_REGEX.toRegex().matches(email)
        }

        fun isPassValid(pass: String): Boolean {
            return PASS_REGEX.toRegex().matches(pass)
        }

        fun isNameValid(name: String): Boolean {
            return NAME_REGEX.toRegex().matches(name)
        }

    }

    val PICK_PHOTO_REQUEST: Int = 1
    var downloadUri: Uri? = null
    private var filePath: Uri? = null
    var isPic: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        pic_spinner.visibility = View.INVISIBLE
        val shake = AnimationUtils.loadAnimation(this, R.anim.animation) as Animation
        buttonPick.setOnClickListener {

            pickPhotoFromGallery()
        }
        btn_register.setOnClickListener {
            UtilityManager.hideKeyboard(NameEditText)
            if (NameEditText?.text.toString() == "" || mailEditText.text.toString() == "" || passwordEditText.text.toString() == "" || pass2EditText.text.toString() == "") {

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
            } else if (!isEmailValid(mailEditText.text.toString())) {

                mailEditText.error = "Wrong Email"
                mailEditText.startAnimation(shake)
                mailEditText.requestFocus()


            } else if (!isPassValid(passwordEditText.text.toString())) {

                passwordEditText.error = "Your password must be at least 8 characters/numbers"
                passwordEditText.startAnimation(shake)
                passwordEditText.requestFocus()


            } else if (!isNameValid(NameEditText.text.toString())) {

                NameEditText.error = "Wrong Name"
                NameEditText.startAnimation(shake)
                NameEditText.requestFocus()


            } else {
                if (passwordEditText.text.toString() != pass2EditText.text.toString()) {
                    passwordEditText.error = "password incorrect"
                    passwordEditText.startAnimation(shake)
                    pass2EditText.error = "password incorrect"
                    pass2EditText.startAnimation(shake)

                } else {
                    if (isPic) {
                        pic_spinner.visibility = View.VISIBLE
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
                                    Toast.makeText(this@SignUpActivity, "Added picture successfully", Toast.LENGTH_LONG).show()

                                    val user = User(name = NameEditText?.text.toString(),
                                            email = mailEditText?.text.toString(),
                                            password = passwordEditText?.text.toString(),
                                            pic = downloadUri.toString())
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
                                            Toast.makeText(this@SignUpActivity, " user email already exists " + t.cause, Toast.LENGTH_LONG).show()
                                        }
                                    })


                                } else {
                                    Toast.makeText(this@SignUpActivity, "Uri Failed ...   ", Toast.LENGTH_LONG).show()
                                }
                            }

                        }
                    } else {

                        val user = User(name = NameEditText?.text.toString(),
                                email = mailEditText?.text.toString(),
                                password = passwordEditText?.text.toString(),
                                pic = "https://firebasestorage.googleapis.com/v0/b/skillplus-6d8b3.appspot.com/o/images%2Fuser-5.png?alt=media&token=b2d4da3e-d672-4f44-94f9-bb469158317c")
                        val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
                        val call: Call<UserResponse>? = user.let { it1 -> service?.addUser(it1) }
                        call?.enqueue(object : Callback<UserResponse> {

                            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                                if (response.isSuccessful) {
                                    if (response.body()!!.status) {
                                        Toast.makeText(this@SignUpActivity, "Successfully Added ", Toast.LENGTH_LONG).show()
                                        val i = Intent(this@SignUpActivity, LoginActivity::class.java)
                                        startActivity(i)
                                        finish()
                                    } else if (response.body()!!.message.equals("email is exist")) {
                                        Toast.makeText(this@SignUpActivity, "Email already exists!", Toast.LENGTH_LONG).show()
                                    }
                                } else {
                                    Toast.makeText(this@SignUpActivity, "Failed to add item one", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                                Toast.makeText(this@SignUpActivity, " Failed to conncet to server" + t.cause, Toast.LENGTH_LONG).show()
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
            isPic = true

            Toast.makeText(this@SignUpActivity, "pic selected " + data.toString(), Toast.LENGTH_LONG).show()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}