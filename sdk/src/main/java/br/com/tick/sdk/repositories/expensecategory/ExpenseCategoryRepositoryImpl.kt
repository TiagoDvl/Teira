package br.com.tick.sdk.repositories.expensecategory

import br.com.tick.sdk.database.CategoryColorDao
import br.com.tick.sdk.database.CategoryDao
import br.com.tick.sdk.database.entities.Category
import br.com.tick.sdk.database.entities.CategoryColor
import br.com.tick.sdk.domain.ExpenseCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ExpenseCategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val categoryColorDao: CategoryColorDao
) : ExpenseCategoryRepository {

    override suspend fun addExpenseCategory(categoryName: String, color: Int?) {
        val persistedCategoriesColors = categoryColorDao.getCategoryColors().first().firstOrNull { it.color == color }
        categoryDao.addCategory(Category(name = categoryName, categoryColorId = persistedCategoriesColors?.id))
    }

    override suspend fun editExpenseCategory(
        expenseCategoryId: Int,
        updatedCategoryName: String,
        updatedCategoryColor: Int
    ) {
        val category = categoryDao.getCategories().first().find { it.categoryId == expenseCategoryId }
        var categoryColor = categoryColorDao.getCategoryColors().first().find { it.color == updatedCategoryColor }

        if (categoryColor == null) {
            categoryColorDao.addCategoryColor(CategoryColor(color = updatedCategoryColor))
            categoryColor = categoryColorDao.getCategoryColors().first().find { it.color == updatedCategoryColor }
        }

        if (category != null && categoryColor != null) {
            categoryDao.updateCategory(category.copy(name = updatedCategoryName, categoryColorId = categoryColor.id))
        }
    }

    override fun getCategories(): Flow<List<ExpenseCategory>> {
        return categoryDao.getCategories().combine(categoryColorDao.getCategoryColors()) { categories, colors ->
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
