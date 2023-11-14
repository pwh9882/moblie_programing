package com.example.mobile_programing.views

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.mobile_programing.R
import com.example.mobile_programing.databinding.ActivityRoutineCreateBinding
import com.example.mobile_programing.models.Routine

const val ROUTINE_CREATED = 101; // 새로운 루틴 추가
const val ROUTINE_UPDATED = 102; // 루틴 업데이트

class RoutineCreateActivity : AppCompatActivity() {

private lateinit var binding: ActivityRoutineCreateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_routine_create)
        binding.lifecycleOwner = this

        val currentRoutine = intent.getSerializableExtra("selected_routine") as Routine

        // binding에서 정보를 읽어와서 루틴을 생성하고 DB에 저장
        binding.btnRoutineDetailCreate.setOnClickListener {

            // TODO: 유효한 입력인지 verify하기
            val newRoutine = Routine(
                id=currentRoutine.id,
                userId = currentRoutine.userId,
                name = binding.etRoutineName.text.toString(),
                description = binding.etRoutineDescription.text.toString(),
                totalTime = 0,
                routineStartTime = binding.etRoutineStartTime.text.toString().toInt(),
                cards = arrayListOf()
            )
            val returnIntent = android.content.Intent(
                this,
                MainActivity::class.java
            ).apply {
                putExtra("selected_routine", newRoutine)
            }
            setResult(ROUTINE_CREATED, returnIntent)
            finish()
        }
    }

}