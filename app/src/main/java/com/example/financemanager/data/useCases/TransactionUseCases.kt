package com.example.financemanager.data.useCases

import com.example.financemanager.data.models.DayInfo
import com.example.financemanager.data.models.Transaction
import com.example.financemanager.data.repositories.TransactionsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

data class TransactionUseCases(
    val getTransactions: GetTransactions,
    val getTransaction: GetTransaction,
    val addTransaction: AddTransaction,
    val updateTransaction: UpdateTransaction,
    val deleteTransaction: DeleteTransaction,
    val getTransactionListForRecyclerView: GetTransactionListFotRecyclerView
)

class GetTransactions(private val repository: TransactionsRepository) {
    operator fun invoke(): Flow<List<Transaction>> {
        return repository.getTransactions()
    }
}

class GetTransaction(private val repository: TransactionsRepository) {
    suspend operator fun invoke(id: Int): Transaction? {
        return repository.getTransactionById(id)
    }
}

class AddTransaction(private val repository: TransactionsRepository) {
    suspend operator fun invoke(transaction: Transaction) {
        repository.insertTransaction(transaction)
    }
}

class UpdateTransaction(private val repository: TransactionsRepository) {
    suspend operator fun invoke(transaction: Transaction) {
        repository.insertTransaction(transaction)
    }
}

class DeleteTransaction(private val repository: TransactionsRepository) {
    suspend operator fun invoke(transaction: Transaction) {
        repository.deleteTransaction(transaction)
    }
}

class GetTransactionListFotRecyclerView(private val repository: TransactionsRepository) {
    suspend operator fun invoke(): List<Any> {
        val result = mutableListOf<Any>()

        val transactions = repository.getTransactions().first()
        val amountsPerDay = repository.getTransactionAmountsPerDay().first()

        if (transactions.isNotEmpty()) {
            var i = 0
            amountsPerDay.forEach {
                val dayInfo = DayInfo(it.transactionDate, it.amountPerDay)
                result.add(dayInfo)

                while (it.transactionDate == transactions[i].date) {
                    result.add(transactions[i])
                    i++
                }
            }
        }
        return result
    }
}
