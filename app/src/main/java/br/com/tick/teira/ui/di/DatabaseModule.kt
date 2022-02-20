package br.com.tick.teira.ui.di

import android.content.Context
import androidx.room.Room
import br.com.tick.teira.ui.datasource.databases.CategoryDao
import br.com.tick.teira.ui.datasource.databases.ExpenseDao
import br.com.tick.teira.ui.datasource.databases.TeiraDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideTeiraDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(app, TeiraDatabase::class.java, "TeiraDb.db").fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideExpenseDao(teiraDatabase: TeiraDatabase): ExpenseDao = teiraDatabase.expenseDao()

    @Singleton
    @Provides
    fun provideCategoryDao(teiraDatabase: TeiraDatabase): CategoryDao = teiraDatabase.categoryDao()
}
