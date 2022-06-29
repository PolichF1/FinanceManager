package com.example.financemanager.data.useCases

import com.example.financemanager.DateUtils.asLocalDate
import com.example.financemanager.DateUtils.getCurrentLocalDate
import com.example.financemanager.data.models.CategoryView
import com.example.financemanager.data.repositories.CategoriesRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

data class CategoryUseCases(
    val getCategoryViews: GetCategoryViews,
)


class GetCategoryViews(private val repository: CategoriesRepository) {
    operator fun invoke(from: LocalDate?, to: LocalDate?): Flow<List<CategoryView>> {

        val minDate: LocalDate = from ?: "2000-01-01".asLocalDate()
        val maxDate: LocalDate = to ?: getCurrentLocalDate()


        return repository.getCategoryViews(minDate, maxDate)
    }
}






