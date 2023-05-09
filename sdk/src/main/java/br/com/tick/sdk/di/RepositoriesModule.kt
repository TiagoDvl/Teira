package br.com.tick.sdk.di

import br.com.tick.sdk.repositories.categorizedexpense.CategorizedExpenseRepository
import br.com.tick.sdk.repositories.categorizedexpense.CategorizedExpensesRepositoryImpl
import br.com.tick.sdk.repositories.categorycolor.CategoryColorRepository
import br.com.tick.sdk.repositories.categorycolor.CategoryColorRepositoryImpl
import br.com.tick.sdk.repositories.expensecategory.ExpenseCategoryRepository
import br.com.tick.sdk.repositories.expensecategory.ExpenseCategoryRepositoryImpl
import br.com.tick.sdk.repositories.localdata.DataStoreRepository
import br.com.tick.sdk.repositories.localdata.LocalDataRepository
import br.com.tick.sdk.repositories.user.UserRepository
import br.com.tick.sdk.repositories.user.UserRepositoryImpl
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
    abstract fun bindCategoryColorRepository(
        categoryColorRepositoryImpl: CategoryColorRepositoryImpl
    ): CategoryColorRepository

    @Binds
    abstract fun bindLocalDataRepository(dataStoreRepository: DataStoreRepository): LocalDataRepository

    @Binds
    abstract fun bindUserRepository(userRepository: UserRepositoryImpl): UserRepository
}
