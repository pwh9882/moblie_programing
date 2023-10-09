package com.example.mobile_programing.Model

val routineRef = database.getReference("Routine")
data class Routine (
    var id: String, // Unique ID for the routine // ID의 생성 방법: 6자리, auto increment
    val name: String, // Name of the routine e.g., "Leg Day"
    var totalTime: Int, // total sum of time of card's timer(default 0 )
    val description: String?, // Optional description for the routine
    val cards: ArrayList<Card>  // List of all 'cards' that make up this routine
){
    //초기화
    init {
        id = routineRef.push().key!!
        if(cards.isEmpty()) totalTime = 0
    }
}