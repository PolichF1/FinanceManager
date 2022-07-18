package com.example.financemanager.data.useCases

import com.example.financemanager.DateUtils.asLocalDate
import com.example.financemanager.DateUtils.getCurrentLocalDate
import com.example.financemanager.data.models.Account
import com.example.financemanager.data.models.TransactionView
import com.example.financemanager.data.repositories.TransactionsRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDate

class GetTransactionsWithDayInfoUseCase(private val repository: TransactionsRepository) {

    suspend operator fun invoke(
        transactions: List<TransactionView>,
        dateRange: Pair<LocalDate?, LocalDate?>,
        account: Account?
    ): List<Any> {

        val minDate: LocalDate = dateRange.first ?: "2000-01-01".asLocalDate()
        val maxDate: LocalDate = dateRange.second ?: getCurrentLocalDate()

        val result = mutableListOf<Any>()

        val amountsPerDay = if (account == null)
            repository.getTransactionAmountsPerDay(minDate, maxDate).first()
        else {
            repository.getTransactionAmountPerDayForAccount(minDate, maxDate, account.id ?: 0).first()
        }

        if (amountsPerDay.isNotEmpty()) {
            var i = 0
            for (it in transactions) {
                result.add(it)

                if (it.date != amountsPerDay[i].transactionDate) {
                    result.add(result.size - 1, amountsPerDay[i])
                    i++
                }
            }
            result.add(amountsPerDay[i])
        }
        return result.reversed()
    }

}