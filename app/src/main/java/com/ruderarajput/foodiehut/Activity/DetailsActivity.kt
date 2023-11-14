package com.ruderarajput.foodiehut.Activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.ruderarajput.foodiehut.Model.CartItems
import com.ruderarajput.foodiehut.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityDetailsBinding.inflate(layoutInflater)
    }
    private var foodName: String? = null
    private var foodImage: String? = null
    private var foodDescription: String? = null
    private var foodIngredient: String? = null
    private var foodPrice: String? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        foodName = intent.getStringExtra("MenuItemName")
        foodDescription = intent.getStringExtra("MenuItemDescription")
        foodPrice = intent.getStringExtra("MenuItemPrice")
        foodIngredient = intent.getStringExtra("MenuItemIngredient")
        foodImage = intent.getStringExtra("MenuItemImage")

        binding.foodName.text = foodName
        binding.ingredientsTxt.text = foodIngredient
        binding.descriptionTxt.text = foodDescription
        binding.foodPrice.text = foodPrice
        Glide.with(this@DetailsActivity).load(Uri.parse(foodImage)).into(binding.foodImage)

        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.addToCartBtn.setOnClickListener {
            addItemToCart()
        }
    }

    private fun addItemToCart() {
        val database = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid ?: ""

        val cartItem = CartItems(
            foodName.toString(),
            foodPrice.toString(),
            foodDescription.toString(),
            foodIngredient.toString(),
            1,
            foodImage.toString()
        )

        database.child("user").child(userId).child("CartItems").push().setValue(cartItem)
            .addOnSuccessListener {
                Toast.makeText(this, "Added To Cart Your Favourite Food", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    "Your Favourite Food Not Added To Cart. Please Try Again",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}