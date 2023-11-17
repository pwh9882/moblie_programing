package com.example.mobile_programing.views

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.mobile_programing.R
import com.example.mobile_programing.databinding.ActivityRoutineCompleteBinding
import com.example.mobile_programing.models.Routine

class RoutineCompleteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRoutineCompleteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_routine_complete)
//        setContentView(R.layout.activity_routine_complete)

        val routine = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("selected_routine", Routine::class.java)
        } else {
            intent.getSerializableExtra("selected_routine") as Routine
        }

//        binding.tvCompletedRoutineName.text = routine!!.name



    }
}