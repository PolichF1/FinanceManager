package com.example.financemanager.data.useCases

import com.example.financemanager.data.models.Transaction
import com.example.financemanager.data.repositories.TransactionsRepository
import kotlinx.coroutines.flow.Flow

data class TransactionsUseCases(
    val getTransactions: GetTransactions,
    val getTransaction: GetTransaction,
    val addTransaction: AddTransaction,
    val updateTransaction: UpdateTransaction,
    val deleteTransaction: DeleteTransaction
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
        repository.deleteTransaction(transaction);
    }
}
