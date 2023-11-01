package com.example.mobile_programing.views

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.mobile_programing.R
import com.example.mobile_programing.databinding.ActivityRoutineDetailBinding
import com.example.mobile_programing.models.Card
import com.example.mobile_programing.models.Routine
import com.example.mobile_programing.views.adapters.RoutineDetailCardAdapter

const val CARD_CREATED = 201 // 새로운 아이템 추가

const val CARD_UPDATED = 202 // 아이템 업데이트

class RoutineDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRoutineDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_routine_detail)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_routine_detail)
        binding.lifecycleOwner = this

        val routine = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("selected_routine",Routine::class.java)
        } else {
            intent.getSerializableExtra("selected_routine") as Routine
        }
        binding.tvRoutineDetailName.text = routine?.name
        binding.tvRoutineDetailDescription.text = routine?.description
        binding.tvRoutineDetailTotalTime.text = routine?.totalTime.toString() + " 초"
        binding.tvRoutineDetailStartTime.text = routine?.routineStartTime.toString()
//        binding.tvRoutineDetailCards.text = routine?.cards.toString()



        val cardUpdateResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == CARD_UPDATED) {
                // Got data from other activity and process that data
                Log.e("Activity result: ","${result.data}")

                // 수정된 카드를 루틴 카드 리스트에서 업데이트
                val updatedCard = result.data?.getSerializableExtra("selected_card") as Card
                routine?.cards?.set(routine?.cards?.indexOfFirst { it.id == updatedCard.id }!!, updatedCard)
                binding.rvRoutineDetailCardList.adapter?.notifyDataSetChanged()
                // TODO: 루틴에 카드 업데이트하는 DB 업데이트 처리하기
            }
            if(result.resultCode == CARD_CREATED) {
                // Got data from other activity and process that data
//                Log.e("${result.data}")
                // 받아온 카드를 루틴에 추가
                val newCard = result.data?.getSerializableExtra("selected_card") as Card
                routine?.cards?.add(newCard)
                binding.rvRoutineDetailCardList.adapter?.notifyDataSetChanged()
                // TODO: 루틴에 카드 추가하는 DB 업데이트 처리하기

            }
        }

        val routineDetailCardAdapter = RoutineDetailCardAdapter(binding, this, cardUpdateResultLauncher)
        binding.rvRoutineDetailCardList.adapter = routineDetailCardAdapter
        binding.rvRoutineDetailCardList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        Log.e("Cards: ", routine?.cards.toString())
        routineDetailCardAdapter.cardList = routine?.cards!!
        routineDetailCardAdapter.notifyDataSetChanged()

        binding.btnRunRoutine.setOnClickListener {
            // cards가 비었으면 루틴 실행 불가
            if(routine.cards.isEmpty()) {
                // Toast로 알림
                Toast.makeText(this, "루틴에 카드가 없습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            startActivity(Intent(this, RoutineProgressActivity::class.java).apply {
                putExtra("selected_routine", routine)
            })
        }

        binding.btnAddCard.setOnClickListener {
            cardUpdateResultLauncher.launch(Intent(this, CardDetailActivity::class.java).apply {
                var emptyCard = Card(
                    // TODO 지금은 간단히 id를 카드 리스트의 길이 + 1로 설정했는데, 이렇게 하면 중간에 카드가 삭제되면 문제가 생길 수 있음.
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


    }

    override fun onDestroy() {
        super.onDestroy()
        // routine이 수정되었으면 viewmodel에 업데이트
        
        // TODO: routine이 수정되었으면 DB에 업데이트

    }
}