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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.reflect.Array


class DbDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //카드와 관련된 메서드를 사용하기 위한 객체 생성
        val cardRepository = CardRepository()
        //루틴과 관련된 메서드를 사용하기 위한 객체 생성
        val routineRepository = RoutineRepository()

        val userId = "kim123"


        var s1 = arrayListOf<String>("hello")
        var listCard = arrayListOf<Card>()
        var card1 = Card("",userId,"kim",0,false,3,true,0,true,3,s1)
        var card2 = Card("",userId,"son",4,true,3,false,0,true,3,s1)
        //card1,card2저장
        cardRepository.createCard(card1)
        cardRepository.createCard(card2)
        listCard.add(card1)
        listCard.add(card2)

        //루틴 생성(추가 카드없음)
        var routine1: Routine = Routine("","운동",0, "description1",listCard)
        var routine2: Routine = Routine("","코딩",0, "description2",listCard)
        var routine3: Routine = Routine("","음식",0, "description3",listCard)
        //처음 생성한 루틴 저장


        routineRepository.createRoutine(routine1)
        routineRepository.createRoutine(routine2)
        routineRepository.createRoutine(routine3)

        /*
        * 기존 루틴에 카드가 추가 되었으므로 fireDatabase의 기존 루틴도 카드가 추가된 루틴으로 갱신되어야 합니다
        * 이때 사용하는 함수는 changeRoutine()입니다
        * changeRoutine()에는 카드가 추가됨으로써 변하게 되는 totalTime을 계산하는 로직이 포함되있습니다
        * */

//        GlobalScope.launch {
//            var cardData = cardRepository.getCard(card1.id)
//            var cards = ArrayList<Card>()
//            Log.d("getCard",cardData.toString())
//            cards = cardRepository.getCardsByUserId(userId)
//            Log.d("cards",cards.toString())
//            var routine_test = routineRepository.getRoutine(routine1.id)
//            Log.d("routine",routine_test.toString())
//
//        }
        //cardRepository.deleteCard(card2.id)


        //update 테스트(Routine3의 내용을 newRoutine으로 변경)
        GlobalScope.launch {
            var newRoutine: Routine = Routine("", "러닝", 10, "description4", listCard)
            Log.d("변경전", "id: ${routine3.id} => ${routine3}")
            routineRepository.updateRoutine(
                routine3.id,
                newRoutine
            )//Routine3아이디에 Routine3의 정보를 newRoutine의 정보로 변경
            Log.d("변경후", "id: ${routine3.id} => ${routineRepository.getRoutine(routine3.id)}")
        }
        finish()
    }







}