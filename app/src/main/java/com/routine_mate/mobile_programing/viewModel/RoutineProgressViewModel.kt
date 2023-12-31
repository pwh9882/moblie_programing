package com.routine_mate.mobile_programing.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.routine_mate.mobile_programing.models.Card
import com.routine_mate.mobile_programing.models.Routine
import com.routine_mate.mobile_programing.repository.RoutineRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoutineProgressViewModel: ViewModel() {
    private val routineRepository = RoutineRepository()

    // 현재 진행중인 루틴
    private val _currentRoutine = MutableLiveData<Routine>()
    val currentRoutine: LiveData<Routine> get() = _currentRoutine

    // 현재 진행중인 카드
    private val _currentCard = MutableLiveData<Card>()
    val currentCard: LiveData<Card> get() = _currentCard

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

    // Add a flag to track whether the timer is paused
    private var _isPaused = MutableLiveData<Boolean>()
    val isPaused: LiveData<Boolean> get() = _isPaused


    // 현재 카드의 진행 단계: 0: 진행 전, 1: 진행 중, 2: 진행 완료
    private val _currentCardProgress = MutableLiveData<Int>()
    val currentCardProgress: LiveData<Int> get() = _currentCardProgress

    // 현재 카드의 반복횟수 (세트)
    private val _currentCardSet = MutableLiveData<Int>()
    val currentCardSet: LiveData<Int> get() = _currentCardSet

    var isRoutineUpdated = false




    init {
        viewModelScope.launch {  }
    }

    fun setCurrentCard(card: Card){
        _currentCard.value = card
    }

    fun updateCurrentRoutineData(routine: Routine){
        _currentRoutine.value = routine
    }
    fun updateCurrentCardIndexData(index: Int){
        _currentCardIndex.value = index
        _currentCardTime.value = _currentRoutine.value!!.cards[index].activeTimerSecs
        _currentCardProgress.value = 0
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

    fun startCardTimer(time: Int){
        _isPaused.value = false
        cardTimerJob?.cancel()
        cardTimerJob = viewModelScope.launch {
            _currentCardTime.value = time
            while (_currentCardTime.value!! >= 0) {
                withContext(Dispatchers.IO) {
                    Thread.sleep(1000)
                }
                if (!isPaused.value!!) {
                    _currentCardTime.value = _currentCardTime.value?.minus(1)
                }
            }
        }
    }
    fun stopCardTimer(){
        cardTimerJob?.cancel()
    }

    // Add methods to pause and resume the timer
    fun pauseTimer() {
        _isPaused.value = true
    }

    fun resumeTimer() {
        _isPaused.value = false
    }

    fun initCardProgressInfo(){
        setCardProgress(0)
        setCardSet(1)

    }

    fun setCardProgress(progress: Int){
        _currentCardProgress.value = progress
    }
    fun setCardSet(set: Int){
        _currentCardSet.value = set
    }

    // 별 하나 획득
    fun incrementUserStars(userId: String){
        viewModelScope.launch {
            routineRepository.incrementUserStars(userId)
        }
    }
}