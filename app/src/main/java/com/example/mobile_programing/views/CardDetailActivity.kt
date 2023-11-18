package com.example.mobile_programing.views

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.mobile_programing.R
import com.example.mobile_programing.databinding.ActivityCardDetailBinding
import com.example.mobile_programing.models.Card


class CardDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityCardDetailBinding
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
        binding.etCardDetailMemo.setText(currentCard?.memo.toString())
        binding.etCardDetailPostTimerSecs.setText(currentCard?.postTimerSecs.toString())

        binding.cbCardDetailPreTimerAutoStart.isChecked = currentCard?.preTimerAutoStart!!
        binding.cbCardDetailActiveTimerAutoStart.isChecked = currentCard.activeTimerAutoStart
        binding.cbCardDetailPostTimerAutoStart.isChecked = currentCard.postTimerAutoStart



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