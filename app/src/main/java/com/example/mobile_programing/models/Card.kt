package com.example.mobile_programing.models

import java.io.Serializable

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

val database = Firebase.database("https://mobile-programing-9ec38-default-rtdb.asia-southeast1.firebasedatabase.app")
val cardRef = database.getReference("Card")



data class Card (
    var id : String, // db에서 auto increment로 생성
    var userId : String,
    var name: String, // Name of the card e.g., "Squats"

    var preTimerSecs: Int,
    var preTimerAutoStart : Boolean,

    var activeTimerSecs : Int,
    var activeTimerAutoStart : Boolean,

    var postTimerSecs : Int,
    var postTimerAutoStart : Boolean,

    var sets : Int,

    var additionalInfo : ArrayList<String>

): Serializable {
    /*초기화
    * 사전타이머 시간과 사후 타이머 시간을 입력하지 않았을때 기본세팅값 3초로 세팅
    * 세트수는 1
    * */
    init {
        if (preTimerSecs == 0) preTimerSecs = 3
        if(postTimerSecs == 0) postTimerSecs = 3
        if(sets == 0) sets = 1
        id = cardRef.push().key!!

    }



}