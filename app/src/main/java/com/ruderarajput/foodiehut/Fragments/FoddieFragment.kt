package com.ruderarajput.foodiehut.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ruderarajput.foodiehut.Adapter.MenuAdapter
import com.ruderarajput.foodiehut.Adapter.PopularAdapter
import com.ruderarajput.foodiehut.Model.MenuModel
import com.ruderarajput.foodiehut.R
import com.ruderarajput.foodiehut.databinding.FragmentFoddieBinding


class FoddieFragment : Fragment() {
    private lateinit var binding: FragmentFoddieBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<MenuModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFoddieBinding.inflate(inflater, container, false)

        binding.viewMenu.setOnClickListener {
            val bottomSheetDialog = MenuBottomSheetFragment()
            bottomSheetDialog.show(parentFragmentManager, "Test")
        }

        retriveAndDisplayPopularItem()
        return binding.root

    }

    private fun retriveAndDisplayPopularItem() {
        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val menuItem = foodSnapshot.getValue(MenuModel::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
                rendomPopularItems()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun rendomPopularItems() {
        val index = menuItems.indices.toList().shuffled()
        val numItemToShow = 10
        val subsSetMenuItem = index.take(numItemToShow).map { menuItems[it] }
        setPopularItemsAdapter(subsSetMenuItem)
    }

    private fun setPopularItemsAdapter(subsSetMenuItem: List<MenuModel>) {
        val adapter = MenuAdapter(subsSetMenuItem, requireContext())
        binding.recViewPopular.layoutManager = LinearLayoutManager(requireContext())
        binding.recViewPopular.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.img1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.img2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.img3, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.img4, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.img5, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.burger, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.dal, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.pizza, ScaleTypes.FIT))

        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList, ScaleTypes.FIT)

        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun doubleClick(position: Int) {

            }

            override fun onItemSelected(position: Int) {
                val itemPosition = imageList[position]
                val itemMessage = "Selected Image $position"
                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}

