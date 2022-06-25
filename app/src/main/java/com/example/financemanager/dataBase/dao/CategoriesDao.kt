package com.example.financemanager.dataBase.dao

import androidx.room.*
import com.example.financemanager.data.models.Category
import com.example.financemanager.data.models.CategoryView
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriesDao {

//    @Query("SELECT * FROM 'categories'")
//    fun getCategories(): Flow<List<Category>>

    @Query("SELECT * FROM 'categories' WHERE id = :id")
    suspend fun getCategoryById(id: Int): Category?

    @Query("SELECT categories.id, categories.name, categories.icon, categories.icon_color, IFNULL(SUM(transactions.amount), 0) AS category_amount FROM categories LEFT JOIN transactions ON transactions.category_id = categories.id GROUP BY categories.id")
    fun getCategoryViews(): Flow<List<CategoryView>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)
}