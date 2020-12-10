package com.example.fuktorial

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.fuktorial.collection.CollectionFragment
import com.example.fuktorial.database.Repository
import com.example.fuktorial.database.RepositoryImpl
import com.example.fuktorial.database.models.Fucktivity
import com.example.fuktorial.databinding.ActivityMainBinding
import com.example.fuktorial.notifications.NotificationWorker
import com.example.fuktorial.settings.SettingsFragment
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return modelClass.getConstructor(Repository::class.java).newInstance(RepositoryImpl)
            }
        }).get(MainViewModel::class.java)
        viewModel.apply {
            initialize(this@MainActivity)
            notificationsEnabled.observe(this@MainActivity, Observer {
                if (it) scheduleNotifications() else cancelNotifications()
            })
            dataLoaded.observe(this@MainActivity, Observer {
                if (it) {
                    replaceFragment(findAppropriateFragment()!!)
                }
            })
            resetProgress().subscribe {
                Log.d("dd", "Usuniete")
            }
        }
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.fucktivity -> replaceFragment(viewModel.findAppropriateFragment()!!)
                R.id.tasks -> Unit
                R.id.collection -> replaceFragment(CollectionFragment::class.java)
                R.id.settings -> replaceFragment(SettingsFragment::class.java)
            }
            true
        }
    }

    private fun cancelNotifications() =WorkManager.getInstance(this).cancelUniqueWork(NotificationWorker.WORK_NAME)

    private fun scheduleNotifications() {
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(1, TimeUnit.DAYS)
            .build()
        WorkManager
            .getInstance(this)
            .enqueueUniquePeriodicWork(NotificationWorker.WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, workRequest)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = Constants.NOTIFICATIONS
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(Constants.NOTIFICATIONS, name, importance)
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}