package com.example.fuktorial.fucktivities.tutorial

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fuktorial.databinding.ActivityTutorialLevelBinding

class TutorialLevelActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        val binding = ActivityTutorialLevelBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}