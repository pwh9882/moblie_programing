package com.example.mobile_programing.views

import android.app.AlertDialog
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
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

    lateinit var binding : ActivityRoutineProgressBinding

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
        binding.tvRoutineProgressName.text = routine!!.name

        routineProgressViewModel.isRoutineUpdated = false
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
        handlePausePlayClick();
        handleCardEditClick();


        routineProgressViewModel.currentCardTime.observe(this) {
            if (it <= -1) {
                val currCard = routineProgressViewModel.currentRoutine.value!!.cards[routineProgressViewModel.currentCardIndex.value!!]
                routineProgressViewModel.stopCardTimer()
                Toast.makeText(this, "카드가 종료되었습니다.", Toast.LENGTH_SHORT).show()
                if ((routineProgressViewModel.currentCardProgress.value == 0 && currCard.preTimerAutoStart) ||
                    (routineProgressViewModel.currentCardProgress.value == 1 && currCard.activeTimerAutoStart) ||
                    (routineProgressViewModel.currentCardProgress.value == 2 && currCard.postTimerAutoStart)
                ) {
                    binding.btnRoutineProgressNextCard.performClick()
                }
            }

        }

    }

    private fun handleCardEditClick() {
        // Create an ActivityResultLauncher for the CardDetailActivity
        val cardEditResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == CARD_UPDATED) {
                // Get the updated card from the result data
                val updatedCard = result.data?.getSerializableExtra("selected_card") as Card
                // Find the index of the card in the routine's card list
                val cardIndex = routineProgressViewModel.currentRoutine.value!!.cards.indexOfFirst { it.id == updatedCard.id }
                // Update the card in the routine's card list
                routineProgressViewModel.currentRoutine.value!!.cards[cardIndex] = updatedCard
                routineProgressViewModel.setCurrentCard(updatedCard)

                // Set the isRoutineUpdated flag to true
                routineProgressViewModel.isRoutineUpdated = true
            }
        }

        // Set an OnClickListener for the btn_routine_progress_card_edit button
        binding.btnRoutineProgressCardEdit.setOnClickListener {
            // Get the current card
            val currentCard = routineProgressViewModel.currentCard.value!!
            // Launch the CardDetailActivity with the current card passed as an extra
            cardEditResultLauncher.launch(Intent(this, CardDetailActivity::class.java).apply {
                putExtra("selected_card", currentCard)
            })
        }
    }

    private fun handlePausePlayClick() {
        binding.btnRoutineProgressPausePlayCard.setOnClickListener {
            if (!routineProgressViewModel.isPaused.value!!){
                routineProgressViewModel.pauseTimer()
            } else {
                routineProgressViewModel.resumeTimer()
            }
        }

        routineProgressViewModel.isPaused.observe(this) {
            if (it) {
                binding.btnRoutineProgressPausePlayCard.setImageResource(R.drawable.custom_next_icon)
            } else {
                binding.btnRoutineProgressPausePlayCard.setImageResource(R.drawable.custom_stop_icon)
            }
        }
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
            binding.tvRoutineProgressTotalTime.text = formatTimeForTotalTime(it)
        }
    }

    private fun formatTimeForTotalTime(totalSeconds: Int?): String {
        if (totalSeconds == null || totalSeconds == 0) return "현재까지 총 0초 걸렸어요."

        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        var timeString = "현재까지 총 "
        if (hours > 0) timeString += "${hours}시간 "
        if (minutes > 0) timeString += "${minutes}분 "
        if (seconds > 0) timeString += "${seconds}초"

        return timeString.trim() + " 걸렸어요."
    }

    // Handle next card click
    private fun handleNextCardClick() {
        binding.btnRoutineProgressNextCard.setOnClickListener {

            saveMemo()

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

            saveMemo()

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
        val drawable = ContextCompat.getDrawable(this, android.R.drawable.ic_media_ff)
        drawable?.setColorFilter(ContextCompat.getColor(this, android.R.color.black), PorterDuff.Mode.SRC_IN)
        binding.btnRoutineProgressSkipCard.setImageDrawable(drawable)

        // 관계없이 다음 카드로 이동(가능하면)
        binding.btnRoutineProgressSkipCard.setOnClickListener {
            saveMemo()
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

                // Set the isRoutineUpdated flag to true
                routineProgressViewModel.isRoutineUpdated = true
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
        routineProgressViewModel.incrementUserStars(routineProgressViewModel.currentRoutine.value!!.userId)

        intent.putExtra("selected_routine", routineProgressViewModel.currentRoutine.value!!)
        val totalElapsedTime = routineProgressViewModel.currentRoutineTime.value!!
        intent.putExtra("totalElapsedTime", totalElapsedTime)
        startActivity(intent)
        // fl 끝내기;

        if (routineProgressViewModel.isRoutineUpdated) {
            binding.clRoutineProgress.visibility = android.view.View.GONE
            // The routine has been updated, ask the user if they want to apply the updates
            AlertDialog.Builder(this)
                .setTitle("루틴 변경사항 저장")
                .setMessage("루틴 변경사항을 저장하시겠습니까?")
                .setPositiveButton("예") { _, _ ->
                    // User confirmed to save changes, proceed with the existing logic
                    val returnIntent = Intent().apply {
                        putExtra("selected_routine", routineProgressViewModel.currentRoutine.value!!)
                    }
                    setResult(ROUTINE_UPDATED, returnIntent)
                    finish()
                }
                .setNegativeButton("아니오"){
                    _, _ -> // User cancelled the dialog, just finish the activity
                    finish()
                }
                .show() // Show the dialog
        } else {
            finish()
        }
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


    override fun onBackPressed() {
        saveMemo()
        // Create a new AlertDialog Builder
        AlertDialog.Builder(this)
            .setTitle("루틴 종료")
            .setMessage("정말로 루틴을 종료하시겠습니까?")
            .setPositiveButton("예") { _, _ ->
                if(routineProgressViewModel.isRoutineUpdated) {
                    // User confirmed to exit, ask if they want to save changes
                    AlertDialog.Builder(this)
                        .setTitle("루틴 변경사항 저장")
                        .setMessage("루틴 변경사항을 저장하시겠습니까?")
                        .setPositiveButton("예") { _, _ ->
                            // User confirmed to save changes, proceed with the existing logic
                            val returnIntent = Intent().apply {
                                putExtra("selected_routine", routineProgressViewModel.currentRoutine.value!!)
                            }
                            setResult(ROUTINE_UPDATED, returnIntent)
                            finish()
                        }
                        .setNegativeButton("아니오") { _, _ -> // User cancelled the dialog, just finish the activity
                            finish()
                        }
                        .show() // Show the dialog
                } else {
                    finish()
                }
            }
            .setNegativeButton("아니오", null) // User cancelled the dialog, do nothing
            .show() // Show the dialog
    }

    fun saveMemo(){
        // card memo 내용을 비교, 다르면 저장
        if(routineProgressViewModel.currentRoutine.value!!.cards[routineProgressViewModel.currentCardIndex.value!!].memo != binding.etCardMemo.text.toString()){
            routineProgressViewModel.currentRoutine.value!!.cards[routineProgressViewModel.currentCardIndex.value!!].memo = binding.etCardMemo.text.toString()
            routineProgressViewModel.isRoutineUpdated = true
        }
    }


}