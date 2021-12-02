package br.com.tick.teira.ui.datasource.databases

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.tick.teira.ui.datasource.databases.entities.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert
    suspend fun addExpense(expense: Expense)

    @Query("DELETE FROM expense WHERE uid = :expenseId")
    suspend fun removeExpenseById(expenseId: Int)

    @Query("SELECT * FROM expense ORDER BY uid DESC LIMIT :numberOfExpenses")
    fun getExpenses(numberOfExpenses: Int): Flow<List<Expense>>
}
