package com.routine_mate.mobile_programing.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.routine_mate.mobile_programing.models.Routine
import com.routine_mate.mobile_programing.repository.RoutineRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.pow
import kotlin.math.roundToInt

class MainViewModel(): ViewModel() {
    // data repo dummy 설정
//    private val routineRepository: RoutineRepositoryDummy = RoutineRepositoryDummy()
    private val routineRepository: RoutineRepository = RoutineRepository()

    // live data로 list 설정.
    private val _routineList = MutableLiveData<MutableList<Routine>>()
    val routineList: LiveData<MutableList<Routine>> get() = _routineList

    // 별 개수 설정
    private val _starCount = MutableLiveData<Int>()
    val starCount: LiveData<Int> get() = _starCount

    init {
        viewModelScope.launch {  }
    }

    // 별 개수 가져와서 업데이트
    fun fetchUserStarCount() {
        viewModelScope.launch {
            _starCount.value = routineRepository.getUserStars(Firebase.auth.currentUser!!.uid)
        }
    }

    fun createRoutine(routine: Routine) {
        viewModelScope.launch {
            // Firebase에 루틴을 추가합니다.
            routineRepository.createRoutine(routine)
            updateRoutineListData()
            fetchUserStarCount()
        }
    }

    fun updateRoutine(routine: Routine) {
        viewModelScope.launch {
            // Firebase에 루틴을 업데이트합니다.
            routineRepository.updateRoutine(routine.id, routine)
            updateRoutineListData()
            fetchUserStarCount()
        }
    }

    fun updateRoutineListData(){
        viewModelScope.launch {
            val data = routineRepository.getRoutinesByUserId(Firebase.auth.currentUser!!.uid)
            // data를 mutablelist로 변환
            _routineList.value = data.toMutableList().apply {
                // Sort the list in descending order of lastModifiedTime
                sortWith(Comparator { r1, r2 -> r2.lastModifiedTime.compareTo(r1.lastModifiedTime) })
            }
        }
    }

    fun deleteRoutine(routineId: String) {
        viewModelScope.launch {
            routineRepository.deleteRoutine(routineId)
            updateRoutineListData()
            fetchUserStarCount()
            // Firebase에서 아이템을 삭제합니다.
//            // TODO: 현재 dummy로 무조건 true를 반환하도록 되어 있습니다.
//            val isSuccess = routineRepository.deleteRoutine(routine.id)
//            if (isSuccess) {
//                // 로컬 리스트에서 아이템을 삭제하고 LiveData를 업데이트합니다.
//                // 아이템이 존재할때만 스와이프가 가능하고, 삭제가 가능하므로 null 가능성 없음.
//                val updatedList = _routineList.value!!.toMutableList()
//                updatedList.remove(routine)
//                _routineList.value = updatedList
//            } else {
//                // 에러 처리
//            }
        }
    }

    suspend fun getUserStarPercentile(userId: String): Double = suspendCoroutine { continuation ->
        viewModelScope.launch {
            val allStars = routineRepository.getAllUserStars()
            val userStars = routineRepository.getUserStars(userId)
            val percentile = calculatePercentile(allStars, userStars)
            continuation.resume(percentile)
        }
    }

    fun calculatePercentile(allStars: List<Int>, userStars: Int): Double {
        // 사용자가 받은 별의 수보다 많은 별을 받은 사용자의 수를 세는 것
        val countMore = allStars.count { it > userStars }

        // 같은 수의 별을 받은 사용자의 수를 세는 것 (사용자 자신을 제외)
        val countEqual = allStars.count { it == userStars } - 1

        // 퍼센타일 계산: (더 많은 별을 받은 사람들의 비율 + 동일한 별을 받은 사람들의 절반의 비율)
        val percentile = 100 - ((countMore + (0.5 * countEqual)) / allStars.size * 100)

        // 결과를 3자리까지 반올림
        return (100 - percentile).round(3)
    }


    // 소수점 아래 n자리까지 반올림하는 확장 함수
    fun Double.round(n: Int): Double {
        return (this * 10.0.pow(n)).roundToInt() / 10.0.pow(n)
    }


}