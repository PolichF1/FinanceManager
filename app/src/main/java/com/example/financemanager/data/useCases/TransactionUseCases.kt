package com.example.financemanager.data.useCases

import com.example.financemanager.DateUtils.asLocalDate
import com.example.financemanager.DateUtils.getCurrentLocalDate
import com.example.financemanager.data.models.Account
import com.example.financemanager.data.models.Transaction
import com.example.financemanager.data.models.TransactionView
import com.example.financemanager.data.repositories.AccountsRepository
import com.example.financemanager.data.repositories.TransactionsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.time.LocalDate

data class TransactionUseCases(
    val getTransactionViews: GetTransactionViews,
    val getTransactionListWithDayInfo: GetTransactionListWithAmountsPerDay,
    val addTransaction: AddTransaction,
    val deleteTransactionById: DeleteTransactionById
)

class GetTransactionViews(private val repository: TransactionsRepository) {
    operator fun invoke(
        dateRange: Pair<LocalDate?, LocalDate?>,
        account: Account?
    ): Flow<List<TransactionView>> {
        val minDate: LocalDate = dateRange.first ?: "2000-01-01".asLocalDate()
        val maxDate: LocalDate = dateRange.second ?: getCurrentLocalDate()

        return if (account == null)
            repository.getTransactionViews(minDate, maxDate)
        else
            repository.getTransactionsViewsForAccount(minDate, maxDate, account.id ?: 0)
    }
}

class GetTransactionListWithAmountsPerDay(private val repository: TransactionsRepository) {
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
        else
            repository.getTransactionAmountPerDayForAccount(minDate, maxDate, account.id ?: 0).first()

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

class AddTransaction(
    private val transactionsRepository: TransactionsRepository,
    private val accountsRepository: AccountsRepository
) {
    suspend operator fun invoke(transaction: Transaction) {
        val account = accountsRepository.getAccountById(transaction.accountId)

        if (account != null) {
            val amount = transaction.amount + account.amount

            accountsRepository.updateAccountAmount(transaction.accountId, amount)
            transactionsRepository.insertTransaction(transaction)
        }
    }
}

class DeleteTransactionById(
    private val repository: TransactionsRepository,
    private val accountsRepository: AccountsRepository
) {
    suspend operator fun invoke(transaction: TransactionView) {
        val account = accountsRepository.getAccountById(transaction.accountId)

        if (account != null) {
            val amount = account.amount + transaction.amount

            accountsRepository.updateAccountAmount(transaction.accountId, amount)
            repository.deleteTransactionById(transaction.id)
        }
    }
}
