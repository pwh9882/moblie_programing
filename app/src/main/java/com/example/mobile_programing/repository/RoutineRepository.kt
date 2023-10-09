package com.example.mobile_programing.repository

import com.example.mobile_programing.models.Routine

// Contains all functions related to operations on Routines collection in Firestore.
class RoutineRepository {

//    private val db = FirebaseFirestore.getInstance()
//    private val routineCollection = db.collection("routines")

    // Creates a new routine in Firestore.
    fun createRoutine(routine: Routine): Boolean {
        TODO("Implement function to create a new document in Firebase Firestore for the given routine.")
    }

    // Fetches a specific routine using its ID from Firestore.
    fun getRoutine(id: Int): Routine {
        TODO("Implement function to fetch a specific document using its ID from Firebase Firestore.")
    }

    // Deletes a specific routine using its ID from Firestore.
    fun deleteRoutine(id: Int): Boolean {
        TODO("Implement function to delete a specific document (Routine) using its ID from Firebase Firestore.")
    }

    // Updates given fields of an existing routine in Firestore
    fun updateRoutine(id: Int, newRoutine: Routine): Boolean {
        TODO("Implement function to update certain fields of a specific Routine Document in Firebase Firestore")
    }

    // Fetches all routines from Firestore.
    fun getAllRoutines(): ArrayList<Routine> {
        TODO("Implement function fetching all documents (<strong><em>'Routines') present in Firebase Firestore.")
    }

    // firebase uid로 routine 목록을 가져오는 함수
    fun getRoutinesByUserId(userId : String): ArrayList<Routine> {
        TODO(" Implement function for fetching all routines belonging to current logged-in user")
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