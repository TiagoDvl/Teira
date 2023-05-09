package br.com.tick.sdk.repositories.categorycolor

import android.graphics.Color
import br.com.tick.sdk.database.CategoryColorDao
import br.com.tick.sdk.database.entities.CategoryColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryColorRepositoryImpl(
    private val categoryColorDao: CategoryColorDao
): CategoryColorRepository {

    override suspend fun addColor(color: Color) {
        categoryColorDao.addCategoryColor(CategoryColor(color = color.pack()))
    }

    override fun getColors(): Flow<List<Color>> {
        return categoryColorDao.getCategoriesColors().map {
            it.map { categoryColor ->
                Color.valueOf(categoryColor.color)
            }
        }
    }
}