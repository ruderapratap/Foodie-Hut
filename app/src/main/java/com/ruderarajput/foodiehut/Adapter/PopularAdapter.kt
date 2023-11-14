package com.ruderarajput.foodiehut.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ruderarajput.foodiehut.Activity.DetailsActivity
import com.ruderarajput.foodiehut.databinding.PopularItemBinding

class PopularAdapter(private val items:List<String>,private val price:List<String>,private  val image:List<Int>,private val requireContext:Context):RecyclerView.Adapter<PopularAdapter.PopularViewHolder>()

{


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PopularAdapter.PopularViewHolder {
       return PopularViewHolder(PopularItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: PopularAdapter.PopularViewHolder, position: Int) {
        val item=items[position]
        val images=image[position]
        val prices=price[position]
        holder.bind(item,images,prices)

        holder.itemView.setOnClickListener {
            //setOnClick Listner to open details
            val intent=Intent(requireContext,DetailsActivity::class.java)
            intent.putExtra("MenuItemName",item)
            intent.putExtra("MenuItemImage",images)
            requireContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
       return items.size
    }
    class PopularViewHolder(private  val binding:PopularItemBinding):RecyclerView.ViewHolder(binding.root){

        private val imageView=binding.menuImage
        fun bind(item: String, images: Int, prices: String) {
            binding.menuFoodName.text=item
            binding.menuRupees.text=prices
            imageView.setImageResource(images)
        }
    }
}