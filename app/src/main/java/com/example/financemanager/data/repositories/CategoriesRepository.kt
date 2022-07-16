package com.example.financemanager.data.repositories

import com.example.financemanager.data.models.Category
import com.example.financemanager.data.models.CategoryView
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface CategoriesRepository {

    fun getCategoryViewsForAccount(from: LocalDate, to: LocalDate, id: Int): Flow<List<CategoryView>>

    fun getCategoryViews(from: LocalDate, to: LocalDate): Flow<List<CategoryView>>
}