package com.example.financemanager.dataBase.dao

import androidx.room.*
import com.example.financemanager.dataBase.models.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionsDao {

    @Query("SELECT * FROM `transaction`")
    fun getTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM `transaction` WHERE id = :id")
    suspend fun getTransactionById(id: Int): Transaction?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

}