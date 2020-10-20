package com.example.fuktorial

import android.os.Bundle
import android.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.fuktorial.databinding.ActivityMainBinding
import com.example.fuktorial.fucktivities.tutorial.TutorialEntryFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            replaceFragment(TutorialEntryFragment::class.java)
        }
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.fucktivity -> replaceFragment(TutorialEntryFragment::class.java)
                R.id.tasks -> Unit
                R.id.collection -> Unit
                R.id.settings -> replaceFragment(SettingsFragment::class.java)
            }
            true
        }
    }
}