package br.com.tick.sdk.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.tick.sdk.database.entities.CategoryColor
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryColorDao {

    @Insert
    suspend fun addCategoryColor(categoryColor: CategoryColor)

    @Query("SELECT * FROM CategoryColor")
    fun getCategoryColors(): Flow<List<CategoryColor>>
}
