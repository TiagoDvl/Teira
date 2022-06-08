package br.com.tick.ui.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import br.com.tick.sdk.domain.PersistedMonthlyIncome
import br.com.tick.sdk.repositories.LocalDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : LocalDataRepository {

    override fun getMonthlyIncome(): Flow<PersistedMonthlyIncome> {
        return dataStore.data.map { preferences ->
            PersistedMonthlyIncome(preferences[doublePreferencesKey(PersistedMonthlyIncome.MONTHLY_INCOME_KEY)] ?: 0.0)
        }
    }

    override suspend fun saveMonthlyIncome(value: Double) {
        dataStore.edit { settings ->
            settings[doublePreferencesKey(PersistedMonthlyIncome.MONTHLY_INCOME_KEY)] = value
        }
    }
}
