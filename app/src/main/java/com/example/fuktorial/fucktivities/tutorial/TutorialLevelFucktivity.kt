package com.example.fuktorial.fucktivities.tutorial

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
import com.example.fuktorial.databinding.FucktivityTutorialLevelBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable


class TutorialLevelFucktivity : AppCompatActivity() {

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel =  ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return modelClass.getConstructor(Repository::class.java).newInstance(RepositoryImpl)
            }
        }).get(TutorialViewModel::class.java)
        viewModel.initialize(this)
        val binding = FucktivityTutorialLevelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            viewModel.numberOfClicks += 1
            if (viewModel.numberOfClicks > 10) {
                disposable.add(
                viewModel.masterFucktivity(FucktivitiesInfo.getFucktivityName(this::class.java))
                    .subscribe {
                        showFinishDialog()
                    }
                )
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

    private fun showFinishDialog() =
        AlertDialog.Builder(this)
            .setTitle(FucktivitiesInfo.getFucktivityName(TutorialLevelFucktivity::class.java))
            .setView(R.layout.fucktivity_mastered)
            .setPositiveButton(R.string.ok) { _: DialogInterface, _: Int ->
                finish()
            }
            .create()
            .show()

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }
}