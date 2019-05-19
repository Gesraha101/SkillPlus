package com.example.lost.skillx.views.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.lost.skillx.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btn_sign_in.setOnClickListener {
            // your code to perform when the user clicks on the button
            if (
                    passwordEditText.text.toString() == "123456" && emalEditText.text.toString() =="sherif") {
                Toast.makeText(this@LoginActivity, "You clicked me.", Toast.LENGTH_SHORT).show()
                val i = Intent(this@LoginActivity, SessionActivity::class.java)
                startActivity(i)
            }
            else{
                Toast.makeText(this@LoginActivity, "Try again.", Toast.LENGTH_SHORT).show()
            }
        }
        btn_sign_up.setOnClickListener {
            // your code to perform when the user clicks on the button
            val i = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(i)
        }

    }
}
