package com.example.mobile_programing.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_programing.models.Routine
import com.example.mobile_programing.repository.RoutineRepositoryDummy
import kotlinx.coroutines.launch
import java.util.Collections

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

    fun deleteRoutine(routine: Routine) {
        viewModelScope.launch {
            // Firebase에서 아이템을 삭제합니다.
            // TODO: 현재 dummy로 무조건 true를 반환하도록 되어 있습니다.
            val isSuccess = routineRepository.deleteRoutine(routine.id)
            if (isSuccess) {
                // 로컬 리스트에서 아이템을 삭제하고 LiveData를 업데이트합니다.
                // 아이템이 존재할때만 스와이프가 가능하고, 삭제가 가능하므로 null 가능성 없음.
                val updatedList = _routineList.value!!.toMutableList()
                updatedList.remove(routine)
                _routineList.value = updatedList
            } else {
                // 에러 처리
            }
        }
    }

}