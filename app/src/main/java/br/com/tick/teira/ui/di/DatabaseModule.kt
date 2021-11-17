package br.com.tick.teira.ui.di

import br.com.tick.teira.ui.datasource.databases.ExpenseDao
import br.com.tick.teira.ui.datasource.databases.ExpenseDaoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {

    @Singleton
    @Binds
    abstract fun bindExpenseDao(expenseDaoImpl: ExpenseDaoImpl): ExpenseDao
}