package br.com.tick.sdk.di

import android.content.Context
import androidx.room.Room
import br.com.tick.sdk.database.CategoryColorDao
import br.com.tick.sdk.database.CategoryDao
import br.com.tick.sdk.database.ExpenseDao
import br.com.tick.sdk.database.MIGRATION_5_6
import br.com.tick.sdk.database.TeiraDatabase
import br.com.tick.sdk.database.UserDao
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
    ): TeiraDatabase {
        return Room.databaseBuilder(
            app,
            TeiraDatabase::class.java, "TeiraDb.db"
        ).addMigrations(MIGRATION_5_6).build()
    }

    @Singleton
    @Provides
    fun provideUserDao(teiraDatabase: TeiraDatabase): UserDao = teiraDatabase.userDao()

    @Singleton
    @Provides
    fun provideExpenseDao(teiraDatabase: TeiraDatabase): ExpenseDao = teiraDatabase.expenseDao()

    @Singleton
    @Provides
    fun provideCategoryDao(teiraDatabase: TeiraDatabase): CategoryDao = teiraDatabase.categoryDao()

    @Singleton
    @Provides
    fun provideCategoryColorDao(teiraDatabase: TeiraDatabase): CategoryColorDao = teiraDatabase.categoryColorDao()
}
