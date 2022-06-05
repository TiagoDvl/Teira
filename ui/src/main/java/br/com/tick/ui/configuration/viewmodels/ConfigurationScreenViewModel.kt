package br.com.tick.ui.configuration.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tick.sdk.repositories.LocalDataRepository
import br.com.tick.ui.configuration.states.MonthlyIncomeStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigurationScreenViewModel @Inject constructor(
    private val dataStoreRepository: LocalDataRepository
) : ViewModel() {

    val monthlyIncomeFlow = dataStoreRepository.getMonthlyIncome().map {
        MonthlyIncomeStates.of(it)
    }

    fun saveMonthlyIncome(value: Double) {
        viewModelScope.launch {
            dataStoreRepository.saveMonthlyIncome(value)
        }
    }
}
