package br.com.tick.teira.di

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import br.com.tick.sdk.di.MainActivityPendingIntent
import br.com.tick.teira.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object PendingIntentModule {

    @MainActivityPendingIntent
    @Provides
    fun providesMainActivityPendingIntent(@ApplicationContext context: Context): PendingIntent {
        val resultIntent = Intent(context, MainActivity::class.java)
        return TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }
    }
}
