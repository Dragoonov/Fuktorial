package com.example.fuktorial

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.example.fuktorial.database.Repository
import com.example.fuktorial.database.models.Fucktivity
import com.example.fuktorial.database.models.Fuquote
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    init {
        repository.open()
    }
    val discoveredFucktivities: LiveData<List<Fucktivity>> = LiveDataReactiveStreams.fromPublisher(
        repository.getDiscoveredFucktivities().toFlowable(BackpressureStrategy.BUFFER)
    )
    val getAllFucktivities: LiveData<List<Fucktivity>> = LiveDataReactiveStreams.fromPublisher(
    repository.getAllFucktivities().toFlowable(BackpressureStrategy.BUFFER)
    )
    fun getDiscoveredFuquotes(): LiveData<List<Fuquote>> = LiveDataReactiveStreams.fromPublisher(
        repository.getDiscoveredFuquotes().toFlowable(BackpressureStrategy.BUFFER)
    )
    fun getAllFuquotes(): LiveData<List<Fuquote>> = LiveDataReactiveStreams.fromPublisher(
        repository.getAllFuquotes().toFlowable(BackpressureStrategy.BUFFER)
    )

    fun discoverQuote(): Completable = repository.discoverQuote()
    fun discoverFucktivity(): Completable = repository.discoverFucktivity()
    fun masterFucktivity(): Completable = repository.masterFucktivity()
    fun resetProgress(): Completable = repository.resetProgress()

    override fun onCleared() {
        repository.close()
        super.onCleared()
    }
}