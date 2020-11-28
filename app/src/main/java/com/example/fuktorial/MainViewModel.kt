package com.example.fuktorial

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fuktorial.database.Repository
import com.example.fuktorial.database.models.Fucktivity
import com.example.fuktorial.database.models.Fuquote
import com.example.fuktorial.notifications.NotificationWorker
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class MainViewModel(private val repository: Repository) : ViewModel() {

    private var _notificationsEnabled: MutableLiveData<Boolean> = MutableLiveData()
    val notificationsEnabled: LiveData<Boolean> = _notificationsEnabled

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

    val undiscoveredFucktivities: LiveData<List<Fucktivity>> = LiveDataReactiveStreams.fromPublisher(
        repository.getUndicoveredFucktivities().toFlowable(BackpressureStrategy.BUFFER)
    )

    val allFuquotes: LiveData<List<Fuquote>> = LiveDataReactiveStreams.fromPublisher(
        repository.getAllFuquotes().toFlowable(BackpressureStrategy.BUFFER)
    )

    fun initialize(context: Context) {
        repository.open(context)
        val notificationsTurnedOn = (context as Activity).getPreferences(MODE_PRIVATE).getBoolean("notifications", true)
        _notificationsEnabled.value = notificationsTurnedOn
    }

    fun enableNotifications(value: Boolean) = run {_notificationsEnabled.value = value}

    fun discoverFuquote(fuquoteName: String): Completable =
        repository.updateFuquote(Fuquote(fuquoteName, true))

    fun discoverFucktivity(fucktivityName: String): Completable =
        repository.updateFucktivity(Fucktivity(fucktivityName, discovered = true, mastered = false))


    fun masterFucktivity(fucktivityName: String): Completable =
        repository.updateFucktivity(Fucktivity(fucktivityName, true, mastered = true))


    fun resetProgress(): Completable = repository.resetProgress()

    override fun onCleared() {
        repository.close()
        super.onCleared()
    }
}