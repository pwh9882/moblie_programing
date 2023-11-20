package com.example.mobile_programing.models

import java.io.Serializable

val routineRef = database.getReference("Routine")
data class Routine  (
    var id: String, // Unique ID for the routine // ID의 생성 방법: 6자리, auto increment
    var userId : String,
    var name: String, // Name of the routine e.g., "Leg Day"
    var routineStartTime: Int, // Time when the routine was started
    var totalTime: Int, // total sum of time of card's timer
    var description: String?, // Optional description for the routine
    var cards: ArrayList<Card>,  // List of all 'cards' that make up this routine
    var numStar: Int,
    var lastModifiedTime: Long = System.currentTimeMillis()
): Serializable {
    //초기화
    init {
        if(numStar == null) numStar = 0
        if(id == "") id = routineRef.push().key!!
        if(cards.isEmpty()) totalTime = 0
    }

    fun updateTotalTime() {
        totalTime = cards.sumOf { it.preTimerSecs + it.activeTimerSecs + it.postTimerSecs }
    }
}