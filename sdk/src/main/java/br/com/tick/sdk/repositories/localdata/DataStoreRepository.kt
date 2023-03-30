package br.com.tick.sdk.repositories.localdata

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import br.com.tick.sdk.domain.NotificationPeriodicity
import br.com.tick.sdk.domain.PersistedMonthlyIncome
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : LocalDataRepository {

    companion object {
        const val MONTHLY_INCOME_KEY = "MONTHLY_INCOME_KEY"
        const val NOTIFICATION_PERIODICITY = "NOTIFICATION_PERIODICITY_KEY"
        const val PERIODIC_NOTIFICATION_ID_KEY = "PERIODIC_NOTIFICATION_ID_KEY"
    }

    override fun getMonthlyIncome(): Flow<PersistedMonthlyIncome> {
        return dataStore.data.map { preferences ->
            PersistedMonthlyIncome(preferences[doublePreferencesKey(MONTHLY_INCOME_KEY)] ?: 0.0)
        }
    }

    override suspend fun saveMonthlyIncome(value: Double) {
        dataStore.edit { settings ->
            settings[doublePreferencesKey(MONTHLY_INCOME_KEY)] = value
        }
    }

    override fun getPeriodicNotificationId(): Flow<Int?> {
        return dataStore.data.map { preferences ->
            preferences[intPreferencesKey(PERIODIC_NOTIFICATION_ID_KEY)]
        }
    }

    override suspend fun setPeriodicNotificationId(notificationId: Int) {
        dataStore.edit { settings ->
            settings[intPreferencesKey(PERIODIC_NOTIFICATION_ID_KEY)] = notificationId
        }
    }

    override fun getNotificationPeriodicity(): Flow<NotificationPeriodicity?> {
        return dataStore.data.map { preferences ->
            val cachedPeriodicity = preferences[stringPreferencesKey(NOTIFICATION_PERIODICITY)]

            cachedPeriodicity?.let { NotificationPeriodicity.valueOf(it) }
        }
    }

    override suspend fun setNotificationPeriodicity(notificationPeriodicity: NotificationPeriodicity) {
        dataStore.edit { settings ->
            settings[stringPreferencesKey(NOTIFICATION_PERIODICITY)] = notificationPeriodicity.name
        }
    }
}
