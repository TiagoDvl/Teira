package br.com.tick.teira.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import br.com.tick.R
import br.com.tick.sdk.dispatchers.DispatcherProvider
import br.com.tick.sdk.extensions.getPeriodicityTimeDiff
import br.com.tick.sdk.notifications.NotificationCenter
import br.com.tick.sdk.repositories.localdata.LocalDataRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import br.com.tick.teira.R as AppR

@HiltWorker
class PeriodicWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val notificationCenter: NotificationCenter,
    private val dispatcherProvider: DispatcherProvider,
    private val dataStoreRepository: LocalDataRepository
) : CoroutineWorker(context, workerParams) {

    companion object {
        private const val TAG = "PeriodicWorker"
    }

    override suspend fun doWork(): Result {
        return with(dispatcherProvider.io()) {
            showPeriodicExpenseReminderNotification()
            val notificationPeriodicity =
                dataStoreRepository.getNotificationPeriodicity().first() ?: return@with Result.failure()

            val dailyWorkRequest = OneTimeWorkRequestBuilder<PeriodicWorker>()
                .setInitialDelay(LocalDateTime.now().getPeriodicityTimeDiff(notificationPeriodicity), TimeUnit.SECONDS)
                .addTag(TAG)
                .build()

            WorkManager.getInstance(applicationContext).enqueue(dailyWorkRequest)
            Result.success()
        }
    }

    private suspend fun showPeriodicExpenseReminderNotification() {
        notificationCenter.sendNotification(
            applicationContext.getString(AppR.string.periodic_reminder_title),
            applicationContext.getString(AppR.string.periodic_reminder_description),
            R.drawable.ic_wallet,
            applicationContext.getString(AppR.string.teira_periodic_reminder_channel_id)
        )
    }

}
