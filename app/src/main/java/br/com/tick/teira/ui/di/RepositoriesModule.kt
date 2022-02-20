package br.com.tick.teira.ui.di

import br.com.tick.teira.ui.datasource.repositories.CategoryRepository
import br.com.tick.teira.ui.datasource.repositories.CategoryRepositoryImpl
import br.com.tick.teira.ui.datasource.repositories.DataStoreRepository
import br.com.tick.teira.ui.datasource.repositories.ExpenseRepository
import br.com.tick.teira.ui.datasource.repositories.ExpensesRepositoryImpl
import br.com.tick.teira.ui.datasource.repositories.LocalDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {

    @Binds
    abstract fun bindExpensesRepository(expensesRepositoryImpl: ExpensesRepositoryImpl): ExpenseRepository

    @Binds
    abstract fun bindCategoryRepository(categoryRepositoryImpl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    abstract fun bindLocalDataRepository(dataStoreRepository: DataStoreRepository): LocalDataRepository
}
