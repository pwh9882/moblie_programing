package com.example.mobile_programing.views

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.mobile_programing.R
import com.example.mobile_programing.databinding.ActivityRoutineProgressBinding
import com.example.mobile_programing.models.Card
import com.example.mobile_programing.models.Routine
import com.example.mobile_programing.viewModel.RoutineProgressViewModel
import com.example.mobile_programing.views.fragments.CardDisplayFragment

class RoutineProgressActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRoutineProgressBinding

    private val routineProgressViewModel : RoutineProgressViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_routine_progress)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_routine_progress)
        binding.lifecycleOwner = this

        val routine = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("selected_routine", Routine::class.java)
        } else {
            intent.getSerializableExtra("selected_routine") as Routine
        }
        routineProgressViewModel.updateCurrentRoutineData(routine!!)
        routineProgressViewModel.updateCurrentCardIndexData(0)
        routineProgressViewModel.startRoutineTimer()

        // fragment 설정
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val cardDisplayFragment = CardDisplayFragment.newInstance(routineProgressViewModel)
        fragmentTransaction.replace(R.id.fl_routine_progress, CardDisplayFragment())
        fragmentTransaction.commit()
        supportFragmentManager.fragments[0].arguments = Bundle().apply {
            putSerializable("selected_routine", routine)
        }

        // binding
        routineProgressViewModel.currentRoutineTime.observe(this) {
            binding.tvRoutineProgressTotalTime.text = it.toString()
        }

        // onClickListener
        binding.btnRoutineProgressNextCard.setOnClickListener {
            if(routineProgressViewModel.currentCardIndex.value!! < routineProgressViewModel.currentRoutine.value!!.cards.size - 1)
                routineProgressViewModel.updateCurrentCardIndexData(routineProgressViewModel.currentCardIndex.value!! + 1)
            // exit condition
            else {
                Toast.makeText(this, "루틴이 종료되었습니다.", Toast.LENGTH_SHORT).show()
                // TODO: 루틴 종료에 관한 DB 업데이트 처리하기
                finish()
            }
        }
        binding.btnRoutineProgressPreviousCard.setOnClickListener {
            if(routineProgressViewModel.currentCardIndex.value!! > 0)
                routineProgressViewModel.updateCurrentCardIndexData(routineProgressViewModel.currentCardIndex.value!! - 1)
        }
        binding.btnRoutineProgressSkipCard.setOnClickListener {
            // 현재는 next 와 동일. 추후에 skip 기능 추가
            if(routineProgressViewModel.currentCardIndex.value!! < routineProgressViewModel.currentRoutine.value!!.cards.size - 1)
                routineProgressViewModel.updateCurrentCardIndexData(routineProgressViewModel.currentCardIndex.value!! + 1)
        }


        val cardCreateResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == CARD_CREATED) {
                // Got data from other activity and process that data
                Log.e("Activity result: ","${result.data}")
                // 현재 카드 리스트에 결과 카드 추가(현재 카드 번호를 이용해 다음 카드 위치로 삽입.)
                routineProgressViewModel.currentRoutine.value!!.cards.add(
                    routineProgressViewModel.currentCardIndex.value!! + 1,
                    result.data!!.getSerializableExtra("selected_card") as Card
                )

                // 다음 카드로 이동
                routineProgressViewModel.updateCurrentCardIndexData(routineProgressViewModel.currentCardIndex.value!! + 1)
            }
        }
        binding.btnRoutineProgressCreateCard.setOnClickListener {
            cardCreateResultLauncher.launch(Intent(this, CardDetailActivity::class.java).apply {
                // TODO: id는 데이터 베이스 용으로 적절하게 수정해야함.
                var emptyCard = Card(
                    id = 1000,
                    name = "추가생성카드",
                    preTimerSecs = 0,
                    preTimerAutoStart = true,
                    activeTimerSecs = 0,
                    activeTimerAutoStart = true,
                    postTimerSecs = 0,
                    postTimerAutoStart = true,
                    sets = 0,
                    additionalInfo = arrayListOf()
                )
                putExtra("selected_card", emptyCard)
                putExtra("createFlag", true)
            })
        }

        routineProgressViewModel.currentCardIndex.observe(this) {
//            if(it == routineProgressViewModel.currentRoutine.value!!.cards.size - 1)
//                binding.btnRoutineProgressNextCard.text = "루틴 종료"
//            else
//                binding.btnRoutineProgressNextCard.text = "다음 카드"
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        // 전체 카운터 스레드 종료
        routineProgressViewModel.stopRoutineTimer()
        routineProgressViewModel.stopCardTimer()
    }
}