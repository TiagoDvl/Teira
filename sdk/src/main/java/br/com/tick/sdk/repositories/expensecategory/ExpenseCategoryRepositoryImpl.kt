package br.com.tick.sdk.repositories.expensecategory

import br.com.tick.sdk.database.CategoryDao
import br.com.tick.sdk.database.entities.Category
import br.com.tick.sdk.domain.ExpenseCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ExpenseCategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : ExpenseCategoryRepository {

    override suspend fun addCategory(categoryName: String) {
        categoryDao.addCategory(Category(name = categoryName))
    }

    override fun getCategories(): Flow<List<ExpenseCategory>> = categoryDao.getCategories().map {
        it.map { category -> ExpenseCategory(category.categoryId, category.name) }
    }
}
