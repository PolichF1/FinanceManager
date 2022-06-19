package com.example.financemanager.dataBase.dao

import androidx.room.*
import com.example.financemanager.data.models.DayInfo
import com.example.financemanager.data.models.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionsDao {

    @Query("SELECT * FROM 'transaction' ORDER BY 'datetime' ASC")
    fun getTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM 'transaction' WHERE id = :id")
    suspend fun getTransactionById(id: Int): Transaction?

    @Query("SELECT date, SUM(amount) AS amount_per_day FROM 'transaction' GROUP BY date ORDER BY date DESC")
    fun getTransactionAmountPerDay(): Flow<List<DayInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

}