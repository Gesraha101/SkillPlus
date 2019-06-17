package com.example.lost.skillplus.views.activities

import RetrofitManager
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.managers.BackendServiceManager
import com.example.lost.skillplus.models.managers.PreferencesManager
import com.example.lost.skillplus.models.managers.UtilityManager
import com.example.lost.skillplus.models.podos.raw.User
import com.example.lost.skillplus.models.podos.responses.UserResponse
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val shake = AnimationUtils.loadAnimation(this, R.anim.animation) as Animation
        btn_sign_in.setOnClickListener {
            UtilityManager.hideKeyboard(emailEditText)
            mProgressBar.visibility= View.VISIBLE
            val logUser = User(email = emailEditText?.text.toString(),
                    password = passEditText?.text.toString())

            if (passEditText.text.toString().isEmpty() && emailEditText.text.toString().isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(emailEditText.text.toString()).matches()) {
                if (emailEditText?.text.toString() == "") {
                    emailEditText.error = "Required field"
                    emailEditText.startAnimation(shake)
                    emailEditText.requestFocus()
                }
                if (passEditText?.text.toString() == "") {
                    passEditText.error = "Required field"
                    passEditText.startAnimation(shake)
                    passEditText.requestFocus()
                }
                mProgressBar.visibility = View.GONE
            } else {
                val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
                val call: Call<UserResponse>? = service?.loginUser(logUser)
                call?.enqueue(object : Callback<UserResponse> {

                    override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                        mProgressBar.visibility= View.GONE
                        if (response.isSuccessful) {
                            if (response.body()?.status == true) {
                                if (response.body()?.userlogined?.id != null) {
                                    Log.d("user", response.body()?.userlogined?.id.toString())
                                    val share = PreferencesManager(this@LoginActivity)
                                    share.setUser(response.body()?.userlogined!!)
                                    share.setId(response.body()?.userlogined?.id!!)
//                                    share.setName(response.body()?.userlogined?.name!!)
                                } else {
                                    Log.d("user", response.body()?.userlogined?.id.toString())
                                }
                                PreferencesManager(this@LoginActivity).setFlag(true)
                                startActivity(Intent(this@LoginActivity, HomeActivity::class.java))

                                finish()
                            } else if (response.body()?.status == false) {
                                if (response.body()?.message.equals("Wrong password")) {
                                    passEditText.error = "wrong password"
                                    passEditText.startAnimation(shake)
                                    passEditText.requestFocus()

                                } else if (response.body()?.message.equals("Email is not registered")) {
                                    emailEditText.error = "Wrong email"
                                    emailEditText.startAnimation(shake)
                                    emailEditText.requestFocus()

                                }
                            }
                        } else {
                            Toast.makeText(this@LoginActivity, "Failed to log in", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                        mProgressBar.visibility= View.GONE
                        Toast.makeText(this@LoginActivity, "Failed to connect to server ", Toast.LENGTH_LONG).show()
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

    override fun onStart() {
        super.onStart()

        if (PreferencesManager(this@LoginActivity).getFlag()) {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}

