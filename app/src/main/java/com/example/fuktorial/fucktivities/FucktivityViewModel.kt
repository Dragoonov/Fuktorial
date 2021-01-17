package com.example.fuktorial.fucktivities

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.fuktorial.database.Repository
import com.example.fuktorial.database.models.Fucktivity
import io.reactivex.rxjava3.core.Completable

abstract class FucktivityViewModel(private val repository: Repository): ViewModel() {

    fun initialize(context: Context) = repository.open(context)

    fun masterFucktivity(fucktivityName: String): Completable =
        repository.updateFucktivity(Fucktivity(fucktivityName, true, mastered = true))
}