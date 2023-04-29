package br.com.tick.sdk.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.tick.sdk.database.entities.Category
import br.com.tick.sdk.database.entities.Expense
import br.com.tick.sdk.database.entities.User

@Database(
    entities = [
        User::class,
        Expense::class,
        Category::class
    ],
    version = 4,
    exportSchema = false
)
abstract class TeiraDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun expenseDao(): ExpenseDao

    abstract fun categoryDao(): CategoryDao
}
