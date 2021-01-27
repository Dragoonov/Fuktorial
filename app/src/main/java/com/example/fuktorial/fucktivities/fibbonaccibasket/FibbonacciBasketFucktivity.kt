package com.example.fuktorial.fucktivities.fibbonaccibasket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.fuktorial.databinding.ActivityFibbonacciBasketFucktivityBinding

class FibbonacciBasketFucktivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFibbonacciBasketFucktivityBinding.inflate(layoutInflater)
        Log.v("Kupa", (binding.container == binding.root).toString())
        setContentView(binding.root)
    }
}