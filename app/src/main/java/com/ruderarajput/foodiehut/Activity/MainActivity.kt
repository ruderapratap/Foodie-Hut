package com.ruderarajput.foodiehut.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ruderarajput.foodiehut.Fragments.Notification_Blank_Fragment
import com.ruderarajput.foodiehut.R
import com.ruderarajput.foodiehut.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var navController=findNavController(R.id.fragmentContainerView2)
        var bottomnav=findViewById<BottomNavigationView>(R.id.bottom_navi)
        bottomnav.setupWithNavController(navController)

        binding.notification.setOnClickListener {
            val bottomSheetDialog=Notification_Blank_Fragment()
            bottomSheetDialog.show(supportFragmentManager,"Test")
        }
    }
}