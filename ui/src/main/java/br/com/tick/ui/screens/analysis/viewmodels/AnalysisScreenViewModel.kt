package br.com.tick.ui.screens.analysis.viewmodels

import androidx.lifecycle.ViewModel
import br.com.tick.sdk.dispatchers.DispatcherProvider
import br.com.tick.ui.screens.analysis.states.AnalysisGraphStates
import br.com.tick.ui.screens.analysis.states.FinancialHealth
import br.com.tick.ui.screens.analysis.states.MostExpensiveCategoriesStates
import br.com.tick.ui.screens.analysis.usecases.CalculateFinancialHealthSituation
import br.com.tick.ui.screens.analysis.usecases.FetchLastMonthExpenses
import br.com.tick.ui.screens.analysis.usecases.GetMostExpensiveCategories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class AnalysisScreenViewModel @Inject constructor(
    private val fetchLastMonthExpenses: FetchLastMonthExpenses,
    private val getMostExpensiveCategories: GetMostExpensiveCategories,
    private val calculateFinancialHealthSituation: CalculateFinancialHealthSituation,
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

    val financialHealthSituation: Flow<FinancialHealth>
        get() = flow {
            calculateFinancialHealthSituation().collect {
                emit(it)
            }
        }.flowOn(dispatcherProvider.io())
}