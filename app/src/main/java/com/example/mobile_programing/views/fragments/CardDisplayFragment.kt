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
            binding.tvRoutineProgressCardIndex.text = it.toString()
            binding.tvRoutineProgressCardName.text = routineProgressViewModel.currentRoutine.value!!.cards[it].name
            routineProgressViewModel.stopCardTimer()
            routineProgressViewModel.startCardTimer()
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