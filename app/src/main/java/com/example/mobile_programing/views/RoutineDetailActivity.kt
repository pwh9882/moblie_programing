package com.example.mobile_programing.views

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile_programing.R
import com.example.mobile_programing.databinding.ActivityRoutineDetailBinding
import com.example.mobile_programing.models.Card
import com.example.mobile_programing.models.Routine
import com.example.mobile_programing.views.adapters.RoutineDetailCardAdapter

const val CARD_CREATED = 201
const val CARD_UPDATED = 202

class RoutineDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRoutineDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_routine_detail)
        binding.lifecycleOwner = this

        val routine = getRoutineFromIntent()!!

        setupUI(routine)

        val cardUpdateResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleActivityResult(result, routine)
        }

        setupRecyclerView(routine, cardUpdateResultLauncher)

        binding.btnRunRoutine.setOnClickListener { runRoutine(routine) }

        binding.btnAddCard.setOnClickListener { addCardToRoutine(routine, cardUpdateResultLauncher) }
    }

    private fun getRoutineFromIntent(): Routine? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("selected_routine", Routine::class.java)
        } else {
            intent.getSerializableExtra("selected_routine") as Routine
        }
    }

    private fun setupUI(routine: Routine) {
        binding.tvRoutineDetailName.text = routine?.name
        binding.tvRoutineDetailDescription.text = routine?.description
        binding.tvRoutineDetailTotalTime.text = routine?.totalTime.toString() + " 초"
        binding.tvRoutineDetailStartTime.text = routine?.routineStartTime.toString()
    }

    private fun handleActivityResult(result: ActivityResult, routine: Routine) {
        if(result.resultCode == CARD_UPDATED) {
            updateCardInRoutine(result, routine)
        }
        if(result.resultCode == CARD_CREATED) {
            addCardToRoutine(result, routine)
        }
    }

    private fun updateCardInRoutine(result: ActivityResult, routine: Routine) {
        val updatedCard = result.data?.getSerializableExtra("selected_card") as Card
        routine?.cards?.set(routine?.cards?.indexOfFirst { it.id == updatedCard.id }!!, updatedCard)
        binding.rvRoutineDetailCardList.adapter?.notifyDataSetChanged()
    }

    private fun addCardToRoutine(result: ActivityResult, routine: Routine) {
        val newCard = result.data?.getSerializableExtra("selected_card") as Card
        routine?.cards?.add(newCard)
        binding.rvRoutineDetailCardList.adapter?.notifyDataSetChanged()
    }

    private fun setupRecyclerView(routine: Routine, cardUpdateResultLauncher: ActivityResultLauncher<Intent>) {
        val routineDetailCardAdapter = RoutineDetailCardAdapter(binding, this, cardUpdateResultLauncher)
        binding.rvRoutineDetailCardList.adapter = routineDetailCardAdapter
        binding.rvRoutineDetailCardList.layoutManager = LinearLayoutManager(this)

        routineDetailCardAdapter.cardList = routine.cards
        routineDetailCardAdapter.notifyDataSetChanged()
    }

    private fun runRoutine(routine: Routine) {
        if(routine.cards.isEmpty()) {
            Toast.makeText(this, "루틴에 카드가 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        startActivity(Intent(this, RoutineProgressActivity::class.java).apply {
            putExtra("selected_routine", routine)
        })
    }

    private fun addCardToRoutine(routine: Routine, cardUpdateResultLauncher: ActivityResultLauncher<Intent>) {
        cardUpdateResultLauncher.launch(Intent(this, CardDetailActivity::class.java).apply {
            var emptyCard = Card(
                id = routine.cards.size+1,
                name = "비어 있는 카드",
                preTimerSecs = 0,
                preTimerAutoStart = true,
                activeTimerSecs = 0,
                activeTimerAutoStart = true,
                postTimerSecs = 0,
                postTimerAutoStart = true,
                sets = 0,
                additionalInfo = arrayListOf()
            )

            putExtra("createFlag", true)
            putExtra("selected_card", emptyCard)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        // TODO: routine이 수정되었으면 DB에 업데이트
    }
}