package com.example.mobile_programing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.mobile_programing.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    // Data binding
    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Data binding
        // setContentView(R.layout.activity_login)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.loginButton.setOnClickListener {
            run {
                onLoginClicked()
            }
        }
    }

    private fun onLoginClicked(){
        moveMainPage()
    }
    private fun moveMainPage(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}