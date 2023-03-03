package com.example.majika.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.set
import com.example.majika.R

@Suppress("Deprecation")
class SplashActivity : AppCompatActivity() {

    private val handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        val logo: ImageView = findViewById(R.id.splash_logo)
        val splash_text: TextView = findViewById(R.id.splash_quote_tv)
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.split_screen)
        slideAnimation.startOffset = 500
        logo.startAnimation(slideAnimation)
        splash_text.startAnimation(slideAnimation)

        handler.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 1500)
    }
}