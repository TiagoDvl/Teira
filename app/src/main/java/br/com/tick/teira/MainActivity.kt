package br.com.tick.teira

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import br.com.tick.sdk.domain.NotificationPeriodicity
import br.com.tick.sdk.extensions.getPeriodicityTimeDiff
import br.com.tick.teira.worker.PeriodicWorker
import br.com.tick.ui.TeiraScaffold
import br.com.tick.ui.extensions.collectAsEffect
import br.com.tick.ui.theme.TeiraTheme
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TeiraTheme {
                viewModel.initialPeriodicNotificationRegistration.collectAsEffect {
                    registerInitialPeriodicNotification(it)
                }
                TeiraScaffold()
            }
        }
    }

    private fun registerInitialPeriodicNotification(notificationPeriodicity: NotificationPeriodicity) {
        val name = getString(R.string.teira_periodic_reminder_channel_name)
        val descriptionText = getString(R.string.teira_periodic_reminder_channel_description)
        val channelId = getString(R.string.teira_periodic_reminder_channel_id)
        val periodicWorker = OneTimeWorkRequestBuilder<PeriodicWorker>()
            .setInitialDelay(LocalDateTime.now().getPeriodicityTimeDiff(notificationPeriodicity).also {  Log.d("Tiago", "Diff de tempo foi: $it") }, TimeUnit.SECONDS)
            .build()

        viewModel.setupPeriodicNotification(name, descriptionText, channelId)
        WorkManager.getInstance(this).enqueue(periodicWorker)
    }
}
