package br.com.tick.ui.di

import br.com.tick.sdk.repositories.CategorizedExpenseRepository
import br.com.tick.sdk.repositories.ExpenseCategoryRepository
import br.com.tick.sdk.repositories.LocalDataRepository
import br.com.tick.ui.repositories.CategorizedExpensesRepositoryImpl
import br.com.tick.ui.repositories.DataStoreRepository
import br.com.tick.ui.repositories.ExpenseCategoryRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {

    @Binds
    abstract fun bindCategorizedExpensesRepository(
        categorizedExpensesRepositoryImpl: CategorizedExpensesRepositoryImpl
    ): CategorizedExpenseRepository

    @Binds
    abstract fun bindExpenseCategoryRepository(
        expenseCategoryRepositoryImpl: ExpenseCategoryRepositoryImpl
    ): ExpenseCategoryRepository

    @Binds
    abstract fun bindLocalDataRepository(dataStoreRepository: DataStoreRepository): LocalDataRepository
}
