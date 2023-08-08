package br.com.tick.ui.screens.shared.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tick.sdk.dispatchers.DispatcherProvider
import br.com.tick.sdk.repositories.categorycolor.CategoryColorRepository
import br.com.tick.sdk.repositories.expensecategory.ExpenseCategoryRepository
import br.com.tick.ui.screens.wallet.usecases.GetCategoryColors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryDialogViewModel @Inject constructor(
    private val expenseCategoryRepository: ExpenseCategoryRepository,
    private val categoryColorRepository: CategoryColorRepository,
    getCategoryColors: GetCategoryColors,
    private val dispatcherProvider: DispatcherProvider
): ViewModel() {


    val categoryColors = getCategoryColors()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = listOf()
        )

    fun addCategory(categoryName: String, color: Int?) {
        viewModelScope.launch(dispatcherProvider.io()) {
            expenseCategoryRepository.addExpenseCategory(categoryName, color)
        }
    }

    fun editExpenseCategory(expenseCategoryId: Int, updatedCategoryName: String, updatedCategoryColor: Int) {
        viewModelScope.launch(dispatcherProvider.io()) {
            expenseCategoryRepository.editExpenseCategory(expenseCategoryId, updatedCategoryName, updatedCategoryColor)
        }
    }

    fun addNewColor(color: Int) {
        viewModelScope.launch(dispatcherProvider.io()) {
            categoryColorRepository.addColor(color)
        }
    }
}
