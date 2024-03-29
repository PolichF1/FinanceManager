package com.example.financemanager.dataBase.repositories

import com.example.financemanager.data.models.DayInfo
import com.example.financemanager.data.models.Transaction
import com.example.financemanager.data.models.TransactionView
import com.example.financemanager.data.repositories.TransactionsRepository
import com.example.financemanager.dataBase.dao.TransactionsDao
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class TransactionsRepositoryImpl(
    private val dao: TransactionsDao
) : TransactionsRepository {

    override fun getTransactionViews(from: LocalDate, to: LocalDate): Flow<List<TransactionView>> {
        return dao.getTransactionViews(from, to)
    }

    override fun getTransactionsViewsForAccount(
        from: LocalDate,
        to: LocalDate,
        id: Int
    ): Flow<List<TransactionView>> {
        return dao.getTransactionViewsForAccount(from, to, id)
    }

    override fun getTransactionAmountsPerDay(from: LocalDate, to: LocalDate): Flow<List<DayInfo>> {
        return dao.getTransactionAmountsPerDay(from, to)
    }

    override fun getTransactionAmountPerDayForAccount(
        from: LocalDate,
        to: LocalDate,
        id: Int
    ): Flow<List<DayInfo>> {
        return dao.getTransactionAmountsPerDayForAccount(from, to, id)
    }

    override suspend fun insertTransaction(transaction: Transaction) {
        dao.insertTransaction(transaction)
    }

    override suspend fun deleteTransactionById(id: Int) {
        dao.deleteTransactionById(id)
    }
}