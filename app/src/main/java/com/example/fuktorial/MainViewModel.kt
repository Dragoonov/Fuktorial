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

class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    init {
        repository.open()
    }

    val discoveredFucktivities: LiveData<List<Fucktivity>> = LiveDataReactiveStreams.fromPublisher(
        repository.getDiscoveredFucktivities().toFlowable(BackpressureStrategy.BUFFER)
    )
    val allFucktivities: LiveData<List<Fucktivity>> = LiveDataReactiveStreams.fromPublisher(
        repository.getAllFucktivities().toFlowable(BackpressureStrategy.BUFFER)
    )

    val masteredFucktivities: LiveData<List<Fucktivity>> = LiveDataReactiveStreams.fromPublisher(
        repository.getMasteredFucktivities().toFlowable(BackpressureStrategy.BUFFER)
    )

    val discoveredFuquotes: LiveData<List<Fuquote>> = LiveDataReactiveStreams.fromPublisher(
        repository.getDiscoveredFuquotes().toFlowable(BackpressureStrategy.BUFFER)
    )

    val allFuquotes: LiveData<List<Fuquote>> = LiveDataReactiveStreams.fromPublisher(
        repository.getAllFuquotes().toFlowable(BackpressureStrategy.BUFFER)
    )

    fun discoverFuquote(fuquote: Fuquote): Completable {
        fuquote.discovered = true
        return repository.updateFuquote(fuquote)
    }
    fun discoverFucktivity(fucktivity: Fucktivity): Completable {
        fucktivity.discovered = true
        return repository.updateFucktivity(fucktivity)
    }

    fun masterFucktivity(fucktivity: Fucktivity): Completable {
        fucktivity.discovered = true
        fucktivity.mastered = true
        return repository.updateFucktivity(fucktivity)
    }

    fun resetProgress(): Completable = repository.resetProgress()

    override fun onCleared() {
        repository.close()
        super.onCleared()
    }
}