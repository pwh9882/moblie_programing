package com.example.mobile_programing.views.fragments

import android.graphics.Color
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mobile_programing.R
import com.example.mobile_programing.databinding.FragmentCardDisplayBinding
import com.example.mobile_programing.viewModel.RoutineProgressViewModel
import com.example.mobile_programing.views.RoutineProgressActivity


/**
 * A simple [Fragment] subclass.
 * Use the [CardDisplayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CardDisplayFragment : Fragment() {

    private lateinit var routineProgressViewModel: RoutineProgressViewModel
    private lateinit var binding: FragmentCardDisplayBinding

    private var maxTime: Int = 0

    // Define a TextWatcher as a property of your class
    private var memoTextWatcher: TextWatcher? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCardDisplayBinding.inflate(inflater, container, false)
        routineProgressViewModel = ViewModelProvider(requireActivity())[RoutineProgressViewModel::class.java]

        routineProgressViewModel.currentCardIndex.observe(viewLifecycleOwner) {
            routineProgressViewModel.setCurrentCard(routineProgressViewModel.currentRoutine.value!!.cards[it])
            binding.tvRoutineProgressCardIndex.text = "${it+1}/${routineProgressViewModel.currentRoutine.value!!.cards.size} 카드"
            binding.pbRoutineProgress.progress = it+1
            binding.pbRoutineProgress.max = routineProgressViewModel.currentRoutine.value!!.cards.size
            // RoutineProgressActivity의 binding.etCardMemo에도 반영
            (activity as RoutineProgressActivity).binding.etCardMemo.setText(routineProgressViewModel.currentCard.value!!.memo)
        }

        routineProgressViewModel.currentCard.observe(viewLifecycleOwner) {
            binding.tvRoutineProgressCardName.text = it.name
            binding.tvRoutineProgressSets.text = "${routineProgressViewModel.currentCardSet.value}/${it.sets}"
            binding.pbCardSetsProgress.max = it.sets
            binding.pbCardSetsProgress.progress = routineProgressViewModel.currentCardSet.value?:0
            routineProgressViewModel.initCardProgressInfo()
            binding.pbCardSetsProgress.max = it.sets
            binding.pbCardSetsProgress.progress = 1
//            binding.etCardMemo.setText(it.memo)
        }


        routineProgressViewModel.currentCardProgress.observe(viewLifecycleOwner) {
            val currCard = routineProgressViewModel.currentRoutine.value!!.cards[routineProgressViewModel.currentCardIndex.value!!]
            binding.tvRoutineProgressStatus.text = when(it) {
                0 -> "진행 전"
                1 -> "진행 중"
                2 -> "진행 후"
                else -> "오류"
            }

            // 0: pretimer, 1: activeTimer, 2: postTimer
            maxTime = when(it) {
                0 -> currCard.preTimerSecs
                1 -> currCard.activeTimerSecs
                2 -> currCard.postTimerSecs
                else -> 0
            }
//            routineProgressViewModel.stopCardTimer()
            routineProgressViewModel.startCardTimer(maxTime)
            // ProgressBar 업데이트
            setProgressBarValues(maxTime, it)
        }

        routineProgressViewModel.currentCardSet.observe(viewLifecycleOwner) {
            binding.tvRoutineProgressSets.text = "${it}/${routineProgressViewModel.currentRoutine.value!!.cards[routineProgressViewModel.currentCardIndex.value!!].sets} 세트"
            binding.pbCardSetsProgress.progress = it

            // 다음 세트로 이동하면 pregress를 0으로 초기화
            routineProgressViewModel.setCardProgress(0)
        }


        routineProgressViewModel.currentCardTime.observe(viewLifecycleOwner) {
            if(it >= 0){
                binding.tvRoutineProgressCardLeftTime.text = it.toString()
                binding.pbCardTimeProgress.progress = it
            }

        }

        return binding.root
    }

    /**
     * 원형 프로그레스 바에 값 세팅
     */
    private fun setProgressBarValues(time: Int, progressIndex: Int) {
        binding.pbCardTimeProgress.max =  time
        binding.pbCardTimeProgress.progress = time
        binding.pbCardStatusProgress.progress = progressIndex + 1
        var progressDrawable = binding.pbCardTimeProgress.progressDrawable.mutate()

        // Drawable이 ShapeDrawable인지 확인하고 색상을 변경함
        if (progressDrawable is GradientDrawable) {
            progressDrawable.setColor(ContextCompat.getColor(requireContext(), when(progressIndex) {
                0 -> android.R.color.holo_green_light
                1 -> android.R.color.holo_orange_light
                2 -> android.R.color.holo_purple
                else -> android.R.color.holo_red_light
            }))
        }
        // background 색상 변경: 해당 색깔의 dark 버전으로
        progressDrawable = binding.pbCardTimeProgress.background.mutate()
        if (progressDrawable is GradientDrawable) {
            progressDrawable.setColor(ContextCompat.getColor(requireContext(), when(progressIndex) {
                0 -> android.R.color.holo_green_dark
                1 -> android.R.color.holo_orange_dark
                2 -> R.color.dark_purple
                else -> android.R.color.holo_red_dark
            }))
        }


        val statusProgressDrawable = binding.pbCardStatusProgress.progressDrawable.mutate() as LayerDrawable
        val clipDrawable = statusProgressDrawable.findDrawableByLayerId(android.R.id.progress) as ClipDrawable
        val gradientDrawable = clipDrawable.drawable!!.mutate()

        if (gradientDrawable is GradientDrawable) {
            gradientDrawable.setColor(ContextCompat.getColor(requireContext(), when(progressIndex) {
                0 -> android.R.color.holo_green_light
                1 -> android.R.color.holo_orange_light
                2 -> android.R.color.holo_purple
                else -> android.R.color.holo_red_light
            }))
        }

    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param routineProgressViewModel Parameter 1.
         * @return A new instance of fragment CardDisplayFragment.
         */
        @JvmStatic
        fun newInstance(routineProgressViewModel: RoutineProgressViewModel) =
            CardDisplayFragment().apply {

            }
    }
}