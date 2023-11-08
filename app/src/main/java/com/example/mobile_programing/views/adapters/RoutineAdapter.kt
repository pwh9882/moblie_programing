package com.example.mobile_programing.views.adapters

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_programing.R
import com.example.mobile_programing.databinding.ActivityMainBinding
import com.example.mobile_programing.models.Routine
import com.example.mobile_programing.viewModel.MainViewModel
import com.example.mobile_programing.views.RoutineDetailActivity
import java.util.Collections


class RoutineAdapter constructor(private val binding: ActivityMainBinding, val viewModel: MainViewModel, val activity: Activity, private val routineUpdateResultLauncher: ActivityResultLauncher<Intent>) : RecyclerView.Adapter<RoutineAdapter.CustomViewHolder>(){
    var routineList: MutableList<Routine> = mutableListOf()

    class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById<TextView>(R.id.tv_routine_name)
        val description: TextView = itemView.findViewById(R.id.tv_rountine_desc)
        val totalTime: TextView = itemView.findViewById(R.id.tv_rountine_total_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_routine, parent, false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {
                val selectedRoutine: Routine = routineList[absoluteAdapterPosition]

                routineUpdateResultLauncher.launch(Intent(activity, RoutineDetailActivity::class.java).apply {
                    putExtra("selected_routine", selectedRoutine)
                })

//                activity.startActivity(Intent(activity, RoutineDetailActivity::class.java).apply {
//                    putExtra("selected_routine", selectedRoutine)
//                })
            }

        }
    }

    override fun getItemCount(): Int {
        return routineList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val routine = routineList[position]
        Log.e("Routine: ", routine.toString())
        holder.name.text = routine.name
        holder.description.text = routine.description
        holder.totalTime.text =  routine.totalTime.toString()
    }

    fun removeAt(position: Int) {
        // 아이템 삭제 로직
        routineList.removeAt(position)
        notifyItemRemoved(position)
        //TODO: DB에서도 삭제
    }


    fun onItemMove(fromPosition: Int, toPosition: Int) {
        // 이동할 객체를 저장합니다.
        val routine = routineList[fromPosition]
        // 이동할 객체를 삭제합니다.
        routineList.removeAt(fromPosition)
        // 이동하고 싶은 position에 객체를 추가합니다.
        routineList.add(toPosition, routine)

        // Adapter에 데이터 이동을 알립니다.
        notifyItemMoved(fromPosition, toPosition)
    }


}

