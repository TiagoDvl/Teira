package br.com.tick.ui.screens.wallet.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tick.sdk.dispatchers.DispatcherProvider
import br.com.tick.sdk.repositories.categorizedexpense.CategorizedExpenseRepository
import br.com.tick.sdk.repositories.categorycolor.CategoryColorRepository
import br.com.tick.sdk.repositories.expensecategory.ExpenseCategoryRepository
import br.com.tick.sdk.repositories.user.UserRepository
import br.com.tick.ui.screens.wallet.usecases.GetCategoryColors
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
    private val categoryColorRepository: CategoryColorRepository,
    getCategoryColors: GetCategoryColors,
    userRepository: UserRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    val categories = categoryRepository.getCategories().flowOn(dispatcherProvider.io())
    val categoryColors = getCategoryColors().flowOn(dispatcherProvider.io())
    val currency = userRepository.getUser().flowOn(dispatcherProvider.io()).map { it.currency }

    fun saveQuickExpense(categoryId: Int, name: String, value: Double, expenseDate: LocalDate) {
        viewModelScope.launch(dispatcherProvider.io()) {
            expenseRepository.addExpense(categoryId, name, value, expenseDate)
        }
    }

    fun addCategory(categoryName: String, color: Int?) {
        viewModelScope.launch(dispatcherProvider.io()) {
            categoryRepository.addCategory(categoryName, color)
        }
    }

    fun addNewColor(color: Int) {
        viewModelScope.launch(dispatcherProvider.io()) {
            categoryColorRepository.addColor(color)
        }
    }
}
