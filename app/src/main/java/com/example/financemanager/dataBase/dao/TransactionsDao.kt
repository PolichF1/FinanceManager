package com.example.financemanager.dataBase.dao

import androidx.room.*
import com.example.financemanager.data.models.DayInfo
import com.example.financemanager.data.models.Transaction
import com.example.financemanager.data.models.TransactionView
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionsDao {

//    @Query("SELECT * FROM 'transactions' ORDER BY date ASC, time ASC")
//    fun getTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM 'transactions' WHERE id = :id")
    suspend fun getTransactionById(id: Int): Transaction?

    @Query("SELECT date, SUM(amount) AS amount_per_day FROM 'transactions' GROUP BY date ORDER BY date ASC")
    fun getTransactionAmountPerDay(): Flow<List<DayInfo>>

    @Query("SELECT transactions.id, transactions.note, transactions.amount, transactions.date, transactions.time, categories.name AS category_name, accounts.name AS account_name, categories.icon, categories.icon_color FROM transactions JOIN accounts ON accounts.id = transactions.account_id JOIN categories ON categories.id = transactions.category_id ORDER BY date ASC, time ASC")
    fun getTransactionViews(): Flow<List<TransactionView>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

}