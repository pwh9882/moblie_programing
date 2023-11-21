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
import java.util.Calendar

const val ROUTINE_CREATED = 101; // 새로운 루틴 추가
const val ROUTINE_UPDATED = 102; // 루틴 업데이트

class RoutineCreateActivity : AppCompatActivity() {

private lateinit var binding: ActivityRoutineCreateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_routine_create)
        binding.lifecycleOwner = this

        val currentRoutine = intent.getSerializableExtra("selected_routine") as Routine
        var routineReturnCode = ROUTINE_CREATED

        if (currentRoutine.name == "") {
            // Get the current time
            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calendar.get(Calendar.MINUTE)

            // Set the TimePicker to the current time
            binding.tpRoutineStartTime.hour = currentHour
            binding.tpRoutineStartTime.minute = currentMinute
        } else {
            routineReturnCode = ROUTINE_UPDATED
            // Set the fields with the currentRoutine values
            binding.etRoutineName.setText(currentRoutine.name)
            binding.etRoutineDescription.setText(currentRoutine.description)
            binding.tpRoutineStartTime.hour = currentRoutine.routineStartTime / 60
            binding.tpRoutineStartTime.minute = currentRoutine.routineStartTime % 60
        }



        // binding에서 정보를 읽어와서 루틴을 생성하고 DB에 저장
        binding.btnRoutineDetailCreate.setOnClickListener {

            // 유효한 입력인지 verify하기
            val routineName = binding.etRoutineName.text.toString().trim()

            // 루틴명이 공백인 경우 오류 메시지를 표시하고 리턴
            if (routineName.isEmpty()) {
                binding.etRoutineName.error = "루틴명은 필수 입력항목입니다."
                return@setOnClickListener
            }

            val newRoutine = Routine(
                id=currentRoutine.id,
                userId = currentRoutine.userId,
                name = binding.etRoutineName.text.toString(),
                description = binding.etRoutineDescription.text.toString(),
                totalTime = 0,
                routineStartTime = binding.tpRoutineStartTime.hour * 60 + binding.tpRoutineStartTime.minute,
                cards = currentRoutine.cards,
                numStar = currentRoutine.numStar

            )
            val returnIntent = android.content.Intent(
                this,
                MainActivity::class.java
            ).apply {
                putExtra("selected_routine", newRoutine)
            }
            setResult(routineReturnCode, returnIntent)
            finish()
        }

        binding.btnRoutineCreateCancelBtn.setOnClickListener() {
            onBackPressed()
        }

        // 요일 체크박스 설정
        setupCheckBoxes()



    }

    private fun setupCheckBoxes() {
        val checkBoxSun = binding.cbSun
        val checkBoxMon = binding.cbMon
        val checkBoxTue = binding.cbTue
        val checkBoxWed = binding.cbWed
        val checkBoxThu = binding.cbThu
        val checkBoxFri = binding.cbFri
        val checkBoxSat = binding.cbSat

        val checkBoxSunDrawableOff = resources.getDrawable(R.drawable.custom_s_offday)
        val checkBoxMonDrawableOff = resources.getDrawable(R.drawable.custom_m_offday)
        val checkBoxTueDrawableOff = resources.getDrawable(R.drawable.custom_t_offday)
        val checkBoxWedDrawableOff = resources.getDrawable(R.drawable.custom_w_offday)
        val checkBoxThuDrawableOff = resources.getDrawable(R.drawable.custom_t_offday)
        val checkBoxFriDrawableOff = resources.getDrawable(R.drawable.custom_f_offday)
        val checkBoxSatDrawableOff = resources.getDrawable(R.drawable.custom_s_offday)

        val checkBoxSunDrawableOn = resources.getDrawable(R.drawable.custom_s_onday)
        val checkBoxMonDrawableOn = resources.getDrawable(R.drawable.custom_m_onday)
        val checkBoxTueDrawableOn = resources.getDrawable(R.drawable.custom_t_onday)
        val checkBoxWedDrawableOn = resources.getDrawable(R.drawable.custom_w_onday)
        val checkBoxThuDrawableOn = resources.getDrawable(R.drawable.custom_t_onday)
        val checkBoxFriDrawableOn = resources.getDrawable(R.drawable.custom_f_onday)
        val checkBoxSatDrawableOn = resources.getDrawable(R.drawable.custom_s_onday)

        val checkBoxes = listOf(checkBoxSun, checkBoxMon, checkBoxTue, checkBoxWed, checkBoxThu, checkBoxFri, checkBoxSat)
        val checkBoxesDrawableOff = listOf(checkBoxSunDrawableOff, checkBoxMonDrawableOff, checkBoxTueDrawableOff, checkBoxWedDrawableOff, checkBoxThuDrawableOff, checkBoxFriDrawableOff, checkBoxSatDrawableOff)
        val checkBoxesDrawableOn = listOf(checkBoxSunDrawableOn, checkBoxMonDrawableOn, checkBoxTueDrawableOn, checkBoxWedDrawableOn, checkBoxThuDrawableOn, checkBoxFriDrawableOn, checkBoxSatDrawableOn)



        checkBoxes.forEachIndexed { index, checkBox ->
            checkBox.background = checkBoxesDrawableOff[index]
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    checkBox.background = checkBoxesDrawableOn[index]
                } else {
                    checkBox.background = checkBoxesDrawableOff[index]
                }
            }
        }
    }

}