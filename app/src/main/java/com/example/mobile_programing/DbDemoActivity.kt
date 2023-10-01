package com.example.mobile_programing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.mobile_programing.Model.Card
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class DbDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_db_demo)

        val database = Firebase.database("https://mobile-programing-9ec38-default-rtdb.asia-southeast1.firebasedatabase.app")
        val cardRef = database.getReference("Card")
        val bttn:Button = findViewById(R.id.bttn_demo)
        var id:String
        var additionalInfo= arrayListOf<String>("hello")
        bttn.setOnClickListener {
            id = cardRef.push().key!!
            additionalInfo.add("hello")
            val card:Card = Card(id.toInt(),"김도경",3,true,3,false,4,false,5,additionalInfo)

            cardRef.child(id).setValue(card)
        }
    }







}