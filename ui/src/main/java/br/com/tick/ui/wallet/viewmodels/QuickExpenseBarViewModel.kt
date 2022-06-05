package br.com.tick.ui.wallet.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tick.sdk.domain.ExpenseCategory
import br.com.tick.sdk.repositories.CategoryRepository
import br.com.tick.sdk.repositories.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuickExpenseBarViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    val categories: Flow<List<ExpenseCategory>>
        get() = flow {
            categoryRepository.getCategories().collect {
                emit(it)
            }
        }

    fun saveQuickExpense(categoryId: Int, name: String, value: Double, expenseDate: Long) {
        viewModelScope.launch {
            expenseRepository.addExpense(categoryId, name, value, expenseDate)
        }
    }

    fun addCategory(categoryName: String) {
        viewModelScope.launch {
            categoryRepository.addCategory(categoryName)
        }
    }
}
