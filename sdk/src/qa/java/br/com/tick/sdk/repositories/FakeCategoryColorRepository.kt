package br.com.tick.sdk.repositories

import br.com.tick.sdk.database.entities.CategoryColor
import br.com.tick.sdk.repositories.categorycolor.CategoryColorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeCategoryColorRepository : CategoryColorRepository {

    private val categoryColors = mutableListOf<CategoryColor>()

    override suspend fun addColor(color: Int) {
        categoryColors.add(CategoryColor(color = color))
    }

    override fun getColors(): Flow<List<CategoryColor>> {
        return flowOf(categoryColors)
    }
}
