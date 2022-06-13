package com.example.financemanager.dataBase.dao

import androidx.room.*
import com.example.financemanager.data.models.Account

@Dao
interface AccountsDao {

    @Query("SELECT * FROM 'account'")
    fun getAccounts(): kotlinx.coroutines.flow.Flow<List<Account>>

    @Query("SELECT * FROM 'account' WHERE id = :id")
    suspend fun getAccountById(id: Int): Account?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: Account)

    @Delete
    suspend fun deleteAccount(account: Account)
}