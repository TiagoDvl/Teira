package br.com.tick.teira.ui.di

import br.com.tick.teira.ui.datasource.repositories.ExpenseRepository
import br.com.tick.teira.ui.datasource.repositories.ExpensesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {

    @Binds
    abstract fun bindExpensesRepository(expensesRepositoryImpl: ExpensesRepositoryImpl): ExpenseRepository
}