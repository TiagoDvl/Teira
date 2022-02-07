package br.com.tick.teira.ui.datasource.databases

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.tick.teira.ui.datasource.databases.entities.Category
import br.com.tick.teira.ui.datasource.databases.entities.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert
    suspend fun addExpense(expense: Expense)

    @Query("DELETE FROM expense WHERE expense_id = :expenseId")
    suspend fun removeExpenseById(expenseId: Int)

    @Query("SELECT * FROM expense ORDER BY expense_id DESC LIMIT :numberOfExpenses")
    fun getExpenses(numberOfExpenses: Int): Flow<List<Expense>>

    @Insert
    suspend fun addCategory(category: Category): Long

    @Query("SELECT * FROM category")
    fun getCategories(): Flow<List<Category>>

    @Query("SELECT * FROM category WHERE category_id = :categoryId")
    suspend fun getCategoryById(categoryId: Int): Category
}
