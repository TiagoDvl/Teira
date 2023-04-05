package br.com.tick.ui.screens.wallet.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tick.sdk.dispatchers.DispatcherProvider
import br.com.tick.sdk.repositories.categorizedexpense.CategorizedExpenseRepository
import br.com.tick.sdk.repositories.expensecategory.ExpenseCategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class QuickExpenseBarViewModel @Inject constructor(
    private val expenseRepository: CategorizedExpenseRepository,
    private val categoryRepository: ExpenseCategoryRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    val categories = categoryRepository.getCategories()
        .flowOn(dispatcherProvider.io())
        .map {
            it.map { expenseCategory ->
                expenseCategory.name
            }
        }

    fun saveQuickExpense(categoryId: Int, name: String, value: Double, expenseDate: LocalDate) {
        viewModelScope.launch(dispatcherProvider.io()) {
            expenseRepository.addExpense(categoryId, name, value, expenseDate)
        }
    }

    fun addCategory(categoryName: String) {
        viewModelScope.launch(dispatcherProvider.io()) {
            categoryRepository.addCategory(categoryName)
        }
    }
}
