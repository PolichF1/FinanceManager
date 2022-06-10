package com.example.financemanager.dataBase.repositories

import com.example.financemanager.dataBase.models.Category
import kotlinx.coroutines.flow.Flow

interface CategoriesRepository {

    fun getCategories(): Flow<List<Category>>

    suspend fun getCategoryById(id: Int): Category?

    suspend fun insertCategory(category: Category)

    suspend fun deleteCategory(category: Category)
}