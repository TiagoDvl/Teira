package br.com.tick.ui.screens.expense.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tick.sdk.dispatchers.DispatcherProvider
import br.com.tick.sdk.domain.CategorizedExpense
import br.com.tick.sdk.domain.CurrencyFormat
import br.com.tick.sdk.repositories.categorizedexpense.CategorizedExpenseRepository
import br.com.tick.sdk.repositories.expensecategory.ExpenseCategoryRepository
import br.com.tick.sdk.repositories.user.UserRepository
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    categoryRepository: ExpenseCategoryRepository,
    userRepository: UserRepository,
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

    val currency = userRepository.getUser()
        .flowOn(dispatcherProvider.io())
        .map { it.currency }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CurrencyFormat.EURO
        )

    fun getExpense(expenseId: Int) {
        Log.d("Tiago", "ExpenseID $expenseId")
        viewModelScope.launch(dispatcherProvider.io()) {
            categorizedExpenseRepository
                .getCategorizedExpense(expenseId = expenseId)
                .collect {
                    _categorizedExpense.emit(it)
                }
        }
    }

    fun handleExpense(
        expenseId: Int? = null,
        categoryId: Int,
        name: String,
        value: Double,
        expenseDate: LocalDate,
        location: LatLng?,
        photoUri: Uri?
    ) {
        viewModelScope.launch(dispatcherProvider.io()) {
            if (expenseId != null) {
                saveExpense(expenseId, categoryId, name, value, expenseDate, location, photoUri)
            } else {
                addExpense(categoryId, name, value, expenseDate, location, photoUri)
            }
        }
    }

    private suspend fun saveExpense(
        expenseId: Int,
        categoryId: Int,
        name: String,
        value: Double,
        expenseDate: LocalDate,
        location: LatLng?,
        photoUri: Uri?
    ) {
        categorizedExpenseRepository.updateExpense(expenseId, categoryId, name, value, expenseDate, location, photoUri)
    }

    private suspend fun addExpense(
        categoryId: Int,
        name: String,
        value: Double,
        expenseDate: LocalDate,
        location: LatLng?,
        photoUri: Uri?
    ) {
        categorizedExpenseRepository.addExpense(categoryId, name, value, expenseDate, location, photoUri)
    }

    fun removeExpense(expenseId: Int) {
        viewModelScope.launch(dispatcherProvider.io()) {
            categorizedExpenseRepository.removeExpense(expenseId)
        }
    }
}
