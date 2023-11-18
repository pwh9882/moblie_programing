package com.example.mobile_programing.views

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.example.mobile_programing.R
import com.example.mobile_programing.databinding.ActivityRoutineProgressBinding
import com.example.mobile_programing.models.Card
import com.example.mobile_programing.models.Routine
import com.example.mobile_programing.viewModel.RoutineProgressViewModel
import com.example.mobile_programing.views.fragments.CardDisplayFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

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
        routineProgressViewModel.updateCurrentRoutineData(routine!!);
        routineProgressViewModel.updateCurrentCardIndexData(0);
        routineProgressViewModel.startRoutineTimer();

        // Set up the fragment
        initCardDisplayFragment(routine);

        // Observe the routine time
        updateRoutineTime();

        // Set up click listeners
        handleNextCardClick();
        handlePreviousCardClick();
        handleSkipCardClick();
        handleCardCreation();

    }

    override fun onDestroy() {
        super.onDestroy()

        // 전체 카운터 스레드 종료
        routineProgressViewModel.stopRoutineTimer()
        routineProgressViewModel.stopCardTimer()
    }


    // Initialize card display fragment
    private fun initCardDisplayFragment(routine: Routine) {
        val cardDisplayFragment = CardDisplayFragment.newInstance(routineProgressViewModel)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_routine_progress, CardDisplayFragment())
            .commit()
    //    supportFragmentManager.fragments[0].arguments = Bundle().apply {
    //        putSerializable("selected_routine", routine)
    //    }
        routineProgressViewModel.currentCardProgress.observe(this) {
            // 배경화면 색 설정
//            when(it) {
//                0 -> binding.clRoutineProgress.setBackgroundResource(R.color.light_green)
//                1 -> binding.clRoutineProgress.setBackgroundResource(R.color.bright_orange)
//                2 -> binding.clRoutineProgress.setBackgroundResource(R.color.light_purple)
//            }
        }
    }

    // Update routine time
    private fun updateRoutineTime() {
        routineProgressViewModel.currentRoutineTime.observe(this) {
            binding.tvRoutineProgressTotalTime.text = it.toString()
        }
    }

    // Handle next card click
    private fun handleNextCardClick() {
        binding.btnRoutineProgressNextCard.setOnClickListener {
            // card set이 끝나지 않았다면 pregress를 증가, progress==2로 현 진행 세트를 마쳤으면 세트 수 증가, 모두 완료하면 다음 카드로 이동
            if (routineProgressViewModel.currentCardProgress.value!! < 2)
                routineProgressViewModel.setCardProgress(routineProgressViewModel.currentCardProgress.value!! + 1)
            else if (routineProgressViewModel.currentCardSet.value!! < routineProgressViewModel.currentRoutine.value!!.cards[routineProgressViewModel.currentCardIndex.value!!].sets)
                routineProgressViewModel.setCardSet(routineProgressViewModel.currentCardSet.value!! + 1)
            else
            if (isNotLastCard())
                incrementCardIndex()
            else
                finishRoutine()
        }
    }

    // Handle previous card click
    private fun handlePreviousCardClick() {
        // handleNextCardClick의 반대기능
        binding.btnRoutineProgressPreviousCard.setOnClickListener {
            if (routineProgressViewModel.currentCardProgress.value!! > 0)
                routineProgressViewModel.setCardProgress(routineProgressViewModel.currentCardProgress.value!! - 1)
            else if (routineProgressViewModel.currentCardSet.value!! > 1)
                routineProgressViewModel.setCardSet(routineProgressViewModel.currentCardSet.value!! - 1)
            else
            if (isNotFirstCard())
                decrementCardIndex()
            else
                Toast.makeText(this, "첫 번째 카드입니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // Handle skip card click
    private fun handleSkipCardClick() {
        // 관계없이 다음 카드로 이동(가능하면)
        binding.btnRoutineProgressSkipCard.setOnClickListener {
            if (isNotLastCard())
                incrementCardIndex()
            else
                finishRoutine()
        }
    }

    // Handle card creation
    private fun handleCardCreation() {
        val cardCreateResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == CARD_CREATED) {
                addCardToRoutine(result.data!!.getSerializableExtra("selected_card") as Card)
                incrementCardIndex()
            }
        }
        binding.btnRoutineProgressCreateCard.setOnClickListener {
            cardCreateResultLauncher.launch(Intent(this, CardDetailActivity::class.java).apply {
                var emptyCard = Card(
                    id = "",
                    userId = Firebase.auth.currentUser!!.uid,
                    name = "test",
                    preTimerSecs = 5,
                    postTimerSecs = 10,
                    activeTimerSecs = 3,
                    preTimerAutoStart = false,
                    postTimerAutoStart = false,
                    activeTimerAutoStart = false,
                    sets = 5,
                    memo = ""
                )
                putExtra("selected_card", emptyCard)
                putExtra("createFlag", true)
            })
        }
    }

    // Check if it's not the last card
    private fun isNotLastCard(): Boolean {
        return routineProgressViewModel.currentCardIndex.value!! < routineProgressViewModel.currentRoutine.value!!.cards.size - 1
    }

    // Check if it's not the first card
    private fun isNotFirstCard(): Boolean {
        return routineProgressViewModel.currentCardIndex.value!! > 0
    }

    // Increment card index
    private fun incrementCardIndex() {
        routineProgressViewModel.updateCurrentCardIndexData(routineProgressViewModel.currentCardIndex.value!! + 1)
    }

    // Decrement card index
    private fun decrementCardIndex() {
        routineProgressViewModel.updateCurrentCardIndexData(routineProgressViewModel.currentCardIndex.value!! - 1)
    }

    // Finish routine
    private fun finishRoutine() {
        Toast.makeText(this, "루틴이 종료되었습니다.", Toast.LENGTH_SHORT).show()
        // TODO: 루틴 종료에 관한 DB 업데이트 처리하기
        val intent = Intent(this, RoutineCompleteActivity::class.java)
        intent.putExtra("selected_routine", routineProgressViewModel.currentRoutine.value!!)
        val totalElapsedTime = routineProgressViewModel.currentRoutineTime.value!!
        intent.putExtra("totalElapsedTime", totalElapsedTime)
        startActivity(intent)
        finish()
    }

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            routineProgressViewModel.updateCurrentCardIndexData(position)
        }
    }

// Add card to routine
    private fun addCardToRoutine(card: Card) {
        routineProgressViewModel.currentRoutine.value!!.cards.add(
            routineProgressViewModel.currentCardIndex.value!! + 1,
            card
        )
    }

}