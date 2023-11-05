package com.example.mobile_programing.repository

import android.util.Log
import com.example.mobile_programing.Model.Card
import com.example.mobile_programing.Model.Routine
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine



// Contains all functions related to operations on Routines collection in Firestore.
class RoutineRepository {
    val database =
        Firebase.database("https://mobile-programing-9ec38-default-rtdb.asia-southeast1.firebasedatabase.app")
    val routineRef = database.getReference("Routine")

    // Creates a new routine in Firestore.
    fun createRoutine(routine: Routine): Boolean {
        //firebase에 생성한 루틴 저장
        routineRef.child(routine.id).setValue(routine)

        //루틴에 카드가 추가됬을경우 totalTime을 계산해주는 로직
//        if (!routine.cards.isEmpty()) {
//            var time = 0
//            for (card in routine.cards) {
//                time += (card.preTimerSecs + card.activeTimerSecs + card.postTimerSecs) * card.sets
//            }
//            routine.totalTime = time
//            routineRef.child(routine.id).setValue(routine)
//        }
        return true
        TODO("Implement function to create a new document in Firebase Firestore for the given routine.")
    }

    // Fetches a specific routine using its ID from Firestore.
    suspend fun getRoutine(id: String) = suspendCoroutine<Routine> { continuation ->
        val routine = Routine("", "", "",0, "", arrayListOf())
        routineRef.child(id).get().addOnSuccessListener { snapshot ->
            routine.id = snapshot.child("id").value.toString()
            routine.userId = snapshot.child("userId").value.toString()
            routine.name = snapshot.child("name").value.toString()
            routine.totalTime = Integer.parseInt(snapshot.child("totalTime").value.toString())
            routine.description = snapshot.child("description").value.toString()

            // Parsing Card objects
            val cardsSnapshot = snapshot.child("cards")
            cardsSnapshot.children.forEach { cardData ->
                val card = Card(
                    id = cardData.child("id").value.toString(),
                    userId = cardData.child("userId").value.toString(),
                    name = cardData.child("name").value.toString(),
                    preTimerSecs = cardData.child("preTimerSecs").value.toString().toInt(),
                    preTimerAutoStart = cardData.child("preTimerAutoStart").value.toString().toBoolean(),
                    activeTimerSecs = cardData.child("activeTimerSecs").value.toString().toInt(),
                    activeTimerAutoStart = cardData.child("activeTimerAutoStart").value.toString().toBoolean(),
                    postTimerSecs = cardData.child("postTimerSecs").value.toString().toInt(),
                    postTimerAutoStart = cardData.child("postTimerAutoStart").value.toString().toBoolean(),
                    sets = cardData.child("sets").value.toString().toInt(),
                    additionalInfo = (cardData.child("additionalInfo").value as List<String>).toCollection(ArrayList())
                )
                routine.cards.add(card)
            }

            continuation.resume(routine)
        }
    }


    // Deletes a specific routine using its ID from Firestore.
        fun deleteRoutine(id: String): Boolean {
            routineRef.child(id).removeValue()
            return true;
        }

        // Updates given fields of an existing routine in Firestore
        fun updateRoutine(id: String, newRoutine: Routine): Boolean {
            TODO("Implement function to update certain fields of a specific Routine Document in Firebase Firestore")
        }


        // Fetches all routines from Firestore.
        fun getAllRoutines(): ArrayList<Routine> {
            TODO("Implement function fetching all documents (<strong><em>'Routines') present in Firebase Firestore.")
        }

        // firebase uid로 routine 목록을 가져오는 함수
        suspend fun getRoutinesByUserId(userId: String): List<Routine> = suspendCoroutine { continuation ->
            routineRef.child(userId).get().addOnSuccessListener { snapshot ->
                val routines = snapshot.children.mapNotNull { routineSnapshot ->
                    // This checks if the userId of the routine matches the one we're looking for
                    if (routineSnapshot.child("userId").value.toString() != userId) return@mapNotNull null

                    val cards = ArrayList<Card>()
                    routineSnapshot.child("cards").children.forEach { cardData ->
                        val userIdOfCard = cardData.child("userId").value.toString()
                        // Only add cards that match the userId we're interested in
                        if (userIdOfCard != userId) return@forEach

                        val card = Card(
                            id = cardData.child("id").value.toString(),
                            userId = userIdOfCard,
                            name = cardData.child("name").value.toString(),
                            preTimerSecs = cardData.child("preTimerSecs").value.toString().toIntOrNull() ?: 0,
                            preTimerAutoStart = cardData.child("preTimerAutoStart").value.toString().toBoolean(),
                            activeTimerSecs = cardData.child("activeTimerSecs").value.toString().toIntOrNull() ?: 0,
                            activeTimerAutoStart = cardData.child("activeTimerAutoStart").value.toString().toBoolean(),
                            postTimerSecs = cardData.child("postTimerSecs").value.toString().toIntOrNull() ?: 0,
                            postTimerAutoStart = cardData.child("postTimerAutoStart").value.toString().toBoolean(),
                            sets = cardData.child("sets").value.toString().toIntOrNull() ?: 0,
                            additionalInfo = (cardData.child("additionalInfo").value as? List<String>)?.toCollection(ArrayList()) ?: arrayListOf<String>()
                        )
                        cards.add(card)
                    }

                    Routine(
                        id = routineSnapshot.child("id").value.toString(),
                        userId = userId,
                        name = routineSnapshot.child("name").value.toString(),
                        totalTime = routineSnapshot.child("totalTime").value.toString().toIntOrNull() ?: 0,
                        description = routineSnapshot.child("description").value.toString(),
                        cards = cards
                    )
                }
                continuation.resume(routines)
            }.addOnFailureListener { exception ->
                continuation.resumeWith(Result.failure(exception))
            }
        }




    // user-id와 그에 해당하는 history routine 목록을 가져오는 함수
        fun getHistoryRoutinesByUserId(userId: String): ArrayList<Routine> {
            TODO(" Implement function for fetching all history routines belonging to current logged-in user")
        }

        // user-id와 그에 해당하는 favorite routine 목록을 가져오는 함수
        fun getFavoriteRoutinesByUserId(userId: String): ArrayList<Routine> {
            TODO(" Implement function for fetching all favorite routines belonging to current logged-in user")
        }

        // 추가사항: user-id에 해당하는 routine 목록을 폴더 형식으로 가져오는 함수


}