package com.ruderarajput.foodiehut.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ruderarajput.foodiehut.databinding.BuyItemagainBinding

class BuyAgainAdapter(
    private val buyAgainFoodName: MutableList<String>,
    private val buyAgainFoodPrice: MutableList<String>,
    private val buyAgainFoodImage: MutableList<String>,
    private var requireContext: Context
) : RecyclerView.Adapter<BuyAgainAdapter.BuyAgainViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BuyAgainAdapter.BuyAgainViewHolder {
        val binding =
            BuyItemagainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BuyAgainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BuyAgainAdapter.BuyAgainViewHolder, position: Int) {
        holder.bind(
            buyAgainFoodName[position],
            buyAgainFoodPrice[position],
            buyAgainFoodImage[position]
        )
    }

    override fun getItemCount(): Int = buyAgainFoodName.size

    inner class BuyAgainViewHolder(private val binding: BuyItemagainBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(foodName: String, foodPrice: String, foodImage: String) {
            binding.itemDisName.text = foodName
            binding.itemCartRupees.text = foodPrice
           val uriString=foodImage
            val uri= Uri.parse(uriString)
            Glide.with(requireContext).load(uri).into(binding.itemImage)

        }
    }

}