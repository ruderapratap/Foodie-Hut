package com.ruderarajput.foodiehut.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ruderarajput.foodiehut.Adapter.MenuAdapter
import com.ruderarajput.foodiehut.Adapter.PopularAdapter
import com.ruderarajput.foodiehut.Model.MenuModel
import com.ruderarajput.foodiehut.R
import com.ruderarajput.foodiehut.databinding.FragmentMenuBottomSheetBinding

class MenuBottomSheetFragment :BottomSheetDialogFragment(){

    private lateinit var binding:FragmentMenuBottomSheetBinding
    private lateinit var database:FirebaseDatabase
    private lateinit var menuItems:MutableList<MenuModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentMenuBottomSheetBinding.inflate(inflater, container, false)
        retriveMenuItem()
       return binding.root
    }

    private fun retriveMenuItem() {
        database=FirebaseDatabase.getInstance()
        val foodRef:DatabaseReference=database.reference.child("menu")
        menuItems= mutableListOf()

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children){
                    val menuItem=foodSnapshot.getValue(MenuModel::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun setAdapter() {
        val adapter=MenuAdapter(menuItems,requireContext())
        val layoutManager = LinearLayoutManager(context)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        binding.recViewCart.layoutManager=layoutManager
        binding.recViewCart.adapter=adapter
    }
}