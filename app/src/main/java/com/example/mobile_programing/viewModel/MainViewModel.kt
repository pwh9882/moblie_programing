package com.example.mobile_programing.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_programing.models.Routine
import com.example.mobile_programing.repository.RoutineRepositoryDummy
import kotlinx.coroutines.launch

class MainViewModel(): ViewModel() {
    // data repo dummy 설정
    private val routineRepository: RoutineRepositoryDummy = RoutineRepositoryDummy()

    // live data로 list 설정.
    private val _routineList = MutableLiveData<MutableList<Routine>>()
    val routineList: LiveData<MutableList<Routine>> get() = _routineList

    init {
        viewModelScope.launch {  }
    }

    fun updateRoutineListData(){
        viewModelScope.launch {
            val data = routineRepository.getRoutinesByUserId("dummy")
            _routineList.value = data
        }
    }
}