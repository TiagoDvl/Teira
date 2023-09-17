package br.com.tick.ui.screens.analysis.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tick.sdk.dispatchers.DispatcherProvider
import br.com.tick.sdk.domain.CurrencyFormat
import br.com.tick.sdk.repositories.user.UserRepository
import br.com.tick.ui.screens.analysis.states.AnalysisGraphStates
import br.com.tick.ui.screens.analysis.states.FinancialHealth
import br.com.tick.ui.screens.analysis.states.MostExpensiveCategoriesStates
import br.com.tick.ui.screens.analysis.usecases.CalculateFinancialHealthSituation
import br.com.tick.ui.screens.analysis.usecases.FetchLastMonthExpenses
import br.com.tick.ui.screens.analysis.usecases.GetMostExpensiveCategories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalysisScreenViewModel @Inject constructor(
    private val fetchLastMonthExpenses: FetchLastMonthExpenses,
    private val getMostExpensiveCategories: GetMostExpensiveCategories,
    private val calculateFinancialHealthSituation: CalculateFinancialHealthSituation,
    userRepository: UserRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    val graphStates: Flow<AnalysisGraphStates>
        get() = flow {
            fetchLastMonthExpenses().collect {
                emit(it)
            }
        }.flowOn(dispatcherProvider.io())

    val mostExpenseCategoryList: Flow<MostExpensiveCategoriesStates>
        get() = flow {
            getMostExpensiveCategories().collect {
                emit(MostExpensiveCategoriesStates.of(it))
            }
        }.flowOn(dispatcherProvider.io())

    val currency = userRepository.getUser()
        .flowOn(dispatcherProvider.io())
        .map { it.currency }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CurrencyFormat.EURO
        )

    private val _financialHealthSituation = MutableStateFlow<FinancialHealth>(FinancialHealth.NoDataAvailable)
    val financialHealthSituation: Flow<FinancialHealth> = _financialHealthSituation

    init {
        viewModelScope.launch(dispatcherProvider.io()) {
            calculateFinancialHealthSituation().collect {
                _financialHealthSituation.emit(it)
            }
        }
    }
}
