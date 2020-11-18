package com.example.fuktorial.notifications

import android.content.Context
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
        val fuquote = "\""+FuquotesInfo.fuquotesList[Random(System.currentTimeMillis()).nextInt(FuquotesInfo.fuquotesList.size)]+"\""
        val notification = NotificationCompat.Builder(applicationContext, Utils.NOTIFICATIONS_CHANNEL)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(applicationContext.getString(R.string.claim_fuquote))
            .setContentText(fuquote)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(fuquote)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
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