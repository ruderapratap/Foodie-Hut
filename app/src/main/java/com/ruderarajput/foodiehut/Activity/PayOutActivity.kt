package com.ruderarajput.foodiehut.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ruderarajput.foodiehut.Fragments.CongratsBottomSheet
import com.ruderarajput.foodiehut.Model.OrderDetails
import com.ruderarajput.foodiehut.databinding.ActivityPayOutBinding

class PayOutActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityPayOutBinding.inflate(layoutInflater)
    }
    private lateinit var name: String
    private lateinit var address: String
    private lateinit var phone: String
    private lateinit var totalAmount: String
    private lateinit var foodItemName: ArrayList<String>
    private lateinit var foodItemPrice: ArrayList<String>
    private lateinit var foodItemDescription: ArrayList<String>
    private lateinit var foodItemIngredients: ArrayList<String>
    private lateinit var foodItemQuantity: ArrayList<Int>
    private lateinit var foodItemImage: ArrayList<String>
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference()

        setUserData()

        val intent = intent
        foodItemName = intent.getStringArrayListExtra("FoodItemName") as ArrayList<String>
        foodItemPrice = intent.getStringArrayListExtra("FoodItemPrice") as ArrayList<String>
        foodItemDescription =
            intent.getStringArrayListExtra("FoodItemDescription") as ArrayList<String>
        foodItemIngredients =
            intent.getStringArrayListExtra("FoodItemIngredients") as ArrayList<String>
        foodItemQuantity = intent.getIntegerArrayListExtra("FoodItemQuantity") as ArrayList<Int>
        foodItemImage = intent.getStringArrayListExtra("FoodItemImage") as ArrayList<String>

        totalAmount = calculateTotalAmount().toString()
        binding.payOutTotalPrice.setText(totalAmount)


        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.placeMyOrderBtn.setOnClickListener {

            name = binding.etName.text.toString().trim()
            address = binding.etAddress.text.toString().trim()
            phone = binding.etPhoneNumber.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(this, "Please Enter Your Name", Toast.LENGTH_SHORT).show()
                binding.etName.requestFocus()
            } else if (address.isEmpty()) {
                Toast.makeText(this, "Please Enter Your Address", Toast.LENGTH_SHORT).show()
                binding.etAddress.requestFocus()
            } else if (phone.isEmpty()) {
                Toast.makeText(this, "Please Enter Your Phone Number", Toast.LENGTH_SHORT).show()
                binding.etPhoneNumber.requestFocus()
            } else {
                placeOrder()
            }
        }
    }

    private fun placeOrder() {
        userId=auth.currentUser?.uid?:""
        val time=System.currentTimeMillis()
        val itemPushKey=databaseReference.child("OrderDetails").push().key
        val orderDetails=OrderDetails(userId,name,foodItemName,foodItemPrice,foodItemImage,foodItemQuantity,address,totalAmount,phone,time,itemPushKey,false,false)
        val orderReference=databaseReference.child("OrderDetails").child(itemPushKey!!)
        orderReference.setValue(orderDetails).addOnSuccessListener {
            val bottomSheetDialog = CongratsBottomSheet()
            bottomSheetDialog.show(supportFragmentManager, "Test")
            removeItemFromCart()
            addOrderToHistory(orderDetails)
        }.addOnFailureListener {
            Toast.makeText(this, "Failed Your Favourite Food. Please Try Again.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addOrderToHistory(orderDetails: OrderDetails) {
        databaseReference.child("user").child(userId).child("BuyHistory")
            .child(orderDetails.itemPushKey!!)
            .setValue(orderDetails).addOnSuccessListener {
                
            }
    }

    private fun removeItemFromCart() {
        val cartItemsReference=databaseReference.child("user").child(userId).child("CartItems")
        cartItemsReference.removeValue()
    }

    private fun calculateTotalAmount(): Int {
        var totalAmount = 0
        for (i in 0 until foodItemPrice.size) {
            var price = foodItemPrice[i]
            val priceIntValue = price.toInt()
            var quantity = foodItemQuantity[i]
            totalAmount += priceIntValue * quantity
        }
        return totalAmount
    }

    private fun setUserData() {
        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid
            val userReference: DatabaseReference = databaseReference.child("user").child(userId)
            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val names = snapshot.child("name").getValue(String::class.java) ?: ""
                        val addresss = snapshot.child("address").getValue(String::class.java) ?: ""
                        val phones = snapshot.child("phone").getValue(String::class.java) ?: ""

                        binding.etName.setText(names)
                        binding.etAddress.setText(addresss)
                        binding.etPhoneNumber.setText(phones)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

    }
}