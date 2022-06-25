package com.example.financemanager.data.repositories

import com.example.financemanager.data.models.Category
import com.example.financemanager.data.models.CategoryView
import kotlinx.coroutines.flow.Flow

interface CategoriesRepository {

//    fun getCategories(): Flow<List<Category>>

    suspend fun getCategoryById(id: Int): Category?

    fun getCategoryViews(): Flow<List<CategoryView>>

    suspend fun insertCategory(category: Category)

    suspend fun updateCategory(category: Category)

    suspend fun deleteCategory(category: Category)
}