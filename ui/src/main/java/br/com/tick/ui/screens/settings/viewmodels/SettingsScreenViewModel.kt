package br.com.tick.ui.screens.settings.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tick.sdk.dispatchers.DispatcherProvider
import br.com.tick.sdk.domain.AccountingDate
import br.com.tick.sdk.domain.CurrencyFormat
import br.com.tick.sdk.domain.NotificationPeriodicity
import br.com.tick.sdk.repositories.user.UserRepository
import br.com.tick.ui.screens.settings.states.MonthlyIncomeStates
import br.com.tick.ui.screens.settings.states.SettingsAccountingDateStates
import br.com.tick.ui.screens.settings.states.SettingsCurrencyFormatStates
import br.com.tick.ui.screens.settings.states.SettingsNotificationPeriodicityStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    val monthlyIncomeFlow = userRepository
        .getUser()
        .flowOn(dispatcherProvider.io())
        .map {
            MonthlyIncomeStates.of(it.monthlyIncome, it.monthlyIncomeVisibility)
        }

    val notificationPeriodicity = userRepository
        .getUser()
        .flowOn(dispatcherProvider.io())
        .map {
            SettingsNotificationPeriodicityStates.Content(it.notificationPeriodicity)
        }

    val currencyFormat = userRepository
        .getUser()
        .flowOn(dispatcherProvider.io())
        .map {
            SettingsCurrencyFormatStates.Content(it.currency)
        }

    val startDate = userRepository
        .getUser()
        .flowOn(dispatcherProvider.io())
        .map {
            SettingsAccountingDateStates.Content(it.accountingDate)
        }

    val currency = userRepository
        .getUser()
        .flowOn(dispatcherProvider.io())
        .map {
            it.currency
        }

    fun saveMonthlyIncome(value: Double) {
        viewModelScope.launch(dispatcherProvider.io()) {
            userRepository.setMonthlyIncome(value)
        }
    }

    fun setNotificationPeriodicity(notificationPeriodicity: NotificationPeriodicity) {
        viewModelScope.launch(dispatcherProvider.io()) {
            userRepository.setNotificationPeriodicity(notificationPeriodicity)
        }
    }

    fun setCurrencyFormat(currencyFormat: CurrencyFormat) {
        viewModelScope.launch(dispatcherProvider.io()) {
            userRepository.setCurrency(currencyFormat)
        }
    }

    fun setAccountingDate(accountingDate: AccountingDate) {
        viewModelScope.launch(dispatcherProvider.io()) {
            userRepository.setAccountingDate(accountingDate)
        }
    }

    fun toggleMonthlyIncomeVisibility() {
        viewModelScope.launch(dispatcherProvider.io()) {
            userRepository.toggleMonthlyIncomeVisibility()
        }
    }
}
