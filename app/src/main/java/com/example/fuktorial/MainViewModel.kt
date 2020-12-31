package com.example.fuktorial

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import com.example.fuktorial.database.Repository
import com.example.fuktorial.database.models.Fucktivity
import com.example.fuktorial.database.models.Fuquote
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Completable
import java.util.Date

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val TAG = MainViewModel::class.java.simpleName
    private val observerFucktivities = Observer<List<Fucktivity>> {}

    private val observerDiscovery = Observer<Date> {}

    private val observerDisplayedEntry = Observer<String> {}

    private var _notificationsEnabled: MutableLiveData<Boolean> = MutableLiveData()
    val notificationsEnabled: LiveData<Boolean> = _notificationsEnabled

    private val displayedEntry: LiveData<String> = LiveDataReactiveStreams.fromPublisher(
        repository.getDisplayedEntry().toFlowable(BackpressureStrategy.BUFFER)
    )

    val discoveredFucktivities: LiveData<List<Fucktivity>> = LiveDataReactiveStreams.fromPublisher(
        repository.getDiscoveredFucktivities().toFlowable(BackpressureStrategy.BUFFER)
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

    val lastFucktivityDiscovery: LiveData<Date> = LiveDataReactiveStreams.fromPublisher(
        repository.getLastDiscovery().toFlowable(BackpressureStrategy.BUFFER)
    )

    private val predicate: (MutableLiveData<Boolean>) -> Observer<Any> = { liveData ->
        Observer {
            liveData.value = undiscoveredFucktivities.value != null &&
                    lastFucktivityDiscovery.value != null &&
                    displayedEntry.value != null
        }
    }

    private var _dataLoaded = MediatorLiveData<Boolean>().apply {
        addSource(undiscoveredFucktivities, predicate(this))
        addSource(lastFucktivityDiscovery, predicate(this))
        addSource(displayedEntry, predicate(this))
    }
    val dataLoaded: LiveData<Boolean> get() = _dataLoaded


    fun initialize(context: Context) {
        repository.open(context)
        undiscoveredFucktivities.observeForever(observerFucktivities)
        lastFucktivityDiscovery.observeForever(observerDiscovery)
        displayedEntry.observeForever(observerDisplayedEntry)
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val notificationsTurnedOn = preferences.getBoolean(Constants.NOTIFICATIONS, true)
        enableNotifications(notificationsTurnedOn)
    }

    fun enableNotifications(value: Boolean) = run { _notificationsEnabled.value = value }

    fun refreshNextFragment(): Class<out Fragment> {
        val fragment = findAppropriateFragment()
        repository.updateDisplayedEntry(fragment!!.simpleName).subscribe()
        return fragment
    }

    private fun findAppropriateFragment() =
        if (displayedEntry.value!!.isNotEmpty()) {
            FucktivitiesInfo.getEntryByName(displayedEntry.value!!)
        } else if (undiscoveredFucktivities.value!!.isEmpty() ||
            System.currentTimeMillis() - lastFucktivityDiscovery.value!!.time < Constants.WAITING_TIME
        ) {
            NoFucktivityFragment::class.java
        } else {
            FucktivitiesInfo.getEntryByName(undiscoveredFucktivities.value!!.random().name)
        }

    fun discoverFucktivity(fucktivityName: String): Completable {
        val date = System.currentTimeMillis()
        return repository
            .updateLastDiscovery(Date(date))
            .andThen(resetDisplayedEntry())
            .andThen(repository.updateFucktivity(Fucktivity(fucktivityName, discovered = true, mastered = false)))
    }

    fun resetDisplayedEntry() = repository.updateDisplayedEntry("")

    fun resetProgress(): Completable = repository.resetProgress()

    fun calculateTimeLeft(timePassed: Long): Triple<Int, Int, Int> {
        val time = System.currentTimeMillis() - timePassed
        val hours = 4 - time.div(1000 * 60 * 60).toInt()
        val minutes = 59 - (time.div(1000 * 60) % 60).toInt()
        val seconds = 59 - (time.div(1000) % 60).toInt()
        return Triple(hours, minutes, seconds)
    }

    override fun onCleared() {
        undiscoveredFucktivities.removeObserver(observerFucktivities)
        displayedEntry.removeObserver(observerDisplayedEntry)
        lastFucktivityDiscovery.removeObserver(observerDiscovery)
        repository.close()
        super.onCleared()
    }
}