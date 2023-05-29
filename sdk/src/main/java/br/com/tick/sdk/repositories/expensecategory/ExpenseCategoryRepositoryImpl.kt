package br.com.tick.sdk.repositories.expensecategory

import br.com.tick.sdk.database.CategoryColorDao
import br.com.tick.sdk.database.CategoryDao
import br.com.tick.sdk.database.entities.Category
import br.com.tick.sdk.domain.ExpenseCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ExpenseCategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val categoryColorDao: CategoryColorDao,
) : ExpenseCategoryRepository {

    override suspend fun addCategory(categoryName: String, color: Int?) {
        val persistedCategoriesColors = categoryColorDao.getCategoriesColors().first().firstOrNull { it.color == color }
        categoryDao.addCategory(Category(name = categoryName, categoryColorId = persistedCategoriesColors?.id))
    }

    override fun getCategories(): Flow<List<ExpenseCategory>> {
        return categoryDao.getCategories().combine(categoryColorDao.getCategoriesColors()) { categories, colors ->
            categories.map { category ->
                if (category.categoryColorId != null) {
                    val categoryColor = colors.first { it.id == category.categoryColorId }
                    ExpenseCategory(category.categoryId, category.name, categoryColor.color)
                } else {
                    ExpenseCategory(category.categoryId, category.name, null)
                }
            }
        }
    }
}
