package com.ruderarajput.foodiehut.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.ruderarajput.foodiehut.Adapter.RecentBuyAdapter
import com.ruderarajput.foodiehut.Model.OrderDetails
import com.ruderarajput.foodiehut.R
import com.ruderarajput.foodiehut.databinding.ActivityRecentOrderItemsBinding

class RecentOrderItemsActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityRecentOrderItemsBinding.inflate(layoutInflater)
    }
    private lateinit var allFodeNames: ArrayList<String>
    private lateinit var allFodeImage: ArrayList<String>
    private lateinit var allFodePrice: ArrayList<String>
    private lateinit var allFodeQuantity: ArrayList<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

        val recentOrderItems =
            intent.getSerializableExtra("RecentBuyOrderItem") as ArrayList<OrderDetails>
        recentOrderItems?.let { orderDetails ->
            if (orderDetails.isNotEmpty()) {
                val recentOrderItems = orderDetails[0]

                allFodeNames = recentOrderItems.foodNames as ArrayList<String>
                allFodeImage = recentOrderItems.foodImages as ArrayList<String>
                allFodePrice = recentOrderItems.foodPrices as ArrayList<String>
                allFodeQuantity = recentOrderItems.foodQuantities as ArrayList<Int>
            }
        }
        setAdapter()
    }

    private fun setAdapter() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        binding.recViewRecentOrder.layoutManager = layoutManager
        val adapter =
            RecentBuyAdapter(this, allFodeNames, allFodeImage, allFodePrice, allFodeQuantity)
        binding.recViewRecentOrder.adapter = adapter
    }
}