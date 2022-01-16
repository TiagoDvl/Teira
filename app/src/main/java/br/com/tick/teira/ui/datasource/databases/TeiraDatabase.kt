package br.com.tick.teira.ui.datasource.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.tick.teira.ui.datasource.databases.entities.Expense

@Database(entities = [Expense::class], version = 3)
abstract class TeiraDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao

}
