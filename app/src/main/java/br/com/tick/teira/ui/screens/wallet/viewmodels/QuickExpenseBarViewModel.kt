package br.com.tick.teira.ui.screens.wallet.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tick.teira.ui.datasource.repositories.CategoryRepository
import br.com.tick.teira.ui.datasource.repositories.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuickExpenseBarViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    val categories = flow {
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
