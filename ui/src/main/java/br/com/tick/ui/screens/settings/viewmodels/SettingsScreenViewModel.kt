package br.com.tick.ui.screens.settings.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tick.sdk.dispatchers.DispatcherProvider
import br.com.tick.sdk.domain.CurrencyFormat
import br.com.tick.sdk.domain.NotificationPeriodicity
import br.com.tick.sdk.repositories.localdata.LocalDataRepository
import br.com.tick.ui.screens.settings.states.MonthlyIncomeStates
import br.com.tick.ui.screens.settings.states.SettingsCurrencyFormatStates
import br.com.tick.ui.screens.settings.states.SettingsNotificationPeriodicityStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val dataStoreRepository: LocalDataRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    val monthlyIncomeFlow = dataStoreRepository
        .getMonthlyIncome()
        .flowOn(dispatcherProvider.io())
        .map {
            MonthlyIncomeStates.of(it.monthlyIncomeValue)
        }

    val notificationPeriodicity = dataStoreRepository
        .getNotificationPeriodicity()
        .flowOn(dispatcherProvider.io())
        .map {
            SettingsNotificationPeriodicityStates.of(it)
        }

    val currencyFormat = dataStoreRepository
        .getCurrencyFormat()
        .flowOn(dispatcherProvider.io())
        .map {
            SettingsCurrencyFormatStates.of(it)
        }

    fun saveMonthlyIncome(value: Double) {
        viewModelScope.launch(dispatcherProvider.io()) {
            dataStoreRepository.saveMonthlyIncome(value)
        }
    }

    fun setNotificationPeriodicity(notificationPeriodicity: NotificationPeriodicity) {
        viewModelScope.launch(dispatcherProvider.io()) {
            dataStoreRepository.setNotificationPeriodicity(notificationPeriodicity)
        }
    }

    fun setCurrencyFormat(currencyFormat: CurrencyFormat) {
        viewModelScope.launch(dispatcherProvider.io()) {
            dataStoreRepository.setCurrencyFormat(currencyFormat)
        }
    }
}
