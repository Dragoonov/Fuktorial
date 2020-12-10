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

    private var displayedEntry: String = ""

    private val observerFucktivities = Observer<List<Fucktivity>> {}

    private val observerDiscovery = Observer<Date> {}

    private var _notificationsEnabled: MutableLiveData<Boolean> = MutableLiveData()
    val notificationsEnabled: LiveData<Boolean> = _notificationsEnabled

    val discoveredFucktivities: LiveData<List<Fucktivity>> = LiveDataReactiveStreams.fromPublisher(
        repository.getDiscoveredFucktivities().toFlowable(BackpressureStrategy.BUFFER)
    )

    val masteredFucktivities: LiveData<List<Fucktivity>> = LiveDataReactiveStreams.fromPublisher(
        repository.getMasteredFucktivities().toFlowable(BackpressureStrategy.BUFFER)
    )

    val discoveredFuquotes: LiveData<List<Fuquote>> = LiveDataReactiveStreams.fromPublisher(
        repository.getDiscoveredFuquotes().toFlowable(BackpressureStrategy.BUFFER)
    )

    private val undiscoveredFucktivities: LiveData<List<Fucktivity>> = LiveDataReactiveStreams.fromPublisher(
        repository.getUndicoveredFucktivities().toFlowable(BackpressureStrategy.BUFFER)
    )

    val lastFucktivityDiscovery: LiveData<Date> = LiveDataReactiveStreams.fromPublisher(
        repository.getLastDiscovery().toFlowable(BackpressureStrategy.BUFFER)
    )

    private var _dataLoaded = MediatorLiveData<Boolean>().apply {
        addSource(undiscoveredFucktivities) { value = it != null && lastFucktivityDiscovery.value != null }
        addSource(lastFucktivityDiscovery) { value = it != null && undiscoveredFucktivities.value != null }
    }
    val dataLoaded: LiveData<Boolean> get() = _dataLoaded

    private val FIVE_HOURS = 1000 * 60 * 60 * 5


    fun initialize(context: Context) {
        repository.open(context)
        undiscoveredFucktivities.observeForever(observerFucktivities)
        lastFucktivityDiscovery.observeForever(observerDiscovery)
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val notificationsTurnedOn = preferences.getBoolean(Constants.NOTIFICATIONS, true)
        enableNotifications(notificationsTurnedOn)
    }

    fun enableNotifications(value: Boolean) = run { _notificationsEnabled.value = value }

    fun findAppropriateFragment() =
        if (displayedEntry.isNotEmpty()) {
            FucktivitiesInfo.getEntryByName(displayedEntry)
        } else if (undiscoveredFucktivities.value!!.isEmpty() ||
            System.currentTimeMillis() - lastFucktivityDiscovery.value!!.time < FIVE_HOURS
        ) {
            displayedEntry = NoFucktivityFragment::class.java.simpleName
            NoFucktivityFragment::class.java
        } else {
            val klas = FucktivitiesInfo.getEntryByName(undiscoveredFucktivities.value!!.random().name)
            displayedEntry = klas!!.simpleName
            klas
        }

    fun discoverFucktivity(fucktivityName: String, context: Context): Completable {
        val date = System.currentTimeMillis()
        PreferenceManager
            .getDefaultSharedPreferences(context)
            .edit()
            .putLong(Constants.LAST_DISCOVERY, date)
            .apply()
        repository.updateLastDiscovery(Date(date)).subscribe {}
        displayedEntry = ""
        return repository.updateFucktivity(Fucktivity(fucktivityName, discovered = true, mastered = false))
    }

    fun resetProgress(): Completable = repository.resetProgress()

    override fun onCleared() {
        undiscoveredFucktivities.removeObserver(observerFucktivities)
        repository.close()
        super.onCleared()
    }
}