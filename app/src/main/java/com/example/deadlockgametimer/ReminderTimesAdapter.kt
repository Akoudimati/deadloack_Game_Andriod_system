package com.example.deadlockgametimer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReminderTimesAdapter(private val reminderTimes: List<Double>) :
    RecyclerView.Adapter<ReminderTimesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeText: TextView = view.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val time = reminderTimes[position]
        val minutes = time.toInt()
        val seconds = ((time % 1) * 60).toInt()
        holder.timeText.text = String.format("%d:%02d", minutes, seconds)
        holder.timeText.setTextColor(0xFFFF8C00.toInt())
    }

    override fun getItemCount() = reminderTimes.size
} 