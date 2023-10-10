package com.example.mobile_programing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.mobile_programing.Model.Card
import com.example.mobile_programing.Model.Routine
import com.example.mobile_programing.Model.cardRef
import com.example.mobile_programing.repository.CardRepository
import com.example.mobile_programing.repository.RoutineRepository
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class DbDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //카드와 관련된 메서드를 사용하기 위한 객체 생성
        val cardRepository = CardRepository()
        //루틴과 관련된 메서드를 사용하기 위한 객체 생성
        val routineRepository = RoutineRepository()


        var s1 = arrayListOf<String>("hello")
        var listCard = arrayListOf<Card>()
        var card1 = Card("","kim",0,false,3,true,0,true,3,s1)
        var card2 = Card("","son",4,true,3,false,0,true,3,s1)
        //card1,card2저장
        Log.d("db",cardRepository.createCard(card1).toString())
        cardRepository.createCard(card2)

        //루틴 생성(추가 카드없음)
        var routine1: Routine = Routine("","운동",0, "dsajfldsf",listCard)
        //처음 생성한 루틴 저장
        routineRepository.createRoutine(routine1)

        //루틴 카드추가(card1,card2추가)
        routine1.cards.add(card1)
        routine1.cards.add(card2)

        /*
        * 기존 루틴에 카드가 추가 되었으므로 fireDatabase의 기존 루틴도 카드가 추가된 루틴으로 갱신되어야 합니다
        * 이때 사용하는 함수는 changeRoutine()입니다
        * changeRoutine()에는 카드가 추가됨으로써 변하게 되는 totalTime을 계산하는 로직이 포함되있습니다
        * */

        //갱신된 루틴 Friebase Database에서도 갱신
        routineRepository.createRoutine(routine1)

        finish()

    }







}