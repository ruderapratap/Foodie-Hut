package com.ruderarajput.foodiehut.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ruderarajput.foodiehut.Adapter.MenuAdapter
import com.ruderarajput.foodiehut.Model.MenuModel
import com.ruderarajput.foodiehut.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: MenuAdapter
    private lateinit var database: FirebaseDatabase
    private val orignalMenuItems = mutableListOf<MenuModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)


        //setup for searchView
        setupSearchView()

        retriveMenuItems()

        return binding.root
    }

    private fun retriveMenuItems() {
        database = FirebaseDatabase.getInstance()

        val foodReference: DatabaseReference = database.reference.child("menu")
        foodReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val menuItem = foodSnapshot.getValue(MenuModel::class.java)
                    menuItem?.let {
                        orignalMenuItems.add(it)
                    }
                }
                showAllMenu()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun showAllMenu() {
        val filteredMenuItem = ArrayList(orignalMenuItems)
        setAdapter(filteredMenuItem)
    }

    private fun setAdapter(filteredMenuItem: List<MenuModel>) {
        adapter = MenuAdapter(filteredMenuItem, requireContext())
        binding.recViewSearch.layoutManager = LinearLayoutManager(requireContext())
        binding.recViewSearch.adapter = adapter
    }

    private fun setupSearchView() {

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                filterItemMenu(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterItemMenu(newText)
                return true
            }
        })
    }

    private fun filterItemMenu(query: String) {
        val filterItemMenu = orignalMenuItems.filter {
            it.foodName?.contains(query, ignoreCase = true) == true
        }
        setAdapter(filterItemMenu)
    }
}
