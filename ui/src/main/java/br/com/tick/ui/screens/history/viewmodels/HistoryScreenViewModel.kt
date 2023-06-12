package br.com.tick.ui.screens.history.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tick.sdk.dispatchers.DispatcherProvider
import br.com.tick.sdk.repositories.categorizedexpense.CategorizedExpenseRepository
import br.com.tick.ui.screens.history.models.MonthlyGroupedExpenses
import br.com.tick.ui.screens.history.states.ExpensesHistoryStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryScreenViewModel @Inject constructor(
    categorizedExpenseRepository: CategorizedExpenseRepository,
    val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    val availableHistoryMonths = categorizedExpenseRepository
        .getCategorizedExpenses()
        .map {
            it.map { categorizedExpense ->
                "${categorizedExpense.date.month.value}/${categorizedExpense.date.year}"
            }.distinct()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = listOf()
        )

    private val _expensesHistoryState = MutableStateFlow<ExpensesHistoryStates>(ExpensesHistoryStates.Empty)
    val expensesHistoryStates = _expensesHistoryState.asStateFlow()

    fun getExpenses(chosenMonths: List<String>) {
        viewModelScope.launch(dispatcherProvider.io()) {
            val listToEmit = mutableListOf<MonthlyGroupedExpenses>()

            chosenMonths.forEach {
                listToEmit.add(MonthlyGroupedExpenses(it, listOf(), false))
            }

            _expensesHistoryState.emit(ExpensesHistoryStates.Content(listToEmit))
        }
    }
}
