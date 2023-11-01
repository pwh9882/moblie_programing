package com.example.mobile_programing.views.adapters

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_programing.R
import com.example.mobile_programing.databinding.ActivityRoutineDetailBinding
import com.example.mobile_programing.models.Card
import com.example.mobile_programing.views.CardDetailActivity
import com.example.mobile_programing.views.RoutineDetailActivity

class RoutineDetailCardAdapter constructor(private val binding: ActivityRoutineDetailBinding, private val activity: Activity, private val cardUpdateResultLauncher: ActivityResultLauncher<Intent>) : RecyclerView.Adapter<RoutineDetailCardAdapter.CustomViewHolder>(){
    var cardList: MutableList<Card> = mutableListOf()
    class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById<TextView>(R.id.tv_card_name)
        val description: TextView = itemView.findViewById<TextView>(R.id.tv_card_desc)
        val totalTime: TextView = itemView.findViewById<TextView>(R.id.tv_card_total_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_card, parent, false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {
                val selectedCard: Card = cardList[absoluteAdapterPosition]
                // cardDetail activit로 이동해서 수정사항있으면 업데이트
                cardUpdateResultLauncher.launch(Intent(activity, CardDetailActivity::class.java).apply {
                    putExtra("selected_card", selectedCard)
                })
//                activity.startActivity(Intent(activity, CardDetailActivity::class.java).apply {
//                    putExtra("selected_card", selectedCard)
//                })



            }

        }
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val card = cardList[position]
        holder.name.text = card.name
        holder.description.text = "아직..."
        holder.totalTime.text =  (card.preTimerSecs + card.activeTimerSecs + card.postTimerSecs).toString() + " 초"

    }

}