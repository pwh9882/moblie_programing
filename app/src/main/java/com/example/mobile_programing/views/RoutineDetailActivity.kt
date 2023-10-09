package com.example.mobile_programing.views

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mobile_programing.R
import com.example.mobile_programing.databinding.ActivityRoutineDetailBinding
import com.example.mobile_programing.models.Routine
import com.example.mobile_programing.viewModel.MainViewModel
import com.example.mobile_programing.views.adapters.RoutineDetailCardAdapter

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

        val routineDetailCardAdapter = RoutineDetailCardAdapter(binding, this)


        binding.rvRoutineDetailCardList.adapter = routineDetailCardAdapter
        binding.rvRoutineDetailCardList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        Log.e("Cards: ", routine?.cards.toString())
        routineDetailCardAdapter.cardList = routine?.cards!!
        routineDetailCardAdapter.notifyDataSetChanged()
    }
}