package com.example.financemanager.dataBase.repositories

import com.example.financemanager.data.models.Transaction
import com.example.financemanager.data.repositories.TransactionsRepository
import com.example.financemanager.dataBase.dao.TransactionsDao
import kotlinx.coroutines.flow.Flow

class TransactionsRepositoryImpl(
    private val dao: TransactionsDao
) : TransactionsRepository {

    override fun getTransactions(): Flow<List<Transaction>> {
        return dao.getTransactions()
    }

    override suspend fun getTransactionById(id: Int): Transaction? {
        return dao.getTransactionById(id)
    }

    override suspend fun insertTransaction(transaction: Transaction) {
        return dao.insertTransaction(transaction)
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        return dao.deleteTransaction(transaction)
    }
}