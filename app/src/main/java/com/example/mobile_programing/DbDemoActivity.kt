package com.example.mobile_programing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.mobile_programing.models.Card
import com.example.mobile_programing.models.Routine
//import com.example.mobile_programing.repository.CardRepository
import com.example.mobile_programing.repository.RoutineRepository
//import com.google.firebase.database.ktx.database
//import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.reflect.Array


class DbDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //카드와 관련된 메서드를 사용하기 위한 객체 생성
//        val cardRepository = CardRepository()
        //루틴과 관련된 메서드를 사용하기 위한 객체 생성
        val routineRepository = RoutineRepository()

        val userId = "kim123"


        var s1 = "hello"
        var listCard = arrayListOf<Card>()
        var card1 = Card("",userId,"kim",0,false,3,true,0,true,3,s1)
        var card2 = Card("",userId,"son",4,true,3,false,0,true,3,s1)
        //card1,card2저장
//        cardRepository.createCard(card1)
//        cardRepository.createCard(card2)
        listCard.add(card1)
        listCard.add(card2)

        //루틴 생성(추가 카드없음)
        var routine1: Routine = Routine("","","운동",0, 0, "", listCard)
        //처음 생성한 루틴 저장

        routineRepository.createRoutine(routine1)
        GlobalScope.launch {
        Log.d("루틴",routineRepository.getRoutinesByUserId("lcy1McTm92WNsSfOFj9DWysoj6k1").toString())
        }





        finish()
    }







}