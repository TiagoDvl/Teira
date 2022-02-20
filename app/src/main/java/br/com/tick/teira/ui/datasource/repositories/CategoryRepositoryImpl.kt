package br.com.tick.teira.ui.datasource.repositories

import br.com.tick.teira.ui.datasource.databases.CategoryDao
import br.com.tick.teira.ui.datasource.databases.entities.Category
import br.com.tick.teira.ui.datasource.domain.ExpenseCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(private val categoryDao: CategoryDao) : CategoryRepository {

    override suspend fun addCategory(categoryName: String): Boolean {
        return categoryDao.addCategory(Category(name = categoryName)) > 0
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
