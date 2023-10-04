package com.example.mobile_programing.views.adapters

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_programing.R
import com.example.mobile_programing.databinding.ActivityMainBinding
import com.example.mobile_programing.models.Routine
import com.example.mobile_programing.viewModel.MainViewModel


class RoutineAdapter constructor(private val binding: ActivityMainBinding, private val viewModel: MainViewModel, private val activity: Activity) : RecyclerView.Adapter<RoutineAdapter.CustomViewHolder>(){
    var routineLint: MutableList<Routine> = mutableListOf()

    class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById<TextView>(R.id.tv_routine_name)
        val description: TextView = itemView.findViewById(R.id.tv_rountine_desc)
        val totalTime: TextView = itemView.findViewById(R.id.tv_rountine_total_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_routine, parent, false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {

            }

        }
    }

    override fun getItemCount(): Int {
        return routineLint.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val routine = routineLint[position]
        Log.e("Routine: ", routine.toString())
        holder.name.text = routine.name
        holder.description.text = routine.description
        holder.totalTime.text =  routine.totalTime.toString()
    }

}