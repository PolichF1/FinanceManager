package com.example.financemanager.data.useCases

import com.example.financemanager.DateUtils.asLocalDate
import com.example.financemanager.DateUtils.getCurrentLocalDate
import com.example.financemanager.data.models.Account
import com.example.financemanager.data.models.CategoryView
import com.example.financemanager.data.repositories.CategoriesRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

data class CategoryUseCases(
    val getCategoryViews: GetCategoryViewsUseCase
)

class GetCategoryViewsUseCase(private val repository: CategoriesRepository) {
    operator fun invoke(
        dateRange: Pair<LocalDate?, LocalDate?>,
        account: Account?
    ): Flow<List<CategoryView>> {
        val minDate: LocalDate = dateRange.first ?: "2000-01-01".asLocalDate()
        val maxDate: LocalDate = dateRange.second ?: getCurrentLocalDate()

        return if (account == null)
            repository.getCategoryViews(minDate, maxDate)
        else
            repository.getCategoryViewsForAccount(minDate, maxDate, account.id ?: 0)
    }
}






