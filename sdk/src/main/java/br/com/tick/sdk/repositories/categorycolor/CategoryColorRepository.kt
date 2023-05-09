package br.com.tick.sdk.repositories.categorycolor

import android.graphics.Color
import kotlinx.coroutines.flow.Flow

interface CategoryColorRepository {

    suspend fun addColor(color: Color)

    fun getColors(): Flow<List<Color>>
}