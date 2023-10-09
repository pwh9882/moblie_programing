package com.example.mobile_programing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.mobile_programing.Model.Card
import com.example.mobile_programing.Model.Routine
import com.example.mobile_programing.Model.cardRef
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class DbDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var s1 = arrayListOf<String>("hello")
        var listCard = arrayListOf<Card>()
        var card1: Card = Card("","kim",0,false,3,true,0,true,3,s1)
        var card2: Card = Card("","son",4,true,3,false,0,true,3,s1)
        //card1,card2저장
        card1.saveData()
        card2.saveData()

        //루틴 생성(추가 카드없음)
        var routine1: Routine = Routine("","운동",0, "dsajfldsf",listCard)
        //루틴 저장
        routine1.saveData()

        //루틴 카드추가(card1,card2추가) -- 알아서 데이터베이스 갱신되도록 구현
        routine1.plusCard(card1)
        routine1.plusCard(card2)

        finish()

    }







}