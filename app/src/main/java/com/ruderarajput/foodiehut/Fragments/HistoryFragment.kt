package com.ruderarajput.foodiehut.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ruderarajput.foodiehut.Activity.RecentOrderItemsActivity
import com.ruderarajput.foodiehut.Adapter.BuyAgainAdapter
import com.ruderarajput.foodiehut.Model.OrderDetails
import com.ruderarajput.foodiehut.R
import com.ruderarajput.foodiehut.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var buyAgainAdapter: BuyAgainAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userId: String
    private var listOfOrderItem: ArrayList<OrderDetails> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()


        retrieveBuyHistory()

        binding.downImage.setOnClickListener {
            seeItemsRecentBuy()
        }
        binding.receivedBtn.setOnClickListener {
            upDateOrderStatus()
        }

        return binding.root
    }

    private fun upDateOrderStatus() {
        Toast.makeText(requireContext(), "Successfully Recevied Your Item", Toast.LENGTH_SHORT)
            .show()
        val itemPushKey = listOfOrderItem[0].itemPushKey
        val completedOrderReference =
            database.reference.child("CompletedOrder").child(itemPushKey!!)
        completedOrderReference.child("paymentReceived").setValue(true)
    }

    private fun seeItemsRecentBuy() {
        listOfOrderItem.firstOrNull()?.let { recentBuy ->
            val intent = Intent(requireContext(), RecentOrderItemsActivity::class.java)
            intent.putExtra("RecentBuyOrderItem", listOfOrderItem)
            startActivity(intent)
        }
    }

    private fun retrieveBuyHistory() {
        userId = auth.currentUser?.uid ?: ""
        val buyItemReference: DatabaseReference =
            database.reference.child("user").child(userId).child("BuyHistory")
        val shortingQuery = buyItemReference.orderByChild("currentTime")

        shortingQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (buySnapshot in snapshot.children) {
                    val buyHistoryItem = buySnapshot.getValue(OrderDetails::class.java)
                    buyHistoryItem?.let {
                        listOfOrderItem.add(it)
                    }
                }
                listOfOrderItem.reverse()
                if (listOfOrderItem.isNotEmpty()) {
                    setDataInRecentBuyItem()
                    setPriviousBuyItemsRecyclerView()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun setDataInRecentBuyItem() {
        binding.recenteItemVisivlty.visibility = View.VISIBLE
        binding.downImage.visibility = View.VISIBLE
        val recentOrderItem = listOfOrderItem.firstOrNull()
        recentOrderItem?.let {
            binding.recentItemFoodName.text = it.foodNames?.firstOrNull() ?: ""
            binding.recentItemFoodPrice.text = it.foodPrices?.firstOrNull() ?: ""
            val image = it.foodImages?.firstOrNull() ?: ""
            val uri = Uri.parse(image)
            Glide.with(requireContext()).load(uri).into(binding.recentItemImage)

            val isOrderIsAccepted = listOfOrderItem[0].orderAccepted

            if (isOrderIsAccepted) {
                binding.orderStatus.background.setTint(Color.GREEN)
                binding.receivedBtn.visibility = View.VISIBLE
            }
        }
    }

    private fun setPriviousBuyItemsRecyclerView() {
        val buyAgainFoodName = mutableListOf<String>()
        val buyAgainFoodPrice = mutableListOf<String>()
        val buyAgainFoodImage = mutableListOf<String>()
        for (i in 1 until listOfOrderItem.size) {
            listOfOrderItem[i].foodNames?.firstOrNull()?.let {
                buyAgainFoodName.add(it)
                listOfOrderItem[i].foodPrices?.firstOrNull()?.let {
                    buyAgainFoodPrice.add(it)
                    listOfOrderItem[i].foodImages?.firstOrNull()?.let {
                        buyAgainFoodImage.add(it)
                    }
                }
            }
        }
        buyAgainAdapter =
            BuyAgainAdapter(
                buyAgainFoodName,
                buyAgainFoodPrice,
                buyAgainFoodImage,
                requireContext()
            )
        val layoutManager = LinearLayoutManager(context)
        binding.recViewHistory.layoutManager = layoutManager
        binding.recViewHistory.adapter = buyAgainAdapter
    }
}
