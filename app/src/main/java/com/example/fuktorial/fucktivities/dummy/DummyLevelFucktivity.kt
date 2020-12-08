package com.example.fuktorial.fucktivities.dummy

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fuktorial.FucktivitiesInfo
import com.example.fuktorial.R
import com.example.fuktorial.database.Repository
import com.example.fuktorial.database.RepositoryImpl
import com.example.fuktorial.databinding.DummyLevelFucktivityBinding
import com.example.fuktorial.fucktivities.tutorial.TutorialLevelFucktivity
import com.example.fuktorial.fucktivities.tutorial.TutorialViewModel

class DummyLevelFucktivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DummyLevelFucktivityBinding.inflate(layoutInflater)
        val viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return modelClass.getConstructor(Repository::class.java).newInstance(RepositoryImpl)
            }
        }).get(TutorialViewModel::class.java)
        viewModel.initialize(this)
        setContentView(binding.root)
        viewModel.masterFucktivity(FucktivitiesInfo.getFucktivityName(this::class.java))
            .subscribe {
                showFinishDialog()
            }
    }

    private fun showFinishDialog() =
        AlertDialog.Builder(this)
            .setTitle(FucktivitiesInfo.getFucktivityName(DummyLevelFucktivity::class.java))
            .setView(R.layout.fucktivity_mastered)
            .setPositiveButton(R.string.ok) { _: DialogInterface, _: Int ->
                finish()
            }
            .create()
            .show()
}