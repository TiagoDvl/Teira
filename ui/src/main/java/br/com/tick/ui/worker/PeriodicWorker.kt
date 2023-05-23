package br.com.tick.ui.worker

import android.app.PendingIntent
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import br.com.tick.sdk.di.MainActivityPendingIntent
import br.com.tick.sdk.dispatchers.DispatcherProvider
import br.com.tick.sdk.extensions.getPeriodicityTimeDiff
import br.com.tick.sdk.notifications.NotificationCenter
import br.com.tick.sdk.repositories.user.UserRepository
import br.com.tick.ui.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

@HiltWorker
class PeriodicWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    @MainActivityPendingIntent private val mainActivityPendingIntent: PendingIntent,
    private val notificationCenter: NotificationCenter,
    private val dispatcherProvider: DispatcherProvider,
    private val userRepository: UserRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return with(dispatcherProvider.io()) {
            showPeriodicExpenseReminderNotification()
            val notificationPeriodicity = userRepository.getUser().first().notificationPeriodicity

            val dailyWorkRequest = OneTimeWorkRequestBuilder<PeriodicWorker>()
                .setInitialDelay(notificationPeriodicity.getPeriodicityTimeDiff(), TimeUnit.MINUTES)
                .addTag(applicationContext.getString(R.string.teira_periodic_reminder_channel_name))
                .build()

            WorkManager.getInstance(applicationContext).enqueue(dailyWorkRequest)
            Result.success()
        }
    }

    private suspend fun showPeriodicExpenseReminderNotification() {
        notificationCenter.sendNotification(
            applicationContext.getString(R.string.periodic_reminder_title),
            applicationContext.getString(R.string.periodic_reminder_description),
            R.drawable.ic_wallet,
            applicationContext.getString(R.string.teira_periodic_reminder_channel_id),
            mainActivityPendingIntent
        )
    }

}
