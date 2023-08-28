package br.com.tick.ui.screens.expense.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tick.sdk.dispatchers.DispatcherProvider
import br.com.tick.sdk.domain.CategorizedExpense
import br.com.tick.sdk.repositories.categorizedexpense.CategorizedExpenseRepository
import br.com.tick.sdk.repositories.expensecategory.ExpenseCategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    categoryRepository: ExpenseCategoryRepository,
    private val categorizedExpenseRepository: CategorizedExpenseRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _categorizedExpense = MutableStateFlow<CategorizedExpense?>(null)
    val categorizedExpense = _categorizedExpense.asStateFlow()

    val categories = categoryRepository.getCategories()
        .flowOn(dispatcherProvider.io())
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = listOf()
        )

    fun getExpense(expenseId: Int) {
        viewModelScope.launch(dispatcherProvider.io()) {
            categorizedExpenseRepository
                .getCategorizedExpense(expenseId = expenseId)
                .collect {
                    _categorizedExpense.emit(it)
                }
        }
    }
}