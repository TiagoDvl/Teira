package br.com.tick.sdk.repositories.categorycolor

import br.com.tick.sdk.database.CategoryColorDao
import br.com.tick.sdk.database.entities.CategoryColor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryColorRepositoryImpl @Inject constructor(
    private val categoryColorDao: CategoryColorDao
): CategoryColorRepository {

    override suspend fun addColor(color: Int) {
        categoryColorDao.addCategoryColor(CategoryColor(color = color))
    }

    override fun getColors(): Flow<List<CategoryColor>> = categoryColorDao.getCategoriesColors()
}