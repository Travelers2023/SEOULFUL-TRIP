package com.seoulfultrip.seoulfultrip

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.seoulfultrip.seoulfultrip.databinding.ActivityHomeDetailBinding

class HomeDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}