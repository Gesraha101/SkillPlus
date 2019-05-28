package com.example.lost.skillplus.views.activities

import RetrofitManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.lost.skillplus.views.retrofit.ServiceManager
import com.example.lost.skillplus.models.podos.User
import com.example.lost.skillplus.models.podos.UserResponse
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.lost.skillplus.R.layout.activity_sign_up)
        val shake = AnimationUtils.loadAnimation(this, com.example.lost.skillplus.R.anim.animation) as Animation
        btn_register.setOnClickListener {
            if (NameEditText?.text.toString() == "" || mailEditText?.text.toString() == "" || passwordEditText.text.toString() == "" || pass2EditText.text.toString() == "") {

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
                    pass2EditText.setError("password incorrect")
                    pass2EditText.startAnimation(shake)
                    pass2EditText.requestFocus()
                } else {

                    val user = User(name = NameEditText?.text.toString(),
                            email = mailEditText?.text.toString(),
                            password = passwordEditText?.text.toString()
                            , pic = "pic")
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
                            Toast.makeText(this@SignUpActivity, "Failed to add item two", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }
}