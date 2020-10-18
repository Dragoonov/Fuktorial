package com.example.fuktorial.fucktivities.tutorial

import android.content.DialogInterface
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.fuktorial.R
import com.example.fuktorial.databinding.ActivityTutorialLevelBinding
import com.example.fuktorial.startFucktivity

class TutorialLevelActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        val binding = ActivityTutorialLevelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showExplanationDialog()
    }

    private fun showExplanationDialog() =
        AlertDialog.Builder(this)
            .setTitle(R.string.welcome)
            .setMessage(R.string.explanation)
            .setPositiveButton(R.string.ok) { _: DialogInterface, _: Int -> }
            .create()
            .show()

}