package com.routine_mate.mobile_programing.views

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ItemTouchHelper
import com.routine_mate.mobile_programing.R
import com.routine_mate.mobile_programing.databinding.ActivityMainBinding
import com.routine_mate.mobile_programing.models.Card
import com.routine_mate.mobile_programing.models.Routine
import com.routine_mate.mobile_programing.views.adapters.RoutineAdapter
import com.routine_mate.mobile_programing.viewModel.MainViewModel
import com.routine_mate.mobile_programing.views.adapters.helpers.ItemTouchHelperCallback
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    // Registering for activity result
    private val routineUpdateResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        handleActivityResult(result)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(logoutReceiver, IntentFilter("com.routine_mate.mobile_programing.ACTION_LOGOUT"))
        setupDataBinding()
        setupUserAuthentication()
        setupRoutineList()
        setupRoutineCreateButton()

        // 백엔드용 버튼
        binding.bttnConnect.setOnClickListener {//데모버튼 클릭시 이벤트
            //startActivity(Intent(this, DbDemoActivity::class.java))
            viewModel.deleteRoutine("-NjD3F6s_DvIcOQXHbIO")
            viewModel.createRoutine(Routine(
                id="",
                userId = Firebase.auth.currentUser!!.uid,
                name = "test",
                description = "test",
                totalTime = 0,
                routineStartTime = 0,
                cards = arrayListOf(
                    Card(
                        id = "",
                        userId = Firebase.auth.currentUser!!.uid,
                        name = "비어 있는 카드",
                        preTimerSecs = 0,
                        preTimerAutoStart = true,
                        activeTimerSecs = 0,
                        activeTimerAutoStart = true,
                        postTimerSecs = 0,
                        postTimerAutoStart = true,
                        sets = 0,
                        memo = "비어 있는 카드입니다."
                    )
                )
            ))
        }

        binding.ibUserAccount.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            viewModel.viewModelScope.launch {
                val percentile = viewModel.getUserStarPercentile(Firebase.auth.currentUser!!.uid)
                intent.putExtra("star_percentile", percentile)
                startActivity(intent)
            }
        }
    }

    private fun setupDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun setupUserAuthentication() {
        val user = Firebase.auth.currentUser
//        binding.test.text = user?.email ?: "cannot load"
        // TODO: 이후 별 개수 설정함.
        viewModel.starCount.observe(this, Observer {
            binding.tvStarCount.text = it.toString() + " "
        })
        viewModel.fetchUserStarCount()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupRoutineList() {
        binding.progressBar.visibility = View.VISIBLE

        viewModel.updateRoutineListData()

        val routineAdapter = RoutineAdapter(binding, viewModel, this, routineUpdateResultLauncher)

        val itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(routineAdapter))
        itemTouchHelper.attachToRecyclerView(binding.rvRoutineList)

        binding.rvRoutineList.adapter = routineAdapter
        viewModel.routineList.observe(this, Observer {
            routineAdapter.routineList = it
            routineAdapter.notifyDataSetChanged()

            binding.progressBar.visibility = View.GONE

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
            Toast.makeText(this, "루틴이 업데이트 되었습니다.", Toast.LENGTH_SHORT).show()
            updateRoutine(result)
        }
        if(result.resultCode == ROUTINE_CREATED) {
            createRoutine(result)
        }
    }

    // Function to update routine
    private fun updateRoutine(result: ActivityResult) {
        val updatedRoutine = result.data?.getSerializableExtra("selected_routine") as Routine

        viewModel.updateRoutine(updatedRoutine)
    }

    // Function to create routine
    private fun createRoutine(result: ActivityResult) {
        val updatedRoutine = result.data?.getSerializableExtra("selected_routine") as Routine
//        viewModel.routineList.value?.add(updatedRoutine)

        // TODO: Update DB with the new routine
        viewModel.createRoutine(updatedRoutine)
//        binding.rvRoutineList.adapter?.notifyDataSetChanged()
    }

    // Function to launch RoutineCreateActivity
    private fun launchRoutineCreateActivity() {
        routineUpdateResultLauncher.launch(
            Intent(this, RoutineCreateActivity::class.java).apply {
                putExtra("selected_routine", viewModel.routineList.value?.let {
                    Routine(
                        id="",
                        userId = Firebase.auth.currentUser!!.uid,
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

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(logoutReceiver)
    }

    private val logoutReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "com.routine_mate.mobile_programing.ACTION_LOGOUT") {
                finish()
            }
        }
    }
}