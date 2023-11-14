package com.ruderarajput.foodiehut.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.ruderarajput.foodiehut.Activity.DetailsActivity
import com.ruderarajput.foodiehut.Model.CartItems
import com.ruderarajput.foodiehut.Model.MenuModel
import com.ruderarajput.foodiehut.databinding.MenuItemBinding

class MenuAdapter(private val menuItems: List<MenuModel>, private val requireContext: Context) :
    RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {


    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuAdapter.MenuViewHolder {
        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuAdapter.MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuItems.size

    inner class MenuViewHolder(private val binding: MenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    openDetailActivity(position)
                }
            }

        }


        private fun openDetailActivity(position: Int) {
            val menuItem = menuItems[position]
            val intent = Intent(requireContext, DetailsActivity::class.java)
            intent.putExtra("MenuItemName", menuItem.foodName)
            intent.putExtra("MenuItemImage", menuItem.foodImage)
            intent.putExtra("MenuItemDescription", menuItem.foodDescription)
            intent.putExtra("MenuItemIngredient", menuItem.foodIngredient)
            intent.putExtra("MenuItemPrice", menuItem.foodPrice)
            requireContext.startActivity(intent)
        }

        fun bind(position: Int) {
            binding.apply {
                val menuItem = menuItems[position]
                menuFoodName.text = menuItem.foodName
                menuRupees.text = menuItem.foodPrice
                val uri = Uri.parse(menuItem.foodImage)
                Glide.with(requireContext).load(uri).into(menuImage)

                binding.menuAddToCart.setOnClickListener {
                    addItemToCart(position)
                }

            }
        }

    }

    private fun addItemToCart(position: Int) {
        val auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid ?: ""

        val menuItem = menuItems[position]
        val cartItem = CartItems(
            menuItem.foodName.toString(),
            menuItem.foodPrice.toString(),
            menuItem.foodDescription.toString(),
            menuItem.foodIngredient.toString(),
            1,
            menuItem.foodImage.toString()
        )

        database.child("user").child(userId).child("CartItems").push().setValue(cartItem)
            .addOnSuccessListener {
                Toast.makeText(
                    requireContext,
                    "Added To Cart Your Favourite Food",
                    Toast.LENGTH_SHORT
                ).show()
            }.addOnFailureListener {
                Toast.makeText(
                    requireContext,
                    "Your Favourite Food Not Added To Cart. Please Try Again",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    interface OnClickListener {
        fun onItemClick(position: Int)
    }
}


