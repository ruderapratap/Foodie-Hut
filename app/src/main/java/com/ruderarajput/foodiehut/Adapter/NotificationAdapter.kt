package com.ruderarajput.foodiehut.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ruderarajput.foodiehut.databinding.NotificationItemBinding

class NotificationAdapter(private var notification:ArrayList<String>,private var notificationImage:ArrayList<Int>):RecyclerView.Adapter<NotificationAdapter.NotifiactionViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationAdapter.NotifiactionViewHolder {
       val binding=NotificationItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NotifiactionViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: NotificationAdapter.NotifiactionViewHolder,
        position: Int
    ) {
       holder.bind(position)
    }

    override fun getItemCount(): Int=notification.size

    inner class NotifiactionViewHolder(private val binding:NotificationItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            binding.apply {
                notifiactionTxt.text=notification[position]
                notifiactionImg.setImageResource(notificationImage[position])
            }
        }

    }
}