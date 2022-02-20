package br.com.tick.teira.ui.screens.analysis.viewmodels

import androidx.lifecycle.ViewModel
import br.com.tick.teira.ui.screens.analysis.usecases.CalculateFinancialHealthSituation
import br.com.tick.teira.ui.screens.analysis.usecases.FetchLastMonthExpenses
import br.com.tick.teira.ui.screens.analysis.usecases.GetMostExpensiveCategories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class AnalysisScreenViewModel @Inject constructor(
    private val fetchExpenses: FetchLastMonthExpenses,
    private val getMostExpensiveCategories: GetMostExpensiveCategories,
    private val calculateFinancialHealthSituation: CalculateFinancialHealthSituation
) : ViewModel() {

    val graphStates = flow {
        fetchExpenses().collect {
            emit(it)
        }
    }

    val mostExpenseCategoryList = flow {
        getMostExpensiveCategories().collect {
            emit(it)
        }
    }

    val financialHealthSituation = flow {
        calculateFinancialHealthSituation().collect {
            emit(it)
        }
    }
}