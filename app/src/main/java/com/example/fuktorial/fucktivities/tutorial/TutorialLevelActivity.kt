package com.example.fuktorial.fucktivities.tutorial

import android.content.DialogInterface
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.fuktorial.R
import com.example.fuktorial.databinding.ActivityTutorialLevelBinding

class TutorialLevelActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        val viewModel = ViewModelProvider(this)[TutorialViewModel::class.java]
        val binding = ActivityTutorialLevelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            viewModel.numberOfClicks += 1
            if (viewModel.numberOfClicks > 10) {
                showFinishDialog()
            }
        }
        showExplanationDialog()
    }

    private fun showExplanationDialog() =
        AlertDialog.Builder(this)
            .setTitle(R.string.welcome)
            .setMessage(R.string.explanation)
            .setPositiveButton(R.string.ok) { _: DialogInterface, _: Int -> }
            .create()
            .show()

    private fun showFinishDialog() {

    }

}