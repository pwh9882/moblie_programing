package com.example.mobile_programing.views

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.mobile_programing.R
import com.example.mobile_programing.databinding.ActivityMainBinding
import com.example.mobile_programing.views.adapters.RoutineAdapter
import com.example.mobile_programing.viewModel.MainViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    // Data binding
    private lateinit var binding : ActivityMainBinding

    // viewModel
    private val viewModel : MainViewModel by viewModels()



    @SuppressLint("NotifyDataSetChanged")
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

        viewModel.updateRoutineListData()
        val routineAdapter = RoutineAdapter(binding, viewModel, this)
        binding.rvRoutineList.adapter = routineAdapter
        viewModel.routineList.observe(this, Observer {
            routineAdapter.routineLint = it
            routineAdapter.notifyDataSetChanged()
//            binding.tvDebugRoutinedata.text = it.toString()
        })

    }
}