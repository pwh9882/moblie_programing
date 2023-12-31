package com.routine_mate.mobile_programing.views

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.routine_mate.mobile_programing.R
import com.routine_mate.mobile_programing.databinding.ActivityRoutineDetailBinding
import com.routine_mate.mobile_programing.models.Card
import com.routine_mate.mobile_programing.models.Routine
import com.routine_mate.mobile_programing.views.adapters.RoutineDetailCardAdapter
import com.routine_mate.mobile_programing.views.adapters.helpers.ItemTouchHelperCallbackForCard
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

const val CARD_CREATED = 201
const val CARD_UPDATED = 202

class RoutineDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRoutineDetailBinding
    lateinit var routine: Routine
    var routineDetailCardAdapter: RoutineDetailCardAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_routine_detail)
        binding.lifecycleOwner = this

        routine = getRoutineFromIntent()!!

        setupUI(routine)

        val cardUpdateResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleActivityResult(result, routine)
        }

        setupRecyclerView(routine, cardUpdateResultLauncher)

        binding.btnRunRoutine.setOnClickListener { runRoutine(routine) }

        binding.btnAddCard.setOnClickListener { addCardToRoutine(routine, cardUpdateResultLauncher) }

        binding.btnRoutineDetailBackBtn.setOnClickListener() {
            onBackPressed()
        }
    }

    private fun getRoutineFromIntent(): Routine? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("selected_routine", Routine::class.java)
        } else {
            intent.getSerializableExtra("selected_routine") as Routine
        }
    }
    // Registering for activity result
    private val routineUpdateResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        handleActivityResult(result, routine)
    }

    private fun setupUI(routine: Routine) {
        binding.tvRoutineDetailName.text = routine.name
        binding.tvRoutineDetailDescription.text = routine.description
        binding.tvRoutineDetailTotalTime.text = formatTimeForTotalTime(routine.totalTime)
        binding.tvRoutineDetailStartTime.text = formatTimeForRoutineStartTime(routine.routineStartTime)



        binding.btnRoutineDetailEditBtn.setOnClickListener {
            // Start RoutineCreateActivity with the current routine
            routineUpdateResultLauncher.launch(
                Intent(this, RoutineCreateActivity::class.java).apply {
                    putExtra("selected_routine", routine)
                }
            )
        }
    }

    private fun formatTimeForTotalTime(totalSeconds: Int?): String {
        if (totalSeconds == null || totalSeconds == 0) return "0초 예상"

        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        var timeString = ""
        if (hours > 0) timeString += "${hours}시간 "
        if (minutes > 0) timeString += "${minutes}분 "
        if (seconds > 0) timeString += "${seconds}초"

        return timeString.trim() + " 예상"
    }

    private fun formatTimeForRoutineStartTime(totalMinutes: Int?): String {
        if (totalMinutes == null) return "오전 00시 00분"

        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        val amPm = if (hours < 12) "오전" else "오후"

        return String.format("%s %02d시 %02d분 시작", amPm, hours, minutes)
    }

    private fun handleActivityResult(result: ActivityResult, routine: Routine) {
        if(result.resultCode == ROUTINE_UPDATED ) {
            Toast.makeText(this, "루틴이 업데이트 되었습니다.", Toast.LENGTH_SHORT).show()
            // Update the UI with the updated routine
            this.routine = result.data?.getSerializableExtra("selected_routine") as Routine
            this.routine.updateTotalTime()  // Update the total time
            setupUI(this.routine)
            routineDetailCardAdapter!!.cardList = this.routine.cards
            routineDetailCardAdapter!!.notifyDataSetChanged()
        }
        if(result.resultCode == CARD_UPDATED) {
            updateCardInRoutine(result, routine)

            routine.updateTotalTime()  // Update the total time
        }
        if(result.resultCode == CARD_CREATED) {
            addCardToRoutine(result, routine)
            routine.updateTotalTime()  // Update the total time
        }
    }



    private fun updateCardInRoutine(result: ActivityResult, routine: Routine) {
        val updatedCard = result.data?.getSerializableExtra("selected_card") as Card

        //TODO: 현재 DB에 Id를 계속 새로 생성해서 같은 id를 못찾아서 튕김!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//        Log.e("updatedCard", updatedCard.toString()
        routine?.cards?.set(routine?.cards?.indexOfFirst { it.id == updatedCard.id }!!, updatedCard)
        binding.rvRoutineDetailCardList.adapter?.notifyDataSetChanged()
    }

    private fun addCardToRoutine(result: ActivityResult, routine: Routine) {
        val newCard = result.data?.getSerializableExtra("selected_card") as Card
        routine?.cards?.add(newCard)

        binding.rvRoutineDetailCardList.adapter?.notifyDataSetChanged()
    }

    private fun setupRecyclerView(routine: Routine, cardUpdateResultLauncher: ActivityResultLauncher<Intent>) {
        routineDetailCardAdapter = RoutineDetailCardAdapter(binding, this, cardUpdateResultLauncher)
        binding.rvRoutineDetailCardList.adapter = routineDetailCardAdapter
        binding.rvRoutineDetailCardList.layoutManager = LinearLayoutManager(this)


        val itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallbackForCard(routineDetailCardAdapter!!))
        itemTouchHelper.attachToRecyclerView(binding.rvRoutineDetailCardList)

        routineDetailCardAdapter!!.cardList = routine.cards
        routineDetailCardAdapter!!.notifyDataSetChanged()
    }

    private fun runRoutine(routine: Routine) {
        if(routine.cards.isEmpty()) {
            Toast.makeText(this, "루틴에 카드가 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        routineUpdateResultLauncher.launch(Intent(this, RoutineProgressActivity::class.java).apply {
            putExtra("selected_routine", routine)
        })
    }

    private fun addCardToRoutine(routine: Routine, cardUpdateResultLauncher: ActivityResultLauncher<Intent>) {
        cardUpdateResultLauncher.launch(Intent(this, CardDetailActivity::class.java).apply {
            var emptyCard = Card(
                id = "",
                userId = Firebase.auth.currentUser!!.uid,
                name = "",
                preTimerSecs = 0,
                preTimerAutoStart = true,
                activeTimerSecs = 0,
                activeTimerAutoStart = false,
                postTimerSecs = 0,
                postTimerAutoStart = true,
                sets = 1,
                memo = ""
            )

            putExtra("createFlag", true)
            putExtra("selected_card", emptyCard)
        })
    }


    override fun onBackPressed() {
        // Create an Intent to hold the result data
        val returnIntent = Intent().apply {
            putExtra("selected_routine", routine)
        }
        // Set the result with a result code and the Intent
        setResult(ROUTINE_UPDATED, returnIntent)
        // Call the super method to actually handle the back press
        super.onBackPressed()
    }

}