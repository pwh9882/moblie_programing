package com.example.mobile_programing.repository

import com.example.mobile_programing.models.Card
import com.example.mobile_programing.models.Routine

// Contains all functions related to operations on Routines collection in Firestore.
class RoutineRepositoryDummy {

//    private val db = FirebaseFirestore.getInstance()
//    private val routineCollection = db.collection("routines")

    // Creates a new routine in Firestore.
    fun createRoutine(routine: Routine): Boolean {
        // 성공했다고 가정하고 dummu data true return
        return true;
    }

    // Fetches a specific routine using its ID from Firestore.
    fun getRoutine(id: String): Routine {
        // 성공했다고 가정하고 dummu data return
        return Routine(id=id, userId = "", name="Leg Day", totalTime=100, routineStartTime = 1, description="Leg Day", cards=ArrayList(), numStar = 0)
    }

    // Deletes a specific routine using its ID from Firestore.
    fun deleteRoutine(id: String): Boolean {
        // 성공했다고 가정하고 dummu data true return
        return true;
    }

    // Updates given fields of an existing routine in Firestore
    fun updateRoutine(id: String, newRoutine: Routine): Boolean {
        // 성공했다고 가정하고 dummu data true return
        return true;
    }

    // Fetches all routines from Firestore.
    fun getAllRoutines(): ArrayList<Routine> {
        val dummy =ArrayList<Routine>()
        return dummy
    }

    // firebase uid로 routine 목록을 가져오는 함수
    fun getRoutinesByUserId(userId : String): ArrayList<Routine> {
        val dummy = ArrayList<Routine>()
        dummy.add(Routine("0","", "루틴1", 0,3000, "루틴1입니다.", ArrayList<Card>().apply {
            add(Card("0", "", "카드1-1", 5, true, 10, true, 3, true, 3, "메모1"))
            add(Card("1", "","카드1-2", 4, true, 10, true, 3, true, 2, "메모2"))
        }))
        dummy.add(Routine("0","", "루틴2", 0,3000, "루틴1입니다.", ArrayList<Card>().apply {
            add(Card("0", "", "카드2-1", 5, true, 10, true, 3, true, 3, "메모1"))
            add(Card("1", "","카드2-2", 4, true, 10, true, 3, true, 2, "메모2"))
        }))
        return dummy
    }

    // user-id와 그에 해당하는 history routine 목록을 가져오는 함수
    fun getHistoryRoutinesByUserId(userId : String): ArrayList<Routine> {
        TODO(" Implement function for fetching all history routines belonging to current logged-in user")
    }

    // user-id와 그에 해당하는 favorite routine 목록을 가져오는 함수
    fun getFavoriteRoutinesByUserId(userId : String): ArrayList<Routine> {
        TODO(" Implement function for fetching all favorite routines belonging to current logged-in user")
    }

    // 추가사항: user-id에 해당하는 routine 목록을 폴더 형식으로 가져오는 함수

}