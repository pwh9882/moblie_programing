package com.example.mobile_programing.views

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.mobile_programing.R
import com.example.mobile_programing.databinding.ActivityMainBinding
import com.example.mobile_programing.models.Card
import com.example.mobile_programing.views.adapters.RoutineAdapter
import com.example.mobile_programing.viewModel.MainViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    // Data binding
    private lateinit var binding : ActivityMainBinding

    // viewModel
    private val viewModel : MainViewModel by viewModels()



    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Data binding
//        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        val user = Firebase.auth.currentUser
        if (user != null) {
            // User is signed in
            binding.test.text = user.email
            ///asdf
        } else {
            // No user is signed in
            binding.test.text = "cannot load"
        }

        viewModel.updateRoutineListData()
        val routineAdapter = RoutineAdapter(binding, viewModel, this)
        binding.rvRoutineList.adapter = routineAdapter
        viewModel.routineList.observe(this, Observer {
            routineAdapter.routineLint = it
            routineAdapter.notifyDataSetChanged()
//            binding.tvDebugRoutinedata.text = it.toString()
        })


        val routineUpdateResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == ROUTINE_UPDATED ) {
                // Got data from other activity and process that data
                Log.e("Activity result: ","${result.data}")

                // 수정된 카드를 루틴 카드 리스트에서 업데이트
                val updatedRoutine = result.data?.getSerializableExtra("selected_routine") as com.example.mobile_programing.models.Routine
                viewModel.routineList.value?.set(viewModel.routineList.value?.indexOfFirst { it.id == updatedRoutine.id }!!, updatedRoutine)
                binding.rvRoutineList.adapter?.notifyDataSetChanged()
                // TODO: 루틴 업데이트하는 DB 업데이트 처리하기
            }
            if(result.resultCode == ROUTINE_CREATED) {
                // Got data from other activity and process that data
//                Log.e("${result.data}")
                // 받아온 카드를 루틴에 추가
                val updatedRoutine = result.data?.getSerializableExtra("selected_routine") as com.example.mobile_programing.models.Routine
                viewModel.routineList.value?.add(updatedRoutine)
                binding.rvRoutineList.adapter?.notifyDataSetChanged()
                // TODO: 루틴 추가하는 DB 업데이트 처리하기

            }
        }
        binding.btnRoutineCreate.setOnClickListener {
            routineUpdateResultLauncher.launch(
                android.content.Intent(
                    this,
                    RoutineCreateActivity::class.java
                ).apply {
                    putExtra("selected_routine", viewModel.routineList.value?.let { it1 ->
                        com.example.mobile_programing.models.Routine(
                            id= it1.size,
                            name = "",
                            description = "",
                            totalTime = 0,
                            routineStartTime = 0,
                            cards = arrayListOf()
                        )
                    })
                }
            )
        }

    }
}