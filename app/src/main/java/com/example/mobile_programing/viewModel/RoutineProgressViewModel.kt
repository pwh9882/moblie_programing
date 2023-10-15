package com.example.mobile_programing.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_programing.models.Routine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoutineProgressViewModel: ViewModel() {
    // 현재 진행중인 루틴
    private val _currentRoutine = MutableLiveData<Routine>()
    val currentRoutine: LiveData<Routine> get() = _currentRoutine

    // 현재 진행중인 카드의 인덱스
    private val _currentCardIndex = MutableLiveData<Int>()
    val currentCardIndex: LiveData<Int> get() = _currentCardIndex

    // 현재 진행중인 전체 루틴 타이머
    private val _currentRoutineTime = MutableLiveData<Int>()
    val currentRoutineTime: LiveData<Int> get() = _currentRoutineTime
    private var routineTimerJob: Job? = null

    // 현재 진행중인 카드 타이머
    private val _currentCardTime = MutableLiveData<Int>()
    val currentCardTime: LiveData<Int> get() = _currentCardTime
    private var cardTimerJob: Job? = null



    init {
        viewModelScope.launch {  }
    }

    fun updateCurrentRoutineData(routine: Routine){
        _currentRoutine.value = routine
    }
    fun updateCurrentCardIndexData(index: Int){
        _currentCardIndex.value = index
        _currentCardTime.value = _currentRoutine.value!!.cards[index].activeTimerSecs
    }
    fun updateCurrentRoutineTimerData(timer: Int){
        _currentRoutineTime.value = timer
    }

    // 타이머를 업데이트하는 함수(종료시까지 계속 1초마다 증가)
    fun startRoutineTimer(){
        routineTimerJob?.cancel()
        routineTimerJob = viewModelScope.launch {
            _currentRoutineTime.value = -1
            while(true){
                _currentRoutineTime.value = _currentRoutineTime.value?.plus(1)
                withContext(Dispatchers.IO) {
                    Thread.sleep(1000)
                }
            }
        }
    }

    fun stopRoutineTimer(){
        routineTimerJob?.cancel()
    }

    fun startCardTimer(){
        cardTimerJob?.cancel()
        cardTimerJob = viewModelScope.launch {
            _currentCardTime.value = _currentRoutine.value!!.cards[_currentCardIndex.value!!].activeTimerSecs+1
            while (true) {
                _currentCardTime.value = _currentCardTime.value?.minus(1)
                withContext(Dispatchers.IO) {
                    Thread.sleep(1000)
                }
            }
        }
    }
    fun stopCardTimer(){
        cardTimerJob?.cancel()
    }
}