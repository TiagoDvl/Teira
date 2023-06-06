package br.com.tick.sdk.repositories

import br.com.tick.sdk.database.entities.CategoryColor
import br.com.tick.sdk.repositories.categorycolor.CategoryColorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeCategoryColorRepository : CategoryColorRepository {

    private val categoryColors = mutableListOf<CategoryColor>()
    private val categoryColorsFlow = MutableSharedFlow<List<CategoryColor>>(replay = 1)

    override suspend fun addColor(color: Int) {
        categoryColors.add(CategoryColor(color = color))
        categoryColorsFlow.emit(categoryColors)
    }

    override fun getColors(): Flow<List<CategoryColor>> {
        return categoryColorsFlow
    }
}
