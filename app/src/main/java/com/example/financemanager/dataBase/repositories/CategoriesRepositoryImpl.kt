package com.example.financemanager.dataBase.repositories

import com.example.financemanager.data.models.CategoryView
import com.example.financemanager.data.repositories.CategoriesRepository
import com.example.financemanager.dataBase.dao.CategoriesDao
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class CategoriesRepositoryImpl(
    private val dao: CategoriesDao
) : CategoriesRepository {

    override fun getCategoryViewsForAccount(
        from: LocalDate,
        to: LocalDate,
        id: Int
    ): Flow<List<CategoryView>> {
        return dao.getCategoryViewsForAccount(from, to, id)
    }

    override fun getCategoryViews(from: LocalDate, to: LocalDate): Flow<List<CategoryView>> {
        return dao.getCategoryViews(from, to)
    }

}