package com.example.mobile_programing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.mobile_programing.models.Card
import com.example.mobile_programing.models.Routine
import com.example.mobile_programing.repository.CardRepository
import com.example.mobile_programing.repository.RoutineRepository
//import com.google.firebase.database.ktx.database
//import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
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
        var card1 = Card("", userId, "kim", 0, false, 3, true, 0, true, 3, s1)
        var card2 = Card("", userId, "son", 4, true, 3, false, 0, true, 3, s1)
        //card1,card2저장
        cardRepository.createCard(card1)
        cardRepository.createCard(card2)
        listCard.add(card1)
        listCard.add(card2)

        //루틴 생성(추가 카드없음)
        var routine1: Routine = Routine("1", "kim", "운동", 0, 0, "", listCard, 0)
        var routine2: Routine = Routine("2", "kim", "러닝", 0, 0, "", listCard, 0)
        var routine3: Routine = Routine("3", "son", "복싱", 0, 0, "", listCard, 0)
        var routine4: Routine = Routine("4", "son", "주짓수", 0, 0, "", listCard, 0)
        var routine5: Routine = Routine("5", "kim", "수영", 0, 0, "", listCard, 0)

        //처음 생성한 루틴 저장

        routineRepository.createRoutine(routine1)
        routineRepository.createRoutine(routine2)
        routineRepository.createRoutine(routine3)
        routineRepository.createRoutine(routine4)
        routineRepository.createRoutine(routine5)


        //별개수 0개 아닐때 확인
        GlobalScope.launch {
            routineRepository.createRoutine(Routine("6", "kim", "수영", 0, 0, "", listCard, 4))
            Log.d("0개아닐때",routineRepository.getRoutine("6").numStar.toString())
            Log.d("0개일때",routineRepository.getRoutine(routine3.id).numStar.toString())
        }


        GlobalScope.launch {
            //routein1 별개수 2개 ,routine2 별개수 1개 사용자 "kim"의 별개수 총 3개
            routineRepository.addStar(routine1.id)
            routineRepository.addStar(routine2.id)


            //routine4의 별개수 1개, routine1 별 1개 추가 -> routine1의 별개수 별 2개
            routineRepository.addStar(routine4.id)
            delay(2000)
            routineRepository.addStar(routine1.id)
            /*
             사용자별 별개수 kim: 3개, son: 1개
             */
            delay(2000)
            Log.d("addStar테스트",routineRepository.getRoutine(routine1.id).numStar.toString())//루틴1
            delay(2000)
            Log.d("userStar테스트",routineRepository.getUserStar("kim").toString())//kim
        }


        finish()
    }
}