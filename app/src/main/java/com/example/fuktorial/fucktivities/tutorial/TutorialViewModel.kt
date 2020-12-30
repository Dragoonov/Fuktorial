package com.example.fuktorial.fucktivities.tutorial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fuktorial.FucktivitiesInfo
import com.example.fuktorial.database.Repository
import com.example.fuktorial.fucktivities.FucktivityViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable


class TutorialViewModel(repository: Repository): FucktivityViewModel(repository) {
    private var numberOfClicks = 0
    private val disposable = CompositeDisposable()

    private var _showDialog = MutableLiveData<Boolean>(false)
    val showDialog: LiveData<Boolean> get() = _showDialog

    fun onClick() {
        numberOfClicks += 1
        if (numberOfClicks > 10) {
            disposable.add(
                masterFucktivity(FucktivitiesInfo.getFucktivityName(this::class.java))
                    .subscribe {
                        _showDialog.value = true
                    }
            )
        }
    }
}