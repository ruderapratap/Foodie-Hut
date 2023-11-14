package com.ruderarajput.foodiehut.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.init
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.ruderarajput.foodiehut.Activity.DetailsActivity
import com.ruderarajput.foodiehut.databinding.CartItemBinding

class CartAdapter(
    private val context: Context,
    private val foodName: MutableList<String>,
    private val foodPrice: MutableList<String>,
    private var foodDescription: MutableList<String>,
    private var foodIngredients: MutableList<String>,
    private val foodQuantity: MutableList<Int>,
    private var foodImage: MutableList<String>
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val auth = FirebaseAuth.getInstance()


    init {
        val database = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid ?: ""
        val cartItemNumber = foodName.size

        itemQuantities = IntArray(cartItemNumber) { 1 }
        cartItemReference = database.reference.child("user").child(userId).child("CartItems")
    }

    companion object {
        private var itemQuantities: IntArray = intArrayOf()
        private lateinit var cartItemReference: DatabaseReference
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartAdapter.CartViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = foodName.size

    inner class CartViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {

            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    openDetailActivity(position)
                }
            }

            binding.apply {
                itemDisName.text = foodName[position]
                itemCartRupees.text = foodPrice[position]
                val quantity = itemQuantities[position]
                cartItemQuantity.text = quantity.toString()
                val uriString = foodImage[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(itemImage)


                plusCart.setOnClickListener {
                    incraseQuantitty(position)
                }
                minusCart.setOnClickListener {
                    deceaseQuantitty(position)
                }
                deleteCart.setOnClickListener {
                    val itemPosition = adapterPosition
                    if (itemPosition != RecyclerView.NO_POSITION) {
                        deleteQuantitty(itemPosition)
                    }
                }
            }
        }

        private fun deceaseQuantitty(position: Int) {
            if (itemQuantities[position] > 1) {
                itemQuantities[position]--
                foodQuantity[position]= itemQuantities[position]
                binding.cartItemQuantity.text = itemQuantities[position].toString()
            }
        }

        private fun incraseQuantitty(position: Int) {
            if (itemQuantities[position] < 10) {
                itemQuantities[position]++
                foodQuantity[position]= itemQuantities[position]
                binding.cartItemQuantity.text = itemQuantities[position].toString()
            }
        }

        private fun deleteQuantitty(position: Int) {
            val positionRetrive = position
            getUniqueKeyAtPosition(positionRetrive) { uniqueKey ->
                if (uniqueKey != null) {
                    removeItem(position, uniqueKey)
                }
            }
        }

    }

    private fun openDetailActivity(position: Int) {
        val foodName = foodName[position]
        val foodPrice = foodPrice[position]
        val foodImage = foodImage[position]
        val foodIngredients = foodIngredients[position]
        val foodDescription = foodDescription[position]
        val intent = Intent(context, DetailsActivity::class.java)
        intent.putExtra("MenuItemName", foodName)
        intent.putExtra("MenuItemImage", foodImage)
        intent.putExtra("MenuItemDescription", foodDescription)
        intent.putExtra("MenuItemIngredient", foodIngredients)
        intent.putExtra("MenuItemPrice", foodPrice)
        context.startActivity(intent)
    }


    private fun removeItem(position: Int, uniqueKey: String) {
        if (uniqueKey != null) {
            cartItemReference.child(uniqueKey).removeValue().addOnSuccessListener {
                foodName.removeAt(position)
                foodPrice.removeAt(position)
                foodDescription.removeAt(position)
                foodIngredients.removeAt(position)
                foodQuantity.removeAt(position)
                foodImage.removeAt(position)
                Toast.makeText(context, "Delete Your Order", Toast.LENGTH_SHORT).show()

                itemQuantities =
                    itemQuantities.filterIndexed { index, i -> index != position }.toIntArray()
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, foodName.size)
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to Delete", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getUniqueKeyAtPosition(positionRetrive: Int, onComplete: (String?) -> Unit) {
        cartItemReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var uniqueKey: String? = null
                snapshot.children.forEachIndexed { index, dataSnapshot ->
                    if (index == positionRetrive) {
                        uniqueKey = dataSnapshot.key
                        return@forEachIndexed
                    }
                }
                onComplete(uniqueKey)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun getUpdateItemsQuantity(): MutableList<Int> {
        val itemQuantity = mutableListOf<Int>()
        itemQuantity.addAll(foodQuantity)
        return itemQuantity
    }

}