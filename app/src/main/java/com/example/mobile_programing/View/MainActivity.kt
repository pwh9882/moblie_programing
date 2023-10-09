package com.example.mobile_programing.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.mobile_programing.DbDemoActivity
import com.example.mobile_programing.R
import com.example.mobile_programing.databinding.ActivityMainBinding
import com.example.mobile_programing.viewModel.MainViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    // Data binding
    private lateinit var binding: ActivityMainBinding

    // viewModel
    private val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Data binding
//        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        val user = Firebase.auth.currentUser
        if (user != null) {
            // User is signed in
            binding.test.text = user.email
            ///asdf
        } else {
            // No user is signed in
            binding.test.text = "cannot load"
        }

        binding.bttnConnect.setOnClickListener {//데모버튼 클릭시 이벤트
            startActivity(Intent(this, DbDemoActivity::class.java))

        }
    }
}