package com.example.financemanager.dataBase.dao

import androidx.room.*
import com.example.financemanager.data.models.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriesDao {

    @Query("SELECT * FROM 'category'")
    fun getCategories(): Flow<List<Category>>

    @Query("SELECT * FROM 'category' WHERE id = :id")
    suspend fun getCategoryById(id: Int): Category?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)
}