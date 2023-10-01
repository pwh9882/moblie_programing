package com.example.mobile_programing.Model

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.reflect.Constructor


val database = Firebase.database("https://mobile-programing-9ec38-default-rtdb.asia-southeast1.firebasedatabase.app")
val cardRef = database.getReference("Card")

data class Card (
    var id: Int, // db에서 auto increment로 생성
    var name: String, // Name of the card e.g., "Squats"

    var preTimerSecs : Int,
    var preTimerAutoStart : Boolean,

    var activeTimerSecs : Int,
    var activeTimerAutoStart : Boolean,

    var postTimerSecs : Int,
    var postTimerAutoStart : Boolean,

    var sets : Int,

    var additionalInfo : ArrayList<String>



){
    constructor(id: Int,name: String,preTimerAutoStart: Boolean,activeTimerSecs: Int,activeTimerAutoStart: Boolean
   ,postTimerAutoStart: Boolean,sets: Int,additionalInfo: ArrayList<String>) : this(id, name,3, preTimerAutoStart, activeTimerSecs, activeTimerAutoStart, 3, postTimerAutoStart, sets, additionalInfo)

    fun saveData(){
        cardRef.child(this.id.toString()).setValue(this)

    }

}