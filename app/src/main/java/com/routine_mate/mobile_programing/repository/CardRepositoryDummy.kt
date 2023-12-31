package com.routine_mate.mobile_programing.repository

import com.routine_mate.mobile_programing.models.Card

//Contains all functions related to operations on Cards sub-collection inside specific routines in Firestore.

class CardRepositoryDummy {

    // Creates a new routine in Firestore.
    fun createCard(card: Card): Boolean {
        // 성공했다고 가정하고 dummu data true return
        return true;
    }

    // Fetches a specific routine using its ID from Firestore.
    fun getCard(id: String): Card {
        // 성공했다고 가정하고 dummu data return
        return Card(
            id=id,
            userId = "kim123",
            name="Leg Press",
            preTimerSecs=5,
            preTimerAutoStart=true,
            activeTimerSecs=10,
            activeTimerAutoStart=true,
            postTimerSecs=3,
            postTimerAutoStart=true,
            sets=3,
            memo=""
        )
    }

    // Deletes a specific routine using its ID from Firestore.
    fun deleteCard(id: Int): Boolean {
        // 성공했다고 가정하고 dummu data true return
        return true;
    }

    // Updates given fields of an existing routine in Firestore
    fun updateRoutine(id: Int, newCard: Card): Boolean {
        // 성공했다고 가정하고 dummu data true return
        return true;
    }

    // Fetches all routines from Firestore.
    fun getAllRoutines(): ArrayList<Card> {
        val dummy =ArrayList<Card>()
        return dummy
    }

    // firebase uid로 routine 목록을 가져오는 함수
    fun getCardsByUserId(userId : String): ArrayList<Card> {
        val dummy = ArrayList<Card>()


        return dummy
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