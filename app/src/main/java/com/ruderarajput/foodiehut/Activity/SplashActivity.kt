package com.ruderarajput.foodiehut.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth
import com.ruderarajput.foodiehut.R

class SplashActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth=FirebaseAuth.getInstance()

        Handler(Looper.getMainLooper()).postDelayed({
            if (auth.currentUser!=null){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this,ActivityStartScreen::class.java))
                finish()
            }
        },5000)
    }
}