package com.example.financemanager.data.useCases

import com.example.financemanager.DateUtils.asLocalDate
import com.example.financemanager.DateUtils.getCurrentLocalDate
import com.example.financemanager.data.models.Account
import com.example.financemanager.data.models.TransactionView
import com.example.financemanager.data.repositories.TransactionsRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class GetTransactionViewsUseCase(private val repository: TransactionsRepository) {

    operator fun invoke(
        dateRange: Pair<LocalDate?, LocalDate?>,
        account: Account?
    ): Flow<List<TransactionView>> {
        val minDate: LocalDate = dateRange.first ?: "2000-01-01".asLocalDate()
        val maxDate: LocalDate = dateRange.second ?: getCurrentLocalDate()
        return if (account == null)
            repository.getTransactionViews(minDate, maxDate)
        else {
            repository.getTransactionsViewsForAccount(minDate, maxDate, account.id ?: 0)
        }
    }

}