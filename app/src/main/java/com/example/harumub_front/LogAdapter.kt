package com.example.harumub_front

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LogAdapter : RecyclerView.Adapter<LogAdapter.ViewHolder>(){
    var items = ArrayList<LogData>()

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        fun setItem(item : LogData){ // 데이터 설정
            itemView.findViewById<TextView>(R.id.log_time).text = item.logData_time
            itemView.findViewById<TextView>(R.id.log_emotion).text = item.logData_emotion
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LogAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_log, parent, false) // // RecyclerView에 들어갈 아이템의 레이아웃 설정

        return ViewHolder(itemView)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: LogAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.setItem(item)
    }
}