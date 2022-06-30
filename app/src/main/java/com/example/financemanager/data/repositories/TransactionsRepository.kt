package com.example.financemanager.data.repositories

import com.example.financemanager.data.models.DayInfo
import com.example.financemanager.data.models.Transaction
import com.example.financemanager.data.models.TransactionView
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TransactionsRepository {

    fun getTransactionViews(from: LocalDate, to: LocalDate): Flow<List<TransactionView>>

    fun getTransactionAmountsPerDay(from: LocalDate, to: LocalDate): Flow<List<DayInfo>>

    suspend fun insertTransaction(transaction: Transaction)

    suspend fun deleteTransactionById(id: Int)
}