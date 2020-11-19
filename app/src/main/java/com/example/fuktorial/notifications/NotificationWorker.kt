package com.example.fuktorial.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.fuktorial.FuquotesInfo
import com.example.fuktorial.R
import com.example.fuktorial.Utils
import kotlin.random.Random

class NotificationWorker(
    appContext: Context,
    workerParameters: WorkerParameters
) : Worker(appContext, workerParameters) {

    override fun doWork(): Result {
        val fuquote = FuquotesInfo.fuquotesList[Random(System.currentTimeMillis()).nextInt(FuquotesInfo.fuquotesList.size)]
        val intent = Intent(applicationContext, NotificationBroadcastReceiver::class.java).apply {
            putExtra(Utils.NOTIFICATIONS, fuquote)
            putExtra(Utils.NOTIFICATION_ID_STRING, Utils.NOTIFICATION_ID)
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getBroadcast(applicationContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        val notification = NotificationCompat.Builder(applicationContext, Utils.NOTIFICATIONS)
            .setSmallIcon(R.drawable.fucktivity)
            .setContentTitle("\"" + applicationContext.getString(R.string.claim_fuquote) +"\"")
            .setContentText(fuquote)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(fuquote)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(R.drawable.collection, applicationContext.getString(R.string.claim), pendingIntent)
            .build()
        with(NotificationManagerCompat.from(applicationContext)) {
            notify(Utils.NOTIFICATION_ID, notification)
        }
        return Result.success()
    }

    companion object {
        val WORK_NAME = "notifications"
    }

}