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
        //var routine1: Routine = Routine("","",5,"운동",0, 0, "", listCard)
        //처음 생성한 루틴 저장
        var routine1: Routine = Routine("1","kim",0,"운동",0, 0, "", listCard)
        var routine2: Routine = Routine("2","kim",0,"러닝",0, 0, "", listCard)
        var routine3: Routine = Routine("3","son",0,"복싱",0, 0, "", listCard)
        var routine4: Routine = Routine("4","son",0,"주짓수",0, 0, "", listCard)
        var routine5: Routine = Routine("5","kim",0,"수영",0, 0, "", listCard)


        routineRepository.createRoutine(routine1)
        routineRepository.createRoutine(routine2)
        routineRepository.createRoutine(routine3)
        routineRepository.createRoutine(routine4)
        routineRepository.createRoutine(routine5)

//        GlobalScope.launch {
//        Log.d("루틴",routineRepository.getRoutinesByUserId("lcy1McTm92WNsSfOFj9DWysoj6k1").toString())
//        }

        GlobalScope.launch {
            Log.d(
                "루틴",
                routineRepository.getRoutinesByUserId("lcy1McTm92WNsSfOFj9DWysoj6k1").toString()
            )
            //routein1 별개수 2개 ,routine2 별개수 1개, 사용자 "kim"의 별개수 총 3개
            routineRepository.addStar(routine1.id)
            routineRepository.addStar(routine2.id)


            //routine4의 별개수 1개, 사용자 "son"의 별개수 총 1개
            routineRepository.addStar(routine4.id)
        }
        GlobalScope.launch{

            Log.d(".",routineRepository.getRoutine(routine1.id).numStar.toString())
            Log.d("kim",routineRepository.getUserStar("kim").toString())}

        finish()
    }




}