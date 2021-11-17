package br.com.tick.teira.ui.screens.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.shareIn

class QuickExpenseBarViewModel: ViewModel() {

    private val expenses: MutableList<String> = mutableListOf()
    val expensesExposedPleaseDelete = expenses.asFlow().shareIn(viewModelScope, SharingStarted.Eagerly)

    fun saveQuickExpense(name: String, value: String, category: String) {
        expenses.add(name)
        Log.d("Tiago", expenses.toString())
    }
}