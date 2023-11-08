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

    // 아이템 위치 변경을 처리하는 함수
    fun swapRoutines(fromPosition: Int, toPosition: Int) {
        // LiveData의 값 내에서 아이템 위치를 변경
        val list = _routineList.value!!
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(list, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(list, i, i - 1)
            }
        }
        // LiveData를 업데이트하여 변경사항을 알립니다.
        _routineList.value = list


        // TODO: Firebase에서도 순서를 업데이트합니다.
//        updateRoutineOrderInFirebase(list)
    }

    fun moveRoutine(fromPosition: Int, toPosition: Int) {
        viewModelScope.launch {
            _routineList.value = _routineList.value?.toMutableList()?.apply {
                if (fromPosition < toPosition) {
                    for (i in fromPosition until toPosition) {
                        Collections.swap(this, i, i + 1)
                    }
                } else {
                    for (i in fromPosition downTo toPosition + 1) {
                        Collections.swap(this, i, i - 1)
                    }
                }
                // 여기에서 Firebase 데이터베이스 업데이트 로직을 추가하세요
            }
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

    fun updateRoutineList(routineList: MutableList<Routine>) {
        viewModelScope.launch {
            _routineList.value = routineList
        }

    }
}