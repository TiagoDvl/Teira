package br.com.tick.sdk.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import br.com.tick.sdk.dispatchers.DispatcherProvider
import br.com.tick.sdk.repositories.localdata.LocalDataRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

class TeiraNotificationCenter @Inject constructor(
    @ApplicationContext private val context: Context,
    private val localDataStore: LocalDataRepository,
    private val dispatcherProvider: DispatcherProvider
) : NotificationCenter, NotificationRegister {

    override fun register(name: String, descriptionText: String, channelId: String) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override suspend fun sendNotification(
        title: String,
        text: String,
        icon: Int,
        channelId: String,
        pendingIntent: PendingIntent
    ) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val notification = NotificationCompat.Builder(context, channelId)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .build()
            NotificationManagerCompat.from(context).notify(getNotificationId(), notification)
        }
    }

    private suspend fun getNotificationId(): Int {
        return withContext(dispatcherProvider.io()) {
            localDataStore.getPeriodicNotificationId().first() ?: Random.nextInt().also {
                localDataStore.setPeriodicNotificationId(it)
            }
        }
    }
}
