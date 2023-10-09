package com.example.mobile_programing.views.adapters

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_programing.R
import com.example.mobile_programing.databinding.ActivityRoutineDetailBinding
import com.example.mobile_programing.models.Card
import com.example.mobile_programing.views.RoutineDetailActivity

class RoutineDetailCardAdapter constructor(private val binding: ActivityRoutineDetailBinding, private val activity: Activity) : RecyclerView.Adapter<RoutineDetailCardAdapter.CustomViewHolder>(){
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
                val selectedRoutine: Card = cardList[absoluteAdapterPosition]
//
//                activity.startActivity(Intent(activity, RoutineDetailActivity::class.java).apply {
//                    putExtra("selected_routine", selectedRoutine)
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