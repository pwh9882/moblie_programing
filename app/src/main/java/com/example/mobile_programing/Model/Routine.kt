package com.example.mobile_programing.Model

data class Routine (
    val id: Int, // Unique ID for the routine // ID의 생성 방법: 6자리, auto increment

    val name: String, // Name of the routine e.g., "Leg Day"
    val totalTime: Int, // total sum of time of card's timer
    val description: String?, // Optional description for the routine
    val cards: ArrayList<Card>  // List of all 'cards' that make up this routine
)