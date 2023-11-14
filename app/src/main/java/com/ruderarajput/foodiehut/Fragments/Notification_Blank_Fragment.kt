package com.ruderarajput.foodiehut.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ruderarajput.foodiehut.Adapter.NotificationAdapter
import com.ruderarajput.foodiehut.R
import com.ruderarajput.foodiehut.databinding.FragmentNotificationBlankBinding


class Notification_Blank_Fragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNotificationBlankBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBlankBinding.inflate(layoutInflater, container, false)
        val notifications = listOf(
            "Your order has been Canceled Succesfully",
            "Order has been taken by the driver",
            "Congrats Your Order Placed"
        )
        val notificationsImages =
            listOf(R.drawable.sad_ic, R.drawable.dliver_truck_ic, R.drawable.checked_ic)
        val adapter = NotificationAdapter(ArrayList(notifications), ArrayList(notificationsImages))

        binding.recViewNotification.layoutManager = LinearLayoutManager(requireContext())
        binding.recViewNotification.adapter = adapter
        return binding.root
    }
}