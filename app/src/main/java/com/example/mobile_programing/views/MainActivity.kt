package com.example.mobile_programing.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.mobile_programing.DbDemoActivity
import com.example.mobile_programing.R
import com.example.mobile_programing.databinding.ActivityMainBinding
import com.example.mobile_programing.models.Routine
import com.example.mobile_programing.views.adapters.RoutineAdapter
import com.example.mobile_programing.viewModel.MainViewModel
import com.example.mobile_programing.views.adapters.helpers.ItemTouchHelperCallback
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    // Registering for activity result
    private val routineUpdateResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        handleActivityResult(result)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDataBinding()
        setupUserAuthentication()
        setupRoutineList()
        setupRoutineCreateButton()

        // 백엔드용 버튼
        binding.bttnConnect.setOnClickListener {//데모버튼 클릭시 이벤트
            startActivity(Intent(this, DbDemoActivity::class.java))

        }
    }

    private fun setupDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun setupUserAuthentication() {
        val user = Firebase.auth.currentUser
        binding.test.text = user?.email ?: "cannot load"
    }

    private fun setupRoutineList() {
        viewModel.updateRoutineListData()

        val routineAdapter = RoutineAdapter(binding, viewModel, this)

        val itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(routineAdapter))
        itemTouchHelper.attachToRecyclerView(binding.rvRoutineList)

        binding.rvRoutineList.adapter = routineAdapter
        viewModel.routineList.observe(this, Observer {
            routineAdapter.routineList = it
            routineAdapter.notifyDataSetChanged()
        })
    }

    private fun setupRoutineCreateButton() {
        binding.btnRoutineCreate.setOnClickListener {
            launchRoutineCreateActivity()
        }
    }


    // Function to handle activity result
    private fun handleActivityResult(result: ActivityResult) {
        if(result.resultCode == ROUTINE_UPDATED ) {
            updateRoutine(result)
        }
        if(result.resultCode == ROUTINE_CREATED) {
            createRoutine(result)
        }
    }

    // Function to update routine
    private fun updateRoutine(result: ActivityResult) {
        val updatedRoutine = result.data?.getSerializableExtra("selected_routine") as Routine
        viewModel.routineList.value?.set(viewModel.routineList.value?.indexOfFirst { it.id == updatedRoutine.id }!!, updatedRoutine)
        binding.rvRoutineList.adapter?.notifyDataSetChanged()
        // TODO: Update DB with the updated routine
    }

    // Function to create routine
    private fun createRoutine(result: ActivityResult) {
        val updatedRoutine = result.data?.getSerializableExtra("selected_routine") as Routine
        viewModel.routineList.value?.add(updatedRoutine)
        binding.rvRoutineList.adapter?.notifyDataSetChanged()
        // TODO: Update DB with the new routine
    }

    // Function to launch RoutineCreateActivity
    private fun launchRoutineCreateActivity() {
        routineUpdateResultLauncher.launch(
            Intent(this, RoutineCreateActivity::class.java).apply {
                putExtra("selected_routine", viewModel.routineList.value?.let {
                    Routine(
                        // TODO: 현재로는 id를 그냥 최대값+1
                        id= it.size.toString(),
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