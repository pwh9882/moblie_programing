package com.routine_mate.mobile_programing.repository

import com.routine_mate.mobile_programing.models.Card
import com.routine_mate.mobile_programing.models.Routine
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine



// Contains all functions related to operations on Routines collection in Firestore.
class RoutineRepository {
    val database =
        Firebase.database("https://mobile-programing-9ec38-default-rtdb.asia-southeast1.firebasedatabase.app")
    val routineRef = database.getReference("Routine")
    val cardRepo = CardRepository()

    // Creates a new routine in Firestore.
    fun createRoutine(routine: Routine): Boolean {
        //firebase에 생성한 루틴 저장
        routineRef.child(routine.id).child("id").setValue(routine.id)
        routineRef.child(routine.id).child("userId").setValue(routine.userId)
        routineRef.child(routine.id).child("name").setValue(routine.name)
        routineRef.child(routine.id).child("routineStartTime").setValue(routine.routineStartTime.toString())
        routineRef.child(routine.id).child("totalTime").setValue(routine.totalTime)
        routineRef.child(routine.id).child("description").setValue(routine.description.toString())
        routineRef.child(routine.id).child("lastModifiedTime").setValue(routine.lastModifiedTime.toString())

        var cardIds = arrayListOf<String>()

        //루틴에 카드가 추가됬을경우 totalTime을 계산해주는 로직
        if (!routine.cards.isEmpty()) {
            var time = 0
            for (card in routine.cards) {
                time += (card.preTimerSecs + card.activeTimerSecs + card.postTimerSecs) * card.sets
                cardIds.add(card.id)
                //카드는 따로 데이터베이스의 Card에저장
                cardRepo.createCard(card)
            }
            routine.totalTime = time
            routineRef.child(routine.id).child("totalTime").setValue(routine.totalTime)
            routineRef.child(routine.id).child("cardIds").setValue(cardIds)
        }
        return true
        TODO("Implement function to create a new document in Firebase Firestore for the given routine.")
    }

    // Fetches a specific routine using its ID from Firestore.
    suspend fun getRoutine(id: String): Routine {
        val routine = Routine("","", "", 0, 0,"", arrayListOf(),0)
        val snapshot = routineRef.child(id).get().await()

        routine.id = snapshot.child("id").value.toString()
        routine.userId = snapshot.child("userId").value.toString()
        routine.name = snapshot.child("name").value.toString()
        routine.totalTime = Integer.parseInt(snapshot.child("totalTime").value.toString())
        routine.description = snapshot.child("description").value.toString()
        routine.lastModifiedTime =
            Integer.parseInt(snapshot.child("lastModifiedTime").value.toString()).toLong()
        routine.numStar = Integer.parseInt(snapshot.child("numStar").value.toString())

        // Parsing Card objects
        var cardIds = snapshot.child("cardIds").value.toString()
        cardIds = cardIds.substring(1, cardIds.length - 1)
        val cardIdList = cardIds.split(",")

        // Fetching cards using structured concurrency
        coroutineScope {
            routine.cards.addAll(cardIdList.map { cardId ->
                async { cardRepo.getCard(cardId) }
            }.awaitAll())
        }

        return routine
    }


    // Deletes a specific routine using its ID from Firestore.
        fun deleteRoutine(id: String): Boolean {
            routineRef.child(id).removeValue()
            return true;
        }

        // Updates given fields of an existing routine in Firestore
        fun updateRoutine(id: String, newRoutine: Routine): Boolean{
            newRoutine.lastModifiedTime = System.currentTimeMillis() // Update the last modified time
            routineRef.child(id).setValue(newRoutine)

            //card만 업데이트
            var cardIds = arrayListOf<String>()
            //card업데이트
            for(card in newRoutine.cards){
                cardIds.add(card.id)
                cardRepo.createCard(card)
            }
            routineRef.child(id).child("cardIds").setValue(cardIds)
            return true
        }

    // Fetches all routines from Firestore.
    suspend fun getAllRoutines(): ArrayList<Routine> = suspendCoroutine { continuation ->
        val routineList = ArrayList<Routine>()
        routineRef.get().addOnSuccessListener { snapshot ->
            snapshot.children.forEach { routineSnapshot -> // 여기서 각 루틴을 순회해야 합니다.
                val routine = Routine(
                    id = routineSnapshot.child("id").value.toString(),
                    userId = routineSnapshot.child("userId").value.toString(),
                    routineStartTime = routineSnapshot.child("routineStartTime").value.toString().toIntOrNull() ?: 0,
                    name = routineSnapshot.child("name").value.toString(),
                    totalTime = routineSnapshot.child("totalTime").value.toString().toIntOrNull() ?: 0,
                    description = routineSnapshot.child("description").value.toString(),
                    cards = arrayListOf(),
                    lastModifiedTime = routineSnapshot.child("lastModifiedTime").value.toString().toLongOrNull() ?: 0,
                    numStar = 0
                )


                val cardsSnapshot = routineSnapshot.child("cardIds")
                cardsSnapshot.children.forEach { cardData ->
                    val card = Card(
                        id = cardData.child("id").value.toString(),
                        userId = cardData.child("userId").value.toString(),
                        name = cardData.child("name").value.toString(),
                        preTimerSecs = cardData.child("preTimerSecs").value.toString().toIntOrNull() ?: 0,
                        preTimerAutoStart = cardData.child("preTimerAutoStart").value.toString().toBoolean(),
                        activeTimerSecs = cardData.child("activeTimerSecs").value.toString().toIntOrNull() ?: 0,
                        activeTimerAutoStart = cardData.child("activeTimerAutoStart").value.toString().toBoolean(),
                        postTimerSecs = cardData.child("postTimerSecs").value.toString().toIntOrNull() ?: 0,
                        postTimerAutoStart = cardData.child("postTimerAutoStart").value.toString().toBoolean(),
                        sets = cardData.child("sets").value.toString().toIntOrNull() ?: 0,
                        memo = cardData.child("memo").value.toString() ?: ""
                    )
                    routine.cards.add(card) // 여기서 카드를 루틴에 추가합니다.
                }
                routineList.add(routine) // 여기서 루틴을 리스트에 추가합니다.
            }
            continuation.resume(routineList) // 데이터가 준비되면 중단함수를 재개합니다.
        }.addOnFailureListener { exception ->
            continuation.resumeWith(Result.failure(exception))
        }
    }

  

    // firebase uid로 routine 목록을 가져오는 함수
    suspend fun getRoutinesByUserId(userId: String): List<Routine> {
        val snapshot = routineRef.orderByChild("userId").equalTo(userId).get().await()

        return coroutineScope {
            snapshot.children.mapNotNull { routineSnapshot ->
                val cardIds = routineSnapshot.child("cardIds").children.map { it.value.toString() }
                val cards = cardIds.map { cardId ->
                    async { cardRepo.getCard(cardId) }
                }.awaitAll()

                Routine(
                    id = routineSnapshot.child("id").value.toString(),
                    userId = userId,
                    routineStartTime = routineSnapshot.child("routineStartTime").value.toString().toIntOrNull() ?: 0,
                    name = routineSnapshot.child("name").value.toString(),
                    totalTime = routineSnapshot.child("totalTime").value.toString().toIntOrNull() ?: 0,
                    description = routineSnapshot.child("description").value.toString(),
                    cards = ArrayList(cards),
                    lastModifiedTime = routineSnapshot.child("lastModifiedTime").value.toString().toLongOrNull() ?: 0,
                    numStar = 0
                )
            }
        }
    }



    // user-id와 그에 해당하는 history routine 목록을 가져오는 함수
    fun getHistoryRoutinesByUserId(userId : String): ArrayList<Routine> {
        TODO(" Implement function for fetching all history routines belonging to current logged-in user")
    }

    // user-id와 그에 해당하는 favorite routine 목록을 가져오는 함수
    fun getFavoriteRoutinesByUserId(userId : String): ArrayList<Routine> {
        TODO(" Implement function for fetching all favorite routines belonging to current logged-in user")
    }

    // TODO: numStar는 사용하지 않음
    suspend fun addStar(routineId:String){
        GlobalScope.launch {
            var routine = getRoutine(routineId)
            routineRef.child(routineId).child("numStar").setValue(routine.numStar + 1)
        }
    }


    suspend fun getUserStar(userId:String) = suspendCoroutine<Int> { continuation ->
        var num = 0
        routineRef.orderByChild("userId").equalTo(userId).get().addOnSuccessListener { snapshot ->
             snapshot.children.mapNotNull { routineSnapshot ->
                num+=Integer.parseInt(routineSnapshot.child("numStar").value.toString())
            }
            continuation.resume(num)
        }
    }

    suspend fun getUserStars(userId: String) = suspendCoroutine<Int> { continuation ->
        val starRef = database.getReference("Stars")
        starRef.child(userId).child("stars").get().addOnSuccessListener { snapshot ->
            val stars = snapshot.value.toString().toIntOrNull() ?: 0
            continuation.resume(stars)
        }.addOnFailureListener { exception ->
            continuation.resumeWith(Result.failure(exception))
        }
    }

    suspend fun incrementUserStars(userId: String) {
        val starRef = database.getReference("Stars")
        val currentStars = getUserStars(userId)
        starRef.child(userId).child("stars").setValue(currentStars + 1)
    }

    fun deleteAllRoutinesByUserId(userId: String) {
        routineRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (routineSnapshot in dataSnapshot.children) {
                    routineSnapshot.ref.removeValue()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors.
            }
        })

        val starRef = database.getReference("Stars")
        // Delete stars
        starRef.child(userId).removeValue()
    }
}