package br.com.tick.teira.ui.datasource.databases

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.tick.teira.ui.datasource.databases.entities.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert
    suspend fun addCategory(category: Category): Long

    @Query("SELECT * FROM category")
    fun getCategories(): Flow<List<Category>>

    @Query("SELECT * FROM category WHERE category_id = :categoryId")
    suspend fun getCategoryById(categoryId: Int): Category
}
