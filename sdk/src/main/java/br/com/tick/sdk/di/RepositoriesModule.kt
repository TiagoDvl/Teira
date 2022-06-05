package br.com.tick.sdk.di

import br.com.tick.sdk.repositories.CategoryRepository
import br.com.tick.sdk.repositories.CategoryRepositoryImpl
import br.com.tick.sdk.repositories.DataStoreRepository
import br.com.tick.sdk.repositories.ExpenseRepository
import br.com.tick.sdk.repositories.ExpensesRepositoryImpl
import br.com.tick.sdk.repositories.LocalDataRepository
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
