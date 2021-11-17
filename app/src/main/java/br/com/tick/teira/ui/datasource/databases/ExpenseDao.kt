package br.com.tick.teira.ui.datasource.databases

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.tick.teira.ui.datasource.databases.entities.Expense

@Dao
interface ExpenseDao {

    @Insert
    suspend fun addExpense(expense: Expense)

    @Query("SELECT * FROM expense")
    suspend fun getExpenses(): List<Expense>
}