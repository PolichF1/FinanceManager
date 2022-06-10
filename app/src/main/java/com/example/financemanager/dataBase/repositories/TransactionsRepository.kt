package com.example.financemanager.dataBase.repositories

import com.example.financemanager.dataBase.models.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionsRepository {

    fun getTransactions(): Flow<List<Transaction>>

    suspend fun getTransactionById(id: Int): Transaction?

    suspend fun insertTransaction(transaction: Transaction)

    suspend fun deleteTransaction(transaction: Transaction)

}