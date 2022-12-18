package br.com.tick.ui.screens.configuration.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tick.sdk.repositories.LocalDataRepository
import br.com.tick.ui.di.DefaultDispatcherProvider
import br.com.tick.ui.di.DispatcherProvider
import br.com.tick.ui.screens.configuration.states.MonthlyIncomeStates
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

    fun saveMonthlyIncome(value: Double) {
        viewModelScope.launch(dispatcherProvider.io()) {
            dataStoreRepository.saveMonthlyIncome(value)
        }
    }
}
