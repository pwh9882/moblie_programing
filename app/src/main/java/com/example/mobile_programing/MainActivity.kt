package com.example.mobile_programing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.mobile_programing.databinding.ActivityMainBinding
import com.example.mobile_programing.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {

    // Data binding
    private lateinit var binding : ActivityMainBinding

    // viewModel
    private val viewModel : MainViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Data binding
//        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

    }
}