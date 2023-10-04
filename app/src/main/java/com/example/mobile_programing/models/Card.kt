package com.example.mobile_programing.models

data class Card (
    val id: Int, // db에서 auto increment로 생성
    val name: String, // Name of the card e.g., "Squats"

    val preTimerSecs : Int,
    val preTimerAutoStart : Boolean,

    val activeTimerSecs : Int,
    val activeTimerAutoStart : Boolean,

    val postTimerSecs : Int,
    val postTimerAutoStart : Boolean,

    val sets : Int,

    val additionalInfo : ArrayList<String>

)