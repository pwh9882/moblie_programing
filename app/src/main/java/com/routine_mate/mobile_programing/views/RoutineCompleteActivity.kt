package com.routine_mate.mobile_programing.views

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.routine_mate.mobile_programing.R
import com.routine_mate.mobile_programing.databinding.ActivityRoutineCompleteBinding
import com.routine_mate.mobile_programing.models.Routine

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
        val totalElapsedTime = intent.getIntExtra("totalElapsedTime", 0)
        // 시간 초 포맷을 시간 분 초 포맷으로 바꾸기
        val hours = totalElapsedTime / 3600
        val minutes = (totalElapsedTime % 3600) / 60
        val seconds = totalElapsedTime % 60
        val timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        binding.tvRoutineCompleteElapsed.text = timeString
//        binding.tvCompletedRoutineName.text = routine!!.name


    }
}