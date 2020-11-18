package com.example.fuktorial

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.fuktorial.database.Repository
import com.example.fuktorial.database.RepositoryImpl
import com.example.fuktorial.databinding.ActivityMainBinding
import com.example.fuktorial.fucktivities.tutorial.TutorialEntryFragment
import com.example.fuktorial.notifications.NotificationWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        scheduleNotificationsIfNeeded()
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return modelClass.getConstructor(Repository::class.java).newInstance(RepositoryImpl(this@MainActivity))
            }
        }).get(MainViewModel::class.java)
        viewModel.apply {
            initialize(this@MainActivity)
            notificationsEnabled.observe(this@MainActivity, Observer {
                if (it) scheduleNotificationsIfNeeded() else cancelNotifications()
            })
        }
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            replaceFragment(TutorialEntryFragment::class.java)
        }
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.fucktivity -> replaceFragment(TutorialEntryFragment::class.java)
                R.id.tasks -> Unit
                R.id.collection -> Unit
                R.id.settings -> replaceFragment(SettingsFragment::class.java)
            }
            true
        }
    }

    private fun cancelNotifications() {
        WorkManager.getInstance(this).cancelUniqueWork(NotificationWorker.WORK_NAME)
    }

    private fun scheduleNotificationsIfNeeded() {
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(1, TimeUnit.DAYS)
            .build()
        WorkManager
            .getInstance(this)
            .enqueueUniquePeriodicWork(NotificationWorker.WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, workRequest)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = Utils.NOTIFICATIONS_CHANNEL
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(Utils.NOTIFICATIONS_CHANNEL, name, importance)
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}