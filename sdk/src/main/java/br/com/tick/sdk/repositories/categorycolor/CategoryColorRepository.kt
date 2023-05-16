package br.com.tick.sdk.repositories.categorycolor

import br.com.tick.sdk.database.entities.CategoryColor
import kotlinx.coroutines.flow.Flow

interface CategoryColorRepository {

    suspend fun addColor(color: Int)

    fun getColors(): Flow<List<CategoryColor>>
}