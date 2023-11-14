package com.ruderarajput.foodiehut.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.ruderarajput.foodiehut.R
import com.ruderarajput.foodiehut.databinding.ActivityChooseLocationBinding

class ChooseLocationActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityChooseLocationBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val locationList = arrayOf("Chandigarh")
        val adapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,locationList)
        val autoCompleteTextView=binding.listOfLocation
        autoCompleteTextView.setAdapter(adapter)

        binding.nextBtn.setOnClickListener {
            startActivity(Intent(this@ChooseLocationActivity,LoginActivity::class.java))
            finish()
        }
    }
}