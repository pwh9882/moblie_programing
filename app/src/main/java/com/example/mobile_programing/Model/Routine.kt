package com.example.mobile_programing.Model


//val database = Firebase.database("https://mobile-programing-9ec38-default-rtdb.asia-southeast1.firebasedatabase.app")
val routineRef = database.getReference("Routine")
data class Routine (
    var id: String, // Unique ID for the routine // ID의 생성 방법: 6자리, auto increment
    val name: String, // Name of the routine e.g., "Leg Day"
    var totalTime: Int, // total sum of time of card's timer(default 0 )
    val description: String?, // Optional description for the routine
    val cards: ArrayList<Card>  // List of all 'cards' that make up this routine
){
    //초기화
    init {
        id = routineRef.push().key!!
        if(cards.isEmpty()) totalTime = 0
    }

    //Firebase에 데이터 저장 함수,카드추가시 다시 이 함수를 호출하여 저장해주어야 Firebase데이터베이스에도 저장됩니다
    fun saveData(){
        routineRef.child(id).setValue(this)
    }

    //카드리스트에 카드추가 기능 & 동시에 totalTime도 갱신(아래 calcToatalTime함수사용) & firebase에 갱신된 데이터 저장
    fun plusCard(card: Card){
        cards.add(card)
        calcTotalTime(cards)
        saveData()
    }


    //totalTime 계산하기
    fun calcTotalTime(cards: ArrayList<Card>){
        var time = 0
        for(card in cards){
            time += (card.preTimerSecs + card.activeTimerSecs + card.postTimerSecs) * card.sets
        }
        this.totalTime = time
    }
}