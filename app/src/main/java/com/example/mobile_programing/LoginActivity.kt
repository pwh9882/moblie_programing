package com.example.mobile_programing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlin.math.log

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener {
            run {
                onLoginClicked()
            }
        }
    }

    fun onLoginClicked(){
        moveMainPage()
    }
    private fun moveMainPage(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}