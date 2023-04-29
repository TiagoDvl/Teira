package br.com.tick.sdk.repositories.localdata

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : LocalDataRepository {

    companion object {
        const val MONTHLY_INCOME_KEY = "MONTHLY_INCOME_KEY"
        const val NOTIFICATION_PERIODICITY_KEY = "NOTIFICATION_PERIODICITY_KEY"
        const val PERIODIC_NOTIFICATION_ID_KEY = "PERIODIC_NOTIFICATION_ID_KEY"
        const val CURRENCY_FORMAT_KEY = "CURRENCY_FORMAT_KEY"
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
}
