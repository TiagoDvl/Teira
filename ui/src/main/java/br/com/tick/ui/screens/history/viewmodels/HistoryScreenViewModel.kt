package br.com.tick.ui.screens.history.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tick.sdk.dispatchers.DispatcherProvider
import br.com.tick.sdk.domain.CategorizedExpense
import br.com.tick.sdk.repositories.categorizedexpense.CategorizedExpenseRepository
import br.com.tick.ui.screens.history.models.Expense
import br.com.tick.ui.screens.history.models.MonthlyGroupedExpenses
import br.com.tick.ui.screens.history.states.ExpensesHistoryStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryScreenViewModel @Inject constructor(
    private val categorizedExpenseRepository: CategorizedExpenseRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private var _chosenMonths = MutableStateFlow<List<String>>(listOf())
    val chosenMonths = _chosenMonths.asStateFlow()

    private val _expensesHistoryState = MutableStateFlow<ExpensesHistoryStates>(ExpensesHistoryStates.Empty)
    val expensesHistoryStates = _expensesHistoryState.asStateFlow()

    val availableHistoryMonths = categorizedExpenseRepository
        .getCategorizedExpenses()
        .map {
            it
                .sortedByDescending { categorizedExpense -> categorizedExpense.date }
                .map { categorizedExpense -> categorizedExpense.toChipLabel() }
                .distinct()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = listOf()
        )

    fun chosenMonths(months: List<String>) {
        viewModelScope.launch {
            _chosenMonths.emit(months)
            getExpenses()
        }
    }

    private fun getExpenses() {
        viewModelScope.launch(dispatcherProvider.io()) {
            categorizedExpenseRepository.getCategorizedExpenses().collect { categorizedExpense ->
                val filteredCategorizedExpenses = categorizedExpense
                    .sortedByDescending { it.date }
                    .filter { _chosenMonths.value.contains(it.toChipLabel()) }
                    .groupBy { it.date.month }
                    .map { (month, categorizedExpenses) ->
                        val createExpenses = categorizedExpenses.map {
                            Expense(it.date, it.name, it.expenseValue, it.category.name, it.category.color)
                        }

                        val isExpanded = when (val currentState = _expensesHistoryState.value) {
                            is ExpensesHistoryStates.Content -> {
                                currentState.expenseHistory.find { it.month == month.name }?.isExpanded ?: true
                            }
                            else -> true
                        }


                        MonthlyGroupedExpenses(month.name, createExpenses, isExpanded)
                    }

                _expensesHistoryState.emit(ExpensesHistoryStates.Content(filteredCategorizedExpenses))
            }
        }
    }

    fun setMonthExpanded(month: String) {
        viewModelScope.launch {
            _expensesHistoryState.update { state ->
                if (state is ExpensesHistoryStates.Content) {
                    val monthInExpansion = state.expenseHistory.map {
                        if (it.month == month) it.copy(isExpanded = !it.isExpanded) else it
                    }

                    ExpensesHistoryStates.Content(monthInExpansion)
                } else {
                    ExpensesHistoryStates.Empty
                }
            }
        }
    }

    private fun CategorizedExpense.toChipLabel(): String {
        return "${date.month.value}/${date.year}"
    }
}
