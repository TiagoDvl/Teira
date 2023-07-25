package br.com.tick.ui.screens.history.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tick.sdk.dispatchers.DispatcherProvider
import br.com.tick.sdk.domain.CategorizedExpense
import br.com.tick.sdk.repositories.categorizedexpense.CategorizedExpenseRepository
import br.com.tick.sdk.repositories.user.UserRepository
import br.com.tick.ui.screens.history.models.GroupedExpense
import br.com.tick.ui.screens.history.models.HistoryChartEntry
import br.com.tick.ui.screens.history.models.MonthlyGroupedExpenses
import br.com.tick.ui.screens.history.models.MonthlyMergedExpense
import br.com.tick.ui.screens.history.states.ExpensesHistoryGraphStates
import br.com.tick.ui.screens.history.states.ExpensesHistoryStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryScreenViewModel @Inject constructor(
    private val categorizedExpenseRepository: CategorizedExpenseRepository,
    private val userRepository: UserRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private var _chosenMonths = MutableStateFlow<List<String>>(listOf())
    val chosenMonths = _chosenMonths.asStateFlow()

    private val _expensesHistoryState = MutableStateFlow<ExpensesHistoryStates>(ExpensesHistoryStates.Empty)
    val expensesHistoryStates = _expensesHistoryState.asStateFlow()

    private val _expensesHistoryGraphState =
        MutableStateFlow<ExpensesHistoryGraphStates>(ExpensesHistoryGraphStates.Empty)
    val expensesHistoryGraphStates = _expensesHistoryGraphState.asStateFlow()

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

    // All logic related to these months are not taking under consideration previous months.
    // This needs attention and should be taken care of.
    fun chosenMonths(months: List<String>) {
        viewModelScope.launch {
            _chosenMonths.emit(months)
            fetchExpenses()
            fetchMergedExpenses()
        }
    }

    private fun fetchExpenses() {
        viewModelScope.launch(dispatcherProvider.io()) {
            val currency = userRepository.getUser().first().currency

            categorizedExpenseRepository.getCategorizedExpenses().collect { _categorizedExpense ->
                val filteredCategorizedExpenses = _categorizedExpense
                    .sortedByDescending { it.date }
                    .filter { _chosenMonths.value.contains(it.toChipLabel()) }
                    .groupBy { it.date.month }
                    .map { (month, categorizedExpenses) ->
                        val groupedExpenses = categorizedExpenses.map {
                            GroupedExpense(
                                it.date,
                                it.name,
                                it.expenseValue,
                                it.category.name,
                                it.category.color,
                                currency
                            )
                        }

                        val isExpanded = when (val currentState = _expensesHistoryState.value) {
                            is ExpensesHistoryStates.Content -> {
                                currentState.expenseHistory.find { it.month == month.name }?.isExpanded ?: true
                            }

                            else -> true
                        }


                        MonthlyGroupedExpenses(month.name, groupedExpenses, isExpanded)
                    }

                val state = if (filteredCategorizedExpenses.isEmpty()) {
                    ExpensesHistoryStates.Empty
                } else {
                    ExpensesHistoryStates.Content(filteredCategorizedExpenses)
                }
                _expensesHistoryState.emit(state)
            }
        }
    }

    private fun fetchMergedExpenses() {
        viewModelScope.launch(dispatcherProvider.io()) {
            categorizedExpenseRepository.getCategorizedExpenses().collect { categorizedExpense ->
                val filteredCategorizedExpenses = categorizedExpense
                    .asSequence()
                    .filter { _chosenMonths.value.contains(it.toChipLabel()) }
                    .sortedByDescending { it.date }
                    .groupBy { it.date.month }
                    .map { (month, expensesOfThisMonth) ->
                        val mergedValues = expensesOfThisMonth
                            .groupBy { expenses -> expenses.date.dayOfMonth }
                            .map { (day, dailyExpenses) ->
                                HistoryChartEntry(day.toFloat(), dailyExpenses.sumOf { it.expenseValue }.toFloat())
                            }
                            .sortedBy { it.x }
                        MonthlyMergedExpense(month, mergedValues)
                    }

                val state = if (filteredCategorizedExpenses.isEmpty()) {
                    ExpensesHistoryGraphStates.Empty
                } else {
                    ExpensesHistoryGraphStates.Content(filteredCategorizedExpenses)
                }
                _expensesHistoryGraphState.emit(state)
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
