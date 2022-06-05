package br.com.tick.sdk.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.tick.sdk.database.entities.Category
import br.com.tick.sdk.database.entities.Expense

@Database(entities = [Expense::class, Category::class], version = 4)
abstract class TeiraDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao

    abstract fun categoryDao(): CategoryDao
}
