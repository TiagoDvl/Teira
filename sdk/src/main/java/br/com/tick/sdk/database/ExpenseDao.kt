package br.com.tick.sdk.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.tick.sdk.database.entities.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert
    suspend fun addExpense(expense: Expense)

    @Update
    suspend fun updateExpense(expense: Expense)

    @Query("DELETE FROM expense WHERE expense_id = :expenseId")
    suspend fun removeExpenseById(expenseId: Int)

    @Query("SELECT * FROM expense ORDER BY expense_id")
    fun getExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM expense")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM expense WHERE expense_id = :expenseId")
    fun getExpense(expenseId: Int): Flow<Expense>
}
