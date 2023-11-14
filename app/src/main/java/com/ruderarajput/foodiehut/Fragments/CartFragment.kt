package com.ruderarajput.foodiehut.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ruderarajput.foodiehut.Activity.PayOutActivity
import com.ruderarajput.foodiehut.Adapter.CartAdapter
import com.ruderarajput.foodiehut.Model.CartItems
import com.ruderarajput.foodiehut.databinding.FragmentCartBinding


class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var foodNames: MutableList<String>
    private lateinit var foodPrices: MutableList<String>
    private lateinit var foodDescriptions: MutableList<String>
    private lateinit var foodIngredients: MutableList<String>
    private lateinit var quantity: MutableList<Int>
    private lateinit var foodImagesUri: MutableList<String>
    private lateinit var adapter: CartAdapter
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = FirebaseAuth.getInstance()
        reteriveCartItems()

        binding = FragmentCartBinding.inflate(inflater, container, false)

        binding.procesedBtn.setOnClickListener {
            getOrderItemsDetails()
        }

        return binding.root
    }

    private fun getOrderItemsDetails() {
        val orderIdReferences: DatabaseReference =
            database.reference.child("user").child(userId).child("CartItems")

        val foodNames = mutableListOf<String>()
        val foodPrices = mutableListOf<String>()
        val foodDescriptions = mutableListOf<String>()
        val foodIngredients = mutableListOf<String>()
        val foodImagesUri = mutableListOf<String>()

        val quantity = adapter.getUpdateItemsQuantity()
        orderIdReferences.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val orderItems = foodSnapshot.getValue(CartItems::class.java)

                    orderItems?.foodName?.let { foodNames.add(it) }
                    orderItems?.foodPrice?.let { foodPrices.add(it) }
                    orderItems?.foodDescription?.let { foodDescriptions.add(it) }
                    orderItems?.foodIngredients?.let { foodIngredients.add(it) }
                    orderItems?.foodImage?.let { foodImagesUri.add(it) }
                }
                orderNow(
                    foodNames,
                    foodPrices,
                    foodDescriptions,
                    foodIngredients,
                    quantity,
                    foodImagesUri
                )
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    requireContext(),
                    "Order making Failed. Please Try Again.",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun orderNow(
        foodNames: MutableList<String>,
        foodPrices: MutableList<String>,
        foodDescriptions: MutableList<String>,
        foodIngredients: MutableList<String>,
        quantity: MutableList<Int>,
        foodImagesUri: MutableList<String>
    ) {
        if (isAdded && context!=null){
            val intent=Intent(requireContext(),PayOutActivity::class.java)
            intent.putExtra("FoodItemName",foodNames as ArrayList<String>)
            intent.putExtra("FoodItemPrice",foodPrices as ArrayList<String>)
            intent.putExtra("FoodItemDescription",foodDescriptions as ArrayList<String>)
            intent.putExtra("FoodItemIngredients",foodIngredients as ArrayList<String>)
            intent.putExtra("FoodItemQuantity",quantity as ArrayList<Int>)
            intent.putExtra("FoodItemImage",foodImagesUri as ArrayList<String>)
            startActivity(intent)
        }

    }

    private fun reteriveCartItems() {


        database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid ?: ""

        val foodReference: DatabaseReference =
            database.reference.child("user").child(userId).child("CartItems")

        foodNames = mutableListOf()
        foodPrices = mutableListOf()
        foodDescriptions = mutableListOf()
        foodIngredients = mutableListOf()
        quantity = mutableListOf()
        foodImagesUri = mutableListOf()

        foodReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val cartItems = foodSnapshot.getValue(CartItems::class.java)

                    cartItems?.foodName?.let { foodNames.add(it) }
                    cartItems?.foodPrice?.let { foodPrices.add(it) }
                    cartItems?.foodDescription?.let { foodDescriptions.add(it) }
                    cartItems?.foodIngredients?.let { foodIngredients.add(it) }
                    cartItems?.foodQuantity?.let { quantity.add(it) }
                    cartItems?.foodImage?.let { foodImagesUri.add(it) }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "data not fetch", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setAdapter() {
         adapter = CartAdapter(
            requireContext(),
            foodNames,
            foodPrices,
            foodDescriptions,
            foodIngredients,
            quantity,
            foodImagesUri
        )
        val layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        binding.recViewCart.layoutManager =layoutManager
        binding.recViewCart.adapter = adapter
    }
}