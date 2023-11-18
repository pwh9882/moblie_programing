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

class RoutineDetailCardAdapter constructor(private val binding: ActivityRoutineDetailBinding,  val activity: RoutineDetailActivity, private val cardUpdateResultLauncher: ActivityResultLauncher<Intent>) : RecyclerView.Adapter<RoutineDetailCardAdapter.CustomViewHolder>(){
    var cardList: MutableList<Card> = mutableListOf()
    class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById<TextView>(R.id.tv_card_name)
        val memo: TextView = itemView.findViewById<TextView>(R.id.tv_card_memo)
        val totalTime: TextView = itemView.findViewById<TextView>(R.id.tv_card_total_time)
        val sets: TextView = itemView.findViewById<TextView>(R.id.tv_card_sets)
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
        holder.memo.text = card.memo
        holder.totalTime.text =  "총 " + (card.preTimerSecs + card.activeTimerSecs + card.postTimerSecs).toString() + " 초"
        holder.sets.text = card.sets.toString() + " 세트"

    }

    fun removeAt(position: Int) {
        // 아이템 삭제 로직
        cardList.removeAt(position)
        notifyItemRemoved(position)
        //TODO: DB에서도 삭제
        //TODO: routine도 업데이트
    }
    fun onItemMove(fromPosition: Int, toPosition: Int) {
        // 이동할 객체를 저장합니다.
        val routine = cardList[fromPosition]
        // 이동할 객체를 삭제합니다.
        cardList.removeAt(fromPosition)
        // 이동하고 싶은 position에 객체를 추가합니다.
        cardList.add(toPosition, routine)

        // Adapter에 데이터 이동을 알립니다.
        notifyItemMoved(fromPosition, toPosition)
    }

}