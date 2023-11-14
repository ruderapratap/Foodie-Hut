package com.ruderarajput.foodiehut.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ruderarajput.foodiehut.R
import com.ruderarajput.foodiehut.databinding.ActivityStartScreenBinding

class ActivityStartScreen : AppCompatActivity() {
    val binding by lazy {
        ActivityStartScreenBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.nextBtn.setOnClickListener{
            startActivity(Intent(this,ChooseLocationActivity::class.java))
            finish()
        }
    }
}