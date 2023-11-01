package com.example.mobile_programing.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
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
            val time = when(it) {
                0 -> currCard.preTimerSecs
                1 -> currCard.activeTimerSecs
                2 -> currCard.postTimerSecs
                else -> 0
            }
            routineProgressViewModel.stopCardTimer()
            routineProgressViewModel.startCardTimer(time)
        }

        routineProgressViewModel.currentCardSet.observe(viewLifecycleOwner) {
            binding.tvRoutineProgressSets.text = "${it} / ${routineProgressViewModel.currentRoutine.value!!.cards[routineProgressViewModel.currentCardIndex.value!!].sets}"
            // 다음 세트로 이동하면 pregress를 0으로 초기화
            routineProgressViewModel.setCardProgress(0)
        }


        routineProgressViewModel.currentCardTime.observe(viewLifecycleOwner) {
            binding.tvRoutineProgressCardLeftTime.text = it.toString()
            if (it == 0) {
                routineProgressViewModel.stopCardTimer()
                Toast.makeText(requireContext(), "카드가 종료되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }



        return binding.root
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