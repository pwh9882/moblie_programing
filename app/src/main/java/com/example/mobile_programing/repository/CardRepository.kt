package com.example.mobile_programing.repository

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.mobile_programing.Model.Card
import com.example.mobile_programing.Model.database
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.childEvents
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.ParseException
import java.util.concurrent.Flow
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.log

//Contains all functions related to operations on Cards sub-collection inside specific routines in Firestore.

class CardRepository {
    val database = Firebase.database("https://mobile-programing-9ec38-default-rtdb.asia-southeast1.firebasedatabase.app")
    val cardRef = database.getReference("Card")


    // Creates a new routine in Firestore.
    fun createCard(card: Card): Boolean {
        //생성한 카드 firebase Database에 저장
        cardRef.child(card.id).setValue(card)

        // 성공했다고 가정하고 dummu data true return
        return true;
    }


    // Fetches a specific routine using its ID from Firestore.
    //동기 방식으로 처리
      suspend fun getCard(id: String) = suspendCoroutine<Card> {
            var continuation = it
            var card = Card("","", "", 0, true, 0, true, 0, false, 0, arrayListOf())
            cardRef.child(id).get().addOnSuccessListener {
                card.id = it.child("id").value.toString()
                card.name = it.child("name").value.toString()
                card.preTimerSecs = Integer.parseInt(it.child("preTimerSecs").value.toString())
                card.preTimerAutoStart = if (it.child("preTimerAutoStart").value.toString() == "true") { true } else { false }
                card.activeTimerSecs = Integer.parseInt(it.child("activeTimerSecs").value.toString())
                card.activeTimerAutoStart = if (it.child("activeTimerAutoStart").value.toString() == "true") { true } else { false }
                card.postTimerSecs = Integer.parseInt(it.child("postTimerSecs").value.toString())
                card.postTimerAutoStart = if (it.child("postTimerAutoStart").value.toString() == "true") { true } else { false }
                card.sets = Integer.parseInt(it.child("sets").value.toString())
                var additionalInfo = it.child("additionalInfo").value.toString()
                additionalInfo = additionalInfo.substring(1, additionalInfo.length - 1)
                var listAdditional = additionalInfo.split(",")
                //카드의 aditionalInfo는 arrayList여서 반복문을 통해 list형인 listAddition의 값들을 다시 additionalInfo에 넣어줌
                for (info: String in listAdditional) {
                    card.additionalInfo.add(info)
                }
                continuation.resume(card)//resume -> return기능
            }
    }

    // Deletes a specific routine using its ID from Firestore.
    fun deleteCard(id: String): Boolean {
        cardRef.child(id).removeValue()
        return true;
    }

    // Updates given fields of an existing routine in Firestore
    fun updateRoutine(id: Int, newCard: Card): Boolean {
        // 성공했다고 가정하고 dummy data true return
        return true;
    }

    // Fetches all routines from Firestore.
    fun getAllRoutines(): ArrayList<Card> {
        val dummy =ArrayList<Card>()
        return dummy
    }

    // firebase uid로 routine 목록을 가져오는 함수
    suspend fun getCardsByUserId(userId : String) = suspendCoroutine<ArrayList<Card>> {
            var cards = ArrayList<Card>()
            var continuation = it
            cardRef.get().addOnSuccessListener {
                it.children.forEach {
                    if(it.child("userId").value.toString() != userId) return@forEach
                    var card = Card("", "", "", 0, true, 0, true, 0, false, 0, arrayListOf())
                    card.id = it.child("id").value.toString()
                    card.userId = it.child("userId").value.toString()
                    card.name = it.child("name").value.toString()
                    card.preTimerSecs = Integer.parseInt(it.child("preTimerSecs").value.toString())
                    card.preTimerAutoStart =
                        if (it.child("preTimerAutoStart").value.toString() == "true") {
                            true
                        } else {
                            false
                        }
                    card.activeTimerSecs =
                        Integer.parseInt(it.child("activeTimerSecs").value.toString())
                    card.activeTimerAutoStart =
                        if (it.child("activeTimerAutoStart").value.toString() == "true") {
                            true
                        } else {
                            false
                        }
                    card.postTimerSecs =
                        Integer.parseInt(it.child("postTimerSecs").value.toString())
                    card.postTimerAutoStart =
                        if (it.child("postTimerAutoStart").value.toString() == "true") {
                            true
                        } else {
                            false
                        }
                    card.sets = Integer.parseInt(it.child("sets").value.toString())
                    var additionalInfo = it.child("additionalInfo").value.toString()
                    additionalInfo = additionalInfo.substring(1, additionalInfo.length - 1)
                    var listAdditional = additionalInfo.split(",")
                    //카드의 aditionalInfo는 arrayList여서 반복문을 통해 list형인 listAddition의 값들을 다시 additionalInfo에 넣어줌
                    for (info: String in listAdditional) {
                        card.additionalInfo.add(info)
                    }
                    cards.add(card)
                }
                continuation.resume(cards)
            }
    }

    // user-id와 그에 해당하는 history routine 목록을 가져오는 함수
    fun getHistoryCardsByUserId(userId : String): ArrayList<Card> {
        TODO(" Implement function for fetching all history routines belonging to current logged-in user")
    }

    // user-id와 그에 해당하는 favorite routine 목록을 가져오는 함수
    fun getFavoriteCardsByUserId(userId : String): ArrayList<Card> {
        TODO(" Implement function for fetching all favorite routines belonging to current logged-in user")
    }


}