package com.example.evolvoai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class IntroductionActivity : AppCompatActivity() {
    private lateinit var btn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)
        val intent = Intent(this,MainActivity::class.java)

        btn = findViewById(R.id.nextBtn)

        btn.setOnClickListener{
            startActivity(intent)
            finish()
        }
    }
}