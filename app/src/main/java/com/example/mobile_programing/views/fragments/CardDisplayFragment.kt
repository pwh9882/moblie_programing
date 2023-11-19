package com.example.mobile_programing.views.fragments

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mobile_programing.R
import com.example.mobile_programing.databinding.FragmentCardDisplayBinding
import com.example.mobile_programing.viewModel.RoutineProgressViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [CardDisplayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CardDisplayFragment : Fragment() {

    private lateinit var routineProgressViewModel: RoutineProgressViewModel
    private lateinit var binding: FragmentCardDisplayBinding

    private var maxTime: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCardDisplayBinding.inflate(inflater, container, false)
        routineProgressViewModel = ViewModelProvider(requireActivity())[RoutineProgressViewModel::class.java]

        routineProgressViewModel.currentCardIndex.observe(viewLifecycleOwner) {
            routineProgressViewModel.initCardProgressInfo()

            binding.tvRoutineProgressCardIndex.text = it.toString()
            binding.tvRoutineProgressCardName.text = routineProgressViewModel.currentRoutine.value!!.cards[it].name


        }


        routineProgressViewModel.currentCardProgress.observe(viewLifecycleOwner) {
            val currCard = routineProgressViewModel.currentRoutine.value!!.cards[routineProgressViewModel.currentCardIndex.value!!]
            binding.tvRoutineProgressStatus.text = when(it) {
                0 -> "진행 전"
                1 -> "진행 중"
                2 -> "진행 완료"
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
            binding.tvRoutineProgressSets.text = "${it} / ${routineProgressViewModel.currentRoutine.value!!.cards[routineProgressViewModel.currentCardIndex.value!!].sets}"
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
        var progressDrawable = binding.pbCardTimeProgress.progressDrawable
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
        progressDrawable = binding.pbCardTimeProgress.background
        if (progressDrawable is GradientDrawable) {
            progressDrawable.setColor(ContextCompat.getColor(requireContext(), when(progressIndex) {
                0 -> android.R.color.holo_green_dark
                1 -> android.R.color.holo_orange_dark
                2 -> R.color.dark_purple
                else -> android.R.color.holo_red_dark
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