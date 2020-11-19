package com.example.fuktorial.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.fuktorial.Utils
import com.example.fuktorial.database.Repository
import com.example.fuktorial.database.RepositoryImpl
import com.example.fuktorial.database.models.Fuquote

class NotificationBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager: NotificationManager =
            context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val repository: Repository = RepositoryImpl(context)
        val id = intent!!.extras!!.getInt(Utils.NOTIFICATION_ID_STRING)
        val fuquote = intent.extras?.getString(Utils.NOTIFICATIONS)!!
        repository.open()
        repository.updateFuquote(Fuquote(fuquote, true))
            .subscribe()
        notificationManager.cancel(id)
    }
}