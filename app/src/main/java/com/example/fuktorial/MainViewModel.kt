package com.example.fuktorial

import android.content.Context
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

    private var _dataLoaded = MediatorLiveData<Boolean>().apply {
        addSource(undiscoveredFucktivities) {
            value = it != null &&
            lastFucktivityDiscovery.value != null &&
            displayedEntry.value != null
        }
        addSource(lastFucktivityDiscovery) {
            value = it != null &&
            undiscoveredFucktivities.value != null &&
            displayedEntry.value != null
        }
        addSource(displayedEntry) {
            value = it != null &&
            lastFucktivityDiscovery.value != null &&
            undiscoveredFucktivities.value != null
        }
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

    fun findAppropriateFragment() =
        if (displayedEntry.value!!.isNotEmpty()) {
            FucktivitiesInfo.getEntryByName(displayedEntry.value!!)
        } else if (undiscoveredFucktivities.value!!.isEmpty() ||
            System.currentTimeMillis() - lastFucktivityDiscovery.value!!.time < Constants.WAITING_TIME
        ) {
            repository.updateDisplayedEntry(NoFucktivityFragment::class.java.simpleName).subscribe {}
            NoFucktivityFragment::class.java
        } else {
            val klas = FucktivitiesInfo.getEntryByName(undiscoveredFucktivities.value!!.random().name)
            repository.updateDisplayedEntry(klas!!.simpleName).subscribe {}
            klas
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

    override fun onCleared() {
        undiscoveredFucktivities.removeObserver(observerFucktivities)
        displayedEntry.observeForever(observerDisplayedEntry)
        lastFucktivityDiscovery.observeForever(observerDiscovery)
        repository.close()
        super.onCleared()
    }
}