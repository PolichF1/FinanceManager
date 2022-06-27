package com.example.financemanager.dataBase.dao

import androidx.room.*
import com.example.financemanager.data.models.Account

@Dao
interface AccountsDao {

    @Query("SELECT * FROM 'accounts'")
    fun getAccounts(): kotlinx.coroutines.flow.Flow<List<Account>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: Account)

    @Update
    suspend fun updateAccount(account: Account)

    @Query("UPDATE accounts SET amount =  amount + :amount WHERE id = :id")
    suspend fun updateAccountAmountById(id: Int, amount: Double)

    @Delete
    suspend fun deleteAccount(account: Account)
}