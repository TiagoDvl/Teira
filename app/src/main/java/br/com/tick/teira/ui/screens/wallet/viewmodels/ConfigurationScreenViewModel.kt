package br.com.tick.teira.ui.screens.wallet.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tick.teira.ui.datasource.repositories.DataStoreRepository
import br.com.tick.teira.ui.screens.wallet.states.MonthlyIncomeStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigurationScreenViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    val monthlyIncomeFlow = dataStoreRepository.getMonthlyIncome().map {
        MonthlyIncomeStates.Value(it)
    }

    fun saveMonthlyIncome(value: Double) {
        viewModelScope.launch {
            dataStoreRepository.saveMonthlyIncome(value)
        }
    }
}
