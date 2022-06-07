package br.com.tick.ui.repositories

import br.com.tick.sdk.database.CategoryDao
import br.com.tick.sdk.database.entities.Category
import br.com.tick.sdk.domain.ExpenseCategory
import br.com.tick.sdk.repositories.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(private val categoryDao: CategoryDao) : CategoryRepository {

    override suspend fun addCategory(categoryName: String) {
        categoryDao.addCategory(Category(name = categoryName))
    }

    override suspend fun getCategories(): Flow<List<ExpenseCategory>> = categoryDao.getCategories().map {
        it.map { category ->
            ExpenseCategory(category.categoryId, category.name)
        }
    }

    override suspend fun getCategoryById(categoryId: Int): ExpenseCategory {
        val category = categoryDao.getCategoryById(categoryId)
        return ExpenseCategory(category.categoryId, category.name)
    }
}
