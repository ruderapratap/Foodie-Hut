package com.ruderarajput.foodiehut.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ruderarajput.foodiehut.databinding.RecentbuyItemBinding

class RecentBuyAdapter(
    private var context: Context,
    private var foodNameList: ArrayList<String>,
    private var foodImageList: ArrayList<String>,
    private var foodPriceList: ArrayList<String>,
    private var foodQuantityList: ArrayList<Int>
) : RecyclerView.Adapter<RecentBuyAdapter.RecentViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecentBuyAdapter.RecentViewHolder {
        val binding = RecentbuyItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return RecentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecentBuyAdapter.RecentViewHolder, position: Int) {
      holder.bind(position)
    }

    override fun getItemCount(): Int =foodNameList.size

    inner class RecentViewHolder(private val binding: RecentbuyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.recentItemFoodName.text=foodNameList[position]
            binding.recentItemFoodPrice.text=foodPriceList[position]
            binding.recentItemQuantity.text= foodQuantityList[position].toString()
            val uriString=foodImageList[position]
            val uri=Uri.parse(uriString)
            Glide.with(context).load(uri).into(binding.recentItemImage)
        }

    }
}