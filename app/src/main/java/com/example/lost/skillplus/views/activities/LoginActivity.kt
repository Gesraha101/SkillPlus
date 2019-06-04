package com.example.lost.skillplus.views.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.podos.raw.User
import com.example.lost.skillplus.models.managers.BackendServiceManager
import com.example.lost.skillplus.models.podos.responses.UserResponse
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val shake = AnimationUtils.loadAnimation(this, R.anim.animation) as Animation
        btn_sign_in.setOnClickListener {
            val loguser = User(email = emailEditText?.text.toString(),
                    password = passEditText?.text.toString())

            if (passEditText.text.toString().isEmpty() && emailEditText.text.toString().isEmpty()) {
                if (emailEditText?.text.toString() == "") {
                    emailEditText.setError("Required field")
                    emailEditText.startAnimation(shake)
                    emailEditText.requestFocus()
                }
                if (passEditText?.text.toString() == "") {
                    passEditText.setError("Required field")
                    passEditText.startAnimation(shake)
                    passEditText.requestFocus()
                }
            }
            else{
                val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
                val call: Call<UserResponse>? = service?.loginUser(loguser)
                call?.enqueue(object : Callback<UserResponse> {

                    override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                        if (response.isSuccessful) {
                            if(response.body()?.status  == true) { startActivity(Intent(this@LoginActivity, HomeActivity::class.java))}
                                else{ Toast.makeText(this@LoginActivity, "la ya habiby " +response.body(), Toast.LENGTH_LONG).show()
                                emailEditText.setError("Wrong email")
                                emailEditText.startAnimation(shake)
                                emailEditText.requestFocus()
                                passEditText.setError("wrong password")
                                passEditText.startAnimation(shake)
                                passEditText.requestFocus()
                            }



//                            val i = Intent(this@LoginActivity, SessionActivity::class.java)
//                            startActivity(i)
                        } else {
                            Toast.makeText(this@LoginActivity, "Failed to log in", Toast.LENGTH_LONG).show()


                        }
                    }

                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, "Failed" + t.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
        btn_sign_up.setOnClickListener {
            // your code to perform when the user clicks on the button_layout
            val i = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(i)
        }

    }
}
