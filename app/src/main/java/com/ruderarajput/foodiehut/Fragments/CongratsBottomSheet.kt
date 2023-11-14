package com.ruderarajput.foodiehut.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ruderarajput.foodiehut.Activity.MainActivity
import com.ruderarajput.foodiehut.databinding.FragmentCongratsBottomSheetBinding

class CongratsBottomSheet :BottomSheetDialogFragment() {

    lateinit var binding:FragmentCongratsBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentCongratsBottomSheetBinding.inflate(layoutInflater, container, false)

        binding.goHomeBtn.setOnClickListener {
           startActivity(Intent(requireContext(), MainActivity::class.java))
            dismiss()
        }
        return binding.root
    }

    companion object {
    }
}