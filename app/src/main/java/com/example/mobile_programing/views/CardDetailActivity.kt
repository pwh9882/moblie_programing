package com.example.mobile_programing.views

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.NumberPicker
import androidx.databinding.DataBindingUtil
import com.example.mobile_programing.R
import com.example.mobile_programing.databinding.ActivityCardDetailBinding
import com.example.mobile_programing.models.Card


class CardDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityCardDetailBinding

    val activeTimeSec = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_detail)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_card_detail)

        val currentCard = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("selected_card", Card::class.java)
        } else {
            intent.getSerializableExtra("selected_card") as Card
        }

        // binding
        binding.etCardDetailName.setText(currentCard?.name)
        binding.etCardDetailPreTimerSecs.setText(currentCard?.preTimerSecs.toString())
        binding.etCardDetailSets.setText(currentCard?.sets.toString())

        binding.etCardDetailActiveTimerSecs.setText(currentCard?.activeTimerSecs.toString())
        binding.etCardDetailPostTimerSecs.setText(currentCard?.postTimerSecs.toString())
        binding.etCardDetailMemo.setText(currentCard?.memo.toString())

        binding.cbCardDetailPreTimerAutoStart.isChecked = currentCard?.preTimerAutoStart!!
        binding.cbCardDetailActiveTimerAutoStart.isChecked = currentCard.activeTimerAutoStart
        binding.cbCardDetailPostTimerAutoStart.isChecked = currentCard.postTimerAutoStart



        // LinearLayout에 클릭 리스너 추가
        binding.llCardDetailPreTimerSecs.setOnClickListener {
            // NumberPicker를 포함하는 다이얼로그 생성
            val numberPicker = NumberPicker(this@CardDetailActivity).apply {
                minValue = 0
                maxValue = 100
                value = currentCard.preTimerSecs
            }

            AlertDialog.Builder(this@CardDetailActivity).apply {
                setTitle("시간 선택")
                setView(numberPicker)
                setPositiveButton("확인") { _, _ ->
                    // 확인 버튼을 누르면 et_card_detail_preTimerSecs의 값을 업데이트
                    binding.etCardDetailPreTimerSecs.text = numberPicker.value.toString()
                }
                setNegativeButton("취소", null)
            }.show()
        }

        // LinearLayout에 클릭 리스너 추가
        binding.llCardDetailActiveTimerSecs.setOnClickListener {
            // 시, 분, 초를 선택할 수 있는 3개의 NumberPicker를 포함하는 다이얼로그 생성

            val currentActiveTimeSec = currentCard.activeTimerSecs
            val hour = currentActiveTimeSec / 3600
            val minute = (currentActiveTimeSec % 3600) / 60
            val second = currentActiveTimeSec % 60

            val hourPicker = NumberPicker(this@CardDetailActivity).apply {
                minValue = 0
                maxValue = 23
                value = hour
            }
            val minutePicker = NumberPicker(this@CardDetailActivity).apply {
                minValue = 0
                maxValue = 59
                value = minute
            }
            val secondPicker = NumberPicker(this@CardDetailActivity).apply {
                minValue = 0
                maxValue = 59
                value = second
            }

            val pickerLayout = LinearLayout(this@CardDetailActivity).apply {
                orientation = LinearLayout.HORIZONTAL
                // gravity center
                gravity = Gravity.CENTER


                addView(hourPicker)
                addView(minutePicker)
                addView(secondPicker)
            }

            AlertDialog.Builder(this@CardDetailActivity).apply {
                setTitle("시간 선택")
                setView(pickerLayout)
                setPositiveButton("확인") { _, _ ->
                    // 확인 버튼을 누르면 et_card_detail_activeTimerSecs의 값을 업데이트

                    val formattedTime = "${hour}시간 ${minute}분 ${second}초"
                    binding.etCardDetailActiveTimerSecs.text = formattedTime
                }
                setNegativeButton("취소", null)
            }.show()
        }

        // LinearLayout에 클릭 리스너 추가
        binding.llCardDetailPostTimerSecs.setOnClickListener {
            // NumberPicker를 포함하는 다이얼로그 생성
            val numberPicker = NumberPicker(this@CardDetailActivity).apply {
                minValue = 0
                maxValue = 60
                value = currentCard.postTimerSecs
            }

            AlertDialog.Builder(this@CardDetailActivity).apply {
                setTitle("시간 선택")
                setView(numberPicker)
                setPositiveButton("확인") { _, _ ->
                    // 확인 버튼을 누르면 et_card_detail_postTimerSecs의 값을 업데이트
                    binding.etCardDetailPostTimerSecs.text = numberPicker.value.toString()
                }
                setNegativeButton("취소", null)
            }.show()
        }

        // LinearLayout에 클릭 리스너 추가
        binding.llCardDetailSets.setOnClickListener {
            // NumberPicker를 포함하는 다이얼로그 생성
            val numberPicker = NumberPicker(this@CardDetailActivity).apply {
                minValue = 1
                maxValue = 100
                value = currentCard.sets
            }

            AlertDialog.Builder(this@CardDetailActivity).apply {
                setTitle("세트 수 선택")
                setView(numberPicker)
                setPositiveButton("확인") { _, _ ->
                    // 확인 버튼을 누르면 et_card_detail_sets의 값을 업데이트
                    binding.etCardDetailSets.text = numberPicker.value.toString()
                }
                setNegativeButton("취소", null)
            }.show()
        }



        binding.btnCardDetailUpdate.setOnClickListener {
            val resultIntent = Intent(this, CardDetailActivity::class.java)

            // binding 값에서 card 객체를 만들어서 intent에 넣어준다.
            val updatedCard = Card(
                // TODO: id를 어떻게 처리할지 고민해보기
                id=currentCard.id,
                userId = currentCard.userId,
                name = binding.etCardDetailName.text.toString(),
                preTimerSecs = binding.etCardDetailPreTimerSecs.text.toString().toInt(),
                sets = binding.etCardDetailSets.text.toString().toInt(),
                activeTimerSecs = binding.etCardDetailActiveTimerSecs.text.toString().toInt(),
                memo = binding.etCardDetailMemo.text.toString(),
                postTimerSecs = binding.etCardDetailPostTimerSecs.text.toString().toInt(),
                preTimerAutoStart = binding.cbCardDetailPreTimerAutoStart.isChecked,
                activeTimerAutoStart = binding.cbCardDetailActiveTimerAutoStart.isChecked,
                postTimerAutoStart = binding.cbCardDetailPostTimerAutoStart.isChecked

            )

            resultIntent.putExtra("selected_card", updatedCard)

            // isCreate 값을 extra에서 읽어서 ture이면 CARD_CREATED를, false이면 CARD_UPDATED를 resultCode로 넘겨준다.
            val createFlag = intent.getBooleanExtra("createFlag", false)
            Log.e("createFlag: ", createFlag.toString())
            if(createFlag)
                setResult(CARD_CREATED, resultIntent)
            else
                setResult(CARD_UPDATED, resultIntent)

            finish()
        }
    }
}