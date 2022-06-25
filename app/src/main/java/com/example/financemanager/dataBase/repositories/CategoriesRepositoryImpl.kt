package com.example.financemanager.dataBase.repositories

import com.example.financemanager.data.models.Category
import com.example.financemanager.data.models.CategoryView
import com.example.financemanager.data.repositories.CategoriesRepository
import com.example.financemanager.dataBase.dao.CategoriesDao
import kotlinx.coroutines.flow.Flow

class CategoriesRepositoryImpl(
    private val dao: CategoriesDao
) : CategoriesRepository {

//    override fun getCategories(): Flow<List<Category>> {
//        return dao.getCategories()
//    }

    override suspend fun getCategoryById(id: Int): Category? {
        return dao.getCategoryById(id)
    }

    override fun getCategoryViews(): Flow<List<CategoryView>> {
        return dao.getCategoryViews()
    }

    override suspend fun insertCategory(category: Category) {
        return dao.insertCategory(category)
    }

    override suspend fun updateCategory(category: Category) {
        return dao.updateCategory(category)
    }

    override suspend fun deleteCategory(category: Category) {
        return dao.deleteCategory(category)
    }
}